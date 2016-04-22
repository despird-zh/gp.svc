package gp.tz;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

public class Acckeyseckey {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		javax.crypto.KeyGenerator generator = javax.crypto.KeyGenerator.getInstance("HMACSHA1");
		generator.init(120);
		byte[] awsAccessKeyId = generator.generateKey().getEncoded();
		generator.init(240);
		byte[] awsSecretAccessKey = generator.generateKey().getEncoded();
		
//		final ByteArrayOutputStream encoded = new ByteArrayOutputStream();
//		final OutputStream encoder = javax.mail.internet.MimeUtility.encode(encoded, "base64");
//		encoder.write(awsAccessKeyId);
//		encoder.flush();
//		encoder.close();
//		String accessKeyId = new String(encoded.toByteArray(), encoding).replaceAll("[\\r\\n]", "");
	}

}
