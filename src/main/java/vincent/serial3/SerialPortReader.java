package vincent.serial3;

import purejavacomm.SerialPortEvent;
import purejavacomm.SerialPortEventListener;

public class SerialPortReader implements SerialPortEventListener {

	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {// If data is available

			// try {
			//
			// final byte buffer[] = sp.readBytes(event.getEventValue());
			// final String readed = new String(buffer);
			// System.out.println("Readed from COM" + sp.getPortName() + ": " + readed);
			// if (readed.startsWith("temp: ")) {
			// Scanner scanner = new Scanner(readed);
			// scanner.useLocale(Locale.US);
			// if (scanner.hasNext()) {
			// scanner.next();
			// // if the next is a float, print found and the float
			// if (scanner.hasNextFloat()) {
			// float temp = scanner.nextFloat();
			// System.out.println("Found :" + temp);
			// floatPrinter.printFloat(temp);
			// }
			// }
			//
			// }
			//
			// } catch (Exception ex) {
			// System.err.println("EX: " + ex);
			// }

		}
	}

}
