package Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.leapmotion.leap.Vector;

import Bean.MyFinger;

public class RecognizeSentence {

	//public static RecognizeChar RC = new RecognizeChar();
	public ArrayList SignIdList = new ArrayList(); 
	public ArrayList SignList = new ArrayList();
	
	
	public void main(String[] args) throws Exception {
		// test
		CollectSampleSent("A C A");
		SignList = TransIdToSign(SignIdList);

		for (int i = 0; i < SignList.size(); i++) {
			long begin = System.currentTimeMillis();
			Thread.sleep(1500);
			RecognizeChar RC = new RecognizeChar();
			RC.CollectSample(SignList.get(i).toString());
			RC.ReadSign(true);

		}

		// ReadSign();

	}
	
	public ArrayList TransIdToSign(ArrayList signIdList2) {
		// TODO Auto-generated method stub

		ArrayList temp = new ArrayList();
		
		for (int i = 0; i < signIdList2.size(); i++){
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				System.out.println("Load the mysql drive successfully!");
			} catch (ClassNotFoundException e1) {
				System.out.println("Cannot find mysql drive!");
				e1.printStackTrace();
			}

			String url = "jdbc:mysql://localhost:3306/mysql";

			Connection conn;
			try {
				conn = DriverManager.getConnection(url, "root", "Carol");
				Statement stmt = conn.createStatement();
				System.out.print("Connected to JDBC!");

				System.out.println();
				System.out.print("---------------------------------------------");
				System.out.println();

				String sql = "select sign from isign.sign_info where id_sign_info = '" + signIdList2.get(i).toString() + "'";
				PreparedStatement pst = conn.prepareStatement(sql);
				System.out.println(sql);
				// pst.setString(1, SignString);

				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next()) {
					System.out.println("Sign is " + rs.getString(1));
					temp.add(rs.getString(1));
					System.out.println("The to-be-collected sign name: "  + temp.toString());

				}

				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return temp;
		
	}
	
	public  void CollectSampleSent(String Sentence) {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Load the mysql drive successfully!");
		} catch (ClassNotFoundException e1) {
			System.out.println("Cannot find mysql drive!");
			e1.printStackTrace();
		}

		String url = "jdbc:mysql://localhost:3306/mysql";

		Connection conn;
		try {
			conn = DriverManager.getConnection(url, "root", "Carol");
			Statement stmt = conn.createStatement();
			System.out.print("Connected to JDBC!");

			System.out.println();
			System.out.print("---------------------------------------------");
			System.out.println();

			String sql = "select * from isign.sentence_info where sent = '" + Sentence + "'";
			PreparedStatement pst = conn.prepareStatement(sql);
			System.out.println(sql);
			// pst.setString(1, SignString);

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				System.out.println("Sentence id " + rs.getInt(1));
				System.out.println("Sentence " + rs.getString(2));
				System.out.println("Sentence size " + rs.getInt(3));
				System.out.println("Sentence sequence " + rs.getInt(4));
				System.out.println("Sentence sign id " + rs.getInt(5));
				
				SignIdList.add(rs.getInt(5));
				System.out.println("The to-be-collected sign id: "  + SignIdList.toString());

			}

			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
