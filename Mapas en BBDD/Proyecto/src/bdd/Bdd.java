/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bdd;

import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Crea una conexion con la base de datos para subir los mapas ya editados como
 * ultima version
 *
 * @author Víctor y Nestor
 */
public class Bdd {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        try {
            /**
             * Cargamos el driver
             */

            Class.forName("com.mysql.cj.jdbc.Driver");

            /**
             * Cargamos los datos de la conexion
             */
            Connection conn = null;
            try {//CONECTA TANTO AL PUERTO 3307 COMO AL 3306
                String url = "jdbc:mysql://localhost:3307/Doomies";
                String user = "administrador";
                String pass = "1234";
                conn = DriverManager.getConnection(url, user, pass);
            } catch (SQLException e) {
                String url = "jdbc:mysql://localhost:3306/Doomies";
                String user = "administrador";
                String pass = "1234";
                conn = DriverManager.getConnection(url, user, pass);
            }
            /**
             * Aqui ya ha empezado la conexion
             */
            /**
             * Borramos los datos dentro
             */
            Bdd.deleteAllFromData(conn);
            /**
             * Voy cargando todos los mapas hasta que no haya mas
             */
            boolean error = false;
            int i = 1;
            while (!error) {
                String mapaActual = getMap(i);
                if (mapaActual == null) {
                    error = true;
                    break;
                }
                SubirMapa(i, mapaActual, conn);
                i++;
            }
            /**
             * Cierro las conexiones
             */
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new Frame(), "" + (((e + "").contains("Communications link failure")) ? "No hay conexion con la base de datos\n" : "Conexion a la base de datos fallida, compruebe que la base de datos esta operativa: " + e), "Conexion fallida", 2);
            System.exit(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new Frame(), "ERROR DESCONOCIDO: " + e, "Conexion fallida", 2);
            System.exit(0);
        }
    }

    /**
     * Carga el mapa en una variable
     *
     * @param index El numero del mapa a cargar
     * @return El mapa en String
     * @throws URISyntaxException Da error si la localizacion no existe
     */
    private static String getMap(int index) throws URISyntaxException {
        String ruta = Bdd.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString().substring(1).replaceAll("Cargador a BDD.jar", "");
        File f = new File(ruta + "/mapas/mapa" + index + ".map");
        String rawString = "";
        if (!f.exists()) {
            return null;
        }
        try {
            Scanner sc = new Scanner(f);
            while (sc.hasNext()) {
                rawString += sc.next() + "\n";
            }
            sc.close();
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR FATAL");
            return null;
        }
        return rawString;
    }

    /**
     * Sube el mapa anteriormente cargado
     *
     * @param index Para introducir en los campos de id_nivel y id_mapa
     * @param mapa El mapa a subir
     * @param conn Conexion a la base de datos
     * @throws SQLException Lanza la excepcion si no permite la subida
     */
    public static void SubirMapa(int index, String mapa, Connection conn) throws SQLException {
        /**
         * Subimos los tiles del mapa
         */
        String mapaTiles = mapa.split(":")[0];
        String lines[] = mapaTiles.split("\n");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = reemplazarANull(lines[i]);
        }
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO mapa VALUES ('" + index + "','" + lines[0] + "','" + lines[1] + "','" + lines[2] + "','" + lines[3] + "','" + lines[4] + "','" + lines[5] + "','" + lines[6] + "','" + lines[7] + "','" + lines[8] + "','" + lines[9] + "','"
                + lines[10] + "','" + lines[11] + "','" + lines[12] + "');");

        stmt = conn.createStatement();
        stmt.execute("INSERT INTO nivel VALUES(" + index + ",+" + index + ",NOW());");

        /**
         * Subimos los enemigos dentro del mapa id enemigo AUTOINCREMENTAL tipo
         * x y id mapa
         */
        try {
            String mapaEnemigos = mapa.split(":")[1];
            String[] enemies = mapaEnemigos.split("\n");
            for (int i = 1; i < enemies.length; i++) {
                String[] line = enemies[i].split(";");
                int tipo = Integer.parseInt(line[0]);
                int x = Integer.parseInt(line[1]);
                int y = Integer.parseInt(line[2]);
                stmt = conn.createStatement();

                stmt.executeUpdate("INSERT INTO enemigo (tipo,posicion_x,posicion_y,id_nivel) VALUES (" + tipo + "," + x + "," + y + "," + index + ");");
            }
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Clase que remplaza todas las xx por vacios para ocupar menos espacio en
     * la bdd
     *
     * @param cadena Linea a sustituir
     * @return Linea formateada
     */
    private static String reemplazarANull(String cadena) {
        if ((cadena.length() + 1) % 3 != 0) {//tiene que ser divisible entre 3 para que este el formato bien porque coje xx- y es +1 porque los ultimos 2 no acaban en -
            System.err.println("DA ERROR");
            System.exit(0);
        }
        for (int i = 0; i < cadena.length(); i += 3) {//Se hace un for para comprobar que lo que se meten son parejas de xx o de numeros y no otra cosa
            if (cadena.substring(i, i + 2).equals("xx")) {//Se comprueba que lo que se mete es una pareja de xx y no otras letras.
            } else {
                try {
                    int block = Integer.parseInt(cadena.substring(i, i + 2));//Se comprueba que lo que se mete es una pareja de numeros y no otras cosas
                } catch (Exception e) {
                    System.err.println("DA ERROR");
                    System.exit(0);
                }
            }
            if (i == cadena.length() - 2) {//Si son los 2 ultimos se los salta y continua
                continue;
            }
            if (!cadena.substring(i + 2, i + 3).equals("-")) {//Se comprueba que en cada 3 posicion haya un - , sino da un error
                System.err.println("DA ERROR");
                System.exit(0);
            }
        }
        cadena = cadena.replace("xx", "");//Se remplazan las xx por espacios vacios para optimizar espacio
        return cadena;
    }

    /**
     * Elimina toda la informacion en la base de datos dejandola vacia
     *
     * @param conn Conexion
     * @throws SQLException Excepcion capturada en main
     */
    public static void deleteAllFromData(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("Delete FROM doomies.enemigo;");
        stmt.execute("Delete FROM doomies.nivel;");
        stmt.execute("Delete FROM doomies.mapa;");
        stmt.close();
    }

}
