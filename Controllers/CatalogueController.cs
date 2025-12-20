using brasilBurger.Services;
using System;
using Microsoft.AspNetCore.Mvc;
using brasilBurger.Models;
using Microsoft.AspNetCore.Authorization;

namespace brasilBurger.Controllers
{
    [Authorize]
    public class CatalogueController : Controller
    {
        private readonly ICatalogueServices _catalogueServices;
        private readonly ILogger<CatalogueController> _logger;
        
        public CatalogueController(ICatalogueServices catSer, ILogger<CatalogueController> logger)
        {
            _logger = logger;
            _catalogueServices = catSer;
            
        }
        [HttpGet]
        public IActionResult Index(int page = 1, string type = "all")
        {
            try
            {
                var catalogue = _catalogueServices.GetCatalogue(page, type);
                ViewBag.CurrentPage = page;
                ViewBag.TotalPages = (int)Math.Ceiling(
                    (double)_catalogueServices.CountTotal(type) / 4
                );
                ViewBag.SelectedType = type;
                return View(catalogue);
            }
            catch (Exception)
            {
                _logger.LogError("Erreur lors de l'affichage du catalogue");
                return View(new List<CatalogueItemVM>());
            }
        }
        
        [HttpGet]
        public IActionResult Details(int id, string type)
        {
            try
            {
                var item = _catalogueServices.GetItemById(id, type);
                var complements = _catalogueServices.GetComplements();
                var similaires = _catalogueServices.GetCatalogue(1, type)
                    .Where(i => i.Id != id)
                    .Where(i => i.Type == type)
                    .ToList();
                if(type.ToLower() == "menu")
                {
                    var menuComplements = _catalogueServices.GetComplementsByMenu(id);
                    ViewBag.MenuComplements = menuComplements;
                }
                ViewBag.Complements = complements;
                ViewBag.Similaires = similaires;
                if (item == null)
                {
                    _logger.LogWarning("Item not found: Id={Id}, Type={Type}", id, type);
                    return NotFound();
                }
                return View(item);
            }catch (Exception)
            {
                _logger.LogError("Erreur lors de l'affichage du produit");
                return View(new CatalogueItemVM());
            }
        }
    }
}