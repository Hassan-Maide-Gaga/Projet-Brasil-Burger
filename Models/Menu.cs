namespace brasilBurger.Models
{
    public class Menu
    {
        public int Id { get; set; }
        public string Nom { get; set; }
        public string Image { get; set; }
        public decimal Montant { get; set; }
        public bool Etat { get; set; }
        public int BurgerId { get; set; }
        public Burger Burger { get; set; }
        public DateTime? CreatedAt { get; set; }
        public ICollection<MenuComplement> MenuComplements { get; set; } = new List<MenuComplement>();
    }
}
