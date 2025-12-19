using brasilBurger.Models;
using Microsoft.EntityFrameworkCore;

namespace brasilBurger.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options)
            : base(options)
        {
        }
        public DbSet<User> Users { get; set; }
        public DbSet<Zone> Zones { get; set; }
        public DbSet<Livreur> Livreurs { get; set; }
        public DbSet<Burger> Burgers { get; set; }
        public DbSet<Menu> Menus { get; set; }
        public DbSet<Complement> Complements { get; set; }
        public DbSet<MenuComplement> MenuComplements { get; set; }
        public DbSet<Commande> Commandes { get; set; }
        public DbSet<CommandeItem> CommandeItems { get; set; }
        public DbSet<Paiement> Paiements { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            
            // User
            modelBuilder.Entity<User>(entity =>
            {
                entity.ToTable("user");
                entity.HasKey(u => u.Id);
                entity.Property(u => u.Id).HasColumnName("id").ValueGeneratedOnAdd();
                entity.Property(u => u.NomComplet)
                    .HasColumnName("nom_complet")
                    .IsRequired()
                    .HasMaxLength(150);
                entity.Property(u => u.Telephone)
                    .HasColumnName("telephone")
                    .IsRequired()
                    .HasMaxLength(20);
                entity.Property(u => u.Email)
                    .HasColumnName("email")
                    .IsRequired()
                    .HasMaxLength(150);
                entity.Property(u => u.Password)
                    .HasColumnName("password")
                    .IsRequired()
                    .HasMaxLength(255);
                entity.Property(u => u.Role)
                    .HasColumnName("role")
                    .HasColumnType("role_user")  
                    .HasConversion<string>()  
                    .IsRequired();
                entity.Property(u => u.Etat)
                    .HasColumnName("etat")
                    .HasDefaultValue(true);
                
                
                entity.Ignore(u => u.CreatedAt);
            });
            
            // Zone
            modelBuilder.Entity<Zone>(entity =>
            {
                entity.ToTable("zone");
                entity.HasKey(z => z.Id);
                entity.Property(z => z.Id).HasColumnName("id").ValueGeneratedOnAdd();
                entity.Property(z => z.Nom)
                    .HasColumnName("nom")
                    .IsRequired()
                    .HasMaxLength(100);
                entity.Property(z => z.PrixLivraison)
                    .HasColumnName("prix_livraison")
                    .HasColumnType("numeric(10,2)")
                    .IsRequired();
                entity.Property(z => z.Etat)
                    .HasColumnName("etat")
                    .HasDefaultValue(true);
                
                
                entity.Ignore(z => z.CreatedAt);
            });
            
            // Livreur
            modelBuilder.Entity<Livreur>(entity =>
            {
                entity.ToTable("livreur");
                entity.HasKey(l => l.Id);
                entity.Property(l => l.Id).HasColumnName("id").ValueGeneratedOnAdd();
                entity.Property(l => l.NomComplet)
                    .HasColumnName("nom_complet")
                    .IsRequired()
                    .HasMaxLength(150);
                entity.Property(l => l.Telephone)
                    .HasColumnName("telephone")
                    .IsRequired()
                    .HasMaxLength(20);
                entity.Property(l => l.ZoneId)
                    .HasColumnName("zone_id")
                    .IsRequired();
                entity.Property(l => l.Etat)
                    .HasColumnName("etat")
                    .HasDefaultValue(true);
                
               
                entity.Ignore(l => l.CreatedAt);
                
                entity.HasOne(l => l.Zone)
                    .WithMany(z => z.Livreurs)
                    .HasForeignKey(l => l.ZoneId)
                    .OnDelete(DeleteBehavior.Restrict);
            });
            
            // Burger
            modelBuilder.Entity<Burger>(entity =>
            {
                entity.ToTable("burger");
                entity.HasKey(b => b.Id);
                entity.Property(b => b.Id).HasColumnName("id").ValueGeneratedOnAdd();
                entity.Property(b => b.Nom)
                    .HasColumnName("nom")
                    .IsRequired()
                    .HasMaxLength(150);
                entity.Property(b => b.Prix)
                    .HasColumnName("prix")
                    .HasColumnType("numeric(10,2)")
                    .IsRequired();
                entity.Property(b => b.Image)
                    .HasColumnName("image");
                entity.Property(b => b.Etat)
                    .HasColumnName("etat")
                    .HasDefaultValue(true);
                
               
                entity.Ignore(b => b.CreatedAt);
            });
            
            // Menu
            modelBuilder.Entity<Menu>(entity =>
            {
                entity.ToTable("menu");
                entity.HasKey(m => m.Id);
                entity.Property(m => m.Id).HasColumnName("id").ValueGeneratedOnAdd();
                entity.Property(m => m.Nom)
                    .HasColumnName("nom")
                    .IsRequired()
                    .HasMaxLength(150);
                entity.Property(m => m.Image)
                    .HasColumnName("image");
                entity.Property(m => m.Montant)
                    .HasColumnName("montant")
                    .HasColumnType("numeric(10,2)")
                    .IsRequired();
                entity.Property(m => m.BurgerId)
                    .HasColumnName("burger_id")
                    .IsRequired();
                entity.Property(m => m.Etat)
                    .HasColumnName("etat")
                    .HasDefaultValue(true);
                
                
                entity.Ignore(m => m.CreatedAt);
                
                entity.HasOne(m => m.Burger)
                    .WithMany(b => b.Menus)
                    .HasForeignKey(m => m.BurgerId)
                    .OnDelete(DeleteBehavior.Restrict);
            });
            
            
            modelBuilder.Entity<Complement>(entity =>
            {
                entity.ToTable("complement");
                entity.HasKey(c => c.Id);
                entity.Property(c => c.Id).HasColumnName("id").ValueGeneratedOnAdd();
                entity.Property(c => c.Nom)
                    .HasColumnName("nom")
                    .IsRequired()
                    .HasMaxLength(150);
                entity.Property(c => c.Prix)
                    .HasColumnName("prix")
                    .HasColumnType("numeric(10,2)")
                    .IsRequired();
                entity.Property(c => c.Image)
                    .HasColumnName("image");
                entity.Property(c => c.Categorie)
                    .HasColumnName("categorie")
                    .HasColumnType("categorie_complement")  
                    .HasConversion<string>() 
                    .IsRequired();
                entity.Property(c => c.Etat)
                    .HasColumnName("etat")
                    .HasDefaultValue(true);
                
                
                entity.Ignore(c => c.CreatedAt);
            });
            
            modelBuilder.Entity<MenuComplement>(entity =>
            {
                entity.ToTable("menu_complement");
                entity.HasKey(mc => mc.Id);  
                entity.Property(mc => mc.Id)
                    .HasColumnName("id")
                    .ValueGeneratedOnAdd();
                    
                entity.Property(mc => mc.MenuId)
                    .HasColumnName("menu_id")
                    .IsRequired();
                    
                entity.Property(mc => mc.ComplementId)
                    .HasColumnName("complement_id")
                    .IsRequired();
                
                
                entity.Ignore(mc => mc.CreatedAt);
                
               
                entity.HasOne(mc => mc.Menu)
                    .WithMany(m => m.MenuComplements)
                    .HasForeignKey(mc => mc.MenuId)
                    .OnDelete(DeleteBehavior.Cascade);
                    
                entity.HasOne(mc => mc.Complement)
                    .WithMany(c => c.MenuComplements)
                    .HasForeignKey(mc => mc.ComplementId)
                    .OnDelete(DeleteBehavior.Cascade);
                    
                
                entity.HasIndex(mc => new { mc.MenuId, mc.ComplementId })
                    .IsUnique();
            });
    
            modelBuilder.Entity<Commande>(entity =>
            {
                entity.ToTable("commande");
                entity.HasKey(c => c.Id);
                entity.Property(c => c.Id).HasColumnName("id").ValueGeneratedOnAdd();
                entity.Property(c => c.DateCommande)
                    .HasColumnName("date_commande")
                    .HasDefaultValueSql("CURRENT_TIMESTAMP");
                    
               
                entity.Property(c => c.Etat)
                    .HasColumnName("etat")
                    .HasColumnType("text")  
                    .HasConversion(
                        v => v.ToString(),  
                        v => (EtatCommande)Enum.Parse(typeof(EtatCommande), v)  // 
                    )
                    .HasDefaultValue(EtatCommande.EN_ATTENTE);
                    
                entity.Property(c => c.Type)
                    .HasColumnName("type")
                    .HasColumnType("text")  
                    .HasConversion(
                        v => v.ToString(),
                        v => (TypeCommande)Enum.Parse(typeof(TypeCommande), v)
                    )
                    .IsRequired();
                    
                entity.Property(c => c.MontantTotal)
                    .HasColumnName("montant_total")
                    .HasColumnType("numeric(10,2)")
                    .IsRequired();
                entity.Property(c => c.ClientId)
                    .HasColumnName("client_id")
                    .IsRequired();
                entity.Property(c => c.ZoneId)
                    .HasColumnName("zone_id");
                entity.Property(c => c.LivreurId)
                    .HasColumnName("livreur_id");
                
              
                entity.Ignore(c => c.CreatedAt);
                
                entity.HasOne(c => c.Client)
                    .WithMany(u => u.Commandes)
                    .HasForeignKey(c => c.ClientId)
                    .OnDelete(DeleteBehavior.Restrict);
                    
                entity.HasOne(c => c.Zone)
                    .WithMany(z => z.Commandes)
                    .HasForeignKey(c => c.ZoneId)
                    .OnDelete(DeleteBehavior.Restrict);
                    
                entity.HasOne(c => c.Livreur)
                    .WithMany(l => l.Commandes)
                    .HasForeignKey(c => c.LivreurId)
                    .OnDelete(DeleteBehavior.Restrict);
                    
                
                entity.HasOne(c => c.PaiementC)
                    .WithOne(p => p.Commande)
                    .HasForeignKey<Paiement>(p => p.CommandeId)
                    .OnDelete(DeleteBehavior.Cascade);
            });
            
         
            modelBuilder.Entity<CommandeItem>(entity =>
            {
                entity.ToTable("commande_item");
                entity.HasKey(ci => ci.Id);
                entity.Property(ci => ci.Id).HasColumnName("id").ValueGeneratedOnAdd();
                entity.Property(ci => ci.CommandeId)
                    .HasColumnName("commande_id")
                    .IsRequired();
                    
                entity.Property(ci => ci.Type)
                    .HasColumnName("type_item")  
                    .HasColumnType("text")  
                    .HasConversion(
                        v => v.ToString(),
                        v => (TypeCommandeItem)Enum.Parse(typeof(TypeCommandeItem), v)
                    )
                    .IsRequired();
                    
               
                entity.Property(ci => ci.ProduitId)
                    .HasColumnName("item_id")  
                    .IsRequired();
                    
                entity.Property(ci => ci.Quantite)
                    .HasColumnName("quantite")
                    .IsRequired();
                entity.Property(ci => ci.PrixUnitaire)
                    .HasColumnName("prix_unitaire")
                    .HasColumnType("numeric(10,2)")
                    .IsRequired();
                
               
                entity.Ignore(ci => ci.CreatedAt);
                
                entity.HasOne(ci => ci.Commande)
                    .WithMany(c => c.CommandeItems)
                    .HasForeignKey(ci => ci.CommandeId)
                    .OnDelete(DeleteBehavior.Cascade);
            });
            
         
            modelBuilder.Entity<Paiement>(entity =>
            {
                entity.ToTable("paiement");
                entity.HasKey(p => p.Id);
                entity.Property(p => p.Id).HasColumnName("id").ValueGeneratedOnAdd();
                entity.Property(p => p.DatePaiement)
                    .HasColumnName("date_paiement")
                    .HasDefaultValueSql("CURRENT_TIMESTAMP");
                entity.Property(p => p.Montant)
                    .HasColumnName("montant")
                    .HasColumnType("numeric(10,2)")
                    .IsRequired();
                    
                entity.Property(p => p.Mode)
                    .HasColumnName("mode_paiement")  
                    .HasColumnType("text")  
                    .HasConversion(
                        v => v.ToString(),
                        v => (ModePaiement)Enum.Parse(typeof(ModePaiement), v)
                    )
                    .IsRequired();
                    
                entity.Property(p => p.CommandeId)
                    .HasColumnName("commande_id")
                    .IsRequired();
                
               
                entity.Ignore(p => p.CreatedAt);
                entity.Ignore(p => p.Reference); 
                entity.Ignore(p => p.Statut);     
                
                entity.HasOne(p => p.Commande)
                    .WithOne(c => c.PaiementC)
                    .HasForeignKey<Paiement>(p => p.CommandeId)
                    .OnDelete(DeleteBehavior.Cascade);
            });
        }
    }
}