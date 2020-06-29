package net.project_2020.server;

import net.project_2020.server.utils.mysql.MySQLManager;
import net.project_2020.server.utils.server.Server;

import java.io.File;
import java.util.Scanner;

public class Workbench {

    public static void main(String[] args) {

        MySQLManager mysql = new MySQLManager(new File("info.dat"));
        Server s = new Server(1304, "Multithreading-Server", mysql);
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
