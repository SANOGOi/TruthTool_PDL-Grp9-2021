import Extractor.ExtractType;
import Extractor.FileMatrix;
import Extractor.UrlMatrix;
import Extractor.WikipediaMatrix;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import javafx.application.Application;
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

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static Extractor.wikiMain.getListofUrls;
import static Extractor.wikiMain.mkCSVFileName;

public class CsvEditor extends Application {

    String createFileWithChoice = "inputdata" + File.separator + "wikiurl" +
            ".txt";

    private final TableView<Model> tableView = new TableView<>();

    private final ObservableList<Model> dataList
            = FXCollections.observableArrayList();
    //    String csvFile = "src/Comparison_(grammar)-0.csv";
    String csvFile = "";
    int numberOfColumns = 0;
    File directory = new File("output");

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

        ComboBox<Integer> comboNbrCsv = new ComboBox<>();
        setComboNbrCsv(comboNbrCsv, directory.getAbsolutePath());

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

//        TextField numTable = new TextField();
//        numTable.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue,
//                                String newValue) {
//                if (!newValue.matches("\\d*")) {
//                    numTable.setText(newValue.replaceAll("[^\\d]", ""));
//                }
//            }
//        });
//        root.getChildren().add(numTable);

        //root2.getChildren().add(new Label("Select Extractor:"));
        //root1.getChildren().add(comboBox);

        //stage.setTitle("ComboxBox (o7planning.org)");
        //Scene scene = new Scene(root1, 350, 300);
        //stage.setScene(scene);
        //
        stage.setTitle("CSV Editor");

        Callback<TableColumn<Model, String>, TableCell<Model, String>> cellFactory = new Callback<TableColumn<Model, String>, TableCell<Model, String>>() {
            @Override
            public TableCell call(TableColumn p) {
                return new EditingCell();
            }
        };

        tableView.setEditable(true);

        VBox vBox = new VBox();
        vBox.setSpacing(100);
        vBox.autosize();

        vBox.getChildren().add(tableView);
        Button btnSave = new Button("Enregistrer modification");

        root.getChildren().add(comboBoxUrl);
        root.getChildren().add(comboBox);
        root.getChildren().add(comboNbrCsv);


        Button btnValider = new Button("Valider");
        root.getChildren().add(btnValider);
        root.getChildren().add(vBox);
        root.getChildren().add(btnSave);

        Label label = new Label("");
        root.getChildren().add(label);

        // action on buttons
        btnSave.setOnAction(event -> {
            for (int i = 0; i < tableView.getItems().size(); i++) {
                for (int j = 0; j < tableView.getColumns().size(); j++) {
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
                tableView.getColumns().clear();
                try {
                    setComboNbrCsv(comboNbrCsv, directory.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (comboBox.getValue().getExtractor().equals("Python")) {
                    if (comboBoxUrl.getValue() != null) {
                        String urlCombo = comboBoxUrl.getValue().getExtractor();
                        String pathInput = "/home/isanogo/PycharmProjects/PDL_2021_groupe_9/input/wikiurls.txt";
                        String pathOutput = "/home/isanogo/PycharmProjects/PDL_2021_groupe_9/output";
                        File outPut = new File(pathOutput);
                        emptyDirectory(outPut);
                        try {
                            writeData(urlCombo, pathInput);
                            appelPythonExtract();
                            setComboNbrCsv(comboNbrCsv, pathOutput);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                if (comboBox.getValue().getExtractor().equals("JHtml")) {
                    if (comboBoxUrl.getValue().toString() != "") {
                        try {
                            extractHtml(comboBoxUrl.getValue().toString());
                            setComboNbrCsv(comboNbrCsv, directory.getAbsolutePath() + File.separator + "html");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                if (comboBox.getValue().getExtractor().equals("JWikiText")) {
                    if (comboBoxUrl.getValue().toString() != "") {
                        try {
                            extractWikitext(comboBoxUrl.getValue().toString());
                            setComboNbrCsv(comboNbrCsv, directory.getAbsolutePath() + File.separator + "wikitext");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                //label.setText(new Date().toString());
            }
        });

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (comboNbrCsv.getValue() != null) {
                    String csvFileName = comboBoxUrl.getValue()+ "-" + (comboNbrCsv.getValue() - 1) + ".csv";
                    tableView.getColumns().clear();
                    try {
                        if (comboBox.getValue().getExtractor().equals("Python")) {
                            numberOfColumns = readCSVPython(comboNbrCsv.getValue());
                        }
                        else if(comboBox.getValue().getExtractor().equals("JHtml")){
                            numberOfColumns = readCSV(directory.getAbsoluteFile() + File.separator + "html" + File.separator + csvFileName);
                        }
                        else if(comboBox.getValue().getExtractor().equals("JWikiText")){
                            numberOfColumns = readCSV(directory.getAbsoluteFile() + File.separator + "wikitext" + File.separator + csvFileName);
                        }
                        if (!dataList.isEmpty()) {
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
                            tableView.setItems(dataList);
                            tableView.getColumns().addAll(tableColumns);
                        }
                        tableView.refresh();
                    } catch (IOException | CsvValidationException ioException) {
                        ioException.printStackTrace();
                    }

                }

            }
        };

        comboNbrCsv.setOnAction(event);


        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    private void setComboNbrCsv (ComboBox<Integer> comboNbrCsv, String path) throws IOException {
        ObservableList<Integer> listIntNbrCsv = ExtractorList.getNbrCsv(path);
            comboNbrCsv.setItems(listIntNbrCsv);
            comboNbrCsv.getSelectionModel().select(1);
    }

    private void writeData(String url, String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(url);

        writer.close();
    }

    private void emptyDirectory(File folder){
        for(File file : folder.listFiles()){
            if(file.isDirectory()){
                emptyDirectory(file);
            }
            file.delete();
        } }

    private void appelPythonExtract() throws IOException {

        String PYTHON_OUTPUT  = "src/codePython.py";
        Process p = Runtime.getRuntime().exec("python3 " + PYTHON_OUTPUT);
        // output
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        // error
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String o;
        while ((o = stdInput.readLine()) != null) {
            System.out.println(o);
        }
        String err;
        while ((err = stdError.readLine()) != null) {
            System.out.println(err);
        }
    }

    private int readCSVPython (int tabNumber) throws IOException, CsvValidationException {
        return readCSV(ExtractorList.getFileSelected(tabNumber).getPath());
    }

    private int readCSV(String csvPath) throws IOException, CsvValidationException {
        dataList.clear();
        CSVReader reader;
        String[] fields;
        reader = new CSVReader(new FileReader(csvPath));
        int numberOfColumns = 0;
        while ((fields = reader.readNext()) != null) {
            Model model = new Model(fields);
            dataList.add(model);
            numberOfColumns = fields.length;
        }
        return numberOfColumns;
    }

    public static void updateCSV(String input, String output, String replace, int row, int col) throws IOException, CsvException {

        CSVReader reader = new CSVReader(new FileReader(input));
        List<String[]> csvBody = reader.readAll();
        csvBody.get(row)[col] = replace;
        reader.close();

        CSVWriter writer = new CSVWriter(new FileWriter(output));
        writer.writeAll(csvBody);
        writer.flush();
        writer.close();
    }

    public void extractHtml(String curl) throws IOException {

        BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter(createFileWithChoice));
        writer.write(curl);
        writer.close();

        File urlsFile = new File(createFileWithChoice);
        WikipediaMatrix wiki = new WikipediaMatrix();
        Set<UrlMatrix> urlMatrixSet;

        wiki.setUrlsMatrix(getListofUrls(urlsFile));
        wiki.setExtractType(ExtractType.HTML);

        urlMatrixSet = wiki.getConvertResult();

        String csvFileName = "";
        String url;
        for (UrlMatrix urlMatrix : urlMatrixSet) {
            int i = 0;
            url = urlMatrix.getLink();
            Set<FileMatrix> fileMatrices = urlMatrix.getFileMatrix();
            File htmlDir = new File(directory.getAbsoluteFile() + "" + File.separator + "html" + File.separator);
            htmlDir.mkdirs();
            deleteAllFiles(htmlDir);
            if (fileMatrices.size() > 0) {
                for (FileMatrix f : fileMatrices) {
                    csvFileName = mkCSVFileName(url.substring(url.lastIndexOf("/") + 1, url.length()), i);
                    f.saveCsv(htmlDir.getAbsolutePath() + File.separator + csvFileName);
                    i++;
                }
            }
        }
    }

    public void extractWikitext(String curl) throws IOException {
        BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter(createFileWithChoice));
        writer.write(curl);
        writer.close();

        File urlsFile = new File(createFileWithChoice);
        WikipediaMatrix wiki = new WikipediaMatrix();
        Set<UrlMatrix> urlMatrixSet;

        wiki.setUrlsMatrix(getListofUrls(urlsFile));
        wiki.setExtractType(ExtractType.WIKITEXT);

        urlMatrixSet = wiki.getConvertResult();

        String csvFileName = "";
        String url;

        for (UrlMatrix urlMatrix : urlMatrixSet) {
            int i = 0;
            url = urlMatrix.getLink();
            Set<FileMatrix> fileMatrices = urlMatrix.getFileMatrix();
            File wikitextDir = new File(directory.getAbsoluteFile() + "" + File.separator + "wikitext");
            wikitextDir.mkdirs();
            deleteAllFiles(wikitextDir);
            if (fileMatrices.size() > 0) {
                for (FileMatrix f : fileMatrices) {
                    csvFileName = mkCSVFileName(url.substring(url.lastIndexOf("/") + 1, url.length()), i);
                    f.saveCsv(wikitextDir.getAbsolutePath() + File.separator + csvFileName);
                    i++;
                }
            }
        }
    }

    public void deleteAllFiles(File dir){
        for(File file: Objects.requireNonNull(dir.listFiles()))
            if (!file.isDirectory())
                file.delete();
    }


    public static void main(String[] args) {

        Application.launch(args);
    }
}
