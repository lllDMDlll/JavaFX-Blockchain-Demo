package blockchainserver2;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class BlockchainServer2 {

    private static final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        while (true) {
            Parser parse = new Parser();
            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(5087);
            } catch (IOException ex) {
                System.out.println("Unable to set up server on this port number. ");
            }

            Socket socket = null;
            InputStream in = null;
            InputStream in2 = null;
            InputStream in3 = null;
            OutputStream out = null;
            OutputStream out2 = null;
            OutputStream out3 = null;

            try {
                socket = serverSocket.accept();
                System.out.println("New read from " + socket);
            } catch (IOException ex) {
                System.out.println("Unable to accept client connection. ");
            }

            try {
                in = socket.getInputStream();
            } catch (IOException ex) {
                System.out.println("Unable to get socket input stream. ");
            }

            byte[] bytes = new byte[8192];

            out2 = new FileOutputStream("C:\\Users\\Avery\\Desktop\\BlockchainData\\tempData.xml");
            int count2;
            while ((count2 = in.read(bytes)) > 0) {
                System.out.println();
                System.out.println("New read from " + socket);
                out2.write(bytes, 0, count2);
                System.out.println();
                break;
            }

            in2 = new FileInputStream("C:\\Users\\Avery\\Desktop\\BlockchainData\\tempData.xml");

            if (!parse.isEmpty("C:\\Users\\Avery\\Desktop\\BlockchainData\\tempData.xml") && !parse.isEmpty("C:\\Users\\Avery\\Desktop\\BlockchainData\\dataCheck.xml") && !parse.isEmpty("C:\\Users\\Avery\\Desktop\\BlockchainData\\readerData.xml")) {
                if (!parse.getTime("C:\\Users\\Avery\\Desktop\\BlockchainData\\dataCheck.xml").equalsIgnoreCase(parse.getTime("C:\\Users\\Avery\\Desktop\\BlockchainData\\tempData.xml"))) {
                    out = new FileOutputStream("C:\\Users\\Avery\\Desktop\\BlockchainData\\readerData.xml");
                    out3 = new FileOutputStream("C:\\Users\\Avery\\Desktop\\BlockchainData\\dataCheck.xml");
                    int totalCount = 0;
                    int count;
                    while ((count = in2.read(bytes)) > 0) {
                        out3.write(bytes, 0, count);
                        break;
                    }
                    
                    in3 = new FileInputStream("C:\\Users\\Avery\\Desktop\\BlockchainData\\dataCheck.xml");
                    
                    bytes = new byte[8192];
                    int count1;
                    while ((count1 = in3.read(bytes)) > 0) {
                        System.out.println();
                        System.out.println("New read from " + socket);
                        totalCount += count1;
                        out.write(bytes, 0, count1);
                        System.out.println();
                        break;
                    }

                    System.out.println("Read Finished: " + count1 + " bytes");
                    System.out.println("Read " + totalCount + " bytes total.");
                    out.flush();
                    out.close();
                }
            }

            out2.flush();
            out2.close();
            in.close();
            in2.close();
            socket.close();
            serverSocket.close();
        }
    }

}
