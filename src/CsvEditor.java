import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.o7planning.javafx.model.ComboExtractor;
import org.o7planning.javafx.model.ComboUrl;
import org.o7planning.javafx.model.ExtractorList;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class CsvEditor extends Application {

    static <S, T> Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> createArrayValueFactory(Function<S, T[]> arrayExtractor, final int index) {
        if (index < 0) {
            return cd -> null;
        }
        return cd -> {
            T[] array = arrayExtractor.apply(cd.getValue());
            return array == null || array.length <= index ? null : new SimpleObjectProperty<>(array[index]);
        };
    }

    private final TableView<Model> tableView = new TableView<>();

    private final ObservableList<Model> dataList
            = FXCollections.observableArrayList();
    String csvFile = "src/Comparison_(grammar)-0.csv";

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

        TextField numTable = new TextField();
        numTable.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    numTable.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        root.getChildren().add(numTable);

        //root2.getChildren().add(new Label("Select Extractor:"));
        //root1.getChildren().add(comboBox);

        //stage.setTitle("ComboxBox (o7planning.org)");
        //Scene scene = new Scene(root1, 350, 300);
        //stage.setScene(scene);
        //
        int numberOfColumns = readCSV(csvFile);
        stage.setTitle("CSV Editor");

        Callback<TableColumn<Model, String>, TableCell<Model, String>> cellFactory = new Callback<TableColumn<Model, String>, TableCell<Model, String>>() {
            @Override
            public TableCell call(TableColumn p) {
                return new EditingCell();
            }
        };

        tableView.setEditable(true);
        TableColumn[] tableColumns = new TableColumn[numberOfColumns];
        for (int i = 0; i < numberOfColumns; i++) {
            // Create a column. For the time being, the property number is the display name.
            TableColumn<Model, String> column = new TableColumn<>("" + i);
            int finalI = i;
            column.setCellValueFactory(features -> features.getValue().propertyAt(finalI));
            column.setCellFactory(cellFactory);
            column.setOnEditCommit(new EventHandler<CellEditEvent<Model, String>>() {
                @Override
                public void handle(CellEditEvent<Model, String> t) {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setAt(finalI, t.getNewValue());
                }
            });
            tableColumns[finalI] = column;
        }
        tableView.getItems().setAll(dataList);
        tableView.getColumns().addAll(tableColumns);

        VBox vBox = new VBox();
        vBox.setSpacing(100);
        vBox.autosize();

        vBox.getChildren().add(tableView);
        Button btnSave = new Button("Enregistrer modification");

        root.getChildren().add(comboBoxUrl);
        root.getChildren().add(comboBox);



        Button btnValider = new Button("Valider");
        root.getChildren().add(btnValider);
        root.getChildren().add(vBox);
        root.getChildren().add(btnSave);

        Label label = new Label("");
        root.getChildren().add(label);

        // action on buttons
        btnSave.setOnAction(event -> {
            for (int i = 0; i<tableView.getItems().size(); i++){
                for (int j = 0; j< tableView.getColumns().size(); j++){
                    String newValue = tableView.getItems().get(i).getAt(j);
                    try {
                        updateCSV(csvFile, csvFile, newValue, i, j);
                    } catch (IOException | CsvException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnValider.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (comboBox.getValue().getExtractor().equals("Python")) {
                    if (comboBoxUrl.getValue() != null) {
                        label.setText(new Date().toString());
                    }

                }
                if (comboBox.getValue().getExtractor().equals("JWikiText")) {
                    if (comboBoxUrl.getValue() != null) {
                        System.out.println(comboBoxUrl.getValue());
                        label.setText(new Date().toString());
                    }

                }

                //label.setText(new Date().toString());
            }
        });


        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    private int readCSV(String csvPath) throws IOException, CsvValidationException {
        CSVReader reader;
        String[] fields;
        reader = new CSVReader(new FileReader(csvPath));
        int numberOfColumns = reader.readNext().length;
        while ((fields = reader.readNext()) != null) {
            Model model = new Model(fields);
            dataList.add(model);
        }
        return numberOfColumns;
    }

    public static void updateCSV(String input, String output, String  replace, int row, int col) throws IOException, CsvException {

        CSVReader reader = new CSVReader(new FileReader(input));
        List<String[]> csvBody = reader.readAll();
        csvBody.get(row)[col]=replace;
        reader.close();

        CSVWriter writer = new CSVWriter(new FileWriter(output));
        writer.writeAll(csvBody);
        writer.flush();
        writer.close();
    }

    public static void main(String[] args) {

        Application.launch(args);
    }
}
