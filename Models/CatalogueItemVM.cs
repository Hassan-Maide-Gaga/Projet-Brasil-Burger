namespace brasilBurger.Models
{
    public class CatalogueItemVM
    {
        public int Id { get; set; }
        public string Nom { get; set; }
        public decimal Prix { get; set; }
        public string Image { get; set; }
        public DateTime? CreatedAt { get; set; }
        public string Type { get; set; } // "Burger" ou "Menu"
         public string Description { get; set; } = string.Empty;
    }
}