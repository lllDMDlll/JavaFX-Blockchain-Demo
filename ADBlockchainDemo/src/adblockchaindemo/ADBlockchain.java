package adblockchaindemo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author Dominic Donofrio
 */
public class ADBlockchain extends Application {

    @Override
    public void start(Stage stage1) throws ParserConfigurationException, SAXException, TransformerException {
        try {
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().
                    getResource("userinterface/FXMLDocument.fxml"));
            Scene scene1 = new Scene(root);
            stage1.setTitle("Avery Dennison Blockchain");

            stage1.setScene(scene1);
            stage1.show();
        } catch (IOException ex) {
            Logger.getLogger(ADBlockchain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        launch(args);
    }

}