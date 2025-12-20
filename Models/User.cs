using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace brasilBurger.Models
{
    public enum RoleUser
    {
        GESTIONNAIRE,
        CLIENT
    }

    [Table("user")]
    public class User
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [Required]
        [Column("nom_complet")]
        public string NomComplet { get; set; }

        [Required]
        [Phone]
        [Column("telephone")]
        public string Telephone { get; set; }

        [Required]
        [EmailAddress]
        [Column("email")]
        public string Email { get; set; }

        [Required]
        [Column("password")]
        public string Password { get; set; }

        [Required]
        [Column("role")]
        public RoleUser Role { get; set; }

        [Column("etat")]
        public bool Etat { get; set; } = true;

        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        // Navigation properties
        public virtual ICollection<Commande> Commandes { get; set; }
    }
}