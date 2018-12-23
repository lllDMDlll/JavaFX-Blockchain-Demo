package adblockchaindemo.userinterface;

import adblockchaindemo.readerdata.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.time.Instant;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.BLUE;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.WHITE;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Dominic Donofrio
 */
public class FXMLDocumentController implements Initializable {

    private String epc;
    private String reader;
    private String time;
    private String tempTime;
    private String rTime;
    private String item;
    private String antenna;
    private int count;
    OutputStream reset;
    InputStream resetData;

    private static final Integer STARTTIME = 2;

    private long startTime = System.currentTimeMillis();

    // Declare timeline and duration for progress bar
    private Timeline timeline;
    private Timeline timeline2;
    private final IntegerProperty timeSeconds = new SimpleIntegerProperty(800);
    private final IntegerProperty timeSeconds2 = new SimpleIntegerProperty(STARTTIME);
    // Declare observable list to store tag entries
    ObservableList<TagEntry> data
            = FXCollections.observableArrayList();

    // Declare all FXML objects for user interface
    @FXML
    private TableView<TagEntry> tableView = new TableView<TagEntry>();

    @FXML
    private TableColumn<TagEntry, String> epcColumn;

    @FXML
    private TableColumn<TagEntry, String> itemColumn;

    @FXML
    private TableColumn<TagEntry, String> locationColumn;

    @FXML
    private TableColumn<TagEntry, String> timeColumn;

    @FXML
    private Rectangle rect1;

    @FXML
    private Rectangle rect2;

    @FXML
    private Rectangle rect3;

    @FXML
    private ProgressBar bar1;

    @FXML
    private ProgressBar bar11;

    @FXML
    private ProgressBar bar2;

    @FXML
    private ProgressBar bar22;

    @FXML
    private ProgressBar bar3;

    @FXML
    private ProgressBar bar33;

    @FXML
    private Circle c1;

    @FXML
    private Circle c2;

    @FXML
    private Circle c3;

    @FXML
    private Circle c4;

    @FXML
    private MenuItem m1;

    @FXML
    private MenuItem m2;

    @FXML
    private MenuItem m3;

    @FXML
    private ImageView check;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            reset = new FileOutputStream("C:\\readerData.xml");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            resetData = new FileInputStream("C:\\resetData.xml");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] bytes = new byte[8192];
        try {
            while ((count = resetData.read(bytes)) > 0) {
                System.out.println();
                reset.write(bytes, 0, count);
                System.out.println();
                break;
            }
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Parser newParse = new Parser();
        Runnable updater = new Runnable() {
            @Override
            public void run() {

                while (true) {
                    System.out.println("Working");
                    try {
                        int dataSize = data.size();
                        tableView.setEditable(true);

                        // Update the columns of the ledger
                        epcColumn.setCellValueFactory(
                                new PropertyValueFactory<>("epc"));
                        itemColumn.setCellValueFactory(
                                new PropertyValueFactory<>("item"));
                        locationColumn.setCellValueFactory(
                                new PropertyValueFactory<>("reader"));
                        timeColumn.setCellValueFactory(
                                new PropertyValueFactory<>("time"));
                        tempTime = newParse.getTime("C:\\tempData.xml");
                        rTime = newParse.getTime("C:\\readerData.xml");
                        if (!newParse.isEmpty("C:\\readerData.xml") && !newParse.isEmpty("C:\\tempData.xml")) {
                            newParse.loadFile("C:\\readerData.xml");
                            epc = newParse.getEPC();
                            reader = newParse.getReader();
                            time = rTime;
                            antenna = newParse.getAntenna();
                            item = newParse.getItem();
                            System.out.println("New data! Ledger updated.");

                            // This series of 'if' statements checks which reader sent the data
                            // and adds a new tag entry with the location of the reader
                            Search s1 = new Search();
                            if (reader.equalsIgnoreCase("Farm") && !item.equalsIgnoreCase("Unknown")) {
                                int index = s1.findItem(data, "Farm", epc);

                                // If an entry with this location already exists, replace it
                                // with the new entry to update the time of the read at that
                                // location
                                if (index == -1) {
                                    data.add(new TagEntry(epc, item, "Farm", time));
                                    tableView.setItems(data);
                                } else {
                                    data.set(index, new TagEntry(epc, item, "Farm", time));
                                }

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Update tracker
                                        c1.setFill(GREEN);
                                        bar1.setOpacity(1);
                                        bar11.setOpacity(0);
                                        bar1.progressProperty().bind(timeSeconds.divide(800)
                                                .subtract(1).multiply(2));
                                        bar2.setOpacity(0);
                                        bar3.setOpacity(0);
                                        bar11.setOpacity(1);
                                        bar22.setOpacity(1);
                                        bar33.setOpacity(1);
                                        check.setOpacity(0);
                                        c2.setFill(Color.web("a1a1a1"));
                                        c3.setFill(Color.web("a1a1a1"));
                                        c4.setFill(Color.web("a1a1a1"));
                                        rect1.setFill(WHITE);
                                        rect2.setFill(WHITE);
                                        rect3.setFill(WHITE);

                                        if (timeline != null) {
                                            timeline.stop();
                                        }

                                        // Start progress bar
                                        timeSeconds.set(400);
                                        timeline = new Timeline();
                                        timeline.getKeyFrames().add(
                                                new KeyFrame(Duration.seconds(8),
                                                        new KeyValue(timeSeconds, 0)));
                                        timeline.playFromStart();
                                    }

                                });

                            }

                            if (reader.equalsIgnoreCase("DistributorR") && !item.equalsIgnoreCase("Unknown")) {
                                int index = s1.findItem(data, "D.C. Received", epc);

                                if (index == -1) {
                                    data.add(new TagEntry(epc, item, "D.C. Received", time));
                                    tableView.setItems(data);
                                } else {
                                    data.set(index, new TagEntry(epc, item, "D.C. Received", time));
                                }

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        c1.setFill(GREEN);
                                        c2.setFill(GREEN);
                                        rect1.setFill(GREEN);
                                        bar1.setOpacity(0);
                                        bar11.setOpacity(0);
                                        bar22.setOpacity(1);
                                        bar33.setOpacity(1);
                                        check.setOpacity(0);
                                        c3.setFill(Color.web("a1a1a1"));
                                        c4.setFill(Color.web("a1a1a1"));
                                        rect2.setFill(WHITE);
                                        rect3.setFill(WHITE);
                                    }

                                });

                            }

                            if (reader.equalsIgnoreCase("DistributorS") && !item.equalsIgnoreCase("Unknown")) {
                                int index = s1.findItem(data, "D.C. Shipped", epc);

                                if (index == -1) {
                                    data.add(new TagEntry(epc, item, "D.C. Shipped", time));
                                    tableView.setItems(data);
                                } else {
                                    data.set(index, new TagEntry(epc, item, "D.C. Shipped", time));
                                }
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        bar2.setOpacity(1);
                                        bar22.setOpacity(0);
                                        bar2.progressProperty().bind(timeSeconds.divide(800)
                                                .subtract(1).multiply(2));
                                        bar33.setOpacity(1);
                                        check.setOpacity(0);
                                        c3.setFill(Color.web("a1a1a1"));
                                        c4.setFill(Color.web("a1a1a1"));
                                        rect2.setFill(WHITE);
                                        rect3.setFill(WHITE);

                                        if (timeline != null) {
                                            timeline.stop();
                                        }
                                        timeSeconds.set(400);
                                        timeline = new Timeline();
                                        timeline.getKeyFrames().add(
                                                new KeyFrame(Duration.seconds(8),
                                                        new KeyValue(timeSeconds, 0)));
                                        timeline.playFromStart();
                                    }

                                });

                            }

                            if (reader.equalsIgnoreCase("Kitchen") && !item.equalsIgnoreCase("Unknown")) {
                                int index = s1.findItem(data, "Kitchen Received", epc);

                                if (index == -1) {
                                    data.add(new TagEntry(epc, item, "Kitchen Received", time));
                                    tableView.setItems(data);
                                } else {
                                    data.set(index, new TagEntry(epc, item, "Kitchen Received", time));
                                }
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        c1.setFill(GREEN);
                                        c2.setFill(GREEN);
                                        rect1.setFill(GREEN);
                                        c3.setFill(GREEN);
                                        rect2.setFill(GREEN);
                                        bar1.setOpacity(0);
                                        bar11.setOpacity(0);
                                        bar2.setOpacity(0);
                                        bar22.setOpacity(0);
                                        bar3.setOpacity(1);
                                        bar33.setOpacity(1);
                                        check.setOpacity(0);
                                        c4.setFill(Color.web("a1a1a1"));
                                        rect3.setFill(WHITE);
                                        bar3.progressProperty().bind(timeSeconds.divide(800)
                                                .subtract(1).multiply(2));
                                        check.setOpacity(0);

                                        if (timeline != null) {
                                            timeline.stop();
                                        }
                                        timeSeconds.set(400);
                                        timeline = new Timeline();
                                        timeline.getKeyFrames().add(
                                                new KeyFrame(Duration.seconds(8),
                                                        new KeyValue(timeSeconds, 0)));
                                        timeline.playFromStart();
                                    }

                                });

                            }

                            if (reader.equalsIgnoreCase("Store") && !item.equalsIgnoreCase("Unknown")) {
                                int index = s1.findItem(data, "Store", epc);

                                if (index == -1) {
                                    data.add(new TagEntry(epc, item, "Store", time));
                                    tableView.setItems(data);
                                } else {
                                    data.set(index, new TagEntry(epc, item, "Store", time));
                                }
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        c1.setFill(GREEN);
                                        c2.setFill(GREEN);
                                        rect1.setFill(GREEN);
                                        c3.setFill(GREEN);
                                        rect2.setFill(GREEN);
                                        c4.setFill(BLUE);
                                        bar1.setOpacity(0);
                                        bar11.setOpacity(0);
                                        bar2.setOpacity(0);
                                        bar22.setOpacity(0);
                                        bar3.setOpacity(0);
                                        bar33.setOpacity(0);
                                        rect3.setOpacity(1);
                                        rect3.setFill(GREEN);
                                    }

                                });

                            }

                            if (reader.equalsIgnoreCase("Customer") && !item.equalsIgnoreCase("Unknown")) {
                                int index = s1.findItem(data, "Customer Purchase", epc);

                                if (index == -1) {
                                    data.add(new TagEntry(epc, item, "Customer Purchase", time));
                                    tableView.setItems(data);
                                } else {
                                    data.set(index, new TagEntry(epc, item, "Customer Purchase", time));
                                }
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        c1.setFill(GREEN);
                                        c2.setFill(GREEN);
                                        rect1.setFill(GREEN);
                                        c3.setFill(GREEN);
                                        rect2.setFill(GREEN);
                                        c4.setFill(GREEN);
                                        rect3.setFill(GREEN);
                                        bar1.setOpacity(0);
                                        bar11.setOpacity(0);
                                        bar2.setOpacity(0);
                                        bar22.setOpacity(0);
                                        bar3.setOpacity(0);
                                        bar33.setOpacity(0);
                                        check.setOpacity(1);
                                    }

                                });

                            }
                        }
                        Thread.sleep(500);

                    } catch (ParserConfigurationException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SAXException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };

        Thread update = new Thread(updater);
        update.start();

        // Action for the "Quit" option
        // Exits the application
        m1.setAccelerator(KeyCombination.keyCombination("Shortcut+Q"));
        m1.setOnAction((ActionEvent q) -> {
            update.stop();
            System.exit(0);
        });

        // Action for the "Reset" option
        // Clears the list and resets the tracker
        m2.setOnAction((ActionEvent c) -> {
            try {
                data.clear();
                c1.setFill(Color.web("a1a1a1"));
                c2.setFill(Color.web("a1a1a1"));
                c3.setFill(Color.web("a1a1a1"));
                c4.setFill(Color.web("a1a1a1"));
                rect1.setFill(WHITE);
                rect2.setFill(WHITE);
                rect3.setFill(WHITE);
                bar1.setOpacity(0);
                bar2.setOpacity(0);
                bar3.setOpacity(0);
                bar11.setOpacity(1);
                bar22.setOpacity(1);
                bar33.setOpacity(1);
                check.setOpacity(0);

                // Update the columns of the ledger
                epcColumn.setCellValueFactory(
                        new PropertyValueFactory<>("epc"));
                itemColumn.setCellValueFactory(
                        new PropertyValueFactory<>("item"));
                locationColumn.setCellValueFactory(
                        new PropertyValueFactory<>("reader"));
                timeColumn.setCellValueFactory(
                        new PropertyValueFactory<>("time"));
                reset = new FileOutputStream("C:\\readerData.xml");
                resetData = new FileInputStream("C:\\resetData.xml");
                byte[] bytes2 = new byte[8192];
                while ((count = resetData.read(bytes2)) > 0) {
                    System.out.println();
                    reset.write(bytes2, 0, count);
                    System.out.println();
                    break;
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
