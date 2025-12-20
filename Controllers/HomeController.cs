using Microsoft.AspNetCore.Mvc;

namespace brasilBurger.Controllers
{
    public class HomeController : Controller
    {
        public IActionResult Index()
        {
           
            if (User.Identity?.IsAuthenticated == true)
            {
                
                return RedirectToAction("Index", "Catalogue");
            }
            
            
            return View();
        }
    }
}