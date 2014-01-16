package vincent.serial3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Scanner;
import java.util.TooManyListenersException;

import purejavacomm.CommPortIdentifier;
import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;
import purejavacomm.SerialPort;
import purejavacomm.SerialPortEvent;
import purejavacomm.SerialPortEventListener;
import purejavacomm.UnsupportedCommOperationException;
import vincent.FloatPrinter;

public class TempSerialReader implements SerialPortEventListener {
	private BufferedReader fluxLecture;
	private SerialPort port;
	private FloatPrinter floatPrinter;

	public TempSerialReader(FloatPrinter floatPrinter) {
		this.floatPrinter = floatPrinter;
		init();
	}

	public static void main(String[] args) {
		TempSerialReader tempSerialReader = new TempSerialReader(null);
	}

	private void init() {
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portIdentifiers = CommPortIdentifier
				.getPortIdentifiers();
		while (portIdentifiers.hasMoreElements()) {
			CommPortIdentifier el = portIdentifiers.nextElement();
			System.out.println(el.getName());

		}

		try {
			CommPortIdentifier portId = CommPortIdentifier
					.getPortIdentifier("/dev/ttyUSB0");
			port = (SerialPort) portId.open("LectureTemp", 10000);

			port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			port.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			port.addEventListener(this);
			port.notifyOnDataAvailable(true);// pour que serialEvent soit appelé
			fluxLecture = new BufferedReader(new InputStreamReader(
					port.getInputStream(), "US-ASCII"));

		} catch (PortInUseException l_ex) {
			// TODO Auto-generated catch block
			l_ex.printStackTrace();
		} catch (UnsupportedCommOperationException l_ex) {
			// TODO Auto-generated catch block
			l_ex.printStackTrace();
		} catch (TooManyListenersException l_ex) {
			// TODO Auto-generated catch block
			l_ex.printStackTrace();
		} catch (NoSuchPortException l_ex) {
			// TODO Auto-generated catch block
			l_ex.printStackTrace();
		} catch (IOException l_ex) {
			// TODO Auto-generated catch block
			l_ex.printStackTrace();
		}
	}

	public void close() {
		try {
			fluxLecture.close();
		} catch (IOException l_ex) {
			// TODO Auto-generated catch block
			l_ex.printStackTrace();
		}
		port.close();
	}

	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

			String readed;
			try {
				readed = fluxLecture.readLine();
				System.out.println(readed);
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
			} catch (IOException l_ex) {
				// TODO Auto-generated catch block
				l_ex.printStackTrace();
			}
		}

	}
}
