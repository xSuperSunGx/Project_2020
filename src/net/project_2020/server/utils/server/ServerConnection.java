package net.project_2020.server.utils.server;

import net.project_2020.server.utils.coding.CodeHelper;
import net.project_2020.server.utils.coding.CodingProperty;
import net.project_2020.server.utils.mysql.MySQLManager;
import net.project_2020.utils.packetoption.ServerCommunication;
import net.project_2020.utils.packetoption.Tag;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerConnection extends Thread{


    private Socket s;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean stopped;
    private MySQLManager mysql;
    private Server server;

    public void setMysql(MySQLManager mysql) {
        this.mysql = mysql;
    }

    public ServerConnection(Socket s, MySQLManager mysql, Server server) throws IOException {
        this.s = s;
        this.server = server;
        stopped = false;
        this.mysql = mysql;

        out = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));

        in = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
        System.out.println(new ObjectOutputStream(s.getOutputStream()));
    }


    public void setClientName(String name) {
        super.setName(name);
    }




    @Override
    public void run() {
        try {

            Object o;
            ServerCommunication message;
            List<ServerCommunication> messages;
            while(!this.isStopped()) {
                while(in.available() == 0) {
                    Thread.sleep(1);
                }
                o = readFromClient();
                if(o instanceof ServerCommunication) {
                    message = (ServerCommunication) o;

                    switch (message.getTag()) {
                        case CONNECTION:
                            if(message.getMessage().equalsIgnoreCase("close")) {
                                this.stopped = true;
                            } else if(message.getMessage().equalsIgnoreCase("join")) {
                                this.server.sendToAllClients(message);
                            } else if(message.getMessage().equalsIgnoreCase("leave")) {
                                this.server.sendToAllClients(message);
                            }

                            break;
                        case MESSAGE:
                            this.server.sendToAllClients(message);


                            break;
                        case LOGIN:
                            ServerCommunication s = new ServerCommunication();
                            s.setTag(Tag.LOGIN);
                            if(mysql.containsCompination(message.getNickname(), message.getMessage())) {
                                s.setMessage(new Boolean(true).toString());
                                super.setName(message.getNickname());
                            } else {
                                s.setMessage(new Boolean(false).toString());

                            }
                            sendToClientFromServer(s);
                            break;
                        case REGISTER:
                            s = new ServerCommunication();
                            s.setTag(Tag.REGISTER);
                            if(mysql.containsUsername(message.getNickname())) {
                                s.setMessage(new Boolean(false).toString()); //Name existiert schon -> false
                            } else {
                                s.setMessage(new Boolean(true).toString()); //Name existiert noch nicht -> true
                                mysql.registerAccount(message.getNickname(), message.getMessage());
                                super.setName(message.getNickname());
                            }

                            sendToClientFromServer(s);
                            break;
                    }

                } else if(o instanceof List) {
                    messages = (List<ServerCommunication>) o;
                    messages.forEach(serverCommunication -> {
                        ServerCommunication s;
                        switch (serverCommunication.getTag()) {
                            case CONNECTION:
                                if(serverCommunication.getMessage().equalsIgnoreCase("close")) {
                                    this.stopped = true;
                                } else if(serverCommunication.getMessage().equalsIgnoreCase("join")) {
                                    this.server.sendToAllClients(serverCommunication);
                                } else if(serverCommunication.getMessage().equalsIgnoreCase("leave")) {
                                    this.server.sendToAllClients(serverCommunication);
                                }

                                break;
                            case MESSAGE:
                                this.server.sendToAllClients(serverCommunication);

                                break;
                            case LOGIN:
                                s = new ServerCommunication();
                                s.setTag(Tag.LOGIN);
                                if(mysql.containsCompination(serverCommunication.getNickname(), serverCommunication.getMessage())) {
                                    s.setMessage(new Boolean(true).toString());
                                    super.setName(serverCommunication.getNickname());
                                } else {
                                    s.setMessage(new Boolean(false).toString());

                                }
                                sendToClientFromServer(s);
                                break;
                            case REGISTER:
                                s = new ServerCommunication();
                                s.setTag(Tag.REGISTER);
                                if(mysql.containsUsername(serverCommunication.getNickname())) {
                                    s.setMessage(new Boolean(false).toString()); //Name existiert schon -> false
                                } else {
                                    s.setMessage(new Boolean(true).toString()); //Name existiert noch nicht -> true
                                    mysql.registerAccount(serverCommunication.getNickname(), serverCommunication.getMessage());
                                    super.setName(serverCommunication.getNickname());
                                }

                                sendToClientFromServer(s);
                                break;
                        }
                    });
                }




            }
            System.out.println(super.getName() + " hat die Verbindung getrennt!");
            in.close();
            out.close();
            s.close();
            this.server.connections.remove(this);
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public synchronized void disconnect() throws IOException {
        sendToClientFromServer("close");

        this.server.connections.remove(this);
    }

    public synchronized boolean isStopped() {
        return stopped;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerConnection)) return false;

        ServerConnection that = (ServerConnection) o;

        if (stopped != that.stopped) return false;
        if (s != null ? !s.equals(that.s) : that.s != null) return false;
        if (in != null ? !in.equals(that.in) : that.in != null) return false;
        return out != null ? out.equals(that.out) : that.out == null;
    }


    public synchronized void sendToClient(Object message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized  void sendToClientFromServer(Object message) {
        try {
            ((ServerCommunication)message).setNickname("Server");
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized Object readFromClient() {
        try {
            return in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
