package net.project_2020.client.login;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.project_2020.client.Workbench;
import net.project_2020.client.utils.ErrorMessage;

import java.awt.*;
import java.io.IOException;

public class Login {

    public synchronized void sendDisconnect() {
        Workbench.error.setHeader("Failed to Connect to Server!");
        Workbench.error.setText("Please restart your game or check your internet connection");
        Toolkit.getDefaultToolkit().beep();
        try {
            Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource(Workbench.file_prefix + "error/Error.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Error");
            stage.getIcons().clear();
            stage.setAlwaysOnTop(true);
            stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(Workbench.file_prefix + "icons/Error.png")));
            stage.setResizable(false);
            Scene scene = new Scene(parent, 380, 160);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int login = 0;

    public static int ALLOWED = 1;
    public static int DENIED = 2;
    public static int NEUTRAL = 0;


    public void errorLogin() {
    }
}
