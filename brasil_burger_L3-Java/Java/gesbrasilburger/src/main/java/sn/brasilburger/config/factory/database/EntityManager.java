package sn.brasilburger.config.factory.database;

import java.util.HashMap;
import java.util.Map;

public final class EntityManager {
    private EntityManager(){}
    public static Map<String, String> persistenceUnit(SgbdName sgbdName){
        switch (sgbdName) {
            case SgbdName.MYSQL:
                return PersistenceUnitMysql();
            case SgbdName.POSTGRESQL:
                return PersistenceUnitPostgresql();
            default:
                throw new IllegalArgumentException("UNKNOW SGBD"+sgbdName);
        }
    }
    private static final Map<String, String> PersistenceUnitMysql(){
        Map<String, String> config = new HashMap<>();
        config.put("driver", "com.mysql.cj.jdbc.Driver");
        config.put("url", "jdbc:mysql://localhost:3306/ges_brasil_burger");
        config.put("user", "root");
        config.put("pwd", "");
        return config;
    }
    /*/
    private static final Map<String, String> PersistenceUnitPostgresql(){
        Map<String, String> config = new HashMap<>();
        config.put("driver", "org.postgresql.Driver");
        config.put("url", "jdbc:postgresql://localhost:5432/ges_brasil_burger");
        config.put("user", "postgres");
        config.put("pwd", "Hassan-1234");
        return config;
    }
        */
    private static final Map<String, String> PersistenceUnitPostgresql(){
    Map<String, String> config = new HashMap<>();
    config.put("driver", "org.postgresql.Driver");
    config.put("url", "jdbc:postgresql://ep-sparkling-cloud-ahju5t0s-pooler.c-3.us-east-1.aws.neon.tech:5432/neondb?sslmode=require&channel_binding=require");
    config.put("user", "neondb_owner");
    config.put("pwd", "npg_uiIs9o6WxwGb");
    return config;
  }

}
