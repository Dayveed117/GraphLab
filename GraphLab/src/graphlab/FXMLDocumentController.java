/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphlab;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 *
 * @author a40284
 */
public class FXMLDocumentController implements Initializable {
    
    private final double radius = 30;
    private final double swidth = 2.5;
    private double srcX, srcY, destX, destY;
    private ArrayList<String> nodes = new ArrayList<>();
    
    @FXML StackPane sp;
    @FXML Pane pane;
    
    // Node Mode
    @FXML ToggleButton NodeMode;
    @FXML ColorPicker nodeColor;
    @FXML TextField nodeName;
    
    // Edge Mode
    @FXML ToggleButton EdgeMode;
    @FXML TextField edgeWeight;
    @FXML ColorPicker edgeColor;
    @FXML ComboBox edgeDirection;
    
    // Pathing
    @FXML ComboBox srcNode;
    @FXML ComboBox destNode;
    @FXML ComboBox pathAlg;
    @FXML Button tracePath;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Elevar o programa ao estado basal
        startup();
        
        // Pane Event Handlers
        pane.setOnMouseClicked((MouseEvent e) -> {
            if(NodeMode.isSelected()) {
                clickNodeOp(e);
            }
            
            if(EdgeMode.isSelected()) {
                // TODO: Saber o que fazer
            }
            
            srcNode.setItems(FXCollections.observableArrayList(nodes));
            destNode.setItems(FXCollections.observableArrayList(nodes));
        });
        
        pane.setOnDragDetected((MouseEvent e) -> {
            if(NodeMode.isSelected()) {
                dragNodeOp(e);
            }
            
            if(EdgeMode.isSelected()) {
                // TODO: Saber o que fazer
            }
        });
        
        tracePath.setOnAction(e -> {
            applyPath();
        });
    }

    public void startup() {
        
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
        
        String[] directions = {"Unidirectional", "Bidirectional"};
        edgeDirection.setItems(FXCollections.observableArrayList(directions));
        edgeDirection.getSelectionModel().select(directions[0]);
        
        nodes.add("None");
        String[] algorithms = {"Djikstra", "ShortestPath", "PPL", "PPP"};
        pathAlg.setItems(FXCollections.observableArrayList(algorithms));
        pathAlg.getSelectionModel().select(algorithms[1]);
    }

    public void clickNodeOp(MouseEvent e) {
        
        if(!e.isShiftDown()) {
            Object obj = e.getTarget();
            if(createNode(e.getX(), e.getY(), obj))
                System.out.println("Added successfully " + e.getX() + " - " + e.getY());
        }
        
        if(e.isShiftDown()) {
            Object obj = e.getTarget();
            if(deleteNode(obj))
                System.out.println("Deleted successfully");
        }
    }
    
    public void dragNodeOp(MouseEvent e) {
        
        Node n = (Node)e.getTarget();
        if(pane.getChildren().contains(n)) {
            
            srcX = e.getSceneX();
            srcY = e.getSceneY();
            destX = n.getTranslateX();
            destY = n.getTranslateY();
            
            n.setOnMouseDragged(nodeOnMouseDraggedEH);
        }
    }
    
    //tmp
    public boolean edgeWork(MouseEvent e) {
        
        if(EdgeMode.isSelected()) {
                if(e.isDragDetect()) {
                    
                    Line edge = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                    edge.setStroke(Color.CHOCOLATE);
                    edge.setStrokeWidth(2.5);
                
                    pane.setOnMouseDragged(evt -> {
                        System.out.println(e.getX() + " - " + e.getY());
                        edge.setEndX(evt.getX());
                        edge.setEndY(evt.getY());
                    });
                }
            }// edge mode
        return true; //tmp
    } 
    

    // drag and drop? 
    EventHandler<MouseEvent> nodeOnMouseDraggedEH = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                double diffX = e.getSceneX() - srcX;
                double diffY = e.getSceneY() - srcY;
                double newX = destX+ diffX;
                double newY = destY+ diffY;
            
                ((Node)(e.getTarget())).setTranslateX(newX);
                ((Node)(e.getTarget())).setTranslateY(newY);
            }
    };
    
    public boolean createNode(double X, double Y, Object obj) {

        // Caso o nome seja repetido, avisar
        if(nodes.contains(nodeName.getText())) {
            nodeName.requestFocus();
            nodeName.setStyle("-fx-background-color: #FF6A6A;");
            return false;
        }
        else {
            nodeName.setStyle("-fx-background-color: white;");
        }
        
        Node n = (Node)obj;
        
        if(!pane.getChildren().contains(n) && !nodeName.getText().equals("")) {
            
            // Descrever o nodo
            Circle circle = new Circle(X, Y, radius);
            circle.setUserData(nodeName.getText());
            circle.setFill(Color.WHITE);
            circle.setStrokeWidth(swidth);
            circle.setStroke(nodeColor.getValue());
            
            
            // TODO: Adicionar nodo na estrutura GRAFO
            nodes.add(nodeName.getText());
            nodeName.clear();
            return pane.getChildren().add(circle);
        }
        
        return false;
    }
    
    public boolean deleteNode(Object obj) {
        Node node = (Node)obj;
        nodes.remove(node.getId());
        // TODO: Remover nodo na estrutura GRAFO
        
        return pane.getChildren().remove(node);
    }
    
    public void createEdge(double X, double Y, Object obj) {
         // TODO: Perceber como criar estas "arestas"
    }
    
    public void deleteEdge(Object obj) {
        // TODO: perceber como fazer createEdge
    }
    
    @FXML
    public boolean applyPath() {
        
        Circle src = (Circle)findNode(srcNode.getValue());
        Circle dest = (Circle)findNode(destNode.getValue());
    
        src.setFill(Color.CRIMSON);
        dest.setFill(Color.CRIMSON);
            
        return true;
    }
    
    public Node findNode(Object o) {
        for(Node n: pane.getChildren()) {
            if(n.getUserData().equals(o))
                return n;
        }
        return null;
    }
            
}
