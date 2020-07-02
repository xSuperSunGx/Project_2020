package net.project_2020.client.chat.chatbubble;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import net.project_2020.client.Workbench;

public class UserBox extends HBox{
    private Color DEFAULT_LEAVE_COLOR = Color.web("#FF0000");
    private Color DEFAULT_JOIN_COLOR = Color.web("#36FF00");
    private Color DEFAULT_BACKGROUND = Color.web("#959595");
    private Background DEFAULT_LEAVE_BACKGROUND, DEFAULT_JOIN_BACKGROUND;

    private String username;
    private UserAction direction;

    private Label displayedText;

    public UserBox(String username, UserAction direction){
        this.username = username;
        this.direction = direction;
        initialiseDefaults();
        setupElements();
    }

    private void initialiseDefaults(){
        DEFAULT_LEAVE_BACKGROUND = new Background(
                new BackgroundFill(Color.TRANSPARENT, new CornerRadii(5,5,5,5,false), Insets.EMPTY));
        DEFAULT_JOIN_BACKGROUND = new Background(
                new BackgroundFill(Color.TRANSPARENT, new CornerRadii(5,5,5,5,false), Insets.EMPTY));
    }

    private void setupElements(){
        displayedText = new Label();
        displayedText.setPadding(new Insets(5));
        displayedText.setWrapText(true);
        displayedText.setFont(Font.font("System", FontPosture.ITALIC, 14));

        if(direction == UserAction.JOIN){
            configureJoin();
        } else if(direction == UserAction.LEAVE){
            configureLeave();
        }
    }

    private void configureJoin(){
        displayedText.setText(username + " hat den Chat betreten");
        displayedText.setBackground(DEFAULT_JOIN_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER_RIGHT);
        displayedText.setTextFill(DEFAULT_JOIN_COLOR);

        HBox container = new HBox(displayedText);
        //Use at most 75% of the width provided to the SpeechBox for displaying the message
        container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER);
    }
    private void configureLeave(){
        displayedText.setText(username + " hat den Chat verlassen");
        displayedText.setBackground(DEFAULT_LEAVE_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER_RIGHT);
        displayedText.setTextFill(DEFAULT_LEAVE_COLOR);

        HBox container = new HBox(displayedText);
        //Use at most 75% of the width provided to the SpeechBox for displaying the message
        container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER);
    }


}
