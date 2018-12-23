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

public class BlockchainServer2 {

    public static void main(String[] args) throws IOException {
        File readerData = new File("C:\\readerData.xml");

        while (true) {
            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(5085);
            } catch (IOException ex) {
                System.out.println("Unable to setup server on this port number. ");
            }

            Socket socket = null;
            InputStream in = null;
            InputStream in2 = null;
            OutputStream out = null;
            OutputStream out2 = null;

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

            try {
                out = new DataOutputStream(socket.getOutputStream());
            } catch (FileNotFoundException ex) {
                System.out.println("Data not found. ");
            }
            
            out2 = new FileOutputStream(readerData);

            byte[] bytes = new byte[8192];
            int totalCount = 0;

            int count1;
            while ((count1 = in.read(bytes)) > 0) {
                System.out.println();
                totalCount += count1;
                out2.write( bytes, 0, count1);
                System.out.println();
                break;
            }
            
            totalCount=0;
            int count2;
            in2 = new FileInputStream(readerData);
            while ((count2 = in2.read(bytes)) > 0) {
                System.out.println();
                System.out.println("New read from " + socket);
                totalCount += count2;
                out.write(bytes, 0, count2);
                System.out.println();
                break;
            }
            
            System.out.println("Read Finished: " + count1 + " bytes");
            System.out.println("Read " + totalCount + " bytes total.");

            out2.flush();
            out.flush();
            out2.close();
            out.close();
            in.close();
            in2.close();
            socket.close();
            serverSocket.close();
        }
    }

}
