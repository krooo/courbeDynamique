package vincent.serial3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Scanner;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private BufferedWriter ecrivainPortSerie;
	private SerialPort port;
	private FloatPrinter floatPrinter;

	private long timeStarted = System.currentTimeMillis();

	private float derniereTemperature;


    private static Logger LOG = LoggerFactory.getLogger(TempSerialReader.class);
    private static Logger LOG_TEMP = LoggerFactory.getLogger("temperature");

	public TempSerialReader(FloatPrinter floatPrinter) {
		this.floatPrinter = floatPrinter;
		init();
	}

	public static void main(String[] args) {
		TempSerialReader tempSerialReader = new TempSerialReader(null);
	}

	private void init() {

		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
		while (portIdentifiers.hasMoreElements()) {
			CommPortIdentifier el = portIdentifiers.nextElement();
			System.out.println(el.getName());

		}

		try {
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier("COM4"); // CommPortIdentifier.getPortIdentifier("/dev/ttyUSB0");
			port = (SerialPort) portId.open("LectureTemp", 10000);

			port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			port.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			port.addEventListener(this);
            port.notifyOnDataAvailable(true);// pour que serialEvent soit appelé
            fluxLecture = new BufferedReader(new InputStreamReader(port.getInputStream(), "UTF-8"));
            ecrivainPortSerie = new BufferedWriter(new OutputStreamWriter(port.getOutputStream(), "UTF-8"));

			LOG_TEMP.info("temps depuis debut du lancement,temps d'allumage (ms),temperature");

		} catch (PortInUseException l_ex) {
            LOG.error("Port utilisé !", l_ex);
		} catch (UnsupportedCommOperationException l_ex) {
            LOG.error("opération non supportée !", l_ex);
		} catch (TooManyListenersException l_ex) {
			LOG.error("un seul listener par port !", l_ex);
		} catch (NoSuchPortException l_ex) {
            LOG.error("Port non trouvé !", l_ex);
		} catch (IOException l_ex) {
			LOG.error("Exception : ", l_ex);
		}
	}

	public void close() {
		try {
			fluxLecture.close();
            ecrivainPortSerie.close();
		} catch (IOException l_ex) {
            LOG.error("Erreur à la fermeture : ", l_ex);
		}
		port.close();
	}



	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

			try {
                String readed = fluxLecture.readLine();
				LOG.debug("message recu :" + readed);

				Scanner scanner = new Scanner(readed);
				scanner.useLocale(Locale.US);

				if (readed.startsWith("temp: ")) {
					if (scanner.hasNext()) {
						scanner.next();
						// if the next is a float, print found and the float
						if (scanner.hasNextFloat()) {
							derniereTemperature = scanner.nextFloat();
							floatPrinter.afficherDerniereTemperature(derniereTemperature);
						}
					}
                } else if (readed.startsWith("timeUp: ")) {
					if (scanner.hasNext()) {
						scanner.next();
						// if the next is a float, print found and the float
						if (scanner.hasNextInt()) {
                            int timeUp = scanner.nextInt();
							LOG_TEMP.info((System.currentTimeMillis() - timeStarted) / 1000 + "," + timeUp + ","
									+ this.derniereTemperature);
						}
					}

                } else if (readed.startsWith("durée à chauffer: ")) {
                    floatPrinter.setDureeDeChauffeEtRelicat(readed);
                } else if (readed.startsWith("coefficients P=")) {
                    floatPrinter.setParamsPid(readed);
                }
                scanner.close();
			} catch (IOException l_ex) {
				LOG.error("Erreur de lecture : ", l_ex);
			}
        }

	}

    public BufferedWriter getEcrivainPortSerie() {
        return ecrivainPortSerie;
    }
}
