package tx_rx;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
/**
 * command struct
 * ! - start
 * . - end
 * 2bit 00  command
 * 2bit 00  Pin
 * 3bit 000 Value
 *
 * CMD 00
 *  Set Pin Output Mode
 *
 * CMD 01
 *  Digital Write
 *
 * CMD 02
 *  Digital Read
 *
 * CMD 03
 *  Analog Write
 *
 * CMD 04
 *  Analog Read
 *
 * CMD 90
 *  Reset
 *
 * ACK NUMBER
 *  14
 *
 * handshake data !9000000.
 *
 * struct
 *  connector
 *  receiver
 *  sender
 *  cmd decoder
 *  cmd encoder
 *  Exception handler
 *  windows10 Arduino sketch commend
 */
public class serial {

    final static int TIMEOUT = 100;
    final static byte[] hand = "!9000000.".getBytes();
    final static int LOW = 0;
    final static int HIGH = 255;

    public static void main(String[] args) {

        byte[] read = new byte[1024];
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();

        CommPortIdentifier port;

        System.out.println(System.getProperty("os.name"));

        CommPort commPort = null;
        SerialPort serialPort = null;
        InputStream out = null;
        OutputStream in = null;
        byte[] cmd;
        while(portList.hasMoreElements()){
            port = (CommPortIdentifier)portList.nextElement();

            if(port.getPortType() == CommPortIdentifier.PORT_SERIAL){
                System.out.println(port.getName());

                try {
                    commPort = port.open("Demo application", TIMEOUT);
                    serialPort = (SerialPort)commPort;

                    serialPort.setSerialPortParams(19200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

                    in = serialPort.getOutputStream();
                    out = serialPort.getInputStream();

                    in.write(hand);
                    Thread.sleep(50);
                    out.read(read);
                    System.out.println(new String(read));

                    cmd = "!0013000.".getBytes();
                    in.write(cmd);
                    Thread.sleep(50);
                    out.read(read);
                    System.out.println(new String(read));

                    cmd = "!0113001.".getBytes();
                    in.write(cmd);
                    Thread.sleep(50);
                    out.read(read);
                    System.out.println(new String(read));

                    cmd = "!0113000.".getBytes();
                    in.write(cmd);
                    Thread.sleep(50);
                    out.read(read);
                    System.out.println(new String(read));

                    cmd = "!0400000.".getBytes();
                    in.write(cmd);
                    Thread.sleep(50);
                    out.read(read);
                    System.out.println(new String(read));


                    commPort.close();

                } catch (PortInUseException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        }

    }

}
