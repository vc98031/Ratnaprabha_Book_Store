/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore.MainUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author Shri
 */
public class HelpWindowController implements Initializable {
    @FXML
    private StackPane root;
    @FXML
    private AnchorPane anchor;
    @FXML
    private JFXButton right;
    @FXML
    private JFXButton left;
    @FXML
    private Tab jayprasad;
    @FXML
    private Circle img1;
    @FXML
    private Circle circle;
    @FXML
    private Tab onkar;
    @FXML
    private Circle img11;
    @FXML
    private Circle circle1;
    @FXML
    private JFXTabPane tabpane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void moveRight(ActionEvent event) 
    {
        int idx=tabpane.getSelectionModel().getSelectedIndex();             
            if((idx+1) <= 2  && ++idx >= 0); // in second condition there is no any special 
            //meaning of ">0" we just want to ++idx but in java if condition requires
            //only condition that generate boolean thats why we take ">0" to generate 
            //boolean of expression ++idx            
            tabpane.getSelectionModel().select(idx);
        // tabpane.getSelectionModel().select(2);         */        
    }

    @FXML
    private void moveLeft(ActionEvent event) 
    {
        int idx=tabpane.getSelectionModel().getSelectedIndex();        
            if((idx-1) >= 0  && --idx >= 0); // in second condition there is no any special 
            //meaning of ">0" we just want to --idx but in java if condition requires
            //only condition that generate boolean thats why we take ">0" to generate 
            //boolean of expression --idx 
            tabpane.getSelectionModel().select(idx);
            //tabpane.getSelectionModel().select(0);       */
        //System.out.println(tabpane.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void load_img(MouseEvent event) 
    {
        
    }
    
}
