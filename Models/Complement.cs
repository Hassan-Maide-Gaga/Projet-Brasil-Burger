namespace brasilBurger.Models
{
    public enum CategorieComplement
    {
        FRITES,
        BOISSON
    }
    public class Complement
    {
        public int Id { get; set; }
        public string Nom { get; set; }
        public decimal Prix { get; set; }
        public string Image { get; set; }
        public bool Etat { get; set; }
        public DateTime? CreatedAt { get; set; }
        public CategorieComplement Categorie { get; set; }
        public ICollection<MenuComplement> MenuComplements { get; set; }
    }
}