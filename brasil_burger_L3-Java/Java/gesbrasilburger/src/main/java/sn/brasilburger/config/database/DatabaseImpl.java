package sn.brasilburger.config.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;

public class DatabaseImpl implements Database {
    private Connection connection;
    private static DatabaseImpl instance=null;
    private DatabaseImpl(String driver, String url, String user, String pwd){
        this.connection = openConnection(driver,url,user,pwd);
    }
    private DatabaseImpl(Map<String, String> config){
        String driver = config.get("driver");
        String url = config.get("url");
        String user = config.get("user");
        String pwd = config.get("pwd");
        this.connection = openConnection(driver,url,user,pwd);
    }
    public static DatabaseImpl getInstance(Map<String, String> config){
        if(instance == null){
            return instance = new DatabaseImpl(config);
        }
        return instance;
    }
    public static DatabaseImpl getInstance(String driver, String url, String user, String pwd){
        if(instance == null){
            return instance = new DatabaseImpl(driver, url, user, pwd);
        }
        return instance;
    }
    @Override
    public Connection getConnection(){
        return this.connection;
    }
    @Override
    public boolean isConnected(){
        return this.getConnection()!=null;
    }
    @Override
    public void closeConnection(){
        if (isConnected()) {
            try {
                this.connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private Connection openConnection(String driver, String url, String user, String pwd){
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url,user,pwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public <T> Optional<T> fetch(PreparedStatement ps, Convert<T> convert) throws SQLException{
        ResultSet rs = ps.executeQuery();
        T data = null;
        if (rs.next()) {
            data = convert.toEntity(rs);
        }
        return Optional.of(data);
    }
    @Override
    public <T> List<T> fetchAll(PreparedStatement ps, Convert<T> convert) throws SQLException{
        ResultSet rs = ps.executeQuery();
        List<T> data = new ArrayList<>();
        while (rs.next()) {
            data.add(convert.toEntity(rs));
        }
        return data;
    }
}
