using brasilBurger.Models;
using brasilBurger.Services;
using Microsoft.AspNetCore.Mvc;

namespace brasilBurger.Controllers
{
    public class CommandeController : Controller
    {
        private readonly ILogger<CommandeController> _logger;
        private readonly ICatalogueServices _catalogueServices;
        private readonly ICommandeServices _commandeServices;
        private readonly IUserServices _userServices;
        public CommandeController(ICommandeServices commandeServices, ILogger<CommandeController> logger, ICatalogueServices cata, IUserServices userServices)
        {
            _logger = logger;
            _commandeServices = commandeServices;
            _catalogueServices = cata;
            _userServices = userServices;
        }
        [HttpPost]
        public IActionResult Commander(int ProduitId, string Type, List<int> SelectedComplements, int _Quantite)
        {
            try
            {
                CatalogueItemVM item = null;
                if(Type == "Burger")
                {
                    item = _catalogueServices.GetItemById(ProduitId, "burger");
                }else if(Type == "Menu")
                {
                    item = _catalogueServices.GetItemById(ProduitId, "Menu");
                }
                var complements = _catalogueServices.GetComplements()
                    .Where(c => SelectedComplements.Contains(c.Id))
                    .ToList();
                var zones = _catalogueServices.GetZones();
                ViewBag.Zones = zones;
                var paiementVM = new PaiementVM
                {
                    ProduitId = item.Id,
                    Type = item.Type,
                    NomProduit = item.Nom,
                    PrixProduit = item.Prix,
                    Complements = complements,
                    Quantite = _Quantite<=0 ? _Quantite : 1,
                    Total = (item.Prix * _Quantite) + complements.Sum(c => c.Prix),
                    ComplementIds = complements.Select(c => c.Id).ToList()
                };
                ViewBag.Client = _userServices.getClientById(1);
                if(item == null)
                    return RedirectToAction("Index", "Catalogue");
                return View("Paiement",paiementVM);
            }catch (Exception)
            {
                _logger.LogError("Erreur lors de la commande");
                return RedirectToAction("Index", "Catalogue");
            }
        }
        [HttpGet]
        public IActionResult Index(int page=1, String etat="all")
        {
            try
            {
                var commandes = _commandeServices.GetCommandesByClient(1,page,etat); 
                ViewBag.CurrentPage = page;
                ViewBag.TotalPages = (int)Math.Ceiling(
                    (double)_commandeServices.CountTotal(1,etat) / 4
                );
                ViewBag.SelectedEtat = etat;
                return View(commandes);
            }catch (Exception)
            {
                _logger.LogError("Erreur lors de l'affichage des commandes");
                return View(new List<Commande>());
            }
        }
        [HttpGet]
        public IActionResult Details(int id)
        {
            try
            {
                var commande = _commandeServices.GetCommandeById(id);
                if(commande == null) 
                    commande = new Commande();
                return View(commande);
            }catch (Exception)
            {
                _logger.LogError("Erreur lors de l'affichage de la commande");
                return View(new Commande());
            }
        }
        // Dans CommandeController.cs
            [Authorize] // Requiert une authentification
            public class CommandeController : Controller
            {
                // Toutes les actions nécessiteront maintenant une connexion
            }

            // Pour une action spécifique
            [Authorize(Roles = "Admin")] 
            public IActionResult AdminPanel()
            {
                return View();
            }
    }
}