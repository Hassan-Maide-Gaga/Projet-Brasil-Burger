package sn.brasilburger.config.factory.repository;

import sn.brasilburger.config.database.*;
import sn.brasilburger.config.factory.EntityName;
import sn.brasilburger.config.factory.database.DatabaseFactory;
import sn.brasilburger.repository.Bd.*;

public final class RepositoryFactory {

    private RepositoryFactory(){
    }    
    public static Object createRepository(EntityName entity){
        Database database = DatabaseFactory.getInstance();
        switch (entity) {
            case BURGER:
                return BurgerRepositoryBd.getInstance(database);
            case COMPLEMENT:
                return ComplementRepositoryBd.getInstance(database);
            case LIVREUR:
                return LivreurRepositoryBd.getInstance(database);
            case LOGIN:
                return LoginRepositoryBd.getInstance(database);
            case MENU:
                return MenuRepositoryBd.getInstance(database);
            case ZONE:
                return ZoneRepositoryBd.getInstance(database);
            case MENU_COMPLEMENT:
                return MenuComplementRepositoryBd.getInstance(database);
            default:
            throw new IllegalArgumentException("Unknon Entity: "+ entity);
        }
    }
}
