using brasilBurger.Models;

namespace brasilBurger.Services
{
    public interface IUserServices
    {
        User getClientById(int id);
        
    }
}