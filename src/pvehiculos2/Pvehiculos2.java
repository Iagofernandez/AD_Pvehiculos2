
package pvehiculos2;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Pvehiculos2 {
 /*
        Ejercicio de manejo de bases de datos
        
        1º) Recogemos datos de una base de Oracle
        2º) Luego con los datos recogidos, se emplean para sacar datos de mongoDB
        3º) Finalmente se deben insertar los datos en un objectDB
        */
    
    /*
    Hay que cambiarle el nombre a las clases por odB?
    */
    
    /*
    Variables static generales
    */
    private static MongoClient mongocliente;
    private static MongoDatabase database;
    private static MongoCollection collection;
    
    
    
    
    public void conexion() throws SQLException{
        /*
        Realizando conexiones 
        */
        
        Connection connection;
        String driver = "jdbc:oracle:thin:";
        String host = "localhost.localdomain"; // tambien puede ser una ip como "192.168.1.14"
        String porto = "1521";
        String sid = "orcl";
        String usuario = "hr";
        String password = "hr";
        String url = driver + usuario + "/" + password + "@" + host + ":" + porto + ":" + sid;
        
        connection = DriverManager.getConnection(url);
        
        /*
        Conexion a mongoDb y a la collection
        */
        
        mongocliente = new MongoClient("loclahost", 27017);
        database = mongocliente.getDatabase("test");
        collection = database.getCollection("vendas");
        
        /*
        Conexion a objectDB
        */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("/home/oracle/objectdb-2.7.5_01/db/vehicli.odb");
        
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
        
        
        
        connection.close();
        
    }
    public static void main(String[] args) {
       
    }
    
}
