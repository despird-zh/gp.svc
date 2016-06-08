package gp.utils;

import com.gp.exception.ServiceException;

public class ExcepTest {

	public static void main(String[] args){
		
		ServiceException e = new ServiceException("fail.delete.ptn", "DUMMY_TBL");
		
		System.out.println(e.getMessage());
		
		ServiceException e1 = new ServiceException("general.ptn", "DUMMY_TBL");
		
		System.out.println(e1.getMessage());
	}
}
