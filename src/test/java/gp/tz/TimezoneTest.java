package gp.tz;

import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import com.gp.util.DateTimeUtils;

import junit.framework.TestCase;

public class TimezoneTest extends TestCase{

	public void test1(){
		
	    // from Joda to JDK
	    DateTime dt = new DateTime();
	    Date jdkDate = dt.toDate();
	    
	    System.out.println("old tz : " + jdkDate);
	    
	    DateTimeZone elzone = DateTimeZone.forID("Europe/London");
	    DateTime d1 = dt.toDateTime(elzone);
	    
	    Date t = d1.toDate();
	    System.out.println("new tz : " + t);
	    
	    DateTime dateTime = new LocalDateTime(t.getTime()).toDateTime(elzone);
	    Date t1 = dateTime.toDate();
	    System.out.println("new tz : " + t1);
	    
	    Date m = DateTimeUtils.toTimeZone(t, elzone);
	    System.out.println("new tzm : " + m);
	    
	    for (String string : TimeZone.getAvailableIDs(TimeZone.getTimeZone(
	            "GMT+08:00").getRawOffset())) {
	        System.out.println(string);
	    }
	    
	    for (String string : TimeZone.getAvailableIDs()){
	    	
	    	System.out.println(string);
	    }
	    
	    DateTimeZone tz1 = DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+08:00"));
	    
	    System.out.println(tz1);
	    
	    System.out.println(DateTimeUtils.toTime(DateTimeUtils.now()));
	}
}
