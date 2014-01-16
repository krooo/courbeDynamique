package vincent;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class EssaiCharset {

	public static void main(final String[] args) {

		byte[] bytes;
		try {
			System.out.println(String.valueOf(Character.toChars(176)));

			bytes = "14°C".getBytes("ISO-8859-1");

			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			// int read;
			// while ((read = byteArrayInputStream.read()) > 0) {
			// System.out.println(read);
			// }
			InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream, "ISO-8859-1");
			BufferedReader buffReader = new BufferedReader(inputStreamReader);
			String readLine = buffReader.readLine();

			System.out.println(readLine);
		} catch (UnsupportedEncodingException l_ex1) {
			// TODO Auto-generated catch block
			l_ex1.printStackTrace();
		} catch (IOException l_ex3) {
			// TODO Auto-generated catch block
			l_ex3.printStackTrace();
		}
	}
}
