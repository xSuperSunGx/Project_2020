package net.project_2020.client.utils.client;

import javafx.application.Platform;
import net.project_2020.client.Workbench;
import net.project_2020.client.login.Login;
import net.project_2020.client.utils.coding.PacketFormat;
import net.project_2020.utils.packetoption.ServerCommunication;
import net.project_2020.utils.packetoption.Tag;

import javax.swing.text.html.HTML;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class Client extends Thread {

    private String host;
    private int port;
    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean stopp;
    private Login login;




    public Client(String host, int port, Login login) {
        this.host = host;
        this.port = port;
        this.stopp = false;
        this.login = login;
    }



    public void setClientName(String name) {
        super.setName(name);
    }



    public boolean connect() {
        try {
            client = new Socket(this.host, this.port);

            out = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
            in = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));

            return true;

        } catch (ConnectException e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                login.sendDisconnect();
                Workbench.mainstage.close();

            });


        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                login.sendDisconnect();
                Workbench.mainstage.close();

            });
        }
        return false;
    }

    public synchronized void disconnect() {
        if(Workbench.chat != null)sendLeave();
        if (client != null && !client.isClosed()) {
            try {
                stopp = true;
                ServerCommunication s = new ServerCommunication();
                s.setMessage("close");
                s.setTag(Tag.CONNECTION);
                sendObject(s);

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

    public synchronized void sendObject(Object message) {

        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized Object readObject() {
        try {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean loginAccount(String username, String password) {
        ServerCommunication serverCommunication = new ServerCommunication();
        serverCommunication.loginRequest( username,  password);

        sendObject(serverCommunication);
        try {
            while (!this.isStopped() && in.available() == 0) {
                Thread.sleep(1);
            }
            ServerCommunication s = (ServerCommunication) readObject();
            return s.getMessage().equalsIgnoreCase("true");

        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean registerAccount(String username, String password) {
        ServerCommunication serverCommunication = new ServerCommunication();
        serverCommunication.registerRequest( username,  password);

        sendObject(serverCommunication);
        try {
            while (!this.isStopped() && in.available() == 0) {
                Thread.sleep(1);
            }
            ServerCommunication s = (ServerCommunication) readObject();
            return s.getMessage().equalsIgnoreCase("true");

        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendJoin() {
        ServerCommunication s = new ServerCommunication();
        s.setNickname(super.getName());
        s.setTag(Tag.CONNECTION);
        s.setMessage("join");
        sendObject(s);
    }
    public void sendLeave() {
        ServerCommunication s = new ServerCommunication();
        s.setNickname(super.getName());
        s.setTag(Tag.CONNECTION);
        s.setMessage("leave");
        sendObject(s);
    }

    @Override
    public void run() {
        try {
            PacketFormat format;
            while (!this.isStopped()) {
                while(!this.isStopped() && in.available() == 0) {
                    Thread.sleep(1);
                }
                ServerCommunication serverCommunication = (ServerCommunication) in.readObject();


                System.out.println("Empfangen");
                switch (serverCommunication.getTag()) {
                    case LOGIN:
                    case REGISTER:

                        if(serverCommunication.isFromServer()) {
                            if(new Boolean(serverCommunication.getMessage())) {
                                login.login = Login.ALLOWED;

                            } else {
                                login.login = Login.DENIED;
                            }
                        }

                        break;
                    case CONNECTION:
                        if(serverCommunication.getMessage().equalsIgnoreCase("close") && serverCommunication.isFromServer()) {
                            stopp = true;
                            disconnect();
                        }  else if(serverCommunication.getMessage().equalsIgnoreCase("join")) {
                            Workbench.chat.v.playerJoin(serverCommunication.getNickname());
                        } else if(serverCommunication.getMessage().equalsIgnoreCase("leave")) {
                            Workbench.chat.v.playerLeave(serverCommunication.getNickname());

                        }
                        break;
                    case MESSAGE:
                        if(super.getName().equalsIgnoreCase(serverCommunication.getNickname())) {
                            Workbench.chat.v.sendMessage(serverCommunication.getMessage());
                        } else {
                            Workbench.chat.v.receiveMessage(serverCommunication.getNickname(), serverCommunication.getMessage());

                        }
                        break;
                }

            }
            Platform.runLater(() -> {

                login.sendDisconnect();
            });
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }



    public synchronized boolean isStopped() {
        return this.stopp;
    }
}
