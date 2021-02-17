package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Controller {
}

class Vertex {
    private String name;
    private double x;
    private double y;

    Vertex(String name , double x,double y){
        this.name=name;
        this.x=x;
        this.y=y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Edge {
    private Vertex from;
    private Vertex to;
    private double weight;
    Edge(Vertex from, Vertex to, double weight){
        this.weight=weight;
        this.from=from;
        this.to=to;
    }

    public Vertex getFrom() {
        return from;
    }

    public void setFrom(Vertex from) {
        this.from = from;
    }

    public Vertex getTo() {
        return to;
    }

    public void setTo(Vertex to) {
        this.to = to;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

class Graph {
    private int VN;
    private int EN;
    private int V;
    private String nullable="";
    Graph(){
        vertex=new ArrayList<Vertex>();
        edges=new ArrayList<Edge>();VN=0;EN=0;
    }
    private ArrayList<Vertex> vertex;
    private ArrayList<Edge> edges;

    public int getVN() {
        return VN;
    }

    public void exportGraph(String stri) throws FileException {
        this.nullable="";
        this.setVN(vertex.size());
        this.setEN(edges.size());
        try(BufferedWriter br = new BufferedWriter(new FileWriter(stri)))
        {
            String str="";
            Collections.sort(vertex,new sortVertex());
            Collections.sort(edges,new sortEdge());
            str+=(this.getVN());str+="\n";
            for( Vertex v : vertex) {
                str=str+v.getName()+" "+(int)v.getX()+" "+(int)v.getY()+"\n";
            }
            str+=(this.getEN());str+="\n";
            for( Edge v : edges) {
                str=str+v.getFrom().getName()+" "+v.getTo().getName()+" "+(int)v.getWeight()+"\n";
            }
            br.write(str);
            br.close();
        }
        catch (Exception e){
            this.nullable += ("Coudn't export file!");
            throw new FileException();
        }
        this.nullable="";
    }

    public void addVertex(String name,double x,double y) throws VNFException {
        Vertex v1=null;
        if(!vertex.isEmpty()){
            for( Vertex v : vertex) {
                if (v.getName().compareTo(name) == 0) {
                    v1 = v ;
                }
            }
        }
        if(v1==null) {
            vertex.add(new Vertex(name,x,y));this.VN++;
        }
        else{
            this.nullable += ("Vertex already exists! Modify for changes..");
            throw  new VNFException();
        }
    }

    public void modVertex(String name,double x,double y) throws VNFException {
        Vertex v1=null;
        if(!vertex.isEmpty()){
            for( Vertex v : vertex) {
                if (v.getName().compareTo(name) == 0) {
                    v1 = v ;
                }
            }
        }
        if(v1==null) {
            this.nullable += ("Vertex not found! Add vertex for changing..");
            throw new VNFException();
        }
        else{
            v1.setX(x);
            v1.setY(y);
        }
    }

    public void modEdge(String name1,String name2,double w) throws ENFException, VNFException {
        Vertex v1=null,v2=null;
        if(!vertex.isEmpty()){
            for( Vertex v : vertex) {
                if (v.getName().compareTo(name1) == 0) {
                    v1 = v ;
                }
                if (v.getName().compareTo(name2) == 0) {
                    v2 = v ;
                }
            }
        }
        if(v1==null) {
            this.nullable += ("From Vertex not found! Add vertex for changing..");
            throw new VNFException();
        }
        else if(v2==null) {
            this.nullable += ("To Vertex not found! Add vertex for changing..");
            throw new VNFException();
        }
        else{
            int index =-1;int i=-1;
            for( Edge e : edges) {
                i++;
                if (e.getFrom().getName().compareTo(name1) == 0 && e.getTo().getName().compareTo(name2) == 0) {
                    index =i;
                }
            }
            if(index!=-1)
                edges.get(index).setWeight(w);
            else {
                this.nullable += ("Given edge between vertices not found! Please add edge to modify...");
                throw new ENFException();
            }

        }
    }

    public  String getPrint()
    {
        String temp =this.nullable;
        this.nullable="";
        return temp;
    }

    public void addEdge(String name, String s1, double s2) throws VNFException, ENFException {
        Vertex v1 = null,v2=null;
        for( Vertex v : vertex){
            if(v.getName().compareTo(name)==0){
                v1=v;
            }
            if(v.getName().compareTo(s1)==0){
                v2=v;
            }
        }
        if(v1==null || v2==null){
            this.nullable += ("Enter Valid Vertices!");
            throw new VNFException();
        }
        else if(isEdge(name,s1)) {
            this.nullable += ("Edge already exist!");
            throw new ENFException();
        }
        else {
            edges.add(new Edge(v1, v2, s2));
            ++this.EN;
        }
    }

    public void delVertex(String name) throws VNFException {
        int index =-1;int i=-1;
        for( Vertex v : vertex) {
            i++;
            if (v.getName().compareTo(name) == 0) {
                index =i;
            }
        }
        if(index!=-1) {
            vertex.remove(index);
            this.VN--;
        }
        else {
            this.nullable += ("Vertex not found!");
            throw new VNFException();
        }
        int l=edges.size();
        for(int k=0;k<l;k++) {
            i = -1;
            index = -1;
            for (Edge e : edges) {
                i++;
                if (e.getFrom().getName().compareTo(name) == 0 || e.getTo().getName().compareTo(name) == 0) {
                    index = i;
                }
            }
            if (index != -1)
                edges.remove(index);
        }
    }

    public void searchVertex(String name) throws VNFException {
        int index =-1;int i=-1;
        for( Vertex v : vertex) {
            i++;
            if (v.getName().compareTo(name) == 0) {
                index =i;
            }
        }
        if(index!=-1) {
            this.nullable += (vertex.get(index).getName() + " " + vertex.get(index).getX() + " " + vertex.get(index).getY());
        }
        else {
//            this.nullable += ("Vertex not found!");
            this.nullable += "Vertex not found!";
            throw new VNFException();
        }
    }

    public void searchEdge(String from,String to) throws ENFException {
        int index =-1;int i=-1;
        for( Edge e : edges) {
            i++;
            if (e.getFrom().getName().compareTo(from) == 0 && e.getTo().getName().compareTo(to) == 0) {
                index =i;
            }
        }
        if(index!=-1)
            this.nullable += (edges.get(index).getFrom().getName()+" "+edges.get(index).getTo().getName()+" "+edges.get(index).getWeight());
        else {
            this.nullable += ("Edge not found!");
            throw new ENFException();
        }
    }

    public boolean isEdge(String from,String to){
        int index =-1;int i=-1;
        for( Edge e : edges) {
            i++;
            if (e.getFrom().getName().compareTo(from) == 0 && e.getTo().getName().compareTo(to) == 0) {
                index =i;
            }
        }
        if(index!=-1)
            return true;
        else
            return false;
    }

    public void delEdge(String name1, String name2) throws ENFException {
        int index=-1;int i=-1;
        for( Edge e : edges) {
            i++;
            if (e.getFrom().getName().compareTo(name1) == 0 && e.getTo().getName().compareTo(name2) == 0) {
                index =i;
            }
        }
        if(index!=-1) {
            edges.remove(index);
            this.EN--;
        }
        else {
            this.nullable += ("Edge not found!");
            throw new ENFException();
        }
    }

    public int retIndex(String name) throws VNFException {
        int ret=-1;int i=-1;
        for( Vertex e : vertex) {
            i++;
            if(e.getName().compareTo(name)==0)
                ret=i;
        }
        if(ret==-1)
        {
            this.nullable += (name+" Vertex not found!");
            throw new VNFException();
        }
        return ret;
    }

    private static final int NO_PARENT = -1;
    public void getPath(double[][] adjacencyMatrix,
                        int startVertex,int endVertex) throws UnreachableException {
        if(startVertex == -1 || endVertex==-1)
            return;
        int nVertices = adjacencyMatrix[0].length;
        double[] shortestDistances = new double[nVertices];
        boolean[] added = new boolean[nVertices];
        for (int vertexIndex = 0; vertexIndex < nVertices;
             vertexIndex++)
        {
            shortestDistances[vertexIndex] = Double.MAX_VALUE;
            added[vertexIndex] = false;
        }
        shortestDistances[startVertex] = 0;
        int[] parents = new int[nVertices];
        parents[startVertex] = NO_PARENT;
        for (int i = 1; i < nVertices; i++)
        {
            int nearestVertex = -1;
            double shortestDistance = Double.MAX_VALUE;
            for (int vertexIndex = 0;
                 vertexIndex < nVertices;
                 vertexIndex++)
            {
                if (!added[vertexIndex] && shortestDistances[vertexIndex] < shortestDistance)
                {
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }
            added[nearestVertex] = true;
            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++)
            {
                double edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex];

                if (edgeDistance > 0 && ((shortestDistance + edgeDistance) < shortestDistances[vertexIndex]))
                {
                    parents[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance +
                            edgeDistance;
                }
            }
        }

        printSolution(startVertex, shortestDistances, parents,endVertex);
        this.nullable += ("\n");
    }
    private  void printSolution(int startVertex,
                                double[] distances,
                                int[] parents,
                                int endVertex) throws UnreachableException {
        if(distances[endVertex]==Double.MAX_VALUE) {
            this.nullable+="There is no path betweeen the given vertices";
            throw new UnreachableException();
        }
        this.nullable += ("Vertex\t Distance\t\tPath");
        this.nullable += ("\n" + this.vertex.get(startVertex).getName() + " -> ");
        this.nullable += (this.vertex.get(endVertex).getName() + " \t\t ");
        this.nullable += (distances[endVertex] + "\t\t");
        printPath(endVertex, parents);
    }

    private void printPath(int currentVertex,
                           int[] parents)
    {
        if (currentVertex == NO_PARENT)
        {
            return;
        }
        printPath(parents[currentVertex], parents);
        this.nullable += (this.vertex.get(currentVertex).getName() + " ");
    }


    public double[][] getAdj(int n) {
        double ret[][]= new double[n][n] ;
        for(int i=0;i<n;i++) {
            for (int j = 0; j < n; j++) {
                ret[i][j]=0;
            }

        }
        if(!edges.isEmpty())
        {
            for( Edge e : edges){
                for(int i=0;i<n;i++) {
                    for (int j = 0; j < n; j++) {
                        if (vertex.get(i).getName().compareTo(e.getFrom().getName()) == 0 && vertex.get(j).getName().compareTo(e.getTo().getName())== 0) {
                            ret[i][j]=e.getWeight();
                        }
                    }
                }
            }
        }
        V=ret.length;
        return ret;
    }

    public void setVN(int vn) {
        VN = vn;
    }

    public ArrayList<Vertex> getVertex() {
        return vertex;
    }

    public void setVertex(ArrayList<Vertex> vertex) {
        this.vertex = vertex;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public int getEN() {
        return EN;
    }

    public void setEN(int EN) {
        this.EN = EN;
    }

}

class sortEdge implements Comparator<Edge> {
    @Override
    public int compare(Edge o1, Edge o2) {
        int z= o1.getFrom().getName().compareTo(o2.getFrom().getName());
        if(z==0)
            z=o1.getTo().getName().compareTo(o2.getTo().getName());
        return z;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}

class sortVertex implements Comparator<Vertex> {
    @Override
    public int compare(Vertex o1, Vertex o2) {
        return o1.getName().compareTo(o2.getName());
    }
}

class ENFException extends Exception{
    //Edge Not Found Exception
}

class VNFException extends Exception{
    //Vertex Not Found Exception
}

class FileException extends Exception{
    //Vertex Not Found Exception
}

class UnreachableException extends Exception{
    //Path not reachable
}

public class Main extends Application {
    Graph graph = new Graph();
    String path="C:\\Users\\Asus\\IdeaProjects\\Question2\\src\\sample\\";
    String nullable="";
    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        TextField a1 = new TextField("");
        TextField a2 = new TextField("");
        TextField a3 = new TextField();
        TextField a4 = new TextField();
        TextField a5 = new TextField();
        TextField a11 = new TextField("");
        TextField a6 = new TextField();
        TextField a7 = new TextField();
        TextField a8 = new TextField("");
        TextField a9 = new TextField();
        TextField a10 = new TextField("");

        Button b1 = new Button("Add Vertex");
        Button b2 = new Button("Search Vertex");
        Button b3 = new Button("Delete Vertex");
        Button b4 = new Button("Modify Vertex");
        Button b5 = new Button("Add Edge");
        Button b11 = new Button("Search Edge");
        Button b6 = new Button("Delete Edge");
        Button b7 = new Button("Modify Edge");
        Button b8 = new Button("Load Data");
        Button b9 = new Button("Export Data");
        Button b10 = new Button("Print path");

        Label l1 = new Label("Add Vertex");
        Label l2 = new Label("Search Vertex");
        Label l3 = new Label("Delete Vertex");
        Label l4 = new Label("Modify Vertex");
        Label l5 = new Label("Add Edge");
        Label l11 = new Label("Search Edge");
        Label l6 = new Label("Delete Edge");
        Label l7 = new Label("Modify Edge");
        Label l8 = new Label("Load Data");
        Label l9 = new Label("Export Data");
        Label l10 = new Label("Print path");

        TextArea textArea = new TextArea();
        Alert alert = new Alert(Alert.AlertType.NONE);

        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textArea.clear();
                String[] str= a1.getText().split(" ");
                try {
                    if( str.length!=3)
                        throw new NumberFormatException();
                    graph.addVertex(str[0],Double.valueOf(str[1]),Double.valueOf(str[2]));
                    nullable=graph.getPrint();
                    textArea.setText(nullable);
                }
                catch (VNFException e){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    nullable=graph.getPrint();
                    alert.setHeaderText(nullable);
//                    alert.setContentText(nullable);
                    alert.show();
                }
                catch (NumberFormatException e){
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setHeaderText("Please input the field in <String><Double><Double> format only!");
//                    alert.setContentText(nullable);
                    alert.show();
                }
                finally {
                    a1.clear();
                }
            }
        };
        a1.setOnAction(event1);b1.setOnAction(event1);

        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textArea.clear();
                String[] str= a2.getText().split(" ");
                try {
                    if( str.length!=1)
                        throw new NumberFormatException();
                    graph.searchVertex(str[0]);
                    nullable=graph.getPrint();
                    textArea.setText(nullable);
                }
                catch (VNFException e){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    nullable=graph.getPrint();
                    alert.setHeaderText(nullable);
//                    alert.setContentText(nullable);
                    alert.show();
                }
                catch (NumberFormatException e){
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setHeaderText("Please input the field in <String> format only!");
//                    alert.setContentText(nullable);
                    alert.show();
                }
                finally {
                    a2.clear();
                }
            }
        };
        a2.setOnAction(event2);b2.setOnAction(event2);

        EventHandler<ActionEvent> event3 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textArea.clear();
                String[] str= a3.getText().split(" ");
                try {
                    if( str.length!=1)
                        throw new NumberFormatException();
                    graph.delVertex(str[0]);
                    nullable=graph.getPrint();
                    textArea.setText(nullable);
                }
                catch (VNFException e){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    nullable=graph.getPrint();
                    alert.setHeaderText(nullable);
//                    alert.setContentText(nullable);
                    alert.show();
                }
                catch (NumberFormatException e){
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setHeaderText("Please input the field in <String> format only!");
//                    alert.setContentText(nullable);
                    alert.show();
                }
                finally {
                    a3.clear();
                }
            }
        };
        a3.setOnAction(event3);b3.setOnAction(event3);

        EventHandler<ActionEvent> event4 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textArea.clear();
                String[] str= a4.getText().split(" ");
                try {
                    if( str.length!=3)
                        throw new NumberFormatException();
                    graph.modVertex(str[0],Double.valueOf(str[1]),Double.valueOf(str[2]));
                    nullable=graph.getPrint();
                    textArea.setText(nullable);
                }
                catch (VNFException e){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    nullable=graph.getPrint();
                    alert.setHeaderText(nullable);
//                    alert.setContentText(nullable);
                    alert.show();
                }
                catch (NumberFormatException e){
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setHeaderText("Please input the field in <String><Double><Double> format only!");
//                    alert.setContentText(nullable);
                    alert.show();
                }
                finally {
                    a4.clear();
                }
            }
        };
        a4.setOnAction(event4);b4.setOnAction(event4);

        EventHandler<ActionEvent> event5 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textArea.clear();
                String[] str= a5.getText().split(" ");
                try {
                    if( str.length!=3)
                        throw new NumberFormatException();
                    graph.addEdge(str[0],str[1],Double.valueOf(str[2]));
                    nullable=graph.getPrint();
                    textArea.setText(nullable);
                }
                catch (VNFException e){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    nullable=graph.getPrint();
                    alert.setHeaderText(nullable);
//                    alert.setContentText(nullable);
                    alert.show();
                }
                catch (ENFException e){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    nullable=graph.getPrint();
                    alert.setHeaderText(nullable);
//                    alert.setContentText(nullable);
                    alert.show();
                }
                catch (NumberFormatException e){
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setHeaderText("Please input the field in <String><String><Double> format only!");
//                    alert.setContentText(nullable);
                    alert.show();
                }
                finally {
                    a5.clear();
                }
            }
        };
        a5.setOnAction(event5);b5.setOnAction(event5);

        EventHandler<ActionEvent> event11 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textArea.clear();
                String[] str= a11.getText().split(" ");
                try {
                    if( str.length!=2)
                        throw new NumberFormatException();
                    graph.searchEdge(str[0], str[1]);
                    nullable=graph.getPrint();
                    textArea.setText(nullable);
                }
                catch (ENFException e){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    nullable=graph.getPrint();
                    alert.setHeaderText(nullable);
//                    alert.setContentText(nullable);
                    alert.show();
                }
                catch (NumberFormatException e){
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setHeaderText("Please input the field in <String><String> format only!");
//                    alert.setContentText(nullable);
                    alert.show();
                }
                finally {
                    a11.clear();
                }
            }
        };
        a11.setOnAction(event11);b11.setOnAction(event11);

        EventHandler<ActionEvent> event6 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textArea.clear();
                String[] str= a6.getText().split(" ");
                try {
                    if( str.length!=2)
                        throw new NumberFormatException();
                    graph.delEdge(str[0],str[1]);
                    nullable=graph.getPrint();
                    textArea.setText(nullable);
                }
                catch (ENFException e){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    nullable=graph.getPrint();
                    alert.setHeaderText(nullable);
//                    alert.setContentText(nullable);
                    alert.show();
                }
                catch (NumberFormatException e){
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setHeaderText("Please input the field in <String><String> format only!");
//                    alert.setContentText(nullable);
                    alert.show();
                }
                finally {
                    a6.clear();
                }
            }
        };
        a6.setOnAction(event6);b6.setOnAction(event6);

        EventHandler<ActionEvent> event7 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textArea.clear();
                String[] str= a7.getText().split(" ");
                try {
                    if( str.length!=3)
                        throw new NumberFormatException();
                    graph.modEdge(str[0],str[1],Double.valueOf(str[2]));
                    nullable=graph.getPrint();
                    textArea.setText(nullable);
                }
                catch (VNFException | ENFException e){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    nullable=graph.getPrint();
                    alert.setHeaderText(nullable);
                    alert.show();
                }
                catch (NumberFormatException e){
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setHeaderText("Please input the field in <String><String><Double> format only!");
//                    alert.setContentText(nullable);
                    alert.show();
                }
                finally {
                    a7.clear();
                }
            }
        };
        a7.setOnAction(event7);b7.setOnAction(event7);

        EventHandler<ActionEvent> event8 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textArea.clear();
                String[] str;
                graph = new Graph();
                try
                {   BufferedReader br = new BufferedReader(new FileReader(a8.getText()));
                    a8.clear();
                    int n_ver = Integer.parseInt(br.readLine());
                    while (n_ver > 0) {
                    nullable="Vertex not found! Enter valid vertexes and vertex count!";
                        str= br.readLine().split(" ");
                        if( str.length!=3) {
                            nullable="Vertex name not in <String><Double><Double> format!";
                            throw new NumberFormatException();
                        }
                        graph.addVertex(str[0], Double.valueOf(str[1]),Double.valueOf(str[2]));
                        n_ver--;
                    }
                    int n_edge = Integer.parseInt(br.readLine());
                    while (n_edge > 0) {
                        nullable="Edge not found! Enter valid edges and edge count!";
                        str = br.readLine().split(" ");
                        if( str.length!=3) {
                            nullable="Edge name not in <String><String><Double> format!";
                            throw new NumberFormatException();
                        }
                        graph.addEdge(str[0],str[1],Double.valueOf(str[2]));
                        n_edge--;
                    }
                }
                catch (NullPointerException e){
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setHeaderText("Input Error!");
                    alert.setContentText(nullable);
                    alert.show();
                }
                catch (NumberFormatException e){
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setHeaderText("Input Error!");
                    alert.setContentText(nullable);
                    alert.show();
                }
                catch (IOException e){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    nullable="Coudn't find file!";
                    alert.setHeaderText(nullable);
//                    alert.setContentText(nullable);
                    alert.show();
                }
                catch (Exception e){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    nullable=e.toString();
                    alert.setHeaderText(nullable);
//                    alert.setContentText(nullable);
                    alert.show();
                }
//                load data
                a8.clear();
            }
        };
        a8.setOnAction(event8);b8.setOnAction(event8);

        EventHandler<ActionEvent> event9 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                nullable="";
                textArea.clear();
                String[] str= a9.getText().split(" ");
                try {
                    graph.exportGraph(str[0]);
                    textArea.setText(nullable);
                }
                catch (FileException e){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    nullable=graph.getPrint();
                    alert.setHeaderText(nullable);
//                    alert.setContentText(nullable);
                    alert.show();
                }
                finally {
                    a9.clear();
                }
                nullable="";
            }
        };
        a9.setOnAction(event9);b9.setOnAction(event9);

        EventHandler<ActionEvent> event10 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textArea.clear();
                double[][] arr = graph.getAdj(graph.getVertex().size());
                int i=-1,j=-1;
                try {
                    String str[]= a10.getText().split(" ");
                    if( str.length!=2)
                        throw new NumberFormatException();
                    i=graph.retIndex(str[0]);
                    j=graph.retIndex(str[1]);
                    nullable="";
                    if(i==-1 || j==-1)
                        throw new UnreachableException();
                    graph.getPath(arr,i,j);
                    nullable=graph.getPrint();
                    textArea.setText(nullable);
                }
                catch (VNFException|UnreachableException e){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    nullable=graph.getPrint();
                    alert.setHeaderText(nullable);
//                    alert.setContentText(nullable);
                    alert.show();
                }
                catch (ArrayIndexOutOfBoundsException e){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText("No path possible between the vertices!");
//                    alert.setContentText(nullable);
                    alert.show();
                }
                catch (NumberFormatException e){
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setHeaderText("Please input the field in <String><String> format only!");
//                    alert.setContentText(nullable);
                    alert.show();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                finally {
                    a10.clear();
                }
            }
        };
        a10.setOnAction(event10);b10.setOnAction(event10);

        StackPane root = new StackPane();
        Region r = new Region();
        r.setPrefWidth(15);
        HBox hbox = new HBox(30);
        VBox vbox = new VBox(10);
        textArea.setPrefWidth(100);
        textArea.setPrefHeight(50);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        hbox.getChildren().addAll(l1, a1,b1);
        HBox hbox2 = new HBox(10);
        hbox2.getChildren().addAll(l2,a2,r,b2);
//        VBox.setVgrow(hbox2, Priority.ALWAYS);
        HBox hbox3 = new HBox(10);
        hbox3.getChildren().addAll(l3,a3,r,b3);
//        VBox.setVgrow(hbox3, Priority.ALWAYS);
        HBox hbox4 = new HBox(10);
        hbox4.getChildren().addAll(l4,a4,r,b4);
//        VBox.setVgrow(hbox4, Priority.ALWAYS);
        HBox hbox5 = new HBox(10);
        hbox5.getChildren().addAll(l5,a5,r,b5);
//        VBox.setVgrow(hbox5, Priority.ALWAYS);
        HBox hbox11 = new HBox(10);
        hbox11.getChildren().addAll(l11,a11,r,b11);
//        VBox.setVgrow(hbox11, Priority.ALWAYS);
        HBox hbox6 = new HBox(10);
        hbox6.getChildren().addAll(l6,a6,r,b6);
//        VBox.setVgrow(hbox6, Priority.ALWAYS);
        HBox hbox7 = new HBox(10);
        hbox7.getChildren().addAll(l7,a7,r,b7);
//        VBox.setVgrow(hbox7, Priority.ALWAYS);
        HBox hbox8 = new HBox(10);
        hbox8.getChildren().addAll(l8,a8,r,b8);
//        VBox.setVgrow(hbox8, Priority.ALWAYS);
        HBox hbox9 = new HBox(10);
        hbox9.getChildren().addAll(l9,a9,r,b9);
//        VBox.setVgrow(hbox9, Priority.ALWAYS);
        HBox hbox10 = new HBox(10);
        hbox10.getChildren().addAll(l10,a10,r,b10);
//        VBox.setVgrow(hbox10, Priority.ALWAYS);
        vbox.setPadding(new Insets(20));

        vbox.getChildren().addAll(hbox, hbox2,hbox3,hbox4,hbox5,hbox11,hbox6,hbox7,hbox8,hbox9,hbox10,textArea);


        root.getChildren().add(vbox);
        Scene scene = new Scene(root); // the stack pane is the root node

        primaryStage.setTitle("Question2");
        primaryStage.setScene(scene);


//        primaryStage.setScene(new Scene(tp, 800, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
