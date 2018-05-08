package org.diegoauyon.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;

public class Conexion {
    private Connection conexion;
    private Statement sentencias;
    private static Conexion instancia;

    @SuppressWarnings("CallToPrintStackTrace")
    public Conexion() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            conexion = DriverManager.getConnection("jdbc:sqlserver://LABC26-01;databaseName=DBNextSport2014277;user=dauyon;password=guatemala123456");
//            conexion = DriverManager.getConnection("jdbc:sqlserver://D3H;databaseName=DBNextSport2014277;user=dauyon;password=guatemala123456");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

}