using brasilBurger.Models;

namespace brasilBurger.ViewModels
{
    public class PaiementViewModel
    {
        public int ProduitId { get; set; }
        public string Type { get; set; } = string.Empty;
        public string NomProduit { get; set; } = string.Empty;
        public int Quantite { get; set; } = 1;
        public List<int> SelectedComplements { get; set; } = new List<int>();
        public decimal Total { get; set; }
        
        
        public List<Complement>? Complements { get; set; }
        public List<int> ComplementIds => SelectedComplements;
    }
}