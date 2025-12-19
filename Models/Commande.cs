namespace brasilBurger.Models
{
    public enum TypeCommande
    {
        SUR_PLACE,
        A_RECUPERER,
        LIVRAISON
    }
    public enum EtatCommande
    {
        EN_ATTENTE,
        EN_COURS,
        TERMINEE,
        ANNULEE
    }
    public class Commande
    {
        public int Id { get; set; }
        public DateTime DateCommande { get; set; }
        public EtatCommande Etat { get; set; }
        public TypeCommande Type { get; set; }
        public decimal MontantTotal { get; set; }
        public int ClientId { get; set; }
        public User Client { get; set; }
        public int? ZoneId { get; set; }
        public Zone Zone { get; set; }
        public int? LivreurId { get; set; }
        public Livreur Livreur { get; set; }
        public Paiement PaiementC { get; set; }
        public DateTime? CreatedAt { get; set; }
        public ICollection<CommandeItem> CommandeItems { get; set; } = new List<CommandeItem>();
    }
}