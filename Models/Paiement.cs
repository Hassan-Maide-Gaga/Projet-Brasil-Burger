namespace brasilBurger.Models
{
    public enum ModePaiement
    {
        OM,
        WAVE
    }
    public class Paiement
    {
        public int Id { get; set; }
        public DateTime DatePaiement { get; set; }
        public decimal Montant { get; set; }
        public ModePaiement Mode { get; set; }
        public int CommandeId { get; set; }
        public DateTime? CreatedAt { get; set; }
        public Commande Commande { get; set; }
        public string? Reference { get; set; } 
        public string? Statut { get; set; } 
    }
}