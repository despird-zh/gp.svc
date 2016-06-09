package gp.tz;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.gp.validate.ValidateMessage;
import com.gp.validation.ValidationUtils;


public class T1BeanTest {

	public static void main(String[] args) {
		T1Bean t1b = new T1Bean();
		t1b.setStr2("1");
			
		Set<ValidateMessage> vr = ValidationUtils.validate(Locale.SIMPLIFIED_CHINESE,t1b);
		
		System.out.println(vr);
	}

}
