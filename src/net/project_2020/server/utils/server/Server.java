package net.project_2020.server.utils.server;


import net.project_2020.server.utils.mysql.MySQLManager;
import net.project_2020.server.utils.server.thread.ListRemover;
import net.project_2020.utils.packetoption.ServerCommunication;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{

    public List<ServerConnection> connections;
    private int port;
    private ServerSocket ss;
    private Socket s;
    private ServerConnection sc;
    private boolean stop;
    private MySQLManager mysql;

    public int getPort() {
        return port;
    }

    public Server(int port, String servername, MySQLManager mySQLManager) {
        super(servername);
        this.port = port;
        stop = false;
        this.mysql = mySQLManager;
        connections = new ArrayList<ServerConnection>();
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

    public synchronized void sendToAllClients(ServerCommunication line) {
        for (ServerConnection sc : this.connections) {
            if(sc.isStopped()) {
                this.connections.remove(sc);
            } else {

                sc.sendToClient(line);

            }
        }

    }

    @Override
    public void run() {
        try {
            ListRemover lr = new ListRemover(this);
            lr.start();
            ss = new ServerSocket(this.port);
            System.out.println("Server listened on Port " + this.port);
            while(!isStopped()) {
                s = ss.accept();
                System.out.println("Connecting...");
                sc = new ServerConnection(s, mysql, this);
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

    public void sendToAllClientsExept(ServerCommunication message, ServerConnection serverConnection) {
        for (ServerConnection sc : this.connections) {
            if(sc.isStopped()) {
                this.connections.remove(sc);
            } else if(!sc.equals(serverConnection)){

                sc.sendToClient(message);

            }
        }
    }
}
