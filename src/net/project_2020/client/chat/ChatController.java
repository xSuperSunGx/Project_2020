package net.project_2020.client.chat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.project_2020.client.chat.chatbubble.ConversationView;
import net.project_2020.client.Workbench;
import net.project_2020.utils.packetoption.ServerCommunication;
import net.project_2020.utils.packetoption.Tag;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ChatController implements Initializable {

    @FXML
    public AnchorPane pane, pane_settings;
    @FXML
    public GridPane pane_chat;
    @FXML
    public TextField text;
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
    @FXML
    public AnchorPane scr;
    public JFXTextField d_port;
    public JFXTextField d_data;
    public JFXTextField d_user;
    public JFXPasswordField d_pass;
    public JFXTextField s_host;
    public JFXTextField s_port;
    public ConversationView v;

    public static ChatController cs;

    public Stage stage;

    public ConversationView getConversationView() {
        return v;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Workbench.chat = this;

        logoAnimation(logo);
        settingAnimation(cog_1, cog_2, cog_3, cog_4);
        titleAnimation(title);
        this.pane_chat.toFront();
        v = new ConversationView();
        AnchorPane.setBottomAnchor(v, 0D);
        AnchorPane.setTopAnchor(v, 0D);
        AnchorPane.setLeftAnchor(v, 0D);
        AnchorPane.setRightAnchor(v, 0D);

        scr.getChildren().add(v);



        Workbench.client.sendJoin();
        Platform.runLater(() -> {
            ((Stage) pane.getScene().getWindow()).setOnCloseRequest(event -> {
                System.out.println("Disconnect");
                Workbench.client.disconnect((Stage) pane.getScene().getWindow());
                Platform.exit();
            });
            ;
        });


    }


    private void editMySQL(JFXTextField d_host, JFXTextField d_data, JFXTextField d_port, JFXTextField d_user, JFXPasswordField d_pass) {

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource(Workbench.file_prefix + "database/Database.fxml"));
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







    /*public void addMessage(String message, String nickname) {
        String decode = CodingProperty.decode(CodeHelper.MESSAGE.getCode(), message);
        if (nickname.equalsIgnoreCase(Workbench.client.getName())) {
            addMessage(message, "Du");
            return;
        }
        this.chat.appendText("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + nickname + " » " + decode
                .replaceAll("oe", "ö").replaceAll("ae", "ä").replaceAll("ue","ü").replaceAll("%S", "ß"));
        this.chat.appendText(System.lineSeparator());

    }*/

    @FXML
    public void send() {

        if (!text.getText().equalsIgnoreCase("")) {
            String message = text.getText()
                    .replaceAll("ö", "oe").replaceAll("ä", "ae").replaceAll("ü","ue").replaceAll("ß", "%S");
            //this.addMessage(message, "Du");
            text.clear();
            ServerCommunication s = new ServerCommunication();
            s.setTag(Tag.MESSAGE);
            s.setNickname(Workbench.client.getName());
            s.setMessage(message);
            Workbench.client.sendUTF(s);
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



}
