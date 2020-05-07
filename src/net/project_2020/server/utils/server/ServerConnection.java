package net.project_2020.server.utils.server;

import net.project_2020.server.utils.coding.CodeHelper;
import net.project_2020.server.utils.coding.CodingProperty;
import net.project_2020.server.utils.coding.PacketFormat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class ServerConnection extends Thread{


    private Socket s;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean stopped;


    public ServerConnection(String name, Socket s) {
        super(name);
        this.s = s;
        stopped = false;
    }





    @Override
    public void run() {
        try {
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());
            String line;
            String decode;
            PacketFormat format;
            while(!this.isStopped()) {
                while(in.available() == 0) {
                    Thread.sleep(1);
                }
                line = in.readUTF();
                decode = CodingProperty.decode(CodeHelper.COMMUNICATION.getCode(), line);
                format = new PacketFormat(decode);
                System.out.println(!format.getMessage().equalsIgnoreCase("close"));
                if(!format.getMessage().equalsIgnoreCase("close")) {
                    System.out.println(format.getMessage());
                    System.out.println(format.getNickname());
                    System.out.println(format.getNickname() + " > " + CodingProperty.decode(CodeHelper.MESSAGE.getCode(), format.getMessage()));
                    Server.sendToAllClients(line);
                } else {
                    stopped = true;
                }

            }
            System.out.println(super.getName() + " hat die Verbindung getrennt!");
            in.close();
            out.close();
            s.close();
            Server.connections.remove(this);
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public synchronized void disconnect() throws IOException {
        System.out.println(super.getName() + " hat die Verbindung getrennt!");
        sendToClientFromServer("close");

        Server.connections.remove(this);
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


    public synchronized void sendToClient(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized  void sendToClientFromServer(String message) {
        try {
            out.writeUTF(CodingProperty.encode(CodeHelper.COMMUNICATION.getCode(), message + CodingProperty.seperate + "Server"));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
