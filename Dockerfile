FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

# Copier d'abord uniquement les fichiers projet/solution
COPY *.sln ./
COPY brasilBurger.csproj ./

# Restaurer les dépendances
RUN dotnet restore "CSharp.sln"

# Copier le reste du code source (exclure le sous-dossier brasilBurger si possible)
COPY . .

# Build et publish
RUN dotnet build "CSharp.sln" -c Release -o /app/build
RUN dotnet publish "brasilBurger.csproj" -c Release -o /app/publish

FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS runtime
WORKDIR /app

COPY --from=build /app/publish .
EXPOSE 80
EXPOSE 443
ENTRYPOINT ["dotnet", "brasilBurger.dll"]