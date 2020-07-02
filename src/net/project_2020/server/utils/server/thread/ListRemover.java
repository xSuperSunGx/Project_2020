package net.project_2020.server.utils.server.thread;

import net.project_2020.server.utils.server.Server;
import net.project_2020.server.utils.server.ServerConnection;

public class ListRemover extends Thread{

    private Server server;

    public ListRemover(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(1000*30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < server.connections.size(); i++) {

                if(server.connections.get(i).isStopped()) {
                    server.connections.remove(i);

                }
            }

        }

    }
}
