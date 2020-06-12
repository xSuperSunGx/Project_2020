package net.project_2020.client.database;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.project_2020.client.Workbench;
import net.project_2020.client.utils.mysql.MySQLManager;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DatabaseController implements Initializable {

    @FXML
    public JFXTextField d_host, d_port, d_data, d_user;
    @FXML
    public JFXPasswordField d_pass;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onaction(ActionEvent e) {
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
