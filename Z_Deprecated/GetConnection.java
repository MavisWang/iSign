package Z_Deprecated;

import java.sql.*;

public class GetConnection {
    public static void main(String[] args){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Load the mysql drive successfully!");
        }catch(ClassNotFoundException e1){
            System.out.println("Cannot find mysql drive!");
            e1.printStackTrace();
        }
        
        String url="jdbc:mysql://localhost:3306/mysql";   

        Connection conn;
        try {
            conn = DriverManager.getConnection(url,    "root","Carol");
            Statement stmt = conn.createStatement();
            System.out.print("Connected to JDBC!");
            
            String sql = "select * from isign.sign_info";
            System.out.println();
            System.out.print("---------------------------------------------");
            System.out.println();
            ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()){
                    System.out.print(rs.getInt(1) + "\t");
                    System.out.print(rs.getString(2) + "\t");
                    System.out.print(rs.getInt(3) + "\t");
                    System.out.print(rs.getDouble(4) + "\t");
                    System.out.print(rs.getInt(5) + "\t");
                    System.out.print(rs.getInt(6) + "\t");
                    System.out.print(rs.getInt(7) + "\t");
                    System.out.print(rs.getInt(8) + "\t");
                    System.out.print(rs.getInt(9) + "\t");
                    System.out.println();
                }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        
        
    }
}