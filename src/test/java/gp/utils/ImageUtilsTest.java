package gp.utils;

import java.util.Date;

import com.gp.common.Images;

public class ImageUtilsTest {

	public static void main(String[] args) {
		String fn = Images.getImgFileName(new Date(), 23l, "png");
		System.out.println("file name : " + fn);
		
		Date d = Images.parseTouchDate(fn);
		System.out.println("file date : " + d);
		
		Long id = Images.parseImageId(fn);
		System.out.println("file id : " + id);
	}

}
