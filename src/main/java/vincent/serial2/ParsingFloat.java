package vincent.serial2;

import java.util.Locale;
import java.util.Scanner;

public class ParsingFloat {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String strToParse = "temp: 15.6 °C";
		// Float f = 1.2385f;
		// strToParse = strToParse + f;
		Scanner scanner = new Scanner(strToParse);

		scanner.useLocale(Locale.US);
		// find the next float token and print it
		// loop for the whole scanner
		while (scanner.hasNext()) {
			scanner.next();
			// if the next is a float, print found and the float
			if (scanner.hasNextFloat()) {
				System.out.println("Found :" + scanner.nextFloat());
			}
		}
	}

}
