using brasilBurger.Controllers;
using brasilBurger.Data;
using brasilBurger.Models;
using Microsoft.EntityFrameworkCore;

namespace brasilBurger.Services.Impl
{
    public class CommandeServices : ICommandeServices
    {
        public readonly AppDbContext _context;
        private readonly ILogger<CatalogueController> _logger;
        private const int pageSize = 4;
        public CommandeServices(AppDbContext context,ILogger<CatalogueController> logger)
        {
            _context = context;
            _logger = logger;
        }
        public void CreateCommande(Commande cmd)
        {
            try
            {
                _context.Commandes.Add(cmd);
                _context.SaveChanges();
            }
            catch (Exception)
            {
                _logger.LogError("Erreur lors de la creation de la commande");
                throw;
            }
            
        }
        public void CreateCommandeItem(CommandeItem cmdItem)
        {
            try
            {
                _context.CommandeItems.Add(cmdItem);
                _context.SaveChanges();
            }
            catch (Exception)
            {
                _logger.LogError("Erreur lors de la creation du item de la commande");
                throw;
            }
        }
        public List<Commande> GetCommandesByClient(int clientId,int page=1,string etat = "all")
        {
            try
            {
                if (page < 1) page = 1;
                int offset = (page - 1) * pageSize;
                var commandes = _context.Commandes.Where(c => c.ClientId == clientId);
                if(etat != "all"){
                    var etatCm = etat=="EN_COURS" ? EtatCommande.EN_COURS :  etat=="EN_ATTENTE" ? EtatCommande.EN_ATTENTE :  etat=="ANNULEE" ? EtatCommande.ANNULEE : EtatCommande.TERMINEE;
                    commandes = commandes.Where(c => c.Etat== etatCm);
                }
                return commandes
                    .Include(c => c.CommandeItems)
                    .Include(c => c.Client)
                    .Include(c => c.Zone)
                    .Include(c => c.Livreur)
                    .Include(c => c.PaiementC)
                    .OrderByDescending(c => c.Id)
                    .Skip(offset)
                    .Take(pageSize)
                    .ToList();
            }
            catch (Exception)
            {
                _logger.LogError("Erreur lors de la recupreation des commandes");
                throw;
            }
        }
        public int CountTotal(int clientId, string etat = "all")
        {
            try
            {
                var query = _context.Commandes.Where(c => c.ClientId == clientId).ToList();
                if(etat != "all")
                {
                    var etatC = etat=="EN_COURS" ? EtatCommande.EN_COURS :  etat=="EN_ATTENTE" ? EtatCommande.EN_ATTENTE :  etat=="ANNULEE" ? EtatCommande.ANNULEE : EtatCommande.TERMINEE;
                    query = query.Where(c => c.Etat == etatC).ToList();
                }
                return query.Count();
            }
            catch (Exception)
            {
                _logger.LogError("Erreur lors du compte");
                throw;
            }
        }
        public Commande GetCommandeById(int id)
        {
            try
            {
                return _context.Commandes
                .Include(c => c.CommandeItems)
                .Include(c => c.Client)
                .Include(c => c.Zone)
                .Include(c => c.Livreur)
                .Include(c => c.PaiementC)
                .FirstOrDefault(c => c.Id == id);
            }
            catch (Exception)
            {
                _logger.LogError("Erreur lors de la recuperation de la commande");
                throw;
            }
        }
    }
}