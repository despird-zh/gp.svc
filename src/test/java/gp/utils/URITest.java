package gp.utils;

import java.net.URI;
import java.net.URISyntaxException;

public class URITest {

	public static void main(String[] args) throws URISyntaxException {
		
		URI u = new URI("gp://storage:123/a/b/c.doc");
		System.out.println(u.getScheme());
		System.out.println(u.getPort());
		System.out.println(u.getPath());
	}
}
