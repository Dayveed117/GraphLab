/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphlab;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 *
 * @author a40284
 */
public class FXMLDocumentController implements Initializable {
    
    final double radius = 30;
    
    @FXML StackPane sp;
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
        
        // Toggle Buttons
        NodeMode.setSelected(true);
        EdgeMode.setSelected(false);
        
        NodeMode.setOnAction(e -> {
            if(EdgeMode.isSelected())
                EdgeMode.setSelected(false);
        });
        
        EdgeMode.setOnAction(e -> {
           if(NodeMode.isSelected())
               NodeMode.setSelected(false);
        });
        
        
        // Pane Event Handlers
        pane.setOnMouseClicked(e -> {
            if(NodeMode.isSelected()) {
                if(!e.isShiftDown()) {
                    Object obj = e.getTarget();
                    if(createNode(e.getX(), e.getY(), obj))
                        System.out.println("Added successfully");
                }
                if(e.isShiftDown()) {
                    Object obj = e.getTarget();
                    if(deleteNode(obj))
                        System.out.println("Deleted successfully");
                }
            }// node mode
            
            if(EdgeMode.isSelected()) {
                createEdge(e.getX(), e.getY());
            }// edge mode
        });
    }
    
    
    
    // Funções Ajudantes
    
    @FXML
    public boolean createNode(double X, double Y, Object obj) {
        
        Node n = (Node)obj;
        
        if(!pane.getChildren().contains(n)) {
            Circle node = new Circle(X, Y, radius);
            node.setFill(Color.WHITESMOKE);
            node.setStroke(Color.BLUEVIOLET);

            // TODO: Adicionar nodo na estrutura GRAFO
            
            nodeName.clear();
            return pane.getChildren().add(node);
        }
        
        return false;
    }
    
    @FXML
    public boolean deleteNode(Object obj) {
        Node node = (Node)obj;
        
        // TODO: Remover nodo na estrutura GRAFO
        
        return pane.getChildren().remove(node);
    }
    
    @FXML
    public void createEdge(double X, double Y) {
        // TODO: Perceber como conectar dois objetos
    }
}
