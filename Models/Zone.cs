namespace brasilBurger.Models
{
    public class Zone
    {
        public int Id { get; set; }
        public string Nom { get; set; }
        public decimal PrixLivraison { get; set; }
        public bool Etat { get; set; }
        public DateTime? CreatedAt { get; set; }
        public ICollection<Livreur> Livreurs { get; set; } = new List<Livreur>();
        public ICollection<Commande> Commandes { get; set; } = new List<Commande>();
    }
}