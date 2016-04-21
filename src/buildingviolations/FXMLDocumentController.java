/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polioimmunizations;

import buildingviolations.Entry;
import com.google.gson.*;
import java.net.*;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.chart.*;

/**
 *
 * @author csstudent
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private BarChart<String, Number> chart;
    
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
        
        XYChart.Series<String, Number> violations = new XYChart.Series();
        chart.setTitle("Building Violations");
        chart.getXAxis().setLabel("Address");
        chart.getYAxis().setLabel("# Building Violations");
        
        HashMap<String, Integer> addresses = new HashMap<String, Integer>();
        for (Entry entry : data){
            String street = entry.getAddress();
            if (addresses.get(street) == null){
                addresses.put(street, 1);
            }
            else{
                addresses.put(street, addresses.get(street) + 1);
            }
        }
        for (String street : addresses.keySet()){
            System.out.println(street + ": " + addresses.get(street));
            violations.getData().add(new XYChart.Data(street, addresses.get(street)));
        }
        
        chart.getData().add(violations);
        
        
    }    
    
}
