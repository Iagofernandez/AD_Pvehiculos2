
package pvehiculos2;


import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.bson.Document;

public class Pvehiculos2 {
 /*
        Ejercicio de manejo de bases de datos
        
        1º) Recogemos datos de una base de Oracle
        2º) Luego con los datos recogidos, se emplean para sacar datos de mongoDB
        3º) Finalmente se deben insertar los datos en un objectDB
        */
    
    /*
    VARIABLES DE DATOS ORACLE
    */
    private static double id;
    private static String dni;
    private static String codveh;
    private static java.math.BigDecimal tasas;
    private static double pf;
    private static double tasa;
    
    /*
    Hay que cambiarle el nombre a las clases por odB?
    */
    
    /*
    Variables static generales Mongo
    */
    private static MongoClient mongocliente;
    private static MongoDatabase database;
    private static MongoCollection collection;
    
    /*
    VARIABLES DE DATOS MONGO PRIMERA COLLECTION
    */
    
    private static String id2;
    private static String nomveh;
    private static double prezoorixe;
    private static double anomatricula;
    
    /*
    VARIABLES DE DATOS MONGO SEGUNDA COLLECTION
    */
    private static String id3;
    private static String nomec;
    private static double ncompras;
    
    public void conexion() throws SQLException{
        /*
        Realizando conexiones 
        */
        
        Connection connection;
        String driver = "jdbc:oracle:thin:";
        String host = "localhost.localdomain"; 
        String porto = "1521";
        String sid = "orcl";
        String usuario = "hr";
        String password = "hr";
        String url = driver + usuario + "/" + password + "@" + host + ":" + porto + ":" + sid;
        
        connection = DriverManager.getConnection(url);
        
        PreparedStatement ps = connection.prepareStatement("select * from vendas where id<=5");
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()){
            id = rs.getDouble("id");
            System.out.println("id " + id);
            dni = rs.getString("dni");
            System.out.println("dni " + dni);
            
            //Obteniendo datos del objeto tipo
            // Se realiza diferente
            
            java.sql.Struct jss = (java.sql.Struct) rs.getObject(3);
            
            Object[] campos = jss.getAttributes();
            
            codveh = (String) campos[0];
            tasas = (BigDecimal) campos[1];
            tasa = tasas.doubleValue();
            
            System.out.println("codveh " + codveh);
            System.out.println("tasas " + tasas);
            
        }
        
        
        /*
        Conexion a mongoDb y a la collection
        */
        
        mongocliente = new MongoClient("localhost", 27017);
        database = mongocliente.getDatabase("basevehiculos");
        collection = database.getCollection("vehiculos");
        
        
        FindIterable<Document> datosmongo = collection.find();
        
        for(Document doc: datosmongo){
            id2 = doc.getString("_id");
            System.out.println("_id " + id2);
            nomveh= doc.getString("nomveh");
            System.out.println("nomveh " + nomveh);
            prezoorixe = doc.getDouble("prezoorixe");
            System.out.println("prezoorixe " + prezoorixe);
            anomatricula = doc.getDouble("anomatricula");
            System.out.println("anomatricula " + anomatricula);
        }
        
        System.out.println("*************************************************************");
       
        
        collection = database.getCollection("clientes");
        
        FindIterable<Document> datosmongo2 = collection.find();
        for(Document doc: datosmongo2){
            id3 = doc.getString("_id");
            System.out.println("_id " + id3);
            nomec = doc.getString("nomec");
            System.out.println("nomec " + nomec);
            ncompras = doc.getDouble("ncompras");
            System.out.println("ncompras " + ncompras);
            
        }
        
        mongocliente.close();
        
       
        System.out.println("ncompras " + ncompras);
        if(ncompras>=1){
            pf = prezoorixe-((2019-anomatricula)*500)- 500 + tasa;
        }else{
             pf = prezoorixe-((2019-anomatricula)*500)- 0 + tasa;
        }
        
        
        
//        /*
//        Conexion a objectDB
//        */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("/home/oracle/objectdb-2.7.5_01/db/finalveh.odb");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Venfin ver = new Venfin(id, dni, nomec, nomveh, pf);
        
        em.persist(ver);
        em.getTransaction().commit();
        
        em.close();
        
        connection.close();
        
    }
    public static void main(String[] args) throws SQLException {
       Pvehiculos2 veh = new Pvehiculos2();
       veh.conexion();
    }
    
}
