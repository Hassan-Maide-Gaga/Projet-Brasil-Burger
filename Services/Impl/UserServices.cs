using brasilBurger.Controllers;
using brasilBurger.Data;
using brasilBurger.Models;

namespace brasilBurger.Services.Impl
{
    
    public class UserServices : IUserServices
    {
        private readonly AppDbContext _context;
        private readonly ILogger<CatalogueController> _logger;
        public UserServices(AppDbContext context,ILogger<CatalogueController> logger)
        {
            _context = context;
            _logger = logger;
        }
        public User getClientById(int id)
        {
            try
            {
                return _context.Users
                        .Where(u => u.Etat==true)
                        .FirstOrDefault(u => u.Id == id);
            }
            catch (Exception)
            {
                _logger.LogError("Erreur lors de la recuperation du client");
                throw;
            }
        }
         public async Task<User> GetCurrentUserAsync()
    {
        var userIdClaim = _httpContextAccessor.HttpContext?.User?.FindFirst(ClaimTypes.NameIdentifier);
        
        if (userIdClaim == null || !int.TryParse(userIdClaim.Value, out int userId))
        {
            return null;
        }

        return await _context.Users.FindAsync(userId);
    }
    }
}