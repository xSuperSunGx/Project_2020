package net.project_2020.server.utils.mysql;


import com.blogspot.debukkitsblog.util.FileStorage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.project_2020.client.Workbench;
import net.project_2020.client.utils.ErrorMessage;
import net.project_2020.client.utils.coding.CodeHelper;
import net.project_2020.client.utils.coding.CodingProperty;

import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class MySQLManager {

    private String host;
    private String database;
    private int port;
    private String username;
    private String password;
    private Connection con;
    private File file;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return CodingProperty.encode(CodeHelper.INFORMATION.getCode(), password);
    }

    public String getDatabase() {
        return database;
    }

    public Connection getConnection() {
        return con;
    }

    public boolean containsUsername(String name) {
        try {
            PreparedStatement st = this.con.prepareStatement("SELECT `username` FROM `accounts`");
            ResultSet rs = st.executeQuery();
            System.out.println(CodingProperty.encode(CodeHelper.INFORMATION.getCode(), name));
            while (rs.next()) {
                if(CodingProperty.decode(CodeHelper.INFORMATION.getCode(), rs.getString(1)).equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public boolean containsCompination(String username, String passsword) {
        try {
            PreparedStatement st = this.con.prepareStatement("SELECT * FROM `accounts`");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if(CodingProperty.decode(CodeHelper.INFORMATION.getCode(), rs.getString("username"))
                        .equalsIgnoreCase(username)
                        && CodingProperty.decode(CodeHelper.INFORMATION.getCode(), rs.getString("password"))
                        .equalsIgnoreCase(passsword)) {
                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void registerAccount(String username, String password) {
        try {
            PreparedStatement st = this.con.prepareStatement("INSERT INTO `accounts` (`username`, `password`) VALUES (?,?)");
            st.setString(1, CodingProperty.encode(CodeHelper.INFORMATION.getCode(), username));
            st.setString(2, CodingProperty.encode(CodeHelper.INFORMATION.getCode(), password));
            st.executeUpdate();






        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //String host, String database, int port, String username, String password
    //"localhost", "project", 3306, "project", "BuwuB2W55O8AfOQ6CuJoROP7yEt5BO"
    public MySQLManager(File file) {
        this.host = "localhost";
        this.database = "project";
        this.port = 3306;
        this.username = "project";
        this.password = " ";
        this.file = file;

        try {
            FileStorage storage = new FileStorage(file);
            if(storage.get("info") != null) {
                MySQLData n = (MySQLData) storage.get("info");


                this.host = n.getHost();
                this.database = n.getDatabase();
                this.port = n.getPort();
                this.username = n.getUsername();
                this.password = CodingProperty.decode(CodeHelper.INFORMATION.getCode(), n.getPassword());
                try {
                    con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&characterEncoding=latin1&useConfigs=maxPerformance",
                            username, password);

                    System.out.println("§aMySQL-Connection successful!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                configureTables();
            } else {
                Scanner s = new Scanner(System.in);
                System.out.print("Please enter your MySQL-Hostname: ");
                this.host = s.nextLine();
                System.out.println();
                System.out.print("Please enter your MySQL-Port: ");
                String port = s.nextLine();
                System.out.println();
                System.out.print("Please enter your MySQL-Database: ");
                this.database = s.nextLine();
                System.out.println();
                System.out.print("Please enter your MySQL-Username: ");
                this.username = s.nextLine();
                System.out.println();
                System.out.print("Please enter your MySQL-Password: ");
                this.password = s.nextLine();
                System.out.println();
                System.out.println();
                System.out.println("Connecting...");
                String[] lol = {host, port, database, username, password};
                List<String> g = Arrays.asList(new String[]{});
                g.forEach(s1 -> {
                    System.out.println(s1);
                });

                MySQLData data = new MySQLData(host,database, Integer.parseInt(port), username, this.password);
                storage.store("info", data);
                storage.save();


                MySQLData n = (MySQLData) storage.get("info");


                this.host = n.getHost();
                this.database = n.getDatabase();
                this.port = n.getPort();
                this.username = n.getUsername();
                this.password = n.getPassword();


                try {
                    con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&characterEncoding=latin1&useConfigs=maxPerformance",
                            username, password);

                    System.out.println("§aMySQL-Connection successful!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                configureTables();
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void configureTables() {
        try {
            Scanner scanner = new Scanner(getClass().getClassLoader().getResourceAsStream("net/project_2020/server/utils/mysql/config.cfg"));


            String gesamt = "";
            while (scanner.hasNextLine()) {
                gesamt += scanner.nextLine() + "\n";
            }
            if (gesamt.endsWith("\n")) gesamt = gesamt.substring(0, gesamt.length() - 2);
            String[] args = gesamt.split("%");
            PreparedStatement statement = null;
            for (String arg : args) {
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
            if(con != null)
                if (con != null || !con.isClosed()) con.close();

            System.out.println("MySQL-Connection closed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
