package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.event.ActionEvent;

import javax.swing.*;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

import Bean.MyFinger;
import Bean.Sign;
import Service.LeapListener;

public class CollectionUI {

	public static String Sign2Coll = "";
	public static String out = "Only support single hand for now! Sorry for the inconvenience! \nCollected as follow: \n";
	public static JTextArea SignOutput = new JTextArea();
	public static int count = 1;
	public static Vector zero = Vector.zero();
	public static int max;

	public static Map<Integer, Sign> SignMap = new HashMap<Integer, Sign>();

	public static void main(String[] args) {

		// UI structure
		JFrame frame = new JFrame("iSign - Sign Language Collection");
		frame.setSize(1000, 900);
		int Pos_w = (Toolkit.getDefaultToolkit().getScreenSize().width - 1000) / 2;
		int Pos_h = (Toolkit.getDefaultToolkit().getScreenSize().height - 900) / 2;
		frame.setLocation(Pos_w, Pos_h);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(400, 100));
		placeComponents(panel);
		frame.add(panel);
		frame.setVisible(true);
	}

	public static void placeComponents(JPanel panel) {

		panel.setLayout(null);
		//panel.setBackground(new Color(102, 102, 102));

		JLabel SignLabel = new JLabel("Sign:");
		SignLabel.setFont(new Font("", 0, 25));
		SignLabel.setBounds(10, 30, 80, 50);
		panel.add(SignLabel);

		final JTextField SignText = new JTextField(20);
		SignText.setBounds(100, 30, 165, 50);
		SignText.setFont(new Font("", 0, 25));
		panel.add(SignText);
		final String temp = SignText.getText();

		JButton Start = new JButton("Start to collect!");
		Start.setBounds(10, 100, 200, 50);
		Start.setFont(new Font("", 0, 20));
		Start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Sign2Coll = SignText.getText();
				System.out.println("Successfully get the sign " + Sign2Coll + ". Start collecting!");
				collect(Sign2Coll);
			}
		});

		panel.add(Start);

		JButton Save = new JButton("Save to DB");
		Save.setBounds(230, 100, 200, 50);
		Save.setFont(new Font("", 0, 20));
		Save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Start to save to DB!!");
				saveSign(SignMap);
				System.out.println("Saved to DB!!");
			}
		});

		panel.add(Save);

		JButton Delete = new JButton("Delete");
		Delete.setBounds(450, 100, 200, 50);
		Delete.setFont(new Font("", 0, 20));
		Delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Sign2Coll = SignText.getText();
				System.out.println("Start to delete from DB!!" + Sign2Coll);
				DeleteSign(Sign2Coll);
				System.out.println("Deleted from DB!!");
			}
		});
		panel.add(Delete);

		SignOutput.setTabSize(4);
		SignOutput.setFont(new Font("", 0, 20));
		SignOutput.setText(out);
		SignOutput.setCaretPosition(SignOutput.getDocument().getLength());

		JScrollPane scrollPane = new JScrollPane(SignOutput);
		scrollPane.setBounds(10, 180, 950, 650);

		//panel.add(SignOutput);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		//ImageIcon BG = new ImageIcon("/pics/background.png");
		JLabel background = new JLabel();
		
		Image img = Toolkit.getDefaultToolkit().getImage ("/pics/background.png");
		background.setIcon (new ImageIcon(img));
		background.setPreferredSize(new java.awt.Dimension(1024, 700));
        background.setRequestFocusEnabled(false);
		//background.setText("dddddddddddddddddddd");
		
		//background.setIcon(new ImageIcon("/pics/background.png")); // NOI18N
		background.setBounds(0, 0, 1000, 900);
		panel.add(background);
	}

	//Buggy, to be debugged
	public static void DeleteSign(String sign2Coll2) {
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
			String sql = "select * from isign.sign_info where sign = '" + sign2Coll2 + "'";
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery(sql);
			//get 5-9 to be finger
			String sql2 = "select * from isign.finger_info where finger_id=?";
			PreparedStatement pst2 = conn.prepareStatement(sql2);
			String sql3 = "delete * from isign.bone_info where bone_id=?";
			PreparedStatement pst3 = conn.prepareStatement(sql3);
			String sql4 = "delete * from isign.finger_info where finger_id=?";
			PreparedStatement pst4 = conn.prepareStatement(sql4);
			String sql5 = "delete from isign.sign_info where sign = '" + sign2Coll2 + "'";
			System.out.println(sql5);
			PreparedStatement pst5 = conn.prepareStatement(sql5);
			pst5.executeUpdate();
			
			/*
			for (int i = 5; i < 10; i++){
				while (rs.next()) {
					int temp = rs.getInt(i);
					pst2.setInt(1, temp);
					ResultSet rs2 = stmt.executeQuery(sql2);
					for (int j = 3; j < 7; j++){
						while (rs2.next()){
							pst3.setInt(1, rs2.getInt(j));
							pst3.executeUpdate();
							System.out.println("Deleted bone_id " + rs2.getInt(j));
						}
					}
					pst4.setInt(1, rs.getInt(i));
					pst4.executeUpdate();
					System.out.println("Deleted finger_id " + rs.getInt(i));
					rs2.close();
				}
				
			}*/
			
			//pst5.executeUpdate();
			System.out.println("Deleted Sign " + sign2Coll2);
			SignOutput.append("-------------------------------------------------");
			SignOutput.append("\nDeleted Sign " + sign2Coll2 + " successfully!!!");
			
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void saveSign(Map<Integer, Sign> SignMapTemp) {
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
			String sql = "insert into isign.bone_info (bone_type, x_axis, y_axis, z_axis) values (?, ?, ?, ?)";
			PreparedStatement pst = conn.prepareStatement(sql);
			String sql2 = "insert into isign.finger_info (f_type, f_meta_id, f_prox_id, f_inte_id, f_dist_id)"
					+ "values (?, ?, ?, ?, ?)";
			PreparedStatement pst2 = conn.prepareStatement(sql2);
			String sql3 = "insert into isign.sign_info (sign, if_right_hand, thumb_id, index_id, middle_id, ring_id, pinky_id)"
					+ "values (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pst3 = conn.prepareStatement(sql3);
			String sql4 = "SELECT max(bone_id) FROM isign.bone_info;";
			PreparedStatement pst4 = conn.prepareStatement(sql4);
			String sql5 = "SELECT max(finger_id) FROM isign.finger_info;";
			PreparedStatement pst5 = conn.prepareStatement(sql5);
			
			for(Entry<Integer, Sign> entry: SignMap.entrySet()){
				if (entry.getValue().Sign_average == true && entry.getValue().Sign_name == Sign2Coll){
					for(Entry<String, MyFinger> entryF : entry.getValue().FingerMap.entrySet()){
						for(Entry<String, Vector> entryB : entryF.getValue().BoneMap.entrySet()){
							pst.setString(1, entryB.getKey());
							pst.setFloat(2, entryB.getValue().getX());
							pst.setFloat(3, entryB.getValue().getY());
							pst.setFloat(4, entryB.getValue().getZ());
							pst.executeUpdate();
						}
						ResultSet rs = stmt.executeQuery(sql4);
						while (rs.next()) {
							max = rs.getInt(1);
							System.out.println("Bone id max " + rs.getInt(1));
						}
						pst2.setString(1, entryF.getKey());
						pst2.setInt(2, max-3);
						pst2.setInt(3, max-2);
						pst2.setInt(4, max);
						pst2.setInt(5, max-1);
						pst2.executeUpdate();
						rs.close();
					}
					ResultSet rs = stmt.executeQuery(sql5);
					while (rs.next()) {
						max = rs.getInt(1);
						System.out.println("finger id max " + rs.getInt(1));
					}
					pst3.setString(1, entry.getValue().Sign_name);
					pst3.setInt(2, entry.getValue().Is_Right_Hand);
					pst3.setInt(3, max-4);
					pst3.setInt(4, max);
					pst3.setInt(5, max-1);
					pst3.setInt(6, max-3);
					pst3.setInt(7, max-2);
					pst3.executeUpdate();
					rs.close();
				}
			}

			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		SignOutput.append("\nSaved to DB successfully!!");

	}

	public static void collect(String sign2Coll2) {
		// TODO Auto-generated method stub
		int If_Right_Hand;
		Sign SignAvg = new Sign();
		SignAvg.Sign_average = true;
		SignAvg.Sign_name = sign2Coll2;

		System.out.println("--------------------------------");
		System.out.println("Collection started!!");

		LeapListener listener = new LeapListener();
		Controller controller = new Controller();

		controller.addListener(listener);

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < 3; i++) {

			System.out.println("test thread sleep!");
			SignOutput.append("-------------------------------------------------------------- \n");
			Sign SignTemp = new Sign();

			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			for (Hand hand : controller.frame().hands()) {
				If_Right_Hand = hand.isRight() ? 1 : 0;
				System.out.println("It is the right hand?(1 for yes/0 for No) " + If_Right_Hand);
				SignOutput.append("It is the right hand?(1 for yes/0 for No) " + If_Right_Hand + "\n");
				SignTemp.Is_Right_Hand = If_Right_Hand;
				SignTemp.Sign_name = sign2Coll2;
			}

			for (Finger finger : controller.frame().fingers()) {
				System.out.println("--------------------------------------------------------");
				System.out.println(
						// Each finger
						"Finger Type: " + finger.type()
						// Every 5 records are for a single hand
								+ " ID: " + finger.id());
				SignOutput.append(// Each finger
						"Finger Type: " + finger.type()
						// Every 5 records are for a single hand
								+ " ID: " + finger.id() + "\n");

				MyFinger FingerTemp = new MyFinger();

				for (Bone.Type boneType : Bone.Type.values()) {
					Bone bone = finger.bone(boneType);

					System.out.println(
							// More like finger joint
							"Bond Type: " + bone.type()
							// The direction from your palm to your tip of
							// finger
									+ " Direction of the bone: " + bone.direction());
					SignOutput.append(// More like finger joint
							"Bond Type: " + bone.type()
							// The direction from your palm to your tip of
							// finger
									+ " Direction of the bone: " + bone.direction() + "\n");

					FingerTemp.BoneMap.put(bone.type().toString(), bone.direction());

				}

				SignTemp.FingerMap.put(finger.type().toString(), FingerTemp);
			}

			SignMap.put(count++, SignTemp);
		}

		Sign_GetAvg(SignAvg, SignMap);
		SignMap.put(count++, SignAvg);

		System.out.println("Verify!!! " + "\n"
				+ SignMap.get(count - 4).FingerMap.get("TYPE_PINKY").BoneMap.get("TYPE_INTERMEDIATE") + "\n"
				+ SignMap.get(count - 3).FingerMap.get("TYPE_PINKY").BoneMap.get("TYPE_INTERMEDIATE") + "\n"
				+ SignMap.get(count - 2).FingerMap.get("TYPE_PINKY").BoneMap.get("TYPE_INTERMEDIATE") + "\n"
				+ SignMap.get(count - 1).FingerMap.get("TYPE_PINKY").BoneMap.get("TYPE_INTERMEDIATE"));
		controller.removeListener(listener);

	}

	public static void Sign_GetAvg(Sign signAvg, Map<Integer, Sign> signMap2) {
		// TODO Auto-generated method stub

		for (Entry<Integer, Sign> entry : signMap2.entrySet()) {
			if (entry.getValue().Sign_name == signAvg.Sign_name) {
				signAvg.Is_Right_Hand = entry.getValue().Is_Right_Hand;
				for (Entry<String, MyFinger> entryF : entry.getValue().FingerMap.entrySet()) {

					if (!signAvg.FingerMap.containsKey(entryF.getKey())) {
						signAvg.FingerMap.put(entryF.getKey(), new MyFinger());
						System.out.println(entryF.getKey() + " is created!!!");
					}

					for (Entry<String, Vector> entryB : entryF.getValue().BoneMap.entrySet()) {

						if (!signAvg.FingerMap.get(entryF.getKey()).BoneMap.containsKey(entryB.getKey())) {
							signAvg.FingerMap.get(entryF.getKey()).BoneMap.put(entryB.getKey(),
									new Vector(entryB.getValue()));
							System.out.println(entryF.getKey() + "-->" + entryB.getKey() + " is created!!!");

						} else {

							signAvg.FingerMap.get(entryF.getKey()).BoneMap.get(entryB.getKey()).setX(
									signAvg.FingerMap.get(entryF.getKey()).BoneMap.get(entryB.getKey()).getX() + entryB.getValue().getX());
							signAvg.FingerMap.get(entryF.getKey()).BoneMap.get(entryB.getKey()).setY(
									signAvg.FingerMap.get(entryF.getKey()).BoneMap.get(entryB.getKey()).getY() + entryB.getValue().getY());
							signAvg.FingerMap.get(entryF.getKey()).BoneMap.get(entryB.getKey()).setZ(
									signAvg.FingerMap.get(entryF.getKey()).BoneMap.get(entryB.getKey()).getZ() + entryB.getValue().getZ());
							signAvg.FingerMap.get(entryF.getKey()).BoneMap.get(entryB.getKey()).toString();
							System.out.println(entryF.getKey() + "-->" + entryB.getKey() + " is added!! To be added "
									+ signAvg.FingerMap.get(entryF.getKey()).BoneMap.get(entryB.getKey()).toString()
									+ " " + entryB.getValue().toString());
						}

					}
				}
			}
		}
		
		for(Entry<String, MyFinger> FingerAvgEntry : signAvg.FingerMap.entrySet()){
			for (Entry<String, Vector> BoneAvgEntry : FingerAvgEntry.getValue().BoneMap.entrySet()){
				BoneAvgEntry.setValue(BoneAvgEntry.getValue().divide(3));
			}	
		}
		System.out.println("loop!!! " + signAvg.FingerMap.get("TYPE_PINKY").BoneMap.get("TYPE_INTERMEDIATE"));
	}

}
