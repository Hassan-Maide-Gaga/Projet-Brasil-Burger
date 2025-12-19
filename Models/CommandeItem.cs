namespace brasilBurger.Models
{
    public enum TypeCommandeItem
    {
        BURGER,
        MENU,
        COMPLEMENT
    }
    public class CommandeItem
    {
        public int Id { get; set; }
        public int CommandeId { get; set; }
        public Commande Commande { get; set; }
        public TypeCommandeItem Type { get; set; }
        public int ProduitId { get; set; }
        public int Quantite { get; set; }
        public DateTime? CreatedAt { get; set; }
        public decimal PrixUnitaire { get; set; }
    }

}