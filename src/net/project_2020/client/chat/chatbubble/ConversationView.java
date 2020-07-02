package net.project_2020.client.chat.chatbubble;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ConversationView extends ScrollPane {
    private ObservableList<Node> speechBubbles = FXCollections.observableArrayList();

    private VBox messageContainer;

    public ConversationView(){
        setupElements();
    }

    private void setupElements(){
        setupMessageDisplay();
        setPadding(new Insets(5));
    }



    private void setupMessageDisplay(){
        messageContainer = new VBox(5);
        Bindings.bindContentBidirectional(speechBubbles, messageContainer.getChildren());

        setContent(messageContainer);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        prefWidthProperty().bind(messageContainer.prefWidthProperty().subtract(5));
        setFitToWidth(true);
        //Make the scroller scroll to the bottom when a new message is added
        speechBubbles.addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if(change.wasAdded()){
                    setVvalue(getVmax());
                }
            }
        });
    }



    public void sendMessage(String message){
        speechBubbles.add(new SpeechBox(message, SpeechDirection.RIGHT));
    }

    public void receiveMessage(String username, String message){
        speechBubbles.add(new SpeechBox(username, message, SpeechDirection.LEFT));
    }
    public void playerJoin(String nickname){
        speechBubbles.add(new UserBox(nickname, UserAction.JOIN));
    }
    public void playerLeave(String nickname){
        speechBubbles.add(new UserBox(nickname, UserAction.LEAVE));
    }
}