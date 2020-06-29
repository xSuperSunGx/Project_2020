package net.project_2020.server.utils.server;


import net.project_2020.server.utils.coding.CodeHelper;
import net.project_2020.server.utils.coding.CodingProperty;
import net.project_2020.server.utils.mysql.MySQLManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{

    public static List<ServerConnection> connections = new ArrayList<ServerConnection>();
    private int port;
    private ServerSocket ss;
    private Socket s;
    private ServerConnection sc;
    private boolean stop;
    private MySQLManager mysql;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public int getPort() {
        return port;
    }

    public Server(int port, String servername, MySQLManager mySQLManager) {
        super(servername);
        this.port = port;
        stop = false;
        this.mysql = mySQLManager;
    }

    public synchronized void disconnect() {
        stop = true;
        try {
            for (ServerConnection sc : connections) {
                sc.disconnect();
            }
            ss.close();
            Thread.sleep(5000);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void sendToAllClients(String line) {
        for (ServerConnection sc : Server.connections) {
            if(sc.isStopped()) {
                Server.connections.remove(sc);
            } else {

                sc.sendToClient(line);

            }
        }

    }

    @Override
    public void run() {
        try {
            ss = new ServerSocket(this.port);
            System.out.println("Server listened on Port " + this.port);
            while(!isStopped()) {
                s = ss.accept();
                System.out.println("Connecting...");
                in = new ObjectInputStream(s.getInputStream());
                out = new ObjectOutputStream(s.getOutputStream());

                sc = new ServerConnection(s, mysql);
                sc.start();
                connections.add(sc);
                System.out.println(s + " hat den Server betreten!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized boolean isStopped() {
        return this.stop;
    }

}
