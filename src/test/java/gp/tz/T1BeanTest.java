package gp.tz;

import java.util.List;
import java.util.Locale;

import com.gp.validation.ValidationMessage;
import com.gp.validation.ValidationUtils;


public class T1BeanTest {

	public static void main(String[] args) {
		T1Bean t1b = new T1Bean();
		t1b.setStr2("1");
			
		List<ValidationMessage> vr = ValidationUtils.validate(Locale.SIMPLIFIED_CHINESE,t1b);
		
		System.out.println(vr);
	}

}
