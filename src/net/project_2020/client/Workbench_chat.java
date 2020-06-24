package net.project_2020.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.project_2020.client.chat.ChatController;
import net.project_2020.client.login.LoginController;
import net.project_2020.client.utils.ErrorInit;
import net.project_2020.client.utils.client.Client;
import net.project_2020.client.utils.mysql.MySQLManager;

import java.io.File;

public class Workbench_chat extends Application{

    public static LoginController login;
    public static ChatController chat;
    public static Client client;
    public static ErrorInit error;
    public static Stage mainstage;
    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("chat/Chat.fxml"));
        primaryStage.setTitle("Chat Messager - by Noah & Timo");
        primaryStage.setResizable(false);
        Scene scene = new Scene(root, 975, 546);
        primaryStage.setScene(scene);
        primaryStage.show();
        mainstage = primaryStage;
        Workbench.manager = new MySQLManager(new File(Workbench.file_name), mainstage);
    }

    public static void main(String[] args) {
        error = new ErrorInit();
        Workbench.file_name = "info.dat";
        launch(args);
    }



}
