using brasilBurger.Data;
using brasilBurger.Services;
using brasilBurger.Models;
using brasilBurger.Services.Impl;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);


var connectionString = builder.Configuration.GetConnectionString("DefaultConnection");


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
    .EnableSensitiveDataLogging()  
    .EnableDetailedErrors());      


builder.Services.Configure<CloudinarySettings>(
    builder.Configuration.GetSection("Cloudinary"));
builder.Services.AddScoped<CloudinaryService>();


builder.Services.AddScoped<ICatalogueServices, CatalogueServices>();
builder.Services.AddScoped<ICommandeServices, CommandeServices>();
builder.Services.AddScoped<IPaiementServices, PaiementServices>();
builder.Services.AddScoped<IUserServices, UserServices>();
// var builder = WebApplication.CreateBuilder(args); // DUPLICATA COMMENTÉ
//
builder.Services.AddHttpContextAccessor();
builder.Services.AddScoped<IUserServices, UserServices>();
// Ajouter les services d'authentification
builder.Services.AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme)
    .AddCookie(options =>
    {
        options.LoginPath = "/Account/Login";
        options.LogoutPath = "/Account/Logout";
        options.AccessDeniedPath = "/Account/AccessDenied";
        options.Cookie.HttpOnly = true;
        options.Cookie.SecurePolicy = CookieSecurePolicy.Always;
        options.Cookie.SameSite = SameSiteMode.Strict;
        options.SlidingExpiration = true;
        options.ExpireTimeSpan = TimeSpan.FromDays(30);
    });

// Ajouter l'autorisation
builder.Services.AddAuthorization();

// Ajouter la session (nÃ©cessaire pour l'authentification par cookies)
builder.Services.AddSession(options =>
{
    options.IdleTimeout = TimeSpan.FromMinutes(30);
    options.Cookie.HttpOnly = true;
    options.Cookie.IsEssential = true;
});

// Ajouter les services existants
builder.Services.AddControllersWithViews();
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection")));

builder.Services.AddControllersWithViews();

var app = builder.Build();


if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseRouting();
app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Catalogue}/{action=Index}/{id?}");


using (var scope = app.Services.CreateScope())
{
    var services = scope.ServiceProvider;
    try
    {
        var context = services.GetRequiredService<AppDbContext>();
        
        // Test de connexion Ã  la base de donnÃ©es
        var canConnect = await context.Database.CanConnectAsync();
        Console.WriteLine($"========== BRASIL BURGER ==========");
        Console.WriteLine($"Connexion Ã  la base : {(canConnect ? "âœ… SUCCÃˆS" : "âŒ Ã‰CHEC")}");
        
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
                Console.WriteLine($"âš ï¸  Erreur lors de la lecture des donnÃ©es: {dbEx.Message}");
                Console.WriteLine($"DÃ©tails: {dbEx.InnerException?.Message}");
            }
        }
    }
    catch (Exception ex)
    {
        Console.WriteLine($"âŒ ERREUR lors du test de connexion: {ex.Message}");
        if (ex.InnerException != null)
        {
            Console.WriteLine($"DÃ©tails: {ex.InnerException.Message}");
        }
        Console.WriteLine($"==================================");
    }
}

app.Run();
