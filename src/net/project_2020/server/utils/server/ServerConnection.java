package net.project_2020.server.utils.server;

import net.project_2020.server.utils.mysql.MySQLManager;
import net.project_2020.utils.packetoption.ServerCommunication;
import net.project_2020.utils.packetoption.Tag;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

        out = new ObjectOutputStream(s.getOutputStream());
        in = new ObjectInputStream(s.getInputStream());
        /*System.out.println(s.isInputShutdown());
        System.out.println(s.isConnected());
        System.out.println(s.isClosed());
        System.out.println(s.isOutputShutdown());
        System.out.println(new ObjectInputStream(new FileInputStream(new File("G:\\IntelliJ\\Project_2020\\out\\artifacts\\Client\\info.dat"))));
        System.out.println(s.getInputStream());
        System.out.println(new ObjectOutputStream(s.getOutputStream()));
        System.out.println(new ObjectInputStream(s.getInputStream()));
        System.out.println("Hallo1");
        System.out.println("Hallo2");*/
    }


    public void setClientName(String name) {
        super.setName(name);
    }




    @Override
    public void run() {
        try {

            String input;
            ServerCommunication message;
            while(!this.isStopped()) {
                while(in.available() == 0) {
                    Thread.sleep(1);
                }

                input = readFromClient();

                message = ServerCommunication.getFromString(input);
                int i = 9;

            //System.out.println(message.getNickname());
                //System.out.println(message.getTag().name());
                //System.out.println(message.getMessage());
                switch (message.getTag()) {
                    case CONNECTION:
                        if(message.getMessage().equalsIgnoreCase("close")) {
                            this.stopped = true;
                        } else if(message.getMessage().equalsIgnoreCase("join")) {
                            this.server.sendToAllClients(message);
                        } else if(message.getMessage().equalsIgnoreCase("leave")) {
                            this.server.sendToAllClientsExept(message, this);
                        }

                        break;
                    case MESSAGE:
                        this.server.sendToAllClients(message);


                        break;
                    case LOGIN:
                        ServerCommunication s = new ServerCommunication();
                        s.setTag(Tag.LOGIN);
                        System.out.println("login");
                        if(mysql.containsCompination(message.getNickname(), message.getMessage())) {
                            s.setMessage(new Boolean(true).toString());
                            super.setName(message.getNickname());
                        } else {
                            s.setMessage(new Boolean(false).toString());

                        }
                        sendToClientFromServer(s);
                        System.out.println("send");
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
        ServerCommunication g = new ServerCommunication();
        g.setTag(Tag.CONNECTION);
        g.setMessage("close");
        sendToClientFromServer(g);

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


    public synchronized void sendToClient(ServerCommunication message) {
        try {
            out.writeUTF(message.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized  void sendToClientFromServer(ServerCommunication message) {
        try {
            ServerCommunication sc = (ServerCommunication)message;
            sc.setNickname("Server");
            out.writeUTF(sc.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized String readFromClient() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
