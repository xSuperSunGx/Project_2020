package net.project_2020.server;

import net.project_2020.server.utils.server.Server;

import java.util.Scanner;

public class Workbench {

    public static void main(String[] args) {

        Server s = new Server(1304, "Multithreading-Server");
        s.start();
        Scanner scanner = new Scanner(System.in);
        while(true) {

            if(scanner.hasNextLine()) {
                if(scanner.nextLine().equalsIgnoreCase("quit")) {
                    s.disconnect();
                }
            }
        }


    }
}
