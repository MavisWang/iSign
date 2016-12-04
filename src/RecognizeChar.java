package Service;

import java.awt.*;
//import java.awt.Image;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Image;
import com.leapmotion.leap.ImageList;
import com.leapmotion.leap.Vector;

import Bean.MyFinger;
import Bean.Sign;

//import processing.core.PImage;
//import processing.core.PApplet;

public class RecognizeChar {

	public Sign SignSample;
	public Map<Integer, String> FingerId;
	public Map<Integer, String> BoneId;

	public int finger_id;
	public int bone_id;

	public float Comparasion[] = new float[20];
	public float Similarity;
	
	//test to be true; should be false
	public boolean Success = false;

	public void main(String[] args) throws Exception {
		CollectSample("C");
		ReadSign(false);

	}

	public void CollectSample(String SignString) {
		// TODO Auto-generated method stub
		SignSample = new Sign();
		FingerId = new HashMap<Integer, String>();
		BoneId = new HashMap<Integer, String>();
		
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

			String sql = "select * from isign.sign_info where sign = '" + SignString + "'";
			System.out.println(sql);
			PreparedStatement pst = conn.prepareStatement(sql);
			// pst.setString(1, SignString);

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				System.out.println("Sign id  " + rs.getInt(1));
				//System.out.println("Sign right hand  " + rs.getInt(3));
				//System.out.println("Sign likelihood  " + rs.getDouble(4));
				//System.out.println("TYPE_THUMB  " + rs.getInt(5));
				//System.out.println("TYPE_INDEX  " + rs.getInt(6));
				//System.out.println("TYPE_MIDDLE  " + rs.getInt(7));
				//System.out.println("TYPE_RING  " + rs.getInt(8));
				//System.out.println("TYPE_PINKY  " + rs.getInt(9));

				FingerId.put(rs.getInt(5), "TYPE_THUMB");
				FingerId.put(rs.getInt(6), "TYPE_INDEX");
				FingerId.put(rs.getInt(7), "TYPE_MIDDLE");
				FingerId.put(rs.getInt(8), "TYPE_RING");
				FingerId.put(rs.getInt(9), "TYPE_PINKY");

				SignSample.Sign_name = SignString;
				SignSample.Is_Right_Hand = rs.getInt(3);

				SignSample.FingerMap.put("TYPE_THUMB", new MyFinger());
				SignSample.FingerMap.put("TYPE_INDEX", new MyFinger());
				SignSample.FingerMap.put("TYPE_MIDDLE", new MyFinger());
				SignSample.FingerMap.put("TYPE_RING", new MyFinger());
				SignSample.FingerMap.put("TYPE_PINKY", new MyFinger());

			}

			System.out.println("Sign_info finished, Start finger info!!");

			for (Entry<Integer, String> entry : FingerId.entrySet()) {

				finger_id = entry.getKey();
				String sql2 = "select * from isign.finger_info where finger_id = '" + finger_id + "'";
				PreparedStatement pst2 = conn.prepareStatement(sql2);
				System.out.println("Finger_info started!! " + sql2);
				ResultSet rs2 = stmt.executeQuery(sql2);
				while (rs2.next()) {
					//System.out.println("TYPE_METACARPAL  " + rs2.getInt(3));
					//System.out.println("TYPE_PROXIMAL  " + rs2.getInt(4));
					//System.out.println("TYPE_INTERMEDIATE  " + rs2.getInt(5));
					//System.out.println("TYPE_DISTAL  " + rs2.getInt(6));

					BoneId.put(rs2.getInt(3), "TYPE_METACARPAL");
					BoneId.put(rs2.getInt(4), "TYPE_PROXIMAL");
					BoneId.put(rs2.getInt(5), "TYPE_INTERMEDIATE");
					BoneId.put(rs2.getInt(6), "TYPE_DISTAL");

					System.out.println(entry.getKey());

					SignSample.FingerMap.get(entry.getValue()).BoneMap.put("TYPE_METACARPAL", new Vector());
					SignSample.FingerMap.get(entry.getValue()).BoneMap.put("TYPE_PROXIMAL", new Vector());
					SignSample.FingerMap.get(entry.getValue()).BoneMap.put("TYPE_INTERMEDIATE", new Vector());
					SignSample.FingerMap.get(entry.getValue()).BoneMap.put("TYPE_DISTAL", new Vector());

				}

				for (Entry<Integer, String> entry2 : BoneId.entrySet()) {

					bone_id = entry2.getKey();
					String sql3 = "select * from isign.bone_info where bone_id = '" + bone_id + "'";
					PreparedStatement pst3 = conn.prepareStatement(sql3);
					//System.out.println("Bone_info started!! " + sql3);
					ResultSet rs3 = stmt.executeQuery(sql3);
					while (rs3.next()) {
						//System.out.println("X_axis  " + rs3.getFloat(3));
						//System.out.println("Y_axis  " + rs3.getFloat(4));
						//System.out.println("Z_axis  " + rs3.getFloat(5));

						SignSample.FingerMap.get(entry.getValue()).BoneMap.get(entry2.getValue()).setX(rs3.getFloat(3));
						SignSample.FingerMap.get(entry.getValue()).BoneMap.get(entry2.getValue()).setY(rs3.getFloat(4));
						SignSample.FingerMap.get(entry.getValue()).BoneMap.get(entry2.getValue()).setZ(rs3.getFloat(5));

						//System.out.println(SignSample.Sign_name);
						//System.out.println(
						//		SignSample.FingerMap.get(entry.getValue()).BoneMap.get(entry2.getValue()).toString());
					}

				}

			}

			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void ReadSign(boolean timer) {
		// TODO Auto-generated method stub
		
		if(!timer){
			


		System.out.println("--------------------------------");
		System.out.println("Collection started!!");

		LeapListener listener = new LeapListener();
		Controller controller = new Controller();
		
		Success = false;

		controller.addListener(listener);

		System.out.println("Start comparasion!!!");

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while (!Success) {
			int i = 0;

			for (Hand hand : controller.frame().hands()) {
				if(hand.isRight() == true && SignSample.Is_Right_Hand == 1 
						|| hand.isLeft() == true && SignSample.Is_Right_Hand == 0){
					System.out.println("The correct hand!!! " + SignSample.Sign_name);
				}else{
					break;
				}
			}

			for (Finger finger : controller.frame().fingers()) {
				//System.out.println("--------------------------------------------------------");
				//System.out.println(
						// Each finger
				//		"Finger Type: " + finger.type()
						// Every 5 records are for a single hand
				//				+ " ID: " + finger.id());

				for (Bone.Type boneType : Bone.Type.values()) {
					Bone bone = finger.bone(boneType);

					if(finger.type().toString() == "TYPE_THUMB"){
						if(bone.type().toString() == "TYPE_METACARPAL"){
							Comparasion[i] = 1;
							System.out.println(SignSample.Sign_name + " Comparasion for thumb " + i + ": " + Comparasion[i++] + " " + finger.type() + " " + bone.type());
						}else{
							Comparasion[i] = bone.direction().dot(SignSample.FingerMap.get(finger.type().toString()).BoneMap.get(bone.type().toString()));
							System.out.println(SignSample.Sign_name + " Comparasion for " + i + ": " + Comparasion[i++] + " " + finger.type() + " " + bone.type());
						}
					}else{
						Comparasion[i] = bone.direction().dot(SignSample.FingerMap.get(finger.type().toString()).BoneMap.get(bone.type().toString()));
						System.out.println(SignSample.Sign_name + " Comparasion for " + i + ": " + Comparasion[i++] + " " + finger.type() + " " + bone.type());
					}

				}

			}
			
			
			Success = IsCorrectSign(Comparasion);
		}

		System.out.println("Successfully match the sign!!!");
		controller.removeListener(listener);
		}else{
		
		long begin = System.currentTimeMillis();	


		System.out.println("--------------------------------");
		System.out.println("Collection started!!");

		LeapListener listener = new LeapListener();
		Controller controller = new Controller();
		
		Success = false;

		controller.addListener(listener);

		System.out.println("Start comparasion!!!");

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while (!Success && System.currentTimeMillis() - begin < 10000) {
			int i = 0;

			for (Hand hand : controller.frame().hands()) {
				if(hand.isRight() == true && SignSample.Is_Right_Hand == 1 
						|| hand.isLeft() == true && SignSample.Is_Right_Hand == 0){
					System.out.println("The correct hand!!! " + SignSample.Sign_name);
				}else{
					break;
				}
			}

			for (Finger finger : controller.frame().fingers()) {
				//System.out.println("--------------------------------------------------------");
				//System.out.println(
						// Each finger
				//		"Finger Type: " + finger.type()
						// Every 5 records are for a single hand
				//				+ " ID: " + finger.id());

				for (Bone.Type boneType : Bone.Type.values()) {
					Bone bone = finger.bone(boneType);

					if(finger.type().toString() == "TYPE_THUMB"){
						if(bone.type().toString() == "TYPE_METACARPAL"){
							Comparasion[i] = 1;
							System.out.println(SignSample.Sign_name + " Comparasion for thumb " + i + ": " + Comparasion[i++] + " " + finger.type() + " " + bone.type());
						}else{
							Comparasion[i] = bone.direction().dot(SignSample.FingerMap.get(finger.type().toString()).BoneMap.get(bone.type().toString()));
							System.out.println(SignSample.Sign_name + " Comparasion for " + i + ": " + Comparasion[i++] + " " + finger.type() + " " + bone.type());
						}
					}else{
						Comparasion[i] = bone.direction().dot(SignSample.FingerMap.get(finger.type().toString()).BoneMap.get(bone.type().toString()));
						System.out.println(SignSample.Sign_name + " Comparasion for " + i + ": " + Comparasion[i++] + " " + finger.type() + " " + bone.type());
					}

				}

			}
			
			
			Success = IsCorrectSign(Comparasion);
		}

		if (Success){

		System.out.println("Successfully match the sign!!!");
		}else{
			System.out.println("Cannot match!!!");
		}
		controller.removeListener(listener);
		}

	}

	private boolean IsCorrectSign(float[] comparasion2) {
		// TODO Auto-generated method stub
		int Val = 0;
		
		for(int i = 0; i < 20; i++){
			if (comparasion2[i] > 0.7){
				Val++;
			}
		}
		
		System.out.println("The matched vector: " + Val);
		
		if(Val >= 20*0.7){
			return true;
		}else{
			return false;
		}
	}
	
}
