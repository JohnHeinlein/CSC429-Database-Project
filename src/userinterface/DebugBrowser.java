package userinterface;

import common.PropertyFile;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utilities.Debug;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DebugBrowser {
    private Broker broker;
    private ArrayList<String> tableNames;

    private String selectedTable;

    public DebugBrowser() {
        App app = new App();
    }

    private class App {
        public App() {
            // Create database connection
            broker = new Broker();
            broker.getConnection();

            // Populate our array of table names
            try {
                // Retrieve the metadata for the connection
                DatabaseMetaData dbMeta = broker.getConnection().getMetaData();

                // Retrieve the metadata for the tables in the database we are using
                ResultSet tables = dbMeta.getTables(broker.dbName, null, null, null);

                // Store each table name in an array
                tableNames = new ArrayList<String>();
                while (tables.next()) {
                    // The third column in the tables result is the name of the table
                    tableNames.add(tables.getString(3));
                }
                Debug.logMsg("Retrieved tables: " + Arrays.toString(tableNames.toArray()));
//                for(String tableName : tableNames){
//                    Debug.logErr("""
//
//                            =======================
//                            %s
//                            =======================
//                            """, tableName);
//                    ResultSet colInfo = dbMeta.getColumns(null,null,tableName,null);
//                    ResultSetMetaData colInfoMeta = colInfo.getMetaData();
//
//                    ArrayList<String> fieldNames = new ArrayList<>();
//                    while(colInfo.next()){
//                        for(int i = 1; i <= colInfoMeta.getColumnCount(); i++){
//                            String colName = colInfoMeta.getColumnName(i);
//                            String colStr = colInfo.getString(i);
//
//                            // Only print if we care about the value
//                            if(new ArrayList<String>(Arrays.asList(
//                                    "COLUMN_NAME", "TYPE_NAME",
//                                    "COLUMN_SIZE", "IS_AUTOINCREMENT"
//                            )).contains(colName)){
//                                Debug.logErr("[%d] %s %s",i,colName,colStr);
//                            }
//                        }
//                        Debug.logErr("-----------------");
//                    }
//                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // Create GUI
            Stage mainStage = new Stage();

            Browser browserView = new Browser();
            Scene newScene = new Scene(browserView);

            mainStage.setScene(newScene);
            mainStage.show();
        }
    }

    private class Browser extends Group {
        private BorderPane content;
        private VBox header;
        private HBox footer;

        public Browser() {
            content = new BorderPane();

            makeHeader();
            makeFooter();
            makeLefter();
            //makeRighter();

            try{
                makeCenter();
            }catch(SQLException ex){
                ex.printStackTrace();
            }

            content.setBottom(footer);
            //content.setRight(righter);

            content.setPrefWidth(1000);
            this.getChildren().add(content);
        }

        private void makeHeader() {
            Label serverInfo = new Label(String.format("Viewing database %s on connection %s", broker.dbName, broker.server));

            VBox box = new VBox();
            box.setAlignment(Pos.CENTER);
            box.getChildren().addAll(serverInfo);

            content.setTop(box);
        }

        private void makeFooter() {
            HBox box = new HBox();

            footer = box;
        }

        private void makeLefter() {
            VBox box = new VBox();

            TableView<TableModel> table = new TableView<>();
            table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            TableColumn<TableModel, String> col = new TableColumn<>("Tables");
            col.setCellValueFactory(new PropertyValueFactory<>("tableName"));

            for (String s : tableNames)
                table.getItems().add(new TableModel(s));

            table.getColumns().add(col);
            table.setOnMousePressed(e -> {
                if (e.isPrimaryButtonDown() && e.getClickCount() >= 2) {
                    selectedTable = table.getSelectionModel().getSelectedItem().getTableName();
                    try{
                        makeCenter();
                    }catch(SQLException ex){
                        ex.printStackTrace();
                    }
                }
            });

            box.getChildren().addAll(table);
            box.setPrefWidth(100);

            content.setLeft(box);
        }

//        private void makeRighter(){
//            VBox box = new VBox();
//
//            righter = box
//        }

        private void makeCenter() throws SQLException {
//            // Retrieve the metadata for the connection
//            DatabaseMetaData dbMeta = broker.getConnection().getMetaData();
//
//            // Retrieve the metadata for the table
//            ResultSet colInfo = dbMeta.getColumns(null, null, selectedTable, null);
//            ResultSetMetaData colInfoMeta = colInfo.getMetaData();
//
//            ArrayList<String> fieldNames = new ArrayList<>();
//
//            Debug.logMsg("Parsing table " + selectedTable);
//            while (colInfo.next()) {
//                for (int i = 1; i <= colInfoMeta.getColumnCount(); i++) {
//                    String colName = colInfoMeta.getColumnName(i);
//                    String colStr = colInfo.getString(i);
//
//                    // Only process the name of the column
//                    if (colName.equals("COLUMN_NAME")) {
//                        fieldNames.add(colStr);
//                        Debug.logMsg("\tAdded " + colStr);
//                    }
//                }
//            }

            TableView<ArrayList<Object>> table = new TableView<>();

            if(selectedTable == null){
                content.setCenter(table);
                return;
            }

            Debug.logMsg("Executing query to retrieve " + selectedTable);
            Statement stmt = broker.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from " + selectedTable);
            ResultSetMetaData rsMeta = rs.getMetaData();

            ArrayList<ArrayList<Object>> data = new ArrayList<>();
            ArrayList<String> colNames = new ArrayList<>();

            int columnCount = rsMeta.getColumnCount();
            for (int i = 1 ; i <= columnCount ; i++) {
                colNames.add(rsMeta.getColumnName(i));
            }
            Debug.logMsg("Retrieved column names %s", colNames.toString());

            while (rs.next()) {
                ArrayList<Object> row = new ArrayList<>();
                for (int i = 1 ; i <= columnCount ; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
                Debug.logMsg("Got row " + row.toString());
            }

            // Create Table
            for(int i=0; i < colNames.size(); i++){
                Debug.logMsg("Creating column for " + colNames.get(i));
                TableColumn<ArrayList<Object>,Object> col = new TableColumn<>(colNames.get(i));

                int colIndex = i; // For lambda safety
                col.setCellValueFactory(cellData ->
                    new SimpleObjectProperty<>(cellData.getValue().get(colIndex))
                );
                table.getColumns().add(col);
            }
            table.getItems().setAll(data);

            VBox box = new VBox();
            box.setPrefWidth(500);
            box.getChildren().add(table);
            content.setCenter(table);
        }
    }

    private class Broker {
        public Driver dbDriver;
        public Connection dbConnection;

        public String dbName, username, password, server;

        public Broker() {
            PropertyFile props = new PropertyFile("dbConfig.ini");
            dbName = props.getProperty("dbName");
            username = props.getProperty("username");
            password = props.getProperty("password");
            server = props.getProperty("server");
            String driverClassName = "com.mysql.jdbc.Driver";

            try {
                // load and register the JDBC driver classes
                dbDriver = (Driver) Class.forName(driverClassName).getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException exc) {
                Debug.logErr("Could not load driver class: ClassNotFoundException");
            } catch (InstantiationException exc) {
                Debug.logErr("Could not load driver class: InstantiationException");
            } catch (IllegalAccessException exc) {
                Debug.logErr("Could not load driver class: IllegalAccessException");
            } catch (NoSuchMethodException exc) {
                Debug.logErr("Could not load driver class: NoSuchMethodException");
            } catch (InvocationTargetException exc) {
                Debug.logErr("Could not load driver class: InvocationTargetException");
            }
        }

        public Connection getConnection() {
            if (dbConnection == null
                    && (dbName != null)
                    && (username != null)
                    && (password != null)) {
                try {
                    Debug.logMsg("Connecting to database at " + server);
                    dbConnection = dbDriver.connect(
                            String.format("jdbc:mysql://%s:3306/%s?user=%s&password=%s",
                                    server, dbName, username, password),
                            null
                    );
                    if (dbConnection == null)
                        Debug.logErr("Could not connect to database!");
                } catch (SQLException exc) {
                    Debug.logErr("Could not connect to database! (SQL Exception)" + "\n" + exc.getMessage());
                }
            }
            return dbConnection;
        }
    }

    public class TableModel {
        private String tableName;

        public TableModel(String name) {
            tableName = name;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String name) {
            tableName = name;
        }
    }
}
