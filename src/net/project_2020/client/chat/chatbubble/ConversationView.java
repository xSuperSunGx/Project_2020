package net.project_2020.client.chat.chatbubble;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ConversationView extends VBox {
    private ObservableList<Node> speechBubbles = FXCollections.observableArrayList();

    private ScrollPane messageScroller;
    private VBox messageContainer;

    public ConversationView(){
        super(5);
        setupElements();
    }

    private void setupElements(){
        setupMessageDisplay();
        getChildren().setAll(messageScroller);
        setPadding(new Insets(5));
    }



    private void setupMessageDisplay(){
        messageContainer = new VBox(5);
        Bindings.bindContentBidirectional(speechBubbles, messageContainer.getChildren());

        messageScroller = new ScrollPane(messageContainer);
        messageScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        messageScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageScroller.setPrefHeight(300);
        messageScroller.prefWidthProperty().bind(messageContainer.prefWidthProperty().subtract(5));
        messageScroller.setFitToWidth(true);
        //Make the scroller scroll to the bottom when a new message is added
        speechBubbles.addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if(change.wasAdded()){
                    messageScroller.setVvalue(messageScroller.getVmax());
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