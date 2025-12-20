using brasilBurger.Data;
using brasilBurger.Models;
using brasilBurger.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using System.Security.Claims;
using Microsoft.Extensions.Logging;

namespace brasilBurger.Controllers
{
    [Authorize] // Ajoutez cette ligne pour protéger l'accès
    public class PaiementController : Controller
    {
        private readonly AppDbContext _context;
        private readonly ICommandeServices _commandeServices;
        private readonly ICatalogueServices _catalogueServices;
        private readonly IPaiementServices _paiementServices;
        private readonly ILogger<PaiementController> _logger; // Changez le type
        
        public PaiementController(
            AppDbContext context,
            ILogger<PaiementController> logger, // Changez ici aussi
            ICommandeServices commandeServices, 
            ICatalogueServices catalogueServices, 
            IPaiementServices paiementServices
            )
        {
            _context = context;
            _commandeServices = commandeServices;
            _catalogueServices = catalogueServices;
            _paiementServices = paiementServices;
            _logger = logger;
        }
        
        [HttpGet]
        public IActionResult Payer(int produitId, string type, int quantite = 1)
        {
            // Action GET pour afficher la page de paiement
            try
            {
                // Récupérer les informations du produit
                var produit = _catalogueServices.GetItemById(produitId, type);
                if (produit == null)
                {
                    return RedirectToAction("Index", "Catalogue");
                }
                
                var model = new PaiementVM
                {
                    ProduitId = produitId,
                    Type = type,
                    NomProduit = produit.Nom,
                    Quantite = quantite,
                    PrixProduit = produit.Prix,
                    Total = produit.Prix * quantite
                };
                
                // Récupérer l'utilisateur connecté
                var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
                var user = _context.Users.Find(int.Parse(userId));
                
                ViewBag.Client = user;
                ViewBag.Zones = _context.Zones.Where(z => z.Etat).ToList();
                
                return View(model);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Erreur lors de l'affichage de la page de paiement");
                return RedirectToAction("Index", "Catalogue");
            }
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public IActionResult TraiterPaiement(
            int ProduitId, 
            string Type, 
            int Quantite,
            List<int> SelectedComplements,
            string Telephone,
            string TypeCmd,
            int? ZoneId,
            string ModePaie)
        {
            try
            {
                // 1. Récupérer l'utilisateur connecté
                var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
                if (string.IsNullOrEmpty(userId))
                {
                    return RedirectToAction("Login", "Account");
                }
                
                // 2. Récupérer les informations du produit
                var produit = _catalogueServices.GetItemById(ProduitId, Type);
                if (produit == null)
                {
                    return RedirectToAction("Index", "Catalogue");
                }
                
                // 3. Calculer le total
                decimal total = produit.Prix * Quantite;
                if (SelectedComplements != null && SelectedComplements.Count > 0)
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
                
                // 4. Créer la commande
                var typeEnum = TypeCmd == "SUR_PLACE" ? TypeCommande.SUR_PLACE : 
                              TypeCmd == "A_RECUPERER" ? TypeCommande.A_RECUPERER : 
                              TypeCommande.LIVRAISON;
                
                var cmd = new Commande
                {
                    DateCommande = DateTime.UtcNow,
                    Etat = EtatCommande.EN_ATTENTE,
                    Type = typeEnum,
                    ClientId = int.Parse(userId), // Utilisez l'ID de l'utilisateur connecté
                    ZoneId = TypeCmd == "LIVRAISON" ? ZoneId : null,
                    MontantTotal = total
                };
                
                _commandeServices.CreateCommande(cmd);
                
                // 5. Ajouter l'item principal
                var item = new CommandeItem
                {
                    CommandeId = cmd.Id,
                    ProduitId = ProduitId,
                    Type = Type == "Burger" ? TypeCommandeItem.BURGER : TypeCommandeItem.MENU,
                    Quantite = Quantite,
                    PrixUnitaire = produit.Prix
                };
                _commandeServices.CreateCommandeItem(item);
                
                // 6. Ajouter les compléments
                if (SelectedComplements != null && SelectedComplements.Count > 0)
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
                
                // 7. Créer le paiement
                var paiement = new Paiement
                {
                    CommandeId = cmd.Id,
                    DatePaiement = DateTime.UtcNow,
                    Montant = total,
                    Mode = ModePaie == "WAVE" ? ModePaiement.WAVE : ModePaiement.OM,
                    Statut = "EN_ATTENTE"
                };
                _paiementServices.CreatePaiement(paiement);
                
                return RedirectToAction("Details", "Commande", new { id = cmd.Id });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Erreur lors du traitement du paiement");
                TempData["ErrorMessage"] = "Une erreur est survenue lors du traitement de votre commande.";
                return RedirectToAction("Payer", "Paiement");
            }
        }
    }
}