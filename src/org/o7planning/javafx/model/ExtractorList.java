package org.o7planning.javafx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExtractorList {

    public static ObservableList<ComboExtractor> getExtractorList() {
        ComboExtractor python = new ComboExtractor("PY", "Python");
        ComboExtractor jWikiText = new ComboExtractor("JWIKI", "JWikiText");
        ComboExtractor jHtml = new ComboExtractor("JHTML", "JHtml");

        ObservableList<ComboExtractor> list //
                = FXCollections.observableArrayList(python, jWikiText, jHtml);

        return list;
    }

    public static ObservableList<ComboUrl> getUrlList() {
        String urlPath = "inputdata/wikiurls.txt";
        BufferedReader reader = null;
        List<ComboUrl> urls = new ArrayList<ComboUrl>();

        try {
            reader = new BufferedReader(new FileReader(urlPath));

            while (reader.readLine() != null) {
                String currentLine = reader.readLine();
                urls.add(new ComboUrl(currentLine));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //ComboUrl urls = new ComboUrl("Comparison_between_Esperanto_and_Ido");

        ObservableList<ComboUrl> list //
                = FXCollections.observableArrayList(urls);

        return list;
    }

    public static ObservableList<Integer> getNbrCsv() throws IOException {
        File dir = new File("/Users/bijou/PycharmProjects/PDL_2021_groupe_9/output");
        File[] liste = dir.listFiles();
        int nbrCsv = 0;
        List<Integer> listNbrCSV = new ArrayList<Integer>();
        for (File item : liste) {
            if (item.isFile()) {
                nbrCsv++;
                listNbrCSV.add(nbrCsv);
            }
        }
        ObservableList<Integer> listInt = FXCollections.observableArrayList(listNbrCSV);

        return listInt;
    }

    public static File getFileSelected(int numFile) throws IOException {
        File dir = new File("/Users/bijou/PycharmProjects/PDL_2021_groupe_9/output");
        File[] liste = dir.listFiles();
        int nbrCsv = 0;
        for (File item : liste) {
            if (item.isFile()) {
                nbrCsv++;
                if (nbrCsv == numFile){
                    return item;
                }
            }
        }

        return new File("");
    }
}
