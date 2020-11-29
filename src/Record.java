import javafx.beans.property.SimpleStringProperty;

import java.util.HashMap;
import java.util.Map;

public class Record {
    //Assume each record have 3 elements, all String

    private SimpleStringProperty[] simpleStringPropertyList;

    public SimpleStringProperty f1, f2, f3, f4, f5, f6, f7, f8, f9, f10;

    public String getF0() {
        return f1.get();
    }

//    public SimpleStringProperty[] getSimpleStringPropertyList() {
//        return simpleStringPropertyList;
//    }
//
//    public String getElement(int index){
//        return simpleStringPropertyList[index].get();
//    }
//
//
//    public void setSimpleStringPropertyList(int j, String value) {
//        this.simpleStringPropertyList[j].set(value);
//    }

    public String getF1() {
        return f2.get();
    }

    public String getF2() {
        return f3.get();
    }

    public String getF3() {
        return f4.get();
    }

    public String getF4() {
        return f5.get();
    }

    public String getF5() {
        return f6.get();
    }

    public String getF6() {
        return f7.get();
    }

    public String getF7() {
        return f8.get();
    }

    public String getF8() {
        return f9.get();
    }

    public String getF9() {
        return f10.get();
    }

    public void setF0(String f1) {
        this.f1.set(f1);
    }

    public void setF1(String f2) {
        this.f2.set(f2);
    }

    public void setF2(String f3) {
        this.f3.set(f3);
    }

    public void setF3(String f4) {
        this.f4.set(f4);
    }

    public void setF4(String f5) {
        this.f5.set(f5);
    }

    public void setF5(String f6) {
        this.f6.set(f6);
    }

    public void setF6(String f7) {
        this.f7.set(f7);
    }

    public void setF7(String f8) {
        this.f8.set(f8);
    }

    public void setF8(String f9) {
        this.f9.set(f9);
    }

    public void setF9(String f10) {
        this.f10.set(f10);
    }

    Record(String f1, String f2, String f3){
        this.f1 = new SimpleStringProperty(f1);
        this.f2 = new SimpleStringProperty(f2);
        this.f3 = new SimpleStringProperty(f3);
    }

    //int i = 0;
    Record(String f1, String f2, String f3, String f4, String f5, String f6, String f7, String f8, String f9, String f10) {

//        this.simpleStringPropertyList = new SimpleStringProperty[simpleStringPropertyList.length];
//        for (String f : simpleStringPropertyList) {
//            this.simpleStringPropertyList[i] = new SimpleStringProperty(f);
//            i++;
//        }

        this.f1 = new SimpleStringProperty(f1);
        this.f2 = new SimpleStringProperty(f2);
        this.f3 = new SimpleStringProperty(f3);
        this.f4 = new SimpleStringProperty(f4);
        this.f5 = new SimpleStringProperty(f5);
        this.f6 = new SimpleStringProperty(f6);
        this.f7 = new SimpleStringProperty(f7);
        this.f8 = new SimpleStringProperty(f8);
        this.f9 = new SimpleStringProperty(f9);
        this.f10 = new SimpleStringProperty(f10);
    }

}
