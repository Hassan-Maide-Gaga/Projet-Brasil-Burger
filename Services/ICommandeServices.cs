using brasilBurger.Models;

namespace brasilBurger.Services
{
    public interface ICommandeServices
    {
        void CreateCommande(Commande cmd);
        void CreateCommandeItem(CommandeItem cmdItem);
        List<Commande> GetCommandesByClient(int clientId,int page=1, string etat = "all");
        int CountTotal(int ClientId, string etat = "all");
        Commande GetCommandeById(int id);
    }
}