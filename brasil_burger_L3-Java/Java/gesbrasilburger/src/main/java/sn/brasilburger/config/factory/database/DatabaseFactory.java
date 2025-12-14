package sn.brasilburger.config.factory.database;
import sn.brasilburger.config.database.*;
public final class DatabaseFactory {
    private static final SgbdName sgbdName = SgbdName.POSTGRESQL;
    private DatabaseFactory(){}
    public static Database getInstance(){
        return DatabaseImpl.getInstance(EntityManager.persistenceUnit(sgbdName));
    }
}
