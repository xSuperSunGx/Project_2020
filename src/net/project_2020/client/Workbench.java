package net.project_2020.client;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import net.project_2020.client.chat.ChatController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.project_2020.client.login.Login;
import net.project_2020.client.login.LoginController;
import net.project_2020.client.utils.ErrorInit;
import net.project_2020.client.utils.client.Client;
import net.project_2020.client.utils.coding.CodeHelper;
import net.project_2020.client.utils.coding.CodingProperty;
import net.project_2020.server.utils.mysql.MySQLManager;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Workbench extends Application{

    public static Login login;
    public static ChatController chat;
    public static ErrorInit error;
    public static Stage mainstage;
    public static Client client;
    public static String name = "";
    public static String file_name;
    public static final String file_prefix = "net/project_2020/client/";
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(file_prefix + "login/Login.fxml"));
        //FXMLLoader root = FXMLLoader.load(getClass().getResource("chat/Chat.fxml"));
        primaryStage.setTitle("Chat Messager - by Noah & Timo");
        //root.<ChatController>getController();
        primaryStage.setResizable(false);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                client.disconnect();
            }
        });
        primaryStage.show();
        mainstage = primaryStage;

    }

    public static void main(String[] args) {
        error = new ErrorInit();
        file_name = "info.dat";

        launch(args);
    }





}
