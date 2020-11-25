import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvEditor extends Application{

    public class Record {
        //Assume each record have 3 elements, all String

        private SimpleStringProperty f1, f2, f3;

        public String getF1() {
            return f1.get();
        }

        public String getF2() {
            return f2.get();
        }

        public String getF3() {
            return f3.get();
        }

        Record(String f1, String f2, String f3) {
            this.f1 = new SimpleStringProperty(f1);
            this.f2 = new SimpleStringProperty(f2);
            this.f3 = new SimpleStringProperty(f3);
        }

    }

    private final TableView<Record> tableView = new TableView<>();

    private final ObservableList<Record> dataList
            = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("CSV Editor");

        Group root = new Group();

        TableColumn columnF1 = new TableColumn("f1");
        columnF1.setCellValueFactory(
                new PropertyValueFactory<>("f1"));

        TableColumn columnF2 = new TableColumn("f2");
        columnF2.setCellValueFactory(
                new PropertyValueFactory<>("f2"));

        TableColumn columnF3 = new TableColumn("f3");
        columnF3.setCellValueFactory(
                new PropertyValueFactory<>("f3"));


        tableView.setItems(dataList);
        tableView.getColumns().addAll(
                columnF1, columnF2, columnF3);

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().add(tableView);

        root.getChildren().add(vBox);

        stage.setScene(new Scene(root, 700, 250));
        stage.show();

        readCSV();
    }

    private void readCSV() throws IOException {

        String csvFile = "C:\\Users\\morningstar\\Desktop\\TruthTool_PDL-Grp9-2021\\src\\Comparison_(grammar)-0.csv";
        String fieldDelimiter = ",";

        BufferedReader br;


            br = new BufferedReader(new FileReader(csvFile));

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(fieldDelimiter, -1);

                Record record = new Record(fields[0], fields[1], fields[2]);
                dataList.add(record);

            }

    }

        public static void main(String[] args){
            Application.launch(args);
    }
}
