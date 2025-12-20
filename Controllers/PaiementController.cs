using brasilBurger.Data;
using brasilBurger.Models;
using brasilBurger.ViewModels;
using brasilBurger.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using System.Security.Claims;
using Microsoft.Extensions.Logging;

namespace brasilBurger.Controllers
{
    [Authorize]
    public class PaiementController : Controller
    {
        private readonly AppDbContext _context;
        private readonly ICommandeServices _commandeServices;
        private readonly ICatalogueServices _catalogueServices;
        private readonly IPaiementServices _paiementServices;
        private readonly ILogger<PaiementController> _logger;
        
        public PaiementController(
            AppDbContext context,
            ILogger<PaiementController> logger,
            ICommandeServices commandeServices, 
            ICatalogueServices catalogueServices, 
            IPaiementServices paiementServices)
        {
            _context = context;
            _commandeServices = commandeServices;
            _catalogueServices = catalogueServices;
            _paiementServices = paiementServices;
            _logger = logger;
        }
        
        // GET: /Paiement/Payer
        [HttpGet]
        public IActionResult Payer(int produitId, string type, int quantite = 1, List<int>? complements = null)
        {
            try
            {
                _logger.LogInformation($"Payer GET appelé - ProduitId: {produitId}, Type: {type}, Quantite: {quantite}");
                
                // Récupérer le produit
                var produit = _catalogueServices.GetItemById(produitId, type);
                if (produit == null)
                {
                    _logger.LogWarning($"Produit introuvable - ID: {produitId}, Type: {type}");
                    TempData["ErrorMessage"] = "Produit introuvable";
                    return RedirectToAction("Index", "Catalogue");
                }
                
                // Créer le ViewModel
                var model = new PaiementVM
                {
                    ProduitId = produitId,
                    Type = type,
                    NomProduit = produit.Nom,
                    Quantite = quantite,
                    PrixProduit = produit.Prix,
                    Total = produit.Prix * quantite,
                    ComplementIds = complements ?? new List<int>(),
                    Complements = new List<Complement>()
                };
                
                // Ajouter les compléments
                if (complements != null && complements.Any())
                {
                    _logger.LogInformation($"Chargement de {complements.Count} compléments");
                    foreach(var compId in complements)
                    {
                        var complement = _catalogueServices.GetComplementById(compId);
                        if (complement != null)
                        {
                            model.Complements.Add(complement);
                            model.Total += complement.Prix * quantite;
                        }
                    }
                }
                
                // Récupérer l'utilisateur
                var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
                var user = _context.Users.Find(int.Parse(userId!));
                
                ViewBag.Client = user;
                ViewBag.Zones = _context.Zones.Where(z => z.Etat).ToList();
                
                _logger.LogInformation($"Page Payer chargée avec succès - Total: {model.Total}");
                return View(model);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Erreur dans Payer GET");
                TempData["ErrorMessage"] = "Une erreur est survenue";
                return RedirectToAction("Index", "Catalogue");
            }
        }
        
        // POST: /Paiement/Payer (depuis la page Payer.cshtml)
        [HttpPost]
        [ValidateAntiForgeryToken]
        public IActionResult Payer(
            int ProduitId, 
            string Type, 
            int Quantite,
            List<int>? SelectedComplements,
            string Telephone,
            string TypeCmd,
            int? ZoneId,
            string ModePaie)
        {
            return TraiterPaiement(ProduitId, Type, Quantite, SelectedComplements, 
                                  Telephone, TypeCmd, ZoneId, ModePaie);
        }

        // POST: /Paiement/TraiterPaiement
        [HttpPost]
        [ValidateAntiForgeryToken]
        public IActionResult TraiterPaiement(
            int ProduitId, 
            string Type, 
            int Quantite,
            List<int>? SelectedComplements,
            string Telephone,
            string TypeCmd,
            int? ZoneId,
            string ModePaie)
        {
            try
            {
                // Validation
                if (string.IsNullOrEmpty(Telephone) || string.IsNullOrEmpty(TypeCmd) || 
                    string.IsNullOrEmpty(ModePaie))
                {
                    TempData["ErrorMessage"] = "Veuillez remplir tous les champs obligatoires";
                    return RedirectToAction("Payer", new { 
                        produitId = ProduitId, 
                        type = Type, 
                        quantite = Quantite,
                        complements = SelectedComplements 
                    });
                }
                
                // Récupérer l'utilisateur
                var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
                if (string.IsNullOrEmpty(userId))
                {
                    return RedirectToAction("Login", "Account");
                }
                
                // Récupérer le produit
                var produit = _catalogueServices.GetItemById(ProduitId, Type);
                if (produit == null)
                {
                    TempData["ErrorMessage"] = "Produit introuvable";
                    return RedirectToAction("Index", "Catalogue");
                }
                
                // Calculer le total
                decimal total = produit.Prix * Quantite;
                if (SelectedComplements != null && SelectedComplements.Any())
                {
                    foreach(var compId in SelectedComplements)
                    {
                        var complement = _catalogueServices.GetComplementById(compId);
                        if (complement != null)
                        {
                            total += complement.Prix * Quantite;
                        }
                    }
                }
                
                // Ajouter les frais de livraison si nécessaire
                if (TypeCmd == "LIVRAISON" && ZoneId.HasValue)
                {
                    var zone = _context.Zones.Find(ZoneId.Value);
                    if (zone != null && zone.PrixLivraison > 0)
                    {
                        total += zone.PrixLivraison;
                    }
                }
                
                // Créer la commande
                var typeEnum = TypeCmd switch
                {
                    "SUR_PLACE" => TypeCommande.SUR_PLACE,
                    "A_RECUPERER" => TypeCommande.A_RECUPERER,
                    "LIVRAISON" => TypeCommande.LIVRAISON,
                    _ => TypeCommande.SUR_PLACE
                };
                
                var cmd = new Commande
                {
                    DateCommande = DateTime.Now,
                    Etat = EtatCommande.EN_ATTENTE,
                    Type = typeEnum,
                    ClientId = int.Parse(userId),
                    ZoneId = TypeCmd == "LIVRAISON" ? ZoneId : null,
                    MontantTotal = total
                };
                
                _commandeServices.CreateCommande(cmd);
                
                // Ajouter l'item principal
                var item = new CommandeItem
                {
                    CommandeId = cmd.Id,
                    ProduitId = ProduitId,
                    Type = Type == "Burger" ? TypeCommandeItem.BURGER : TypeCommandeItem.MENU,
                    Quantite = Quantite,
                    PrixUnitaire = produit.Prix
                };
                _commandeServices.CreateCommandeItem(item);
                
                // Ajouter les compléments
                if (SelectedComplements != null && SelectedComplements.Any())
                {
                    foreach(var compId in SelectedComplements)
                    {
                        var complement = _catalogueServices.GetComplementById(compId);
                        if (complement != null)
                        {
                            var compItem = new CommandeItem
                            {
                                CommandeId = cmd.Id,
                                ProduitId = compId,
                                Type = TypeCommandeItem.COMPLEMENT,
                                Quantite = Quantite,
                                PrixUnitaire = complement.Prix
                            };
                            _commandeServices.CreateCommandeItem(compItem);
                        }
                    }
                }
                
                // Créer le paiement - CORRECTION : utiliser l'enum avec le bon nom
                var modeEnum = ModePaie == "WAVE" ? ModePaiement.WAVE : ModePaiement.OM;
                var paiement = new Paiement
                {
                    CommandeId = cmd.Id,
                    DatePaiement = DateTime.Now,
                    Montant = total,
                    Mode = modeEnum,
                    Statut = "EN_ATTENTE"
                };
                _paiementServices.CreatePaiement(paiement);
                
                TempData["SuccessMessage"] = "Commande créée avec succès !";
                return RedirectToAction("Details", "Commande", new { id = cmd.Id });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Erreur lors du traitement du paiement");
                TempData["ErrorMessage"] = "Une erreur est survenue lors du traitement de votre commande.";
                return RedirectToAction("Index", "Catalogue");
            }
        }
    }
}