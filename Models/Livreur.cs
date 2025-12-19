namespace brasilBurger.Models
{
    public class Livreur
    {
        public int Id { get; set; }
        public string NomComplet { get; set; }
        public string Telephone { get; set; }
        public bool Etat { get; set; }

        public int ZoneId { get; set; }
        public Zone Zone { get; set; }
        public DateTime? CreatedAt { get; set; }
        public ICollection<Commande> Commandes { get; set; } = new List<Commande>();
    }
}