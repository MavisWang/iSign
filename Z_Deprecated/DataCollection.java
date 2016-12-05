package Z_Deprecated;

import java.sql.*;

public class DataCollection {
  public static void main(String[] args)throws Exception{
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
            		  
          String sql2 = "insert into isign.sign_info (sign, if_right_hand, sign_likelihood, thumb_id, index_id, middle_id, ring_id, pinky_id) values (?, ?, ?, ?, ?, ?, ?, ?)";
          PreparedStatement pst = conn.prepareStatement(sql2);
          pst.setString(1, "D");
          pst.setInt(2, 1);
          pst.setDouble(3, 0.8);;      
          pst.setInt(4, 16);
          pst.setInt(5, 17);
          pst.setInt(6, 18);
          pst.setInt(7, 19);
          pst.setInt(8, 20);
          pst.executeUpdate();
              
          //删除数�?�的代�?
          String sql3 = "delete from isign.sign_info where sign = ?";
          pst = conn.prepareStatement(sql3);
          pst.setString(1, "D");
          pst.executeUpdate();
              
          ResultSet rs2 = stmt.executeQuery(sql);//创建数�?�对象
          System.out.println();
          System.out.print("---------------------------------------------");
          System.out.println();
          while (rs2.next()){
              System.out.print(rs2.getInt(1) + "\t");
              System.out.print(rs2.getString(2) + "\t");
              System.out.print(rs2.getInt(3) + "\t");
              System.out.print(rs2.getDouble(4) + "\t");
              System.out.print(rs2.getInt(5) + "\t");
              System.out.print(rs2.getInt(6) + "\t");
              System.out.print(rs2.getInt(7) + "\t");
              System.out.print(rs2.getInt(8) + "\t");
              System.out.print(rs2.getInt(9) + "\t");
              System.out.println();
          }
        	
              
          rs.close();
          rs2.close();
          stmt.close();
          conn.close();
          }catch(Exception e)
          {
              e.printStackTrace();
          }
  }
}