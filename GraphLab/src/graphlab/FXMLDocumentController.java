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
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author a40284
 */
public class FXMLDocumentController implements Initializable {
    
    private final double radius = 30;
    private final double swidth = 2.5;
    private double srcX, srcY, destX, destY;
    
    @FXML Pane pane;
    
    // Distinguir ids de nodos ou arestas
    private ArrayList<String> nodes = new ArrayList<>();
    private ArrayList<String> edges = new ArrayList<>();

    // Último nodo clicado
    private Node lastHeld;

    // MenuBar
    @FXML MenuItem clearAll;
    @FXML MenuItem about;
    @FXML MenuItem guide;
    
    
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
        
        // Triggers e encher CB
        startup();
        
        // Pane Event Handlers
        pane.setOnMouseClicked((MouseEvent e) -> {
            
            if(NodeMode.isSelected()) 
                clickNodeOp(e);
            
            if(EdgeMode.isSelected()) {
                createEdge(e);
                
                // Fazer delete a partir do EdgeMode tambem
                if(e.isShiftDown()) {
                    Object obj = e.getTarget();
                    if(deleteNode(obj))
                        System.out.println("Node Deleted successfully");
                }
            }
            
            // refresh das combo boxes
            srcNode.setItems(FXCollections.observableArrayList(nodes));
            destNode.setItems(FXCollections.observableArrayList(nodes));
        });
        
        pane.setOnDragDetected((MouseEvent e) -> {
            
            lastHeld = null;
            
            if(NodeMode.isSelected()) 
                dragNodeOp(e);
            
            if(EdgeMode.isSelected()) {
                // TODO: ??
            }
        });
        
        tracePath.setOnAction(e -> {
            applyPath();
        });
        
        clearAll.setOnAction(e -> {
            fullreset();
        });
        
        about.setOnAction(e -> {
            info_about();
        });
        
        guide.setOnAction(e -> {
            info_guide();
        });
    }

    public void info_guide() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Guia para GraphLab2019");
        alert.setHeaderText("Utilização");
        String frase1 = "NodeMode Toggle:\n\n";
        String frase2 = "Mouse click para inserir um nodo, nome não pode ser vazio, nem repetido.\n";
        String frase3 = "Drag and drop para mover um nodo.\n\n";
        String frase4 = "EdgeMode Toggle:\n\n";
        String frase5 = "Clicar em 2 nodos distintos para uni-los com uma aresta.\n";
        String frase6 = "Drag and drop para mover uma aresta.\n\n";
        String frase7 = "Shift+MouseClick para apagar quer nodo quer aresta.\n";
        
        alert.setContentText(frase1+frase2+frase3+frase4+frase5+frase6+frase7);
        alert.show();
    }

    public void info_about() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("GraphLab 2019 About");
        alert.setHeaderText("GraphLab2019 beta™");
        String frase1 = "Aplicação realizada pelos alunos da Licenciatura em EI:\n\n";
        String frase2 = "David Bugalho a40284\n";
        String frase3 = "André Oliveira a39474\n\n\n";
        String frase4 = "Ideia extremamente ambiciosa, o grupo ficou um pouco aquém da sua ideia de implementação.\n\n";
        String frase5 = "Contudo, o tempo dedicado para a realização deste trabalho jamais será um fracasso.\n\n";
        String frase6 = "Trabalho não funcional, mas honesto, divirta-se com GraphLab!";
        
        alert.setContentText(frase1+frase2+frase3+frase4+frase5+frase6);
        alert.show();
    }
    

    public void createEdge(MouseEvent e) {
        
        Node n = (Node)e.getTarget();
        
        // se for um dos filhos do pane
        // se os nodos forem diferentes
        // se um dos nodos nao for o pane
        // se eles ja tiverem conectados
        if(nodes.contains(n.getUserData().toString())) {    
            if(lastHeld != null && !n.getUserData().equals("pane") && !n.getUserData().toString().equals(lastHeld.getUserData().toString())) {    
                Line line = initLine(n);
                
                pane.getChildren().add(line);
                line.toBack();
                line.requestFocus();
                
                lastHeld = null;
            }
            else
                lastHeld = n;
        }
        // TODO: Saber o que fazer
        // costumizar a aresta?
    }
    
     public Line initLine(Node n1) {
        // Cria um linha, ainda ponto no nodo clicado
        Circle dest = (Circle)n1;
        Circle src = (Circle)lastHeld; 
        
        String edgeName = dest.getUserData().toString() + "__" + src.getUserData().toString();
        String check = src.getUserData().toString() + "__" + dest.getUserData().toString();
        
        Line line = new Line();
        
        if(!edges.contains(edgeName) && !edges.contains(check)) {
            
            edges.add(edgeName);
            
            line.setCursor(Cursor.HAND);
            line.startXProperty().bind(src.centerXProperty());
            line.startYProperty().bind(src.centerYProperty());
            line.endXProperty().bind(dest.centerXProperty());
            line.endYProperty().bind(dest.centerYProperty());

            line.setUserData(edgeName);
            line.setStroke(edgeColor.getValue());
            line.setStrokeWidth(3);
            System.out.println("Edge Added Successfully");
        }

        return line;
    }

     
     // Menu e utilidade
    public void startup() {
        
        NodeMode.setSelected(true);
        EdgeMode.setSelected(false);
        nodes.add("None");
        pane.setUserData("pane");
        
        toggles();
        comboBoxFill();
    }

    public void comboBoxFill() {
        
        String[] directions = {"Unidirectional", "Bidirectional"};
        edgeDirection.setItems(FXCollections.observableArrayList(directions));
        edgeDirection.getSelectionModel().select(directions[0]);
        
        String[] algorithms = {"Djikstra", "ShortestPath", "PPL", "PPP"};
        pathAlg.setItems(FXCollections.observableArrayList(algorithms));
        pathAlg.getSelectionModel().select(algorithms[1]);
    }

    public void toggles() {
        
        NodeMode.setOnAction(e -> {
            if(EdgeMode.isSelected())
                EdgeMode.setSelected(false);
        });
        
        EdgeMode.setOnAction(e -> {
            if(NodeMode.isSelected())
                NodeMode.setSelected(false);
        });
    }
    
    public void fullreset() {
        nodes.clear();
        edges.clear();
        pane.getChildren().clear();
    }

    
    
    public void clickNodeOp(MouseEvent e) {
        
        if(!e.isShiftDown()) {
            Object obj = e.getTarget();
            if(createNode(e.getX(), e.getY(), obj))
                System.out.println("Node Added successfully " + e.getX() + " - " + e.getY());
        }
        
        if(e.isShiftDown()) {
            Object obj = e.getTarget();
            if(deleteNode(obj))
                System.out.println("Node Deleted successfully");
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
            describeNode(circle);

            // TODO: Adicionar nodo na estrutura GRAFO
            nodes.add(nodeName.getText());
            nodeName.clear();
            return pane.getChildren().add(circle);
        }
        
        return false;
    }
    
    public void describeNode(Circle circle) {
        circle.setCursor(Cursor.HAND);
        circle.setUserData(nodeName.getText());
        circle.setFill(Color.WHITE);
        circle.setStrokeWidth(swidth);
        circle.setStroke(nodeColor.getValue());
    }

    public boolean deleteNode(Object obj) {
        Node node = (Node)obj;
        nodes.remove(node.getUserData().toString());
        // TODO: Remover nodo na estrutura GRAFO
        
        return pane.getChildren().remove(node);
    }
    
    @FXML
    public boolean applyPath() {
        
        Circle src = (Circle)findNode(srcNode.getValue());
        Circle dest = (Circle)findNode(destNode.getValue());
    
        if(src != null && dest != null) { // eventualmente implementar lógica do caminho e de ter ou src ou dest como "None"
            
            // regret doing this everytime
            String edgeName = dest.getUserData().toString() + "__" + src.getUserData().toString();
            String check = src.getUserData().toString() + "__" + dest.getUserData().toString();
            
            if(edges.contains(edgeName) || edges.contains(check)) {
                src.setFill(Color.CHOCOLATE);
                dest.setFill(Color.CHOCOLATE);
            }
            
        }
        
        return false;
    }
    
    public Node findNode(Object o) {
        for(Node n: pane.getChildren()) {
            if(n.getUserData().equals(o))
                return n;
        }
        return null;
    }
            
}
