using brasilBurger.Models;

namespace brasilBurger.Services
{
    public interface IUserServices
    {
        Task<User> GetCurrentUserAsync();
        Task<User> GetUserByIdAsync(int id);
        Task<User> GetUserByEmailAsync(string email);
    }
}
