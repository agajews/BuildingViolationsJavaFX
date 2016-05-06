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
import javafx.scene.input.*;

/**
 *
 * @author csstudent
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private BarChart<String, Number> chart;
    
    @FXML
    private Slider min;
    
    @FXML
    private Slider max;
    
    private Map<String, Integer> addresses;
    
    private void graphData(boolean forward){
        System.out.println(forward);
        addresses = MapUtil.sortByValue(addresses, true);
        List<String> keys = new ArrayList<String>();
        keys.addAll(addresses.keySet());
        if(forward){
            Collections.reverse(keys);
        }
        XYChart.Series<String, Number> violations = new XYChart.Series();
        System.out.println(keys);
        for (String street : keys){
            if(addresses.get(street) >= min.getValue() && addresses.get(street) <= max.getValue()){
                violations.getData().add(new XYChart.Data(street, addresses.get(street)));
            }
        }
        chart.getData().clear();
        chart.getData().add(violations);
        
    }
    
    @FXML
    private void handleChangeFilter(MouseEvent event) {
        System.out.println("Clicked slider");
        max.setMin(0);
        max.setMax(50);
        min.setMin(0);
        min.setMax(50);
        if(max.getValue() > 5){
            min.setMax(max.getValue()-5);
        }else{
            max.setMin(min.getValue()+5);
        }
        graphData(true);
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
        graphData(true);
    }
    
}
