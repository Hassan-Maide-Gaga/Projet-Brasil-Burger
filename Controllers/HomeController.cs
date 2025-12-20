using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;

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