/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphlab;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

/**
 *
 * @author a40284
 */
public class FXMLDocumentController implements Initializable {
    
    final double radius = 30;
    
    @FXML Group group;
    @FXML Pane pane;
    
    // Node Mode
    @FXML ToggleButton NodeMode;
    @FXML ComboBox nodeColor;
    @FXML TextField nodeName;
    
    // Edge Mode
    @FXML ToggleButton EdgeMode;
    @FXML TextField edgeWeight;
    @FXML ComboBox edgeColor;
    @FXML ComboBox edgeDirection;
    
    // Pathing
    @FXML ComboBox srcNode;
    @FXML ComboBox destNode;
    @FXML ComboBox pathAlg;
    @FXML Button tracePath;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pane.setOnMouseClicked(e -> {
            createNode(e.getX(), e.getY());
        });
    }
    
    
    
    @FXML
    public void createNode(double X, double Y) {
        Circle node = new Circle(X, Y, radius);
        pane.getChildren().add(node);
    }
}
