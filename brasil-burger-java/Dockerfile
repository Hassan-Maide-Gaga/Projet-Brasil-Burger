# Dockerfile à la racine de votre projet Java (dans brasil-burger-java/)
FROM eclipse-temurin:23-jdk AS build

WORKDIR /app

# Copier les fichiers Maven
COPY pom.xml .
COPY src ./src

# Construire l'application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:23-jre

WORKDIR /app

# Copier le JAR construit
COPY --from=build /app/target/brasil-burger-java-1.0-SNAPSHOT.jar app.jar

# Installer des outils utiles (optionnel)
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Variables d'environnement
ENV JAVA_OPTS=""
ENV PORT=8080

# Exposition du port (même pour une app console, Render en a besoin)
EXPOSE ${PORT}

# Commande d'exécution
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]