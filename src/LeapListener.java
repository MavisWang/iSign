package Service;

import java.io.*;
import java.util.ArrayList;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

import processing.core.*; 

public class LeapListener extends Listener {
	public void onInit(Controller controller){
		System.out.println("Initialized");
	}
	
	public void onConnect(Controller controller){
		System.out.println("Connected to Leap Motion");
		//Should enable all gestures needed so that leap motion can recognize
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
	}
	
	public void onDisconnected(Controller controller){
		System.out.println("Leap Motion disconnected");
	}
	
	public void onExit(Controller controller){
		System.out.println("Exited");
	}
	
	public void onFrame(Controller controller){
		  
		Frame frame = controller.frame();
		/*
		PImage cameras[] = new PImage[2000]; 
		
		 if(frame.isValid()){
			   ImageList images = frame.images();
			   for(Image image : images)
			   {
			     //Processing PImage class
			     PImage camera = cameras[image.id()];
			     camera = createImage(image.width(), image.height(), RBG);
			     camera.loadPixels();
			     
			     //Get byte array containing the image data from Image object
			     byte[] imageData = image.data();

			     //Copy image data into display object, in this case PImage defined in Processing
			     
			     for(int i = 0; i < image.width() * image.height(); i++){
			       r = (imageData[i] & 0xFF) << 16; //convert to unsigned and shift into place
			       g = (imageData[i] & 0xFF) << 8;
			       b = imageData[i] & 0xFF;
			       camera.pixels[i] =  r | g | b; 
			     }
			     
			     //Show the image
			     camera.updatePixels();
			     //image(camera, 640 * image.id(), 0);  
			   }
			} 
			*/
		/*System.out.println("Frame id: " + frame.id() + ", Timestamp: "
				+ ", Hands: " + frame.hands().count() + ", Fingers: " 
				+ frame.fingers().count() + ", Tools: " + frame.tools().count()
				+ ", Gestures: " + frame.gestures().count());
		*/
		/*
		
		for (Hand hand : frame.hands()){
			String handType = hand.isLeft() ? "Left Hand" : "Right hand";
			System.out.println("--------------------------------------------------------");
			System.out.println(
					//Which hand
					handType + ", "
					//Series id
					+ "id: " + hand.id() 
					//xyz position
					+ ", Palm Position: " + hand.palmPosition()); 
				
			Vector normal = hand.palmNormal();
			Vector direction = hand.direction();
			
			System.out.println(
					"Pitch:" + Math.toDegrees(direction.pitch())
					+ " Roll: " + Math.toDegrees(normal.roll())
					+ " Yaw: " + Math.toDegrees(direction.yaw()));

			//if (hand.isRight() == true && hand.grabStrength() == 1) {
			//	System.out.println("-----------------------------------A is detected!!-------------------------------------");
			//}else{
			//	System.out.println("-----------------------------------A is not detected!! Please try again!!--------------");
			//}

		}
	
		
		for (Finger finger : frame.fingers()){
			System.out.println("--------------------------------------------------------");
			System.out.println(
					//Each finger
					"Finger Type: " + finger.type()
					//Every 5 records are for a single hand
					+ " ID: " + finger.id() 
					//Length of the finger, usually not used
					+ " Finger Length: " + finger.length()
					//Width of the finger, usually not used
					+ " Finger Width: " + finger.width());

			for (Bone.Type boneType : Bone.Type.values()){
				Bone bone = finger.bone(boneType);

				System.out.println(
						//More like finger joint
						"Bond Type: " + bone.type()
						//The bottom of finger
						//+ " Start of the bone: " + bone.prevJoint()
						//The tip of finger
						//+ " End of the bone: " + bone.nextJoint()
						//The direction from your palm to your tip of finger
						+ " Direction of the bone: " + bone.direction());
				//Vector Saved = bone.direction();
				//System.out.println(Saved);

			}
		}
			
		//Vector savedDir = customGesture.savedDir; // This is how I'd imagine it could look
		//Vector currentDir = bone.direction();
		//float heading = savedDir.dot(currentDir);
		//if (heading > 0.8) 
			// so, if heading is more than 80% accurate
		  // Mark this bone as matching
			
			
		//tool is not covered in iSign
		//for (Tool tool : frame.tools())
		
		/*GestureList gestures = frame.gestures();
		for (int i = 0; i < gestures.count(); i++) {
			Gesture gesture = gestures.get(i);
			
			switch (gesture.type()) {
			case TYPE_CIRCLE:
				CircleGesture circle = new CircleGesture(gesture);
				
				String clockwiseness;
				if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/4) {
					clockwiseness = "closewise";
				} else {
					clockwiseness = "counter-closewise";
				}
				
				double sweptAngle = 0;
				if (circle.state() != State.STATE_START) {
					CircleGesture previous = new CircleGesture(controller.frame(1).gesture(circle.id()));
					sweptAngle = (circle.progress() - previous.progress()) * 2 * Math.PI;
				}
				
				System.out.println("Circle ID: " + circle.id()
									+ " State: " + circle.state()
									+ " Progress: " + circle.progress()
									+ " Radius: " + circle.radius()
									+ " Angle: " + Math.toDegrees(sweptAngle)
									+ " " + clockwiseness);
				break;
			}
		}*/
	}
		
}
/*
public class LeapController {
	
	public static void main(String[] args){
		LeapListener listener = new LeapListener();
		Controller controller = new Controller();
		
		controller.addListener(listener);
		
		System.out.println("Press enter to quit");
	
		try {
			System.in.read();
		} catch (IOException e){
			e.printStackTrace();
		}
		
		controller.removeListener(listener);
		
	}

}
*/