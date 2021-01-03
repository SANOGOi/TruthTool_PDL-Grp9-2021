import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Model {

    List<SimpleStringProperty> properties;

    Model(String[] values) {
        this.properties = Arrays.stream(values)
                .map(value -> new SimpleStringProperty(value))
                .collect(Collectors.toList());
    }

    public StringProperty propertyAt(int index) {
        return properties.get(index);
    }

    // I don't know if getter/setter should be defined.
    // (I don't know how to handle the model)
    public String getAt(int index) {
        return propertyAt(index).get();
    }

    public void setAt(int index, String value) {
        propertyAt(index).set(value);
    }
}
