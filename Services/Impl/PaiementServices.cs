using brasilBurger.Controllers;
using brasilBurger.Data;
using brasilBurger.Models;

namespace brasilBurger.Services.Impl
{
    public class PaiementServices : IPaiementServices
    {
        private readonly AppDbContext _context;
        private readonly ILogger<PaiementController> _logger;
        public PaiementServices(AppDbContext context,ILogger<PaiementController> logger)
        {
            _context = context;
            _logger = logger;
        }
        public void CreatePaiement(Paiement paiement)
        {
            try
            {
                _context.Paiements.Add(paiement);
                _context.SaveChanges();
            }
            catch (Exception)
            {
                _logger.LogError("Erreur lors du paiement");
                throw;
            }
        }
    }
}