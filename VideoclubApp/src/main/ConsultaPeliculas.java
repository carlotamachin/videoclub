package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsultaPeliculas {

    public static void main(String[] args) {
//esto de aqui es la consulta que se va a realizar en el sql, en la base de datos: entonces estamos haciendo q muestre infromacion de la pelcula
        String sql = "SELECT id_pelicula, titulo, genero, anio, "
                   + "descripcion, precio_alquiler, stock_total "
                   + "FROM peliculas";

        try (Connection conexion = Conexion.conectar();
        		//aqui le estamos diciendo que inicialice la consukta que acabamos de hacer
             PreparedStatement consulta = conexion.prepareStatement(sql);
        		//y con esto q se muestre el resultado de la ejecucuion
             ResultSet resultado = consulta.executeQuery()) {
        	//esto significa q miengtras haya una linea siguiente disponible true, y sino false y pare
            while (resultado.next()) {
                System.out.println("-------------------------");
                System.out.println("ID: " + resultado.getInt("id_pelicula"));
                System.out.println("Título: " + resultado.getString("titulo"));
                System.out.println("Género: " + resultado.getString("genero"));
                System.out.println("Año: " + resultado.getInt("anio"));
                System.out.println("Descripción: "
                        + resultado.getString("descripcion"));
                System.out.println("Precio: "
                        + resultado.getDouble("precio_alquiler") + " €");
                System.out.println("Stock total: "
                        + resultado.getInt("stock_total"));
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar las películas:");
            e.printStackTrace();
        }
    }
}