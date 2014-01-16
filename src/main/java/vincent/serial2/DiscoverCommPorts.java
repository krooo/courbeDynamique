package vincent.serial2;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class DiscoverCommPorts {

	public static void main(String[] args) {
		for (String comPath : SerialPortList.getPortNames()) {
			System.out.println(comPath);
		}

		SerialPort serialPort = new SerialPort("COM1");
		try {
			System.out.println("Port opened: " + serialPort.openPort());
			System.out.println("Params setted: " + serialPort.setParams(9600, 8, 1, 0));
			System.out.println("\"Hello World!!!\" successfully writen to port: "
					+ serialPort.writeBytes("Hello World!!!".getBytes()));

			serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
			serialPort.addEventListener(new SerialPortReader(serialPort, null));

			System.out.println("Port closed: " + serialPort.closePort());

		} catch (SerialPortException ex) {
			System.out.println(ex);
		}
	}

}
