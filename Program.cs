using brasilBurger.Data;
using brasilBurger.Services;
using brasilBurger.Models;
using brasilBurger.Services.Impl;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Authentication.Cookies;

var builder = WebApplication.CreateBuilder(args);

// Récupérer la chaîne de connexion
var connectionString = builder.Configuration.GetConnectionString("DefaultConnection");

// Configurer la base de données
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseNpgsql(connectionString, 
        npgsqlOptions => 
        {
            npgsqlOptions.CommandTimeout(60);
            npgsqlOptions.EnableRetryOnFailure(
                maxRetryCount: 5,
                maxRetryDelay: TimeSpan.FromSeconds(30),
                errorCodesToAdd: null);
        })
    .UseQueryTrackingBehavior(QueryTrackingBehavior.NoTracking)
    .EnableSensitiveDataLogging(builder.Environment.IsDevelopment())  
    .EnableDetailedErrors(builder.Environment.IsDevelopment()));

// Configurer Cloudinary
builder.Services.Configure<CloudinarySettings>(
    builder.Configuration.GetSection("Cloudinary"));
builder.Services.AddScoped<CloudinaryService>();

// Enregistrer les services
builder.Services.AddScoped<ICatalogueServices, CatalogueServices>();
builder.Services.AddScoped<ICommandeServices, CommandeServices>();
builder.Services.AddScoped<IPaiementServices, PaiementServices>();
builder.Services.AddScoped<IUserServices, UserServices>();

builder.Services.AddHttpContextAccessor();

// Ajouter les services d'authentification
builder.Services.AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme)
    .AddCookie(options =>
    {
        options.LoginPath = "/Account/Login";
        options.LogoutPath = "/Account/Logout";
        options.AccessDeniedPath = "/Account/AccessDenied";
        options.Cookie.HttpOnly = true;
        options.Cookie.SecurePolicy = builder.Environment.IsDevelopment() 
            ? CookieSecurePolicy.None 
            : CookieSecurePolicy.Always;
        options.Cookie.SameSite = builder.Environment.IsDevelopment()
            ? SameSiteMode.Lax
            : SameSiteMode.Strict;
        options.SlidingExpiration = true;
        options.ExpireTimeSpan = TimeSpan.FromDays(30);
    });

// Ajouter l'autorisation
builder.Services.AddAuthorization();

// Ajouter la session (nécessaire pour l'authentification par cookies)
builder.Services.AddSession(options =>
{
    options.IdleTimeout = TimeSpan.FromMinutes(30);
    options.Cookie.HttpOnly = true;
    options.Cookie.IsEssential = true;
    options.Cookie.SameSite = builder.Environment.IsDevelopment()
        ? SameSiteMode.Lax
        : SameSiteMode.Strict;
    options.Cookie.SecurePolicy = builder.Environment.IsDevelopment()
        ? CookieSecurePolicy.None
        : CookieSecurePolicy.Always;
});

// Ajouter les services existants
builder.Services.AddControllersWithViews();

var app = builder.Build();

// Configurer le pipeline HTTP
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

// IMPORTANT: UseAuthentication() doit être appelé avant UseAuthorization()
app.UseAuthentication();
app.UseAuthorization();

app.UseSession();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Catalogue}/{action=Index}/{id?}");

// Tester la connexion à la base de données
using (var scope = app.Services.CreateScope())
{
    var services = scope.ServiceProvider;
    try
    {
        var context = services.GetRequiredService<AppDbContext>();
        
        // Test de connexion à la base de données
        var canConnect = await context.Database.CanConnectAsync();
        Console.WriteLine($"========== BRASIL BURGER ==========");
        Console.WriteLine($"Connexion à la base : {(canConnect ? "✓ SUCCÈS" : "✗ ÉCHEC")}");
        
        if (canConnect)
        {
            try
            {
                var burgerCount = await context.Burgers.CountAsync();
                var menuCount = await context.Menus.CountAsync();
                var userCount = await context.Users.CountAsync();
                
                Console.WriteLine($"Burgers dans la base : {burgerCount}");
                Console.WriteLine($"Menus dans la base : {menuCount}");
                Console.WriteLine($"Utilisateurs dans la base : {userCount}");
                Console.WriteLine($"==================================");
                
                var burger = await context.Burgers.FirstOrDefaultAsync();
                if (burger != null)
                {
                    Console.WriteLine($"Exemple burger: {burger.Nom} - {burger.Prix} FCFA");
                }
            }
            catch (Exception dbEx)
            {
                Console.WriteLine($"⚠️  Erreur lors de la lecture des données: {dbEx.Message}");
                Console.WriteLine($"Détails: {dbEx.InnerException?.Message}");
            }
        }
    }
    catch (Exception ex)
    {
        Console.WriteLine($"✗ ERREUR lors du test de connexion: {ex.Message}");
        if (ex.InnerException != null)
        {
            Console.WriteLine($"Détails: {ex.InnerException.Message}");
        }
        Console.WriteLine($"==================================");
    }
}

app.Run();