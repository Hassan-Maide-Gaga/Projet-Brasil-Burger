namespace brasilBurger.Models
{
    public class PaiementVM
    {
        public int ProduitId { get; set; }
        public string Type { get; set; } 
        public int Quantite { get; set; }
        public List<int> ComplementIds { get; set; } = new();
        public string NomProduit { get; set; }
        public decimal PrixProduit { get; set; }
        public DateTime? CreatedAt { get; set; }
        public List<Complement> Complements { get; set; } = new();
        public decimal Total { get; set; }
    }
}