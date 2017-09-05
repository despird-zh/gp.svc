package gp.tz;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.gp.common.IdKey;
import com.gp.validate.ValidateMessage;
import com.gp.validate.ValidateUtils;


public class T1BeanTest {

	public static void main(String[] args) {
		try {
		IdKey t = IdKey.valueOf("s");
		}catch(Exception e) {
			e.printStackTrace();
		}
		T1Bean t1b = new T1Bean();
		t1b.setStr2("1");
			
		Set<ValidateMessage> vr = ValidateUtils.validate(Locale.SIMPLIFIED_CHINESE,t1b);
		
		System.out.println(vr);
	}

}
