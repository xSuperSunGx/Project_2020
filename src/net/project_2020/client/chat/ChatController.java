package net.project_2020.client.chat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.project_2020.client.Workbench;
import net.project_2020.client.utils.client.Client;
import net.project_2020.client.utils.coding.CodeHelper;
import net.project_2020.client.utils.coding.CodingProperty;
import net.project_2020.client.utils.mysql.MySQLManager;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ChatController implements Initializable {

    @FXML
    public JFXButton send;
    @FXML
    public AnchorPane pane;
    @FXML
    public JFXTextArea text, chat;
    @FXML
    public JFXButton settings, settings2;


    public static ChatController cs;

    public Stage stage;



    @Override
    public void initialize(URL location, ResourceBundle resources) {



        this.send.setText("");
        this.chat.setEditable(false);
        this.chat.setFocusTraversable(false);
        this.text.setPromptText("Nachricht schreiben");
        Workbench.manager = new MySQLManager("localhost", "project", 3306, "project", "BuwuB2W55O8AfOQ6CuJoROP7yEt5BO");
        Workbench.client = new Client("localhost", 1304, Workbench.name, this);
        boolean cennect = Workbench.client.connect();
        if(cennect) {
            Workbench.client.start();
        }
        Platform.runLater(() -> {
            ((Stage)pane.getScene().getWindow()).setOnCloseRequest(event -> {
                System.out.println("Disconnect");
                Workbench.client.disconnect();
            });;
        });


    }



    public synchronized void sendDisconnect() {
        Workbench.error.setHeader("Failed to Connect to Server!");
        Workbench.error.setText("Please restart your game or check your internet connection");
        Toolkit.getDefaultToolkit().beep();
        this.pane.setDisable(true);
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("../error/Error.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Error");
            stage.getIcons().clear();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("../icons/Error.png")));
            stage.setResizable(false);
            Scene scene = new Scene(parent, 380, 160);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public void addMessage(String message, String nickname) {
        String decode = CodingProperty.decode(CodeHelper.MESSAGE.getCode(), message);
            if(nickname.equalsIgnoreCase(Workbench.client.getName())) {
                addMessage(message, "Du");
                return;
            }
            this.chat.appendText("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + nickname + " » " + decode);
            this.chat.appendText(System.lineSeparator());

    }

    @FXML
    public void send() {

        if(!text.getText().equalsIgnoreCase("")) {
            String message = CodingProperty.encode(CodeHelper.MESSAGE.getCode(), text.getText());
            //this.addMessage(message, "Du");
            text.clear();
            Workbench.client.sendMessage(message);
        }
    }

}
