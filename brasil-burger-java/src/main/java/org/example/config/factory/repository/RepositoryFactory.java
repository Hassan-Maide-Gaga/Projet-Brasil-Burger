package org.example.config.factory.repository;

import org.example.Repository.*;

public final class RepositoryFactory {

    private RepositoryFactory() {
    }

    public static BurgerRepository getBurgerRepository() {
        return new BurgerRepositoryImpl();
    }

    public static MenuRepository getMenuRepository() {
        return new MenuRepositoryImpl();
    }

    public static ComplementRepository getComplementRepository() {
        return new ComplementRepositoryImpl();
    }

    public static CommandeRepository getCommandeRepository() {
        return new CommandeRepositoryImpl();
    }

    public static ClientRepository getClientRepository() {
        return new ClientRepositoryImpl();
    }
}