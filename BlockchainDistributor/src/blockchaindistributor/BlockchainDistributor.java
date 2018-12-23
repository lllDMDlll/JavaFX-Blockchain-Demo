package blockchaindistributor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Dominic Donofrio
 */
public class BlockchainDistributor {

    private final static String HOST = "Host IP address";
    private static final File readerData = new File("C:\\readerData.xml");

    public static void main(String[] args) throws InterruptedException, ParserConfigurationException, SAXException {

        while (true) {
            Parser parse = new Parser();
            try {
                Socket socket = null;

                try {
                    socket = new Socket(HOST, 5087);
                } catch (IOException ex) {
                    Logger.getLogger(BlockchainDistributor.class.getName()).log(Level.SEVERE, null, ex);
                }

                InputStream in = null;
                try {
                    in = new FileInputStream(readerData);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BlockchainDistributor.class.getName()).log(Level.SEVERE, null, ex);
                }

                OutputStream out1 = null;

                try {
                    out1 = socket.getOutputStream();
                } catch (IOException ex) {
                    Logger.getLogger(BlockchainDistributor.class.getName()).log(Level.SEVERE, null, ex);
                }

                byte[] bytes = new byte[8192];
                int totalCount = 0;
                int count;
                bytes = new byte[8192];
                while ((count = in.read(bytes)) > 0) {
                    System.out.println();
                    System.out.println("New read");
                    totalCount += count;
                    out1.write(bytes, 0, count);
                    System.out.println();
                    break;
                }
                System.out.println("Read Finished: " + count + " bytes");
                System.out.println("Read " + totalCount + " bytes total.");
                out1.close();
                in.close();
                if (socket != null) {
                    socket.close();
                }
                Thread.sleep(650);
            } catch (IOException ex) {
                Logger.getLogger(BlockchainDistributor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
