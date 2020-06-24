package net.project_2020.client.utils.mysql;


import com.blogspot.debukkitsblog.util.FileStorage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.project_2020.client.Workbench;
import net.project_2020.client.utils.ErrorMessage;
import net.project_2020.client.utils.coding.CodeHelper;
import net.project_2020.client.utils.coding.CodingProperty;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.sql.*;
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



    //String host, String database, int port, String username, String password
    //"localhost", "project", 3306, "project", "BuwuB2W55O8AfOQ6CuJoROP7yEt5BO"
    public MySQLManager(File file, Stage primarystage) {
        this.host = "localhost";
        this.database = "project";
        this.port = 3306;
        this.username = "project";
        this.password = "BuwuB2W55O8AfOQ6CuJoROP7yEt5BO";
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
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(MySQLManager.class.getClassLoader().getResource("net/project_2020/client/database/Database.fxml"));
                stage.setTitle("Database Configuration");
                stage.setAlwaysOnTop(true);
                stage.setMinWidth(460);
                stage.setMinHeight(390);
                stage.setOnCloseRequest(event -> {
                    if(primarystage.isShowing()) {
                        ErrorMessage.sendErrorMessage("Failed to conect to database!", "On the first start of the program, you must fill in your data!", "MySQL-Connection Error");
                        event.consume();
                    }
                });
                Scene scene = new Scene(root, 460, 390);
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
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



    public static MySQLManager changeInformation(String host, String database, int port, String username, String password) {

       File file = new File("info.dat");
        try {
            if(!file.exists())file.createNewFile();
            FileStorage storage = new FileStorage(file);
            if(storage.get("info") == null) {
                storage.store("info", new MySQLData(host, database, port, username, password));
                storage.save();
            } else {
                MySQLData data = (MySQLData)storage.get("info");
                data.setAll(host, database, port, username, password);
                storage.store("info", data);
                storage.save();
            }

            return new MySQLManager(file, Workbench.mainstage);




        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
