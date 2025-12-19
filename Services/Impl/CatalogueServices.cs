using brasilBurger.Controllers;
using brasilBurger.Data;
using brasilBurger.Models;

namespace brasilBurger.Services.Impl
{
    public class CatalogueServices : ICatalogueServices
    {
        private readonly AppDbContext _context;
        private readonly ILogger<CatalogueController> _logger;
        private const int pageSize = 4;
        public CatalogueServices(AppDbContext context,ILogger<CatalogueController> logger)
        {
            _context = context;
            _logger = logger;
        }
        public List<CatalogueItemVM> GetCatalogue(int page = 1, string type = "all")
        {
            try
            {
                if (page < 1) page = 1;
                int offset = (page - 1) * pageSize;
                var burgers = _context.Burgers.Select(b => new CatalogueItemVM
                {
                    Id = b.Id,
                    Nom = b.Nom,
                    Prix = b.Prix,
                    Image = b.Image,
                    Type = "Burger"
                });
                var menus = _context.Menus.Select(m => new CatalogueItemVM
                {
                    Id = m.Id,
                    Nom = m.Nom,
                    Prix = m.Montant,
                    Image = m.Image,
                    Type = "Menu"
                });
                var allItems = burgers.Concat(menus);
                if (!string.IsNullOrEmpty(type) && type.ToLower() != "all")
                    allItems = allItems.Where(c => c.Type.ToLower() == type.ToLower());
                return allItems
                    .OrderByDescending(c => c.Id)
                    .Skip(offset)
                    .Take(pageSize)
                    .ToList();
            }catch (Exception)
            {
                _logger.LogError("Erreur lors de la recuperation du catalogue");
                throw;
            }
        }
        public int CountTotal(string type = "all")
        {
            try
            {
                var burgersCount = _context.Burgers.Count();
                var menusCount = _context.Menus.Count();
                type = type?.ToLower() ?? "all";
                if (type == "burger")
                    return burgersCount;
                else if (type == "menu")
                    return menusCount;
                else
                    return burgersCount + menusCount;
            }catch (Exception)
            {
                _logger.LogError("Erreur lors du compte");
                throw;
            }
        }
        public CatalogueItemVM GetItemById(int id, string type)
        {
            try{
                type = type?.ToLower();
                if (type == "burger")
                {
                    var burger = _context.Burgers.Find(id);
                    if (burger == null) return null;
                    return new CatalogueItemVM
                    {
                        Id = burger.Id,
                        Nom = burger.Nom,
                        Prix = burger.Prix,
                        Image = burger.Image,
                        Type = "Burger"
                    };
                }
                else if (type == "menu")
                {
                    var menu = _context.Menus.Find(id);
                    if (menu == null) return null;
                    return new CatalogueItemVM
                    {
                        Id = menu.Id,
                        Nom = menu.Nom,
                        Prix = menu.Montant,
                        Image = menu.Image,
                        Type = "Menu"
                    };
                }
                return null;
            }catch (Exception)
            {
                _logger.LogError("Erreur lors de la recuperation du produit");
                throw;
            }
        }
        public List<Complement> GetComplements()
        {
            try
            {
                return _context.Complements.ToList();
            }catch (Exception)
            {
                _logger.LogError("Erreur lors de la recuperation des complements");
                throw;
            }
        }
        public List<Complement> GetComplementsByMenu(int id)
        {
            try
            {
                return _context.MenuComplements
                    .Where(mc => mc.MenuId == id)
                    .Join(
                        _context.Complements,
                        mc => mc.ComplementId,
                        c => c.Id,
                        (mc, c) => c
                    )
                    .ToList();
            }catch (Exception)
            {
                _logger.LogError("Erreur lors de la recuperation des complements");
                throw;
            }
        }
        public List<Zone> GetZones()
        {
            try
            {
                return _context.Zones
                .Where(z => z.Etat == true)
                .ToList();
            }catch (Exception)
            {
                _logger.LogError("Erreur lors de la recuperation des zones");
                throw;
            }
        }
        public Complement GetComplementById(int id)
        {
            try
            {
                return _context.Complements.Find(id);
            }
            catch (Exception)
            {
                _logger.LogError("Erreur lors de la recuperation des complements");
                throw;
            }
        }
    }
}