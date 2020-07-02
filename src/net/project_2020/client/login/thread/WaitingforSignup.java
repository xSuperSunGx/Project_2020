package net.project_2020.client.login.thread;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.project_2020.client.Workbench;
import net.project_2020.client.login.Login;
import net.project_2020.client.login.SignupController;
import net.project_2020.client.utils.ErrorMessage;

import java.io.IOException;

public class WaitingforSignup extends Thread{

    private SignupController signup;
    private ActionEvent e;
    private String name;

    public WaitingforSignup(SignupController signup, ActionEvent e, String name) {
        this.signup = signup;
        this.e = e;
        this.name = name;
    }

    @Override
    public void run() {

        while(signup.login == Login.NEUTRAL) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        if(signup.login == Login.ALLOWED) {
            Workbench.client.setClientName(name);
            Workbench.name = name;

            Platform.runLater(() -> {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getClassLoader().getResource(Workbench.file_prefix + "chat/Chat.fxml"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                Stage chat = new Stage();
                Scene scene = new Scene(root, 975, 546);
                chat.setTitle("Chat Messager - by Noah & Timo");
                chat.setResizable(false);
                chat.setScene(scene);
                chat.show();
                chat.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        Workbench.client.disconnect();
                    }
                });

                Stage current = (Stage)((Node)e.getSource()).getScene().getWindow();
                current.close();
            });





        } else if(signup.login == Login.DENIED) {
            Platform.runLater(() ->signup.errorLogin());
        }
        signup.login = Login.NEUTRAL;
        signup.b_signup.setDisable(false);
    }
}
