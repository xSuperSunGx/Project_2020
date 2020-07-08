package net.project_2020.client.utils.client;

import javafx.application.Platform;
import javafx.stage.Stage;
import net.project_2020.client.Workbench;
import net.project_2020.client.login.Login;
import net.project_2020.utils.packetoption.ServerCommunication;
import net.project_2020.utils.packetoption.Tag;

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
    private ServerCommunication g;
    private String helping;





    public Client(String host, int port, Login login) {
        this.host = host;
        this.port = port;
        this.stopp = false;
        this.login = login;
    }


    public void setLogin(Login login) {
        this.login = login;
    }

    public void setClientName(String name) {
        super.setName(name);
    }



    public final boolean connect() {
        if(client != null)return true;
        try {
            client = new Socket(this.host, this.port);

            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

            return true;

        } catch (ConnectException e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                Workbench.mainstage.close();

            });


        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                Workbench.mainstage.close();

            });
        }
        return false;
    }

    public synchronized void disconnect(Stage stage) {
        if (client != null && !client.isClosed()) {
            try {
                if(stage.getTitle().startsWith("Chat")) {
                    sendLeave();
                }
                stopp = true;
                ServerCommunication s = new ServerCommunication();
                s.setMessage("close");
                s.setTag(Tag.CONNECTION);
                sendUTF(s);

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
    private synchronized void disconnect() {
        if (client != null && !client.isClosed()) {
            try {
                sendLeave();
                stopp = true;
                ServerCommunication s = new ServerCommunication();
                s.setMessage("close");
                s.setTag(Tag.CONNECTION);
                sendUTF(s);

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


    public synchronized void sendUTF(ServerCommunication message) {

        try {
            out.writeUTF(message.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized String readInput() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void loginAccount(String username, String password) {
        ServerCommunication serverCommunication = new ServerCommunication();
        //erverCommunication.loginRequest( username,  password);
        serverCommunication.setNickname(username);
        serverCommunication.setMessage(password);
        serverCommunication.setTag(Tag.LOGIN);
        sendUTF(serverCommunication);
    }
    public void registerAccount(String username, String password) {
        ServerCommunication serverCommunication = new ServerCommunication();
        serverCommunication.registerRequest( username,  password);

        sendUTF(serverCommunication);

    }

    public void sendJoin() {
        ServerCommunication s = new ServerCommunication();
        s.setNickname(super.getName());
        s.setTag(Tag.CONNECTION);
        s.setMessage("join");
        sendUTF(s);
    }
    public void sendLeave() {
        ServerCommunication s = new ServerCommunication();
        s.setNickname(super.getName());
        s.setTag(Tag.CONNECTION);
        s.setMessage("leave");
        sendUTF(s);
    }

    @Override
    public void run() {
        try {
            while (!this.isStopped()) {
                while(!this.isStopped() && in.available() == 0) {
                    Thread.sleep(1);
                }
                ServerCommunication serverCommunication = ServerCommunication.getFromString(readInput());


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
                        g = serverCommunication;
                        if(serverCommunication.getMessage().equalsIgnoreCase("close") && serverCommunication.isFromServer()) {
                            stopp = true;
                            disconnect();
                        }  else if(serverCommunication.getMessage().equalsIgnoreCase("join")) {
                            Platform.runLater(() -> Workbench.chat.v.playerJoin(g.getNickname()));
                        } else if(serverCommunication.getMessage().equalsIgnoreCase("leave")) {
                            Platform.runLater(() -> Workbench.chat.v.playerLeave(g.getNickname()));

                        }
                        break;
                    case MESSAGE:
                        g = serverCommunication;
                        if(super.getName().equalsIgnoreCase(serverCommunication.getNickname())) {
                            helping = serverCommunication.getMessage()
                                    .replaceAll("oe", "ö").replaceAll("ae", "ä").replaceAll("ue","ü").replaceAll("%S", "ß");
                            Platform.runLater(() -> Workbench.chat.v.sendMessage(helping));
                        } else {
                            helping = serverCommunication.getMessage()
                                    .replaceAll("oe", "ö").replaceAll("ae", "ä").replaceAll("ue","ü").replaceAll("%S", "ß");
                            Platform.runLater(() -> Workbench.chat.v.receiveMessage(g.getNickname(), helping));

                        }
                        break;
                }

            }
            Platform.runLater(() -> {

                login.sendDisconnect();
            });
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }



    public synchronized boolean isStopped() {
        return this.stopp;
    }
}
