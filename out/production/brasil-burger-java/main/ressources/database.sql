-- Script SQL pour Brasil Burger
-- Fichier: src/main/resources/database.sql

-- Suppression des tables si elles existent
DROP TABLE IF EXISTS paiement CASCADE;
DROP TABLE IF EXISTS commande_detail CASCADE;
DROP TABLE IF EXISTS commande CASCADE;
DROP TABLE IF EXISTS menu_composition CASCADE;
DROP TABLE IF EXISTS menu CASCADE;
DROP TABLE IF EXISTS burger CASCADE;
DROP TABLE IF EXISTS complement CASCADE;
DROP TABLE IF EXISTS zone_livraison CASCADE;
DROP TABLE IF EXISTS livreur CASCADE;
DROP TABLE IF EXISTS client CASCADE;
DROP TABLE IF EXISTS gestionnaire CASCADE;
DROP TABLE IF EXISTS utilisateur CASCADE;

-- Table Utilisateur (parent)
CREATE TABLE utilisateur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    type_utilisateur VARCHAR(20) NOT NULL CHECK (type_utilisateur IN ('CLIENT', 'GESTIONNAIRE', 'LIVREUR')),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut_archivage VARCHAR(20) DEFAULT 'ACTIF' CHECK (statut_archivage IN ('ACTIF', 'ARCHIVE'))
);

-- Table Client
CREATE TABLE client (
    id INTEGER PRIMARY KEY REFERENCES utilisateur(id) ON DELETE CASCADE,
    adresse_livraison TEXT
);

-- Table Gestionnaire
CREATE TABLE gestionnaire (
    id INTEGER PRIMARY KEY REFERENCES utilisateur(id) ON DELETE CASCADE,
    role VARCHAR(50) DEFAULT 'GESTIONNAIRE'
);

-- Table Livreur
CREATE TABLE livreur (
    id INTEGER PRIMARY KEY REFERENCES utilisateur(id) ON DELETE CASCADE,
    vehicule VARCHAR(50),
    disponibilite BOOLEAN DEFAULT TRUE
);

-- Table Zone de livraison
CREATE TABLE zone_livraison (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    quartiers TEXT[] NOT NULL,
    prix_livraison DECIMAL(10, 2) NOT NULL,
    statut_archivage VARCHAR(20) DEFAULT 'ACTIF' CHECK (statut_archivage IN ('ACTIF', 'ARCHIVE'))
);

-- Table Burger
CREATE TABLE burger (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prix DECIMAL(10, 2) NOT NULL,
    image_url TEXT,
    image_public_id VARCHAR(255),
    description TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut_archivage VARCHAR(20) DEFAULT 'ACTIF' CHECK (statut_archivage IN ('ACTIF', 'ARCHIVE'))
);

-- Table Complément
CREATE TABLE complement (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prix DECIMAL(10, 2) NOT NULL,
    image_url TEXT,
    image_public_id VARCHAR(255),
    type_complement VARCHAR(50) CHECK (type_complement IN ('BOISSON', 'FRITE', 'AUTRE')),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut_archivage VARCHAR(20) DEFAULT 'ACTIF' CHECK (statut_archivage IN ('ACTIF', 'ARCHIVE'))
);

-- Table Menu
CREATE TABLE menu (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    image_url TEXT,
    image_public_id VARCHAR(255),
    description TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut_archivage VARCHAR(20) DEFAULT 'ACTIF' CHECK (statut_archivage IN ('ACTIF', 'ARCHIVE'))
);

-- Table Composition Menu (Menu = Burger + Boisson + Frites)
CREATE TABLE menu_composition (
    id SERIAL PRIMARY KEY,
    menu_id INTEGER NOT NULL REFERENCES menu(id) ON DELETE CASCADE,
    burger_id INTEGER REFERENCES burger(id) ON DELETE CASCADE,
    complement_id INTEGER REFERENCES complement(id) ON DELETE CASCADE,
    type_element VARCHAR(20) NOT NULL CHECK (type_element IN ('BURGER', 'BOISSON', 'FRITE'))
);

-- Table Commande
CREATE TABLE commande (
    id SERIAL PRIMARY KEY,
    client_id INTEGER NOT NULL REFERENCES client(id),
    date_commande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    type_commande VARCHAR(20) NOT NULL CHECK (type_commande IN ('SUR_PLACE', 'A_EMPORTER', 'LIVRAISON')),
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE' CHECK (statut IN ('EN_ATTENTE', 'EN_PREPARATION', 'PRETE', 'EN_LIVRAISON', 'TERMINEE', 'ANNULEE')),
    montant_total DECIMAL(10, 2) NOT NULL,
    adresse_livraison TEXT,
    zone_livraison_id INTEGER REFERENCES zone_livraison(id),
    livreur_id INTEGER REFERENCES livreur(id),
    date_affectation_livreur TIMESTAMP,
    date_livraison TIMESTAMP,
    statut_paiement VARCHAR(20) DEFAULT 'NON_PAYE' CHECK (statut_paiement IN ('NON_PAYE', 'PAYE')),
    notes TEXT
);

-- Table Détail Commande
CREATE TABLE commande_detail (
    id SERIAL PRIMARY KEY,
    commande_id INTEGER NOT NULL REFERENCES commande(id) ON DELETE CASCADE,
    burger_id INTEGER REFERENCES burger(id),
    menu_id INTEGER REFERENCES menu(id),
    complement_id INTEGER REFERENCES complement(id),
    type_produit VARCHAR(20) NOT NULL CHECK (type_produit IN ('BURGER', 'MENU', 'COMPLEMENT')),
    quantite INTEGER NOT NULL DEFAULT 1,
    prix_unitaire DECIMAL(10, 2) NOT NULL,
    sous_total DECIMAL(10, 2) NOT NULL
);

-- Table Paiement
CREATE TABLE paiement (
    id SERIAL PRIMARY KEY,
    commande_id INTEGER NOT NULL UNIQUE REFERENCES commande(id) ON DELETE CASCADE,
    date_paiement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    montant DECIMAL(10, 2) NOT NULL,
    mode_paiement VARCHAR(20) NOT NULL CHECK (mode_paiement IN ('WAVE', 'ORANGE_MONEY', 'ESPECES', 'CARTE')),
    reference_transaction VARCHAR(100),
    statut VARCHAR(20) DEFAULT 'SUCCES' CHECK (statut IN ('SUCCES', 'ECHEC', 'EN_ATTENTE'))
);

-- Index pour améliorer les performances
CREATE INDEX idx_commande_client ON commande(client_id);
CREATE INDEX idx_commande_date ON commande(date_commande);
CREATE INDEX idx_commande_statut ON commande(statut);
CREATE INDEX idx_commande_livreur ON commande(livreur_id);
CREATE INDEX idx_menu_composition_menu ON menu_composition(menu_id);
CREATE INDEX idx_commande_detail_commande ON commande_detail(commande_id);
CREATE INDEX idx_utilisateur_type ON utilisateur(type_utilisateur);

-- Données de test
-- Gestionnaire par défaut
INSERT INTO utilisateur (nom, prenom, telephone, email, mot_de_passe, type_utilisateur)
VALUES ('Admin', 'Gestionnaire', '+221771234567', 'admin@brasilburger.com', 'admin123', 'GESTIONNAIRE');

INSERT INTO gestionnaire (id, role)
VALUES (currval('utilisateur_id_seq'), 'GESTIONNAIRE');

-- Burgers
INSERT INTO burger (nom, prix, description) VALUES
('Burger Classic', 3500, 'Burger classique avec steak, salade, tomate, oignons'),
('Burger Cheese', 4000, 'Burger avec double fromage fondu'),
('Burger BBQ', 4500, 'Burger sauce BBQ maison avec bacon'),
('Burger Chicken', 3800, 'Burger au poulet croustillant'),
('Burger Veggie', 3200, 'Burger végétarien aux légumes grillés');

-- Compléments
INSERT INTO complement (nom, prix, type_complement) VALUES
('Coca Cola', 500, 'BOISSON'),
('Sprite', 500, 'BOISSON'),
('Fanta Orange', 500, 'BOISSON'),
('Jus de Bissap', 800, 'BOISSON'),
('Frites Small', 800, 'FRITE'),
('Frites Medium', 1200, 'FRITE'),
('Frites Large', 1500, 'FRITE');

-- Menus
INSERT INTO menu (nom, description) VALUES
('Menu Classic', 'Burger Classic + Frites + Boisson'),
('Menu Cheese', 'Burger Cheese + Frites + Boisson'),
('Menu BBQ', 'Burger BBQ + Frites + Boisson'),
('Menu Chicken', 'Burger Chicken + Frites + Boisson');

-- Composition Menu Classic (id=1)
INSERT INTO menu_composition (menu_id, burger_id, type_element) VALUES (1, 1, 'BURGER');
INSERT INTO menu_composition (menu_id, complement_id, type_element) VALUES (1, 5, 'FRITE');
INSERT INTO menu_composition (menu_id, complement_id, type_element) VALUES (1, 1, 'BOISSON');

-- Composition Menu Cheese (id=2)
INSERT INTO menu_composition (menu_id, burger_id, type_element) VALUES (2, 2, 'BURGER');
INSERT INTO menu_composition (menu_id, complement_id, type_element) VALUES (2, 5, 'FRITE');
INSERT INTO menu_composition (menu_id, complement_id, type_element) VALUES (2, 1, 'BOISSON');

-- Zones de livraison
INSERT INTO zone_livraison (nom, quartiers, prix_livraison) VALUES
('Zone Centre', ARRAY['Plateau', 'Médina', 'Point E'], 1000),
('Zone Nord', ARRAY['Parcelles Assainies', 'Grand Yoff'], 1500),
('Zone Sud', ARRAY['Almadies', 'Ngor', 'Ouakam'], 2000);

COMMIT;