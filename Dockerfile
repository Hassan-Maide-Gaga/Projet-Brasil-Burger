FROM php:8.4-cli

# Dépendances système
RUN apt-get update && apt-get install -y \
    git unzip libzip-dev libicu-dev libpq-dev \
    && docker-php-ext-install intl pdo pdo_mysql pdo_pgsql zip \
    && rm -rf /var/lib/apt/lists/*

# Installer Composer
COPY --from=composer:2 /usr/bin/composer /usr/bin/composer

WORKDIR /app
COPY . .

# Installer dépendances Symfony (prod)
RUN composer install --optimize-autoloader

# Permissions Symfony
RUN mkdir -p var/cache var/log var/sessions \
    && chmod -R 777 var

# Vider et réchauffer le cache pour éviter cache:clear errors
RUN php bin/console cache:clear --no-warmup \
    && php bin/console cache:warmup

# Exposer le port pour Render
EXPOSE 10000

# Serveur HTTP intégrée
CMD ["php", "-S", "0.0.0.0:10000", "-t", "public"]
