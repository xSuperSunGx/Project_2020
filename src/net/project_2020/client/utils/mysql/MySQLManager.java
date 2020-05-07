package net.project_2020.client.utils.mysql;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Scanner;

public class MySQLManager {

    private String host;
    private String database;
    private int port;
    private Connection con;

    public String getDatabase() {
        return database;
    }

    public Connection getConnection() {
        return con;
    }

    public MySQLManager(String host, String database, int port, String username, String password) {
        this.host = host;
        this.database = database;
        this.port = port;
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&characterEncoding=latin1&useConfigs=maxPerformance",
                    username, password);

            System.out.println("§aMySQL-Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void configureTables() {
        try {
            Scanner scanner = new Scanner(getClass().getResourceAsStream("config.cfg"));


            String gesamt = "";
            while (scanner.hasNextLine()) {
                gesamt += scanner.nextLine() + "\n";
            }
            if (gesamt.endsWith("\n")) gesamt = gesamt.substring(0, gesamt.length() - 2);
            String[] args = gesamt.split("%");
            PreparedStatement statement = null;
            for (String arg : args) {
                System.out.println(arg);
                statement = con.prepareStatement(arg);
                statement.executeUpdate();
            }
            scanner.close();
            statement.close();


//            st.executeUpdate("CREATE TABLE IF NOT EXISTS `users` (\n" +
//                    "\t`ID` INT NOT NULL AUTO_INCREMENT,\n" +
//                    "\t`Name` VARCHAR(50) NOT NULL,\n" +
//                    "\t`UUID` VARCHAR(50) NOT NULL,\n" +
//                    "\t`onlinetime` BIGINT UNSIGNED ZEROFILL NOT NULL DEFAULT 0,\n" +
//                    "\tPRIMARY KEY (`ID`)\n" +
//                    ")\n" +
//                    "COLLATE='utf8mb4_0900_ai_ci'\n" +
//                    ";\n");
//            st.executeUpdate("CREATE TABLE IF NOT EXISTS `BankAccounts` (\n" +
//                    "\t`ID` INT NOT NULL AUTO_INCREMENT,\n" +
//                    "\t`Account` LONGTEXT NOT NULL,\n" +
//                    "\tPRIMARY KEY (`ID`)\n" +
//                    ")\n" +
//                    "COLLATE='utf8mb4_0900_ai_ci'\n" +
//                    ";\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void disconnect() {
        try {
            if (!con.isClosed()) con.close();
            System.out.println("MySQL-Connection closed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
