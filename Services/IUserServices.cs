using brasilBurger.Models;

namespace brasilBurger.Services
{
    public interface IUserServices
    {
        User getClientById(int id);
        Task<User> GetCurrentUserAsync();
        Task<User> GetUserByIdAsync(int id);
        Task<User> GetUserByEmailAsync(string email);
        Task<User> GetCurrentUserAsync();

    }
}