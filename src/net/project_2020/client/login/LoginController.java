package net.project_2020.client.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.project_2020.client.Workbench;
import net.project_2020.client.login.thread.WaitingforLogin;
import net.project_2020.client.utils.ErrorMessage;
import net.project_2020.client.utils.client.Client;

public class LoginController extends Login implements Initializable{

	@FXML
	public AnchorPane pane;
	@FXML
	public FontAwesomeIconView signal;
	@FXML
	public JFXButton menu, menu2, settings, contact, b_login;
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

		Workbench.login = this;
		if(Workbench.client == null) {
			Workbench.client = new Client("localhost", 7777, Workbench.login);
			if(!Workbench.client.connect()) {
				super.sendDisconnect();
				pane.setDisable(true);
			} else {
				Workbench.client.start();
			}
		} else {
			Workbench.client.setLogin(Workbench.login);

		}

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
			Workbench.client.loginAccount(user.getText(), password.getText());
			WaitingforLogin wfl = new WaitingforLogin(this, e, user.getText());
			wfl.start();
			b_login.setDisable(true);



		} else {
			ErrorMessage.sendErrorMessage("Invalid Username or password", "Please fill in the correct informations", "Error");
		}
	}
	@FXML
	public void startup(ActionEvent e) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Signup.fxml"));
			Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			Scene scene = new Scene(root, 600, 400);
			stage.setTitle("Sign Up - by Noah & Timo");
			stage.setResizable(false);
			stage.setOnCloseRequest(event -> {
				Workbench.client.disconnect(stage);
			});
			stage.setScene(scene);
			stage.show();


		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void errorLogin() {
		ErrorMessage.sendErrorMessage("Failed to login", "Incorrect Password or Username!", "Error");

	}


}
