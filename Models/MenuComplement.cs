namespace brasilBurger.Models
{
    public class MenuComplement
    {
        public int Id { get; set; }
        public int MenuId { get; set; }
        public Menu Menu { get; set; }
        public int ComplementId { get; set; }
        public DateTime? CreatedAt { get; set; }
        public Complement Complement { get; set; }
    }
}