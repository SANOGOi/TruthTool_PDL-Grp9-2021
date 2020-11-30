import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.o7planning.javafx.model.ComboExtractor;
import org.o7planning.javafx.model.ComboUrl;
import org.o7planning.javafx.model.ExtractorList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.function.Function;

public class CsvEditor extends Application{

    static <S, T> Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> createArrayValueFactory(Function<S, T[]> arrayExtractor, final int index) {
        if (index < 0) {
            return cd -> null;
        }
        return cd -> {
            T[] array = arrayExtractor.apply(cd.getValue());
            return array == null || array.length <= index ? null : new SimpleObjectProperty<>(array[index]);
        };
    }

    private final TableView<Record> tableView = new TableView<>();

    private final ObservableList<Record> dataList
            = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) throws IOException, CsvValidationException {

        ComboBox<ComboUrl> comboBoxUrl = new ComboBox<ComboUrl>();

        ObservableList<ComboUrl> list1 = ExtractorList.getUrlList();

        comboBoxUrl.setItems(list1);
        comboBoxUrl.getSelectionModel().select(1);

        ComboBox<ComboExtractor> comboBox = new ComboBox<ComboExtractor>();

        ObservableList<ComboExtractor> list = ExtractorList.getExtractorList();

        comboBox.setItems(list);
        comboBox.getSelectionModel().select(1);

        FlowPane root = new FlowPane();
        //FlowPane root1 = new FlowPane();
        //FlowPane root2 = new FlowPane();
        root.setPadding(new Insets(10));
        root.setHgap(5);

        /*root1.setPadding(new Insets(10));
        root1.setHgap(5);
        root2.setPadding(new Insets(10));
        root2.setHgap(5);
        */

        String styles =
                "-fx-margin-right: 50px;";
        Label labelUrl = new Label("Select Url:");
        labelUrl.setPrefHeight(20);
        root.getChildren().add(labelUrl);
        Label labelExtract = new Label("Select Extractor");
        labelExtract.setStyle(styles);
        root.getChildren().add(labelExtract);
        //root2.getChildren().add(new Label("Select Extractor:"));
        //root1.getChildren().add(comboBox);

        //stage.setTitle("ComboxBox (o7planning.org)");
        //Scene scene = new Scene(root1, 350, 300);
        //stage.setScene(scene);
        //
        readCSV();
        stage.setTitle("CSV Editor");

        //Group root = new Group();
        tableView.setEditable(true);

        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                return new EditingCell();
            }
        };

        TableColumn[] tableColumns = new TableColumn[3];


//        for (int i = 0; i < dataList.size(); i++){
//            for (int j = 0; j < dataList.get(i).getSimpleStringPropertyList().length; j++) {
//                tableColumns[j] = new TableColumn(dataList.get(i).getElement(j));
////                tableColumns[j].setCellValueFactory(
////                        new PropertyValueFactory<>("f4"));
////                tableColumns[j].setCellFactory(cellFactory);
//                tableColumns[j].setCellValueFactory(createArrayValueFactory(Record::getSimpleStringPropertyList, j));
//
//                int finalJ = j;
//                tableColumns[j].setOnEditCommit(new EventHandler<CellEditEvent<Record, String>>() {
//                    @Override
//                    public void handle(CellEditEvent<Record, String> t) {
//                        ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSimpleStringPropertyList(finalJ,t.getNewValue());
//                    }
//                });
//            }
//        }
//        System.out.println(createArrayValueFactory(Record::getSimpleStringPropertyList, 2));

        TableColumn columnF1 = new TableColumn("f1");
        columnF1.setCellValueFactory(new PropertyValueFactory<>("f0"));
        columnF1.setCellFactory(cellFactory);

        columnF1.setOnEditCommit(new EventHandler<CellEditEvent<Record, String>>() {
            @Override
            public void handle(CellEditEvent<Record, String> t) {
                ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).setF0(t.getNewValue());
            }
        });

        TableColumn columnF2 = new TableColumn("f2");
        columnF2.setCellValueFactory(new PropertyValueFactory<>("f1"));
        columnF2.setCellFactory(cellFactory);

        columnF2.setOnEditCommit(new EventHandler<CellEditEvent<Record, String>>() {
            @Override
            public void handle(CellEditEvent<Record, String> t) {
                ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).setF1(t.getNewValue());
            }
        });

        TableColumn columnF3 = new TableColumn("f3");
        columnF3.setCellValueFactory(new PropertyValueFactory<>("f2"));
        columnF3.setCellFactory(cellFactory);

        columnF3.setOnEditCommit(new EventHandler<CellEditEvent<Record, String>>() {
            @Override
            public void handle(CellEditEvent<Record, String> t) {
                ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).setF0(t.getNewValue());
            }
        });

        tableColumns[0] = columnF1;
        tableColumns[1] = columnF2;
        tableColumns[2] = columnF3;

        tableView.setItems(dataList);
        tableView.getColumns().addAll(tableColumns);

        VBox vBox = new VBox();
        vBox.setSpacing(100);
        vBox.autosize();

        vBox.getChildren().add(tableView);
        root.getChildren().add(comboBoxUrl);
        root.getChildren().add(comboBox);
        root.getChildren().add(vBox);
        Button btnValider = new Button("Valider");
        root.getChildren().add(btnValider);
        Label label = new Label("");
        root.getChildren().add(label);

        btnValider.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if(comboBox.getValue().getExtractor().equals("Python")){
                    if (comboBoxUrl.getValue() != null){
                        label.setText(new Date().toString());
                    }

                }
                //label.setText(new Date().toString());
            }
        });


        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    private void readCSV() throws IOException, CsvValidationException {

        String csvFile = "src/Comparison_(grammar)-0.csv";
        String fieldDelimiter = ",";
        CSVReader reader = null;
        BufferedReader br;


            br = new BufferedReader(new FileReader(csvFile));

            String line = "";
        String[] fields;
//        line = br.readLine();
//        String[] fields = line.split(fieldDelimiter, -1);
//            System.out.println();
        reader = new CSVReader(new FileReader(csvFile));
        while ((fields = reader.readNext()) != null) {
            //System.out.println(fields[0]);
            Record record = new Record(fields[0], fields[1], fields[2]);
                dataList.add(record);
        }
//            while ((line = br.readLine()) != null) {
//                fields = line.split(fieldDelimiter, -1);
//                System.out.println(fields[0]);
////                System.out.println(fields[0] + " " + fields[1] + " " + fields[2]);
//                Record record = new Record(fields, fields[0], fields[1], fields[2]);
//
//                dataList.add(record);
//            }
    }

        public static void main(String[] args){

            Application.launch(args);
    }
}
