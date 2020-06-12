package net.project_2020.client.chat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.project_2020.client.Workbench;
import net.project_2020.client.utils.client.Client;
import net.project_2020.client.utils.coding.CodeHelper;
import net.project_2020.client.utils.coding.CodingProperty;
import net.project_2020.client.utils.mysql.MySQLManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ChatController implements Initializable {

    @FXML
    public JFXButton send;
    @FXML
    public AnchorPane pane, pane_settings;
    @FXML
    public GridPane pane_chat;
    @FXML
    public JFXTextArea text, chat;
    @FXML
    public JFXButton b_settings, b_chat, d_edit, s_edit;
    @FXML
    public ImageView logo;
    @FXML
    public Label copyright, title;
    @FXML
    public FontAwesomeIconView cog_1, cog_2, cog_3, cog_4;
    @FXML
    public JFXTextField d_host;
    public JFXTextField d_port;
    public JFXTextField d_data;
    public JFXTextField d_user;
    public JFXPasswordField d_pass;
    public JFXTextField s_host;
    public JFXTextField s_port;

    public static ChatController cs;

    public Stage stage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logoAnimation(logo);
        settingAnimation(cog_1, cog_2, cog_3, cog_4);
        titleAnimation(title);
        this.pane_chat.toFront();
        this.send.setText("");
        this.chat.setEditable(false);
        this.chat.setFocusTraversable(false);
        this.text.setPromptText("Nachricht schreiben");
        manageMySQL(d_host, d_data, d_port, d_user, d_pass);
        Workbench.client = new Client("localhost", 1304, Workbench.name, this);
        boolean cennect = Workbench.client.connect();
        if (cennect) {
            Workbench.client.start();
        }
        Platform.runLater(() -> {
            ((Stage) pane.getScene().getWindow()).setOnCloseRequest(event -> {
                System.out.println("Disconnect");
                Workbench.client.disconnect();
                Platform.exit();
            });
            ;
        });


    }

    private void manageMySQL(JFXTextField d_host, JFXTextField d_data, JFXTextField d_port, JFXTextField d_user, JFXPasswordField d_pass) {
        d_host.setText(Workbench.manager.getHost());
        d_data.setText(Workbench.manager.getDatabase());
        d_port.setText(Workbench.manager.getPort() + "");
        d_user.setText(Workbench.manager.getUsername());
        d_pass.setText(CodingProperty.decode(CodeHelper.INFORMATION.getCode(), Workbench.manager.getPassword()));
        d_host.setDisable(true);
        d_data.setDisable(true);
        d_port.setDisable(true);
        d_user.setDisable(true);
        d_pass.setDisable(true);
    }

    private void editMySQL(JFXTextField d_host, JFXTextField d_data, JFXTextField d_port, JFXTextField d_user, JFXPasswordField d_pass) {

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../../database/Database.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Database Configuration");
            stage.setAlwaysOnTop(true);
            stage.setMinWidth(460);
            stage.setMinHeight(390);
            Scene scene = new Scene(root, 460, 390);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void titleAnimation(Label title) {
        Timeline line = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(title.textFillProperty(), Color.web("#0086ff"))),
                new KeyFrame(Duration.seconds(1), new KeyValue(title.textFillProperty(), Color.web("#FC1681")))
        );
        line.setCycleCount(Animation.INDEFINITE);
        line.setAutoReverse(true);
        line.play();


    }



    private void settingAnimation(Node... cog) {
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.setCycleCount(Animation.INDEFINITE);
        parallelTransition.setAutoReverse(true);
        RotateTransition rotate;

        for (Node iconView : cog) {
            rotate = new RotateTransition(Duration.millis(2500), iconView);
            rotate.setByAngle(360f);
            parallelTransition.getChildren().add(rotate);
        }
        parallelTransition.play();

    }

    private void logoAnimation(Node logo) {

        this.copyright.setTranslateY(100);
        this.copyright.setOpacity(0);

        FadeTransition fade = new FadeTransition(Duration.seconds(4), logo);
        fade.setFromValue(0);
        fade.setToValue(1);


        TranslateTransition transition = new TranslateTransition(Duration.seconds(4), logo);
        transition.setFromY(315);
        transition.setToY(40);
        transition.setOnFinished(event -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), logo);
            scale.setAutoReverse(true);
            scale.setCycleCount(2);
            scale.setByX(1.5f);
            scale.setByY(1.5f);
            scale.setOnFinished(event1 -> {
                TranslateTransition b = new TranslateTransition(Duration.seconds(1), copyright);
                b.setFromY(100);
                b.setToY(75);

                FadeTransition fb = new FadeTransition(Duration.seconds(1), copyright);
                fb.setFromValue(0);
                fb.setToValue(1);

                ParallelTransition p = new ParallelTransition(b, fb);
                p.play();
            });
            scale.play();
        });
        Platform.runLater(() -> {
            ParallelTransition parallel = new ParallelTransition();
            parallel.getChildren().addAll(
                    fade,
                    transition
            );
            parallel.setCycleCount(1);
            parallel.play();

        });

    }


    public synchronized void sendDisconnect() {
        Workbench.error.setHeader("Failed to Connect to Server!");
        Workbench.error.setText("Please restart your game or check your internet connection");
        Toolkit.getDefaultToolkit().beep();
        this.pane_chat.setDisable(true);
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
        if (nickname.equalsIgnoreCase(Workbench.client.getName())) {
            addMessage(message, "Du");
            return;
        }
        this.chat.appendText("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + nickname + " » " + decode);
        this.chat.appendText(System.lineSeparator());

    }

    @FXML
    public void send() {

        if (!text.getText().equalsIgnoreCase("")) {
            String message = CodingProperty.encode(CodeHelper.MESSAGE.getCode(), text.getText());
            //this.addMessage(message, "Du");
            text.clear();
            Workbench.client.sendMessage(message);
        }
    }

    @FXML
    public void settings(ActionEvent e) {
        if (!this.b_settings.getId().equalsIgnoreCase("select")) {
            this.b_settings.setId("select");
            this.b_chat.setId("");
            this.pane_settings.toFront();
        }
    }

    @FXML
    public void chat(ActionEvent e) {
        if (!this.b_chat.getId().equalsIgnoreCase("select")) {
            this.b_chat.setId("select");
            this.b_settings.setId("");
            this.pane_chat.toFront();
        }

    }

    public ParallelTransition parallel= new ParallelTransition();

    @FXML
    public void hoverEnter(MouseEvent e) {
        JFXButton b = (JFXButton) ((Node) e.getSource());
        if (!b.getId().equalsIgnoreCase("select")) {
            parallel.stop();
            Scene scene = ((Node) e.getSource()).getScene();
            scene.setCursor(Cursor.HAND);
            parallel = new ParallelTransition();
            Timeline hover = new Timeline(
                    new KeyFrame(Duration.millis(500), new KeyValue(b.textFillProperty(), Color.web("#0086ff"))),
                    new KeyFrame(Duration.seconds(2), new KeyValue(b.textFillProperty(), Color.web("#FC1681")))
            );

            parallel.getChildren().add(hover);
            hover = new Timeline(
                    new KeyFrame(Duration.millis(500), new KeyValue(((FontAwesomeIconView) b.getGraphic()).fillProperty(), Color.web("#0086ff"))),
                    new KeyFrame(Duration.seconds(2), new KeyValue(((FontAwesomeIconView) b.getGraphic()).fillProperty(), Color.web("#FC1681")))
            );

            parallel.getChildren().add(hover);
            parallel.setAutoReverse(true);
            parallel.setCycleCount(Animation.INDEFINITE);
            parallel.play();
        } else {
            parallel.stop();
            parallel = new ParallelTransition();
            Timeline hover = new Timeline(
                    new KeyFrame(Duration.millis(500), new KeyValue(b.textFillProperty(), Color.WHITE))
            );

            parallel.getChildren().add(hover);
            hover = new Timeline(
                    new KeyFrame(Duration.millis(500), new KeyValue(((FontAwesomeIconView) b.getGraphic()).fillProperty(), Color.WHITE))
            );

            parallel.getChildren().add(hover);
            parallel.setCycleCount(1);
            parallel.play();
        }
    }

    @FXML
    public void hoverExit(MouseEvent e) {
        JFXButton b = (JFXButton) ((Node) e.getSource());
        Scene scene = ((Node) e.getSource()).getScene();
        scene.setCursor(Cursor.DEFAULT);
        parallel.stop();
        parallel = new ParallelTransition();
        Timeline hover = new Timeline(
                new KeyFrame(Duration.millis(500), new KeyValue(b.textFillProperty(), Color.WHITE))
        );

        parallel.getChildren().add(hover);
        hover = new Timeline(
                new KeyFrame(Duration.millis(500), new KeyValue(((FontAwesomeIconView) b.getGraphic()).fillProperty(), Color.WHITE))
        );

        parallel.getChildren().add(hover);
        parallel.setCycleCount(1);
        parallel.play();

    }

    @FXML
    public void on_d_edit(ActionEvent e) {
        if(!d_data.getText().equalsIgnoreCase("") &&
                !d_host.getText().equalsIgnoreCase("") &&
                !d_pass.getText().equalsIgnoreCase("") &&
                !d_port.getText().equalsIgnoreCase("") &&
                !d_user.getText().equalsIgnoreCase("")) {
            int port = 3306;
            try {
                port = Integer.parseInt(d_port.getText());
            }catch (NumberFormatException e1) {
                sendErrorMessage("Failed to set the port!", "Please do only use numbers for the \"Port-field\"", "Error");
                return;
            }
            Workbench.manager.disconnect();
            Workbench.manager = MySQLManager.changeInformation(d_host.getText(), d_data.getText(), port, d_user.getText(), d_pass.getText());
            Workbench.manager.configureTables();
            ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
        } else {
            sendErrorMessage("Invalid text fields", "Please be sure that you filled out all text fields", "Error");
        }
    }
    @FXML
    public void on_s_edit(ActionEvent e) {
        if(!s_host.getText().equalsIgnoreCase("") && !s_port.getText().equalsIgnoreCase("")) {
            int port = 3306;
            try {
                port = Integer.parseInt(s_port.getText());
            }catch (NumberFormatException e1) {
                sendErrorMessage("Failed to set the port!", "Please do only use numbers for the \"Port-field\"", "Error");
                return;
            }
            Workbench.client = new Client(s_host.getText(), port, Workbench.name, this);
            boolean cennect = Workbench.client.connect();
            if (cennect) {
                Workbench.client.start();
            }
        } else {
            sendErrorMessage("Invalid text fields", "Please be sure that you filled out all text fields", "Error");
        }
    }
    public void sendErrorMessage(String header, String text, String title) {
        Workbench.error.setHeader(header);
        Workbench.error.setText(text);
        Toolkit.getDefaultToolkit().beep();
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("../error/Error.fxml"));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.getIcons().clear();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("../icons/Error.png")));
            stage.setResizable(false);
            Scene scene = new Scene(parent, 380, 160);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
