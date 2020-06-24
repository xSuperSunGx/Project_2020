package net.project_2020.client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.project_2020.client.Workbench;

import java.awt.*;
import java.io.IOException;

public class ErrorMessage {

    public static void sendErrorMessage(String header, String text, String title) {
        Workbench.error.setHeader(header);
        Workbench.error.setText(text);
        Toolkit.getDefaultToolkit().beep();
        try {
            Parent parent = FXMLLoader.load(ErrorMessage.class.getClassLoader().getResource(Workbench.file_prefix + "error/Error.fxml"));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.getIcons().clear();
            stage.getIcons().add(new Image(ErrorMessage.class.getClassLoader().getResourceAsStream( Workbench.file_prefix + "icons/Error.png")));
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            Scene scene = new Scene(parent, 380, 160);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
