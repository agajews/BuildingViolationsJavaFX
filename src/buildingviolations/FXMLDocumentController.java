/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buildingviolations;

import buildingviolations.Entry;
import buildingviolations.MapUtil;
import com.google.gson.*;
import com.sun.javafx.collections.ObservableListWrapper;
import java.net.*;
import java.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.*;

/**
 *
 * @author csstudent
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private BarChart<String, Number> chart;
    
    @FXML
    private TextField tf;
    
    private Map<String, Integer> addresses;
    
    @FXML
    private void handleStreet(Event e){
        graphData(tf.getText());
    }
    
    @FXML
    private void close(Event e){
        System.exit(0);
    }
    
    @FXML
    private void about(Event e){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Building Violations Help");
        String s ="The following is a graph of building violations \nby address. To filter the data, enter a two-digit \nstreet number or a '*' to display all items.";
        alert.setContentText(s);
        alert.show();
    }
    
    private void graphData(String street){
        System.out.println(street);
        addresses = MapUtil.sortByValue(addresses, true);
        List<String> keys = new ArrayList<String>();
        keys.addAll(addresses.keySet());
        XYChart.Series<String, Number> violations = new XYChart.Series();
        System.out.println(keys);
        for (String a : keys){
            if(a.substring(0, 2).equals(street) || street.equals("*")){
                violations.getData().add(new XYChart.Data(a, addresses.get(a)));
            }
        }
        chart.getData().clear();
        chart.getData().add(violations);
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String s = "https://data.cityofchicago.org/resource/22u3-xenr.json?$select=address";
        URL address = null;
        try {
            address = new URL(s);
        } catch (Exception e) {
            System.out.println("Improper URL " + s);
            System.exit(-1);
        }
     
        // read from the URL
        Scanner scan = null;
        try {
            scan = new Scanner(address.openStream());
        } catch (Exception e) {
            System.out.println("Could not connect to " + s);
            System.exit(-1);
        }
        
        String str = new String();
        while (scan.hasNext()) {
            str += scan.nextLine() + "\n";
        }
        scan.close();

        Gson gson = new Gson();
        Entry[] data = gson.fromJson(str, Entry[].class);
        
        addresses = new HashMap<String, Integer>();
        for (Entry entry : data){
            String street = entry.getAddress();
            if (addresses.get(street) == null){
                addresses.put(street, 1);
            }
            else{
                addresses.put(street, addresses.get(street) + 1);
            }
        }
        graphData("*");
        /*cb.setItems(FXCollections.observableArrayList("*", "10"));
        cb.setValue("*");
        cb.getSelectionModel().selectedIndexProperty().addListener(new
            ChangeListener<Number>() {
                public void changed(ObservableValue ov,
                    Number value, Number new_value) {
                        System.out.println(new_value);
            }
        });*/
        tf.setText("*");
    }
    
}
