import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaSeH2Memory {

    public static void main(String[] args) throws Exception {

        String url = "jdbc:h2:mem:";



        try {
            Connection con = DriverManager.getConnection(url);
             Statement stm = con.createStatement();



        ResultSet rs = stm.executeQuery("SELECT 1+1");


            if (rs.next()) {

                System.out.println(rs.getInt(1));
            }


        stm.executeUpdate(
                "CREATE TABLE cars(id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), price INT);" +
                        " INSERT INTO cars(name, price) VALUES('Audi', 52642);" +
                        " INSERT INTO cars(name, price) VALUES('Mercedes', 57127);" +
                        " INSERT INTO cars(name, price) VALUES('Skoda', 9000);" +
                        " INSERT INTO cars(name, price) VALUES('Volvo', 29000);" +
                        " INSERT INTO cars(name, price) VALUES('Bentley', 350000);" +
                        " INSERT INTO cars(name, price) VALUES('Citroen', 21000);" +
                        " INSERT INTO cars(name, price) VALUES('Hummer', 41400);" +
                        " INSERT INTO cars(name, price) VALUES('Volkswagen', 21600);");




        rs=stm.executeQuery("SELECT count (*) from cars where price>30000");

            if (rs.next()) {

                System.out.println(rs.getInt(1));
            }



        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(JavaSeH2Memory.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }


    }
}