package userinterface;

import common.PropertyFile;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import utilities.Debug;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DebugBrowser {
    private Broker broker;
    private ArrayList<String> tableNames;

    private String selectedTable;

    public DebugBrowser() {
        // Populate list of tables we will operate on
        getTableNames();

        // Create GUI
        Stage mainStage = new Stage();
        mainStage.setResizable(false);

        Browser browserView = new Browser();
        Scene newScene = new Scene(browserView);

        mainStage.setScene(newScene);
        mainStage.show();
    }

    // Query database metadata for names of tables
    private void getTableNames(){
        broker = new Broker();
        try {
            // Retrieve the metadata for the connection
            DatabaseMetaData dbMeta = broker.getConnection().getMetaData();

            // Retrieve the metadata for the tables in the database we are using
            ResultSet tables = dbMeta.getTables(broker.dbName, null, null, null);

            tableNames = new ArrayList<String>(); // Store each table name in an array
            while (tables.next()) {
                tableNames.add(tables.getString(3)); // The third column in the tables result is the name of the table
            }
            Debug.logMsg("Retrieved tables: " + Arrays.toString(tableNames.toArray()));
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    @SuppressWarnings("SqlNoDataSourceInspection")
    private class Browser extends Group {
        private final BorderPane content;

        public Browser() {
            content = new BorderPane();

            makeHeader();

            try{
                makeLefter();
                makeCenter();
            }catch(SQLException ex){
                ex.printStackTrace();
            }

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

        private void makeLefter() {
            TableView<String> table = new TableView<>();
            TableColumn<String,String> col = new TableColumn<>("Tables");

            // I don't really know what this does
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String, String> p) {
                    return new SimpleStringProperty((p.getValue()));
                }
            });

            for (String s : tableNames)
                table.getItems().add(s);

            table.getColumns().add(col);
            table.setOnMousePressed(e -> {
                if (e.isPrimaryButtonDown() && e.getClickCount() >= 2) {
                    selectedTable = table.getSelectionModel().getSelectedItem();
                    try{
                        makeCenter();
                    }catch(SQLException ex){
                        ex.printStackTrace();
                    }
                }
            });

            table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            VBox box = new VBox();
            box.getChildren().addAll(table);
            box.setPrefWidth(100);

            content.setLeft(box);
        }

        private void makeCenter() throws SQLException {
            TableView<ArrayList<Object>> table = new TableView<>();

            if(selectedTable == null){
                content.setCenter(table);
                return;
            }

            //Debug.logMsg("Executing query to retrieve " + selectedTable);
            Statement stmt = broker.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from " + selectedTable);
            ResultSetMetaData rsMeta = rs.getMetaData();

            ArrayList<ArrayList<Object>> data = new ArrayList<>();
            ArrayList<String> colNames = new ArrayList<>();

            int columnCount = rsMeta.getColumnCount();

            // Retrieve names of columns from table
            for (int i = 1 ; i <= columnCount ; i++)
                colNames.add(rsMeta.getColumnName(i));
            //Debug.logMsg("Retrieved column names %s", colNames.toString());

            // Iterate over results from SQL query, add each row to data
            while (rs.next()) {
                ArrayList<Object> row = new ArrayList<>();
                for (int i = 1 ; i <= columnCount ; i++)
                    row.add(rs.getObject(i));
                data.add(row);
                //Debug.logMsg("Got row " + row.toString());
            }

            // Create Table
            for(int i=0; i < colNames.size(); i++){
                //Debug.logMsg("Creating column for " + colNames.get(i));
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

    private static class Broker {
        private Driver dbDriver;
        private Connection dbConnection;
        private final String dbName, username, password, server;

        public Broker() {
            PropertyFile props = new PropertyFile("dbConfig.ini");
            dbName   = props.getProperty("dbName");
            username = props.getProperty("username");
            password = props.getProperty("password");
            server   = props.getProperty("server");
            String driverClassName = "com.mysql.jdbc.Driver";

            try { // load and register the JDBC driver classes
                dbDriver = (Driver) Class.forName(driverClassName).getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException exc)    { Debug.logErr("Could not load driver class: ClassNotFoundException");
            } catch (InstantiationException exc)    { Debug.logErr("Could not load driver class: InstantiationException");
            } catch (IllegalAccessException exc)    { Debug.logErr("Could not load driver class: IllegalAccessException");
            } catch (NoSuchMethodException exc)     { Debug.logErr("Could not load driver class: NoSuchMethodException");
            } catch (InvocationTargetException exc) { Debug.logErr("Could not load driver class: InvocationTargetException"); }
        }

        protected Connection getConnection() {
            if (dbConnection == null
                    && (dbName != null) && (username != null) && (password != null)) {
                try {
                    Debug.logMsg("Connecting to database at " + server);
                    dbConnection = dbDriver.connect(String.format("jdbc:mysql://%s:3306/%s?user=%s&password=%s",
                                                        server, dbName, username, password),
                                                    null);
                    if (dbConnection == null)
                        Debug.logErr("Could not connect to database!");
                } catch (SQLException exc) {
                    Debug.logErr("Could not connect to database! (SQL Exception)\n" + exc.getMessage());
                }
            }
            return dbConnection;
        }
    }
}
