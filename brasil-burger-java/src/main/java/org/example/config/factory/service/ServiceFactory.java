package org.example.config.factory.service;

import org.example.Service.*;

public final class ServiceFactory {

    private ServiceFactory() {
    }

    public static BurgerService getBurgerService() {
        return new BurgerServiceImpl();
    }

    public static MenuService getMenuService() {
        return new MenuServiceImpl();
    }

    public static ComplementService getComplementService() {
        return new ComplementServiceImpl();
    }

    public static CommandeService getCommandeService() {
        return new CommandeServiceImpl();
    }

    public static ClientService getClientService() {
        return new ClientServiceImpl();
    }
}