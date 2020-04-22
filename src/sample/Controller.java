package sample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class Controller implements Initializable{
	
	@FXML
	private Label label;
	@FXML
	private Button knopf;
	@FXML
	private TextField textfeld;
	@FXML
	private ProgressBar fortschritt;
	@FXML
	private Button knopf2;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		label.setText("");
	}
	
	@FXML
	public void methode1() {
		String text = textfeld.getText();
		label.setText(text);
	}
	@FXML
	public void methode2() {
		if(fortschritt.getProgress() == 1D) {
			fortschritt.setProgress(0);
		}
		fortschritt.setProgress(fortschritt.getProgress()+0.1);
		
	}
}
