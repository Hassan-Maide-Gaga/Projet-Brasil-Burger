using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using Microsoft.EntityFrameworkCore;
using brasilBurger.Data;
using brasilBurger.Models;
using System.Security.Claims;

namespace brasilBurger.Controllers
{
    [Authorize]
    public class CommandeController : Controller
    {
        private readonly AppDbContext _context;
        private readonly ILogger<CommandeController> _logger;
        
        public CommandeController(AppDbContext context, ILogger<CommandeController> logger)
        {
            _context = context;
            _logger = logger;
        }
        
        // GET: Commande/Index
        public IActionResult Index(string etat = "all", int page = 1)
        {
            try
            {
                var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
                if (string.IsNullOrEmpty(userId))
                {
                    return RedirectToAction("Login", "Account");
                }
                
                // CORRECTION : Charger les relations avec Include
                var query = _context.Commandes
                    .Include(c => c.Client)           // Charger le client
                    .Include(c => c.Zone)             // Charger la zone
                    .Include(c => c.CommandeItems)    // IMPORTANT : Charger les items !
                    .Include(c => c.Paiements)        // Charger les paiements
                    .Where(c => c.ClientId == int.Parse(userId));
                
                // Filtrer par état si nécessaire
                if (etat != "all" && !string.IsNullOrEmpty(etat))
                {
                    if (Enum.TryParse<EtatCommande>(etat, out var etatEnum))
                    {
                        query = query.Where(c => c.Etat == etatEnum);
                    }
                }
                
                // Pagination
                int pageSize = 10;
                var totalItems = query.Count();
                var totalPages = (int)Math.Ceiling(totalItems / (double)pageSize);
                
                var commandes = query
                    .OrderByDescending(c => c.DateCommande)
                    .Skip((page - 1) * pageSize)
                    .Take(pageSize)
                    .ToList();
                
                ViewBag.CurrentPage = page;
                ViewBag.TotalPages = totalPages;
                ViewBag.SelectedEtat = etat;
                
                return View(commandes);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Erreur lors du chargement des commandes");
                TempData["ErrorMessage"] = "Une erreur est survenue lors du chargement des commandes";
                return View(new List<Commande>());
            }
        }
        
        // GET: Commande/Details/5
        public IActionResult Details(int id)
        {
            try
            {
                var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
                if (string.IsNullOrEmpty(userId))
                {
                    return RedirectToAction("Login", "Account");
                }
                
                // CORRECTION : Charger toutes les relations nécessaires
                var commande = _context.Commandes
                    .Include(c => c.Client)           // Charger le client
                    .Include(c => c.Zone)             // Charger la zone
                    .Include(c => c.CommandeItems)    // IMPORTANT : Charger les items !
                    .Include(c => c.Paiements)        // Charger les paiements
                    .FirstOrDefault(c => c.Id == id && c.ClientId == int.Parse(userId));
                
                if (commande == null)
                {
                    TempData["ErrorMessage"] = "Commande introuvable";
                    return RedirectToAction("Index");
                }
                
                return View(commande);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Erreur lors du chargement des détails de la commande");
                TempData["ErrorMessage"] = "Une erreur est survenue";
                return RedirectToAction("Index");
            }
        }
        
        // POST: Commande/Annuler/5
        [HttpPost]
        [ValidateAntiForgeryToken]
        public IActionResult Annuler(int id)
        {
            try
            {
                var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
                if (string.IsNullOrEmpty(userId))
                {
                    return RedirectToAction("Login", "Account");
                }
                
                var commande = _context.Commandes
                    .FirstOrDefault(c => c.Id == id && c.ClientId == int.Parse(userId));
                
                if (commande == null)
                {
                    TempData["ErrorMessage"] = "Commande introuvable";
                    return RedirectToAction("Index");
                }
                
                // Vérifier si la commande peut être annulée
                if (commande.Etat == EtatCommande.TERMINEE || commande.Etat == EtatCommande.ANNULEE)
                {
                    TempData["ErrorMessage"] = "Cette commande ne peut pas être annulée";
                    return RedirectToAction("Details", new { id });
                }
                
                commande.Etat = EtatCommande.ANNULEE;
                _context.SaveChanges();
                
                TempData["SuccessMessage"] = "Commande annulée avec succès";
                return RedirectToAction("Details", new { id });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Erreur lors de l'annulation de la commande");
                TempData["ErrorMessage"] = "Une erreur est survenue";
                return RedirectToAction("Index");
            }
        }
    }
}