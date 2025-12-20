namespace brasilBurger.Models
{
    public enum RoleUser
    {
        GESTIONNAIRE,
        CLIENT
    }
    public class User
    {
        public int Id { get; set; }
        public string NomComplet { get; set; }
        public string Telephone { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public RoleUser Role { get; set; }
        public bool Etat { get; set; }
        public DateTime? CreatedAt { get; set; }
        public ICollection<Commande> Commandes { get; set; }
    }
}
