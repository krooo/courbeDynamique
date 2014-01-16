package vincent.serial2;

import java.util.Locale;
import java.util.Scanner;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import vincent.FloatPrinter;

public class SerialPortReader implements SerialPortEventListener {

	private SerialPort sp;
	private FloatPrinter floatPrinter;

	public SerialPortReader(SerialPort serialPort, FloatPrinter floatPrinter) {
		sp = serialPort;
		this.floatPrinter = floatPrinter;
	}

	public void serialEvent(SerialPortEvent serialPortEvent) {
		if (serialPortEvent.isRXCHAR()) {// If data is available

			try {

				final byte buffer[] = sp.readBytes(serialPortEvent.getEventValue());
				final String readed = new String(buffer);
				System.out.println("Readed from COM" + sp.getPortName() + ": " + readed);
				if (readed.startsWith("temp: ")) {
					Scanner scanner = new Scanner(readed);
					scanner.useLocale(Locale.US);
					if (scanner.hasNext()) {
						scanner.next();
						// if the next is a float, print found and the float
						if (scanner.hasNextFloat()) {
							float temp = scanner.nextFloat();
							System.out.println("Found :" + temp);
							floatPrinter.printFloat(temp);
						}
					}

				}

			} catch (Exception ex) {
				System.err.println("EX: " + ex);
			}

		}
	}

}
