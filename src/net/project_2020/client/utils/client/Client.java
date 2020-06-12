package net.project_2020.client.utils.client;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import net.project_2020.client.Workbench;
import net.project_2020.client.chat.ChatController;
import net.project_2020.client.utils.coding.CodeHelper;
import net.project_2020.client.utils.coding.CodingProperty;
import net.project_2020.client.utils.coding.PacketFormat;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class Client extends Thread {

    private String host;
    private int port;
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean stopp;
    private ChatController cc;



    public Client(String host, int port, String nickname, ChatController chatController) {
        super(nickname);
        this.host = host;
        this.port = port;
        this.cc = chatController;
        this.stopp = false;
    }

    public Client(String host, int port, ChatController cc) {

        this.host = host;
        this.port = port;
        this.cc = cc;
        this.stopp = false;
    }

    public void setClientName(String name) {
        super.setName(name);
    }



    public boolean connect() {
        try {
            client = new Socket(this.host, this.port);
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            this.sendMessage("Register");
            cc.pane_chat.setDisable(false);
            return true;

        } catch (ConnectException e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                cc.sendDisconnect();
                Workbench.mainstage.close();

            });


        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                cc.sendDisconnect();
                Workbench.mainstage.close();

            });
        }
        return false;
    }

    public synchronized void disconnect() {
        if (client != null && !client.isClosed()) {
            try {
                stopp = true;
                sendMessage("close");

                sleep(1000);
                client.close();
                client = null;
                in.close();
                out.close();
                in = null;
                out = null;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void sendMessage(String message) {

        try {
            out.writeUTF(CodingProperty.encode(CodeHelper.COMMUNICATION.getCode(), message + CodingProperty.seperate + super.getName()));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            PacketFormat format;
            while (!this.isStopped()) {
                while(in.available() == 0) {
                    Thread.sleep(1);
                }
                String input = CodingProperty.decode(CodeHelper.COMMUNICATION.getCode(), in.readUTF());
                format = new PacketFormat(input);


                System.out.println("Empfangen");
                if(!format.getMessage().equalsIgnoreCase("close")) {
                    cc.addMessage(format.getMessage(), format.getNickname());
                } else {
                    stopp = true;
                }
            }

            cc.sendDisconnect();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }



    public synchronized boolean isStopped() {
        return this.stopp;
    }
}
