package Bean;

import java.util.HashMap;
import java.util.Map;

import com.leapmotion.leap.Vector;

public class Sign {
	public String Sign_name;
	public Boolean Sign_average = false;
	public int Is_Right_Hand = 2; 
	public Map<String, MyFinger> FingerMap = new HashMap<String, MyFinger>();

}
