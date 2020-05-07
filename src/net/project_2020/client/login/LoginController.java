package net.project_2020.client.login;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.icons525.Icons525;
import de.jensd.fx.glyphs.icons525.Icons525View;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.project_2020.client.Workbench;
import net.project_2020.client.utils.mysql.MySQLManager;

public class LoginController implements Initializable{

	@FXML
	public AnchorPane pane;
	@FXML
	public FontAwesomeIconView signal;
	@FXML
	public JFXButton menu, menu2, settings, contact;
	@FXML
	public Label copyright;
	@FXML
	public Rectangle rec;
	@FXML
	public AnchorPane menuanchor;
	private int i = 0;
	@FXML
	public JFXTextField user;
	@FXML
	public JFXPasswordField password;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		menu.setText("");
		menu2.setText("");
		menuanchor.setVisible(false);
		Platform.runLater(() -> {

			menuanchor.getScene().setOnKeyPressed(event -> {
				handleMenu2Key(event);
			});
		});
	}

	@FXML
	public void handleMenu(ActionEvent e) {
		menuanchor.setVisible(true);

		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.seconds(1));
		fadeTransition.setNode(menuanchor);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		fadeTransition.play();

	}
	@FXML
	public void handleMenu2(ActionEvent e) {

		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.seconds(1));
		fadeTransition.setNode(menuanchor);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		fadeTransition.setOnFinished(event -> {
			menuanchor.setVisible(false);
		});
		fadeTransition.play();
	}

	@FXML
	public void handleMenu2Key(KeyEvent e) {

		if(e.getCode() == KeyCode.ESCAPE && menuanchor.isVisible()) {
			FadeTransition fadeTransition = new FadeTransition();
			fadeTransition.setDuration(Duration.seconds(1));
			fadeTransition.setNode(menuanchor);
			fadeTransition.setFromValue(1);
			fadeTransition.setToValue(0);
			fadeTransition.setOnFinished(event -> {
				menuanchor.setVisible(false);
			});
			fadeTransition.play();
		}
	}

	@FXML
	public void login(ActionEvent e){

		if(!user.getText().equalsIgnoreCase("")
				&& !password.getText().equalsIgnoreCase("")) {
				if(Workbench.containsCompination(user.getText(), password.getText())) {
					try {


						Parent root = FXMLLoader.load(getClass().getResource("../chat/Chat.fxml"));
						Stage chat = new Stage();
						Scene scene = new Scene(root, 975, 546);
						chat.setTitle("Chat Messager - by Noah & Timo");
						chat.setResizable(false);
						chat.setScene(scene);
						chat.show();
						Workbench.name = user.getText();
						Platform.runLater(() -> {

							Stage current = (Stage)((Node)e.getSource()).getScene().getWindow();
							current.close();
						});




					} catch (IOException throwables) {
						throwables.printStackTrace();
					}
				} else {
					sendErrorMessage("Failed to login", "Incorrect Password or Username!", "Error");

				}


		} else {
			sendErrorMessage("Invalid Username or password", "Please fill in the correct informations", "Error");
		}
	}
	@FXML
	public void startup(ActionEvent e) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Signup.fxml"));
			Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			Scene scene = new Scene(root, 600, 400);
			stage.setTitle("Sign Up - by NNoah & Timo");
			stage.setResizable(false);
			stage.setOnCloseRequest(event -> {
				Workbench.manager.disconnect();
			});
			stage.setScene(scene);
			stage.show();

		} catch (IOException e1) {
			e1.printStackTrace();
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
