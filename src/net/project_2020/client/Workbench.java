package net.project_2020.client;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.icons525.Icons525;
import de.jensd.fx.glyphs.icons525.Icons525View;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import net.project_2020.client.chat.ChatController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.project_2020.client.login.LoginController;
import net.project_2020.client.utils.ErrorInit;
import net.project_2020.client.utils.client.Client;
import net.project_2020.client.utils.coding.CodeHelper;
import net.project_2020.client.utils.coding.CodingProperty;
import net.project_2020.client.utils.mysql.MySQLManager;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Workbench extends Application{

    public static LoginController login;
    public static ChatController chat;
    public static Client client;
    public static ErrorInit error;
    public static Stage mainstage;
    public static MySQLManager manager;
    public static String name = "";
    public static String file_name;
    public static final String file_prefix = "net/project_2020/client/";
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login/Login.fxml"));
        //FXMLLoader root = FXMLLoader.load(getClass().getResource("chat/Chat.fxml"));
        primaryStage.setTitle("Chat Messager - by Noah & Timo");
        //root.<ChatController>getController();
        primaryStage.setResizable(false);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                manager.disconnect();
            }
        });
        primaryStage.show();
        mainstage = primaryStage;
        manager = new MySQLManager(new File(file_name), mainstage);
    }

    public static void main(String[] args) {
        error = new ErrorInit();
        file_name = "info.dat";
        launch(args);
    }


    public static boolean containsUsername(String name) {
        try {
            PreparedStatement st = manager.getConnection().prepareStatement("SELECT `username` FROM `accounts`");
            ResultSet rs = st.executeQuery();
            System.out.println(CodingProperty.encode(CodeHelper.INFORMATION.getCode(), name));
            while (rs.next()) {
                if(CodingProperty.decode(CodeHelper.INFORMATION.getCode(), rs.getString(1)).equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public static boolean containsCompination(String username, String passsword) {
        try {
            PreparedStatement st = manager.getConnection().prepareStatement("SELECT * FROM `accounts`");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if(CodingProperty.decode(CodeHelper.INFORMATION.getCode(), rs.getString("username"))
                        .equalsIgnoreCase(username)
                        && CodingProperty.decode(CodeHelper.INFORMATION.getCode(), rs.getString("password"))
                        .equalsIgnoreCase(passsword)) {
                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }


}
