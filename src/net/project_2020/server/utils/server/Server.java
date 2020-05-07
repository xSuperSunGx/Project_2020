package net.project_2020.server.utils.server;


import net.project_2020.server.utils.coding.CodeHelper;
import net.project_2020.server.utils.coding.CodingProperty;

import java.io.DataInputStream;
import java.io.IOException;
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

    public int getPort() {
        return port;
    }

    public Server(int port, String servername) {
        super(servername);
        this.port = port;
        stop = false;
    }

    public synchronized void disconnect() {
        stop = true;
        try {
            for (ServerConnection sc : connections) {
                sc.disconnect();
                Thread.currentThread().interrupt();
            }
            ss.close();
            System.exit(0);
        } catch (IOException e) {
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
            String name;
            String[] lol;
            while(!isStopped()) {
                s = ss.accept();
                System.out.println("Connecting...");
                name = new DataInputStream(s.getInputStream()).readUTF();
                name = CodingProperty.decode(CodeHelper.COMMUNICATION.getCode(), name);
                lol = name.split(CodingProperty.seperate);
                sc = new ServerConnection(lol[1], s);
                sc.start();
                connections.add(sc);
                System.out.println(lol[1] + " hat den Server betreten!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized boolean isStopped() {
        return this.stop;
    }

}
