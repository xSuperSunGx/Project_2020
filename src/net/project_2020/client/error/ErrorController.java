package net.project_2020.client.error;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.project_2020.client.Workbench;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ErrorController implements Initializable {
    @FXML
    private Label header;
    @FXML
    private Label text;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        header.setText(Workbench.error.getHeader());
        text.setText(Workbench.error.getText());

    }


    @FXML
    public void onOkay(ActionEvent e) {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.close();


    }
}
