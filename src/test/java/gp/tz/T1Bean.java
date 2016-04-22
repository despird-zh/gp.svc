package gp.tz;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class T1Bean {
	
	@NotNull
	private String str1;

	@Size(min =3,max=5)
	private String str2;
	
	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getStr2() {
		return str2;
	}

	public void setStr2(String str2) {
		this.str2 = str2;
	}	
}
