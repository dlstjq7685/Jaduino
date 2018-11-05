package tx_rx;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.*;
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

/**
 * this class simple rx tx serial communicator
 */
public class serial {

    final static int TIMEOUT = 3600_000;
    final static byte[] hand = "!9000000.".getBytes();
    final static int LOW = 0;
    final static int HIGH = 255;

    private SerialPort eq;
    private InputStream eq_out;
    private OutputStream eq_in;

    private byte[] read_buffer = new byte[1024];
    private byte[] send_buffer = null;

    public boolean connect(){

        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier port;
        byte[] read = new byte[1024];
        CommPort commPort = null;
        SerialPort serialPort = null;
        InputStream out = null;
        OutputStream in = null;

        while(portList.hasMoreElements()){
            port = (CommPortIdentifier)portList.nextElement();

            if(port.getPortType() == CommPortIdentifier.PORT_SERIAL){
                // System.out.println(port.getName());

                try {
                    commPort = port.open("Demo application", TIMEOUT);
                    serialPort = (SerialPort)commPort;

                    serialPort.setSerialPortParams(19200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

                    in = serialPort.getOutputStream();
                    out = serialPort.getInputStream();

                    /**
                     * try hand shake 5times
                     */
                    for(int i = 0; i < 5; i++){
                        in.write(hand);
                        Thread.sleep(20);
                        out.read(read);
                        String cmd = new String(read);
                        if(cmd.contains("ACK")){
                            this.eq = serialPort;
                            eq_in = in;
                            eq_out = out;
                            System.out.println("Find Arduino");
                            // return true;
                        }
                    }

                    commPort.close();

                } catch (PortInUseException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        }
        //System.out.println("Not Found")
        return false;
    }

    public static void main(String[] args) {
        // System.out.println(System.getProperty("os.name"));
        serial dump = new serial();
        dump.connect();
    }

}
