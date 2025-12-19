# Stage 1: Build
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

# Copier les fichiers du projet
COPY . ./

# Restaurer les dépendances
RUN dotnet restore

# Publier l'application en release (framework-dependent)
RUN dotnet publish -c Release -o /app/publish

# Stage 2: Runtime
FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS runtime
WORKDIR /app

# Configurer le port pour Render
ENV PORT=8080
ENV ASPNETCORE_URLS=http://+:$PORT

# Copier les fichiers publiés
COPY --from=build /app/publish ./

# Exposer le port
EXPOSE $PORT

# Lancer l'application (spécifie explicitement le DLL)
CMD ["dotnet", "brasilBurger.dll"]
