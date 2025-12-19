namespace brasilBurger.Models
{
    
    public class Burger
    {
        public int Id { get; set; }
        public string Nom { get; set; }
        public decimal Prix { get; set; }
        public string Image { get; set; }
        public bool Etat { get; set; }
        public DateTime? CreatedAt { get; set; }
        public ICollection<Menu> Menus { get; set; } = new List<Menu>();
    }
}