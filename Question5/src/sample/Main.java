package sample;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

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
    private String nullable="";/*

    EXCEPTION HATA

    */
    Graph() throws FileException {
        vertex=new ArrayList<Vertex>();
        edges=new ArrayList<Edge>();VN=0;EN=0;
        /*
        import ko hata
        */
//        this.importGraph("input.txt");
        /*

         */
    }
    private ArrayList<Vertex> vertex;
    private ArrayList<Edge> edges;

    public int getVN() {
        return VN;
    }

    public void importGraph(String stri) throws FileException {
        String[] str;
        try
        {   BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Asus\\IdeaProjects\\Question5\\src\\sample\\"+stri));
            int n_ver = Integer.parseInt(br.readLine());
            while (n_ver > 0) {
                str= br.readLine().split(" ");
                this.addVertex(str[0], Double.valueOf(str[1]),Double.valueOf(str[2]));
                n_ver--;
            }
            int n_edge = Integer.parseInt(br.readLine());
            while (n_edge > 0) {
                str= br.readLine().split(" ");
                this.addEdge(str[0],str[1],Double.valueOf(str[2]));
                n_edge--;
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void exportGraph(String stri) throws FileException {
        this.setVN(vertex.size());
        this.setEN(edges.size());
        try(BufferedWriter br = new BufferedWriter(new FileWriter("C:\\Users\\Asus\\IdeaProjects\\Question5\\src\\sample\\"+stri)))
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
            this.nullable += ("From Vertex not found! Add vertex for chnaging..");
            throw new VNFException();
        }
        else if(v2==null) {
            this.nullable += ("To Vertex not found! Add vertex for chnaging..");
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
            System.out.println("WTF");
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
    public double getPath(double[][] adjacencyMatrix,
                          int startVertex,int endVertex,ArrayList<Integer> retVertex,ArrayList<Integer> retEdge) throws UnreachableException {
        if(startVertex == -1 || endVertex==-1)
            return -1;
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

        return printSolution(startVertex, shortestDistances, parents,endVertex,retVertex,retEdge);
    }
    private  double printSolution(int startVertex,
                                  double[] distances,
                                  int[] parents,
                                  int endVertex,ArrayList<Integer> retVertex,ArrayList<Integer> retEdge) throws UnreachableException {
        if(distances[endVertex]==Double.MAX_VALUE) {
            this.nullable+=("There is no path between the vertices"+ vertex.get(startVertex) +" and  "+vertex.get(endVertex));
            throw new UnreachableException();
        }
        this.nullable += ("Vertex\t Distance\t\tPath");
        this.nullable += ("\n" + this.vertex.get(startVertex).getName() + " -> ");
        this.nullable += (this.vertex.get(endVertex).getName() + " \t\t ");
        this.nullable += (distances[endVertex] + "\t\t");
        retVertex.add(startVertex);
        retVertex.add(endVertex);
        printPath(endVertex, parents,retVertex,retEdge);
        return  distances[endVertex];
    }

    private void printPath(int currentVertex,
                           int[] parents,ArrayList<Integer> retVertex,ArrayList<Integer> retEdge)
    {
        if (currentVertex == NO_PARENT)
        {
            return ;
        }
        printPath(parents[currentVertex], parents,retVertex,retEdge);
        this.nullable += (this.vertex.get(currentVertex).getName() + " ");
        retEdge.add(currentVertex);
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

class Cross extends Polygon{
    private double height;
    private double width;
    Polygon cross;
    Cross(double height,double width){
        this.height=height/4;
        this.width=width/4;
        this.cross = new Polygon();
        this.cross.getPoints().addAll(
                0*this.width, -1.4*this.height,
                0.7*this.width, -0.7*this.height,
                1.4*this.width, -1.4*this.height,
                0.7*this.width, -2.1*this.height,
                1.4*this.width, -2.8*this.height,
                0.7*this.width, -3.5*this.height,
                0*this.width, -2.8*this.height,
                -0.7*this.width, -3.5*this.height,
                -1.4*this.width, -2.8*this.height,
                -0.7*this.width, -2.12*this.height,
                -1.4*this.width, -1.4*this.height,
                -0.7*this.width, -0.7*this.height,
                0*this.width, -1.4*this.height);
    }
    Polygon getCross(){
        return this.cross;
    }
    public void setFill(Color c){
        super.setFill(c);
    }
    public void setStroke(Color c){
        super.setStroke(c);
    }
}

class Plus extends Shape{
    private double height;
    private double width;
    Polygon plus;
    Plus(double height,double width){
        this.height=height/3;
        this.width=width/3;
        this.plus = new Polygon();
        this.plus.getPoints().addAll(0*this.width, -this.height,
                this.width, -this.height,
                this.width, 0*this.height,
                2*this.width, 0*this.height,
                2*this.width, -this.height,
                3*this.width, -this.height,
                3*this.width, -2*this.height,
                2*this.width, -2*this.height,
                2*this.width, -3*this.height,
                this.width, -3*this.height,
                this.width, -2*this.height,
                0*this.width, -2*this.height,
                0*this.width, -1*this.height);
    }
    Polygon getPlus(){
        return this.plus;
    }
    public void setFill(Color c){
        super.setFill(c);
    }
    public void setStroke(Color c){
        super.setStroke(c);
    }

}

class Triangle extends Shape{
    private double height;
    private double width;
    Polygon triangle;
    Triangle(double height,double width){
        this.height=height/2;
        this.width=width;
        this.triangle = new Polygon();
        this.triangle.getPoints().addAll(0*this.width, -this.height,
                0*this.width, 0*this.height,
                this.width, -1*this.height,
                0*this.width, -2*this.height,
                0*this.width, 0*this.height);
    }
    Polygon getTriangle(){
        return this.triangle;
    }
    public void setFill(Color c){
        super.setFill(c);
    }
    public void setStroke(Color c){
        super.setStroke(c);
    }

}

public class Main extends Application {
    Graph graph = new Graph();
    String path="C:\\Users\\Asus\\IdeaProjects\\Question5\\src\\sample\\";
    String nullable="";
    private ArrayList<Vertex> vertexList =new ArrayList<>();
    private ArrayList<Edge> edgeList =new ArrayList<>();
    private ArrayList<Circle> circles =new ArrayList<>();
    private ArrayList<Line> lines =new ArrayList<>();
    private ArrayList<Label> labelcircles =new ArrayList<>();
    private ArrayList<Label> labellines =new ArrayList<>();
    private ArrayList<String> dijkstraFrom =new ArrayList<>();
    private ArrayList<String> dijkstraTo =new ArrayList<>();
    private ArrayList<String> dijkstraShape =new ArrayList<>();
    TextInputDialog tid = new TextInputDialog("");
    double eventX=0;
    double eventY=0;
    int eventStarted = -1;
    int tempCircle=-1;
    int mode=1;
    int queries=0;


    /*


    TO BE REMOVED



     */
    int startt=1;
    public Main() throws FileException {
//        graph.importGraph("input.txt");
        vertexList=graph.getVertex();
        edgeList=graph.getEdges();
    }

    /*




     */
    public ArrayList<Circle> Circopy(ArrayList<Circle> c){
        ArrayList<Circle> ret = new ArrayList<>();
        for(Circle it : c){
            Circle cir = new Circle();
            cir.setCenterX(it.getCenterX());
            cir.setCenterY(it.getCenterY());
            cir.setFill(it.getFill());
            cir.setStroke(it.getStroke());
            cir.setStrokeWidth(it.getStrokeWidth());
            cir.setRadius(it.getRadius());
            cir.setFocusTraversable(true);
            ret.add(cir);
        }
        return ret;
    }

    public ArrayList<Line> Lincopy(ArrayList<Line> c){
        ArrayList<Line> ret = new ArrayList<>();
        for(Line it : c){
            Line l = new Line();
            l.setStroke(it.getStroke());
            l.setStartX(it.getStartX());
            l.setStartY(it.getStartY());
            l.setEndY(it.getEndY());
            l.setEndX(it.getEndX());
            l.setStrokeWidth(it.getStrokeWidth());
            ret.add(l);
        }
        return ret;
    }

    public Vertex getVObj(String a){
        Vertex ret=null;
        for(Vertex t : vertexList){
            if(t.getName().compareTo(a)==0)
                ret = t;
        }
        return ret;
    }

    public  Line getLine(String a, String b,ArrayList<Line> linesCopy){
        Line ret=null;
        for(Line t : linesCopy){
            if(t.getStartX()==getVObj(a).getX() && t.getStartY()==getVObj(a).getY()
                    &&
                    t.getEndX()==getVObj(b).getX() && t.getEndY()==getVObj(b).getY()
            ){
                ret = t;
            }
        }
        return ret;
    }

    public void show(Group root2){
        root2.getChildren().removeAll(labelcircles);
        root2.getChildren().removeAll(labellines);
        labelcircles = new ArrayList<>();
        labellines = new ArrayList<>();
        for(int i=0;i<circles.size();i++){
            Label l =new Label(vertexList.get(i).getName());
            l.setLayoutX(vertexList.get(i).getX()-10);
            l.setLayoutY(vertexList.get(i).getY()-20);
            l.setFont(new Font(30));
            labelcircles.add(l);
        }
        root2.getChildren().addAll(labelcircles);
        for(int i=0;i<lines.size();i++){
            Label l =new Label(""+(int)edgeList.get(i).getWeight());
            l.setLayoutX(edgeList.get(i).getFrom().getX()/2 +edgeList.get(i).getTo().getX()/2 -10);
            l.setLayoutY(edgeList.get(i).getFrom().getY()/2 +edgeList.get(i).getTo().getY()/2 -10);
            l.setFont(new Font(30));
            labellines.add(l);
        }
        root2.getChildren().addAll(labellines);
    }

    private void showCopy(Group root2) {
        ArrayList<Label> copylabelcircles = copyLabel(labelcircles);
        ArrayList<Label> copylabellines = copyLabel(labellines);
        root2.getChildren().removeAll(copylabelcircles);
        root2.getChildren().removeAll(copylabellines);
        copylabelcircles = new ArrayList<>();
        copylabellines = new ArrayList<>();
        for(int i=0;i<circles.size();i++){
            Label l =new Label(vertexList.get(i).getName());
            l.setLayoutX(vertexList.get(i).getX()-10);
            l.setLayoutY(vertexList.get(i).getY()-20);
            l.setFont(new Font(30));
            copylabelcircles.add(l);
        }
        root2.getChildren().addAll(copylabelcircles);
        for(int i=0;i<lines.size();i++){
            Label l =new Label(""+edgeList.get(i).getWeight());
            l.setLayoutX(edgeList.get(i).getFrom().getX()/2 +edgeList.get(i).getTo().getX()/2 -10);
            l.setLayoutY(edgeList.get(i).getFrom().getY()/2 +edgeList.get(i).getTo().getY()/2  -10);
            l.setFont(new Font(30));
            copylabellines.add(l);
        }
        root2.getChildren().addAll(copylabellines);
    }

    private ArrayList<Label> copyLabel(ArrayList<Label> label) {
        ArrayList<Label> ret = new ArrayList<>();
        for(Label it : label){
            Label l = new Label();
            l.setLayoutX(it.getLayoutX());
            l.setLayoutY(it.getLayoutY());
            l.setFont(it.getFont());
            l.setText(it.getText());
            ret.add(l);
        }
        return ret;
    }

    public void  make(){
        circles =new ArrayList<>();lines =new ArrayList<>();
        for(Vertex v : vertexList){
            Circle c =new Circle();
            c.setRadius(25);
            c.setFocusTraversable(true);
            c.setFill(Color.TRANSPARENT);
            c.setStroke(Color.BLACK);
            c.setStrokeWidth(4);
            c.setCenterX(v.getX());
            c.setCenterY(v.getY());
            circles.add(c);
        }

        for(Edge e : edgeList){
            Line l =new Line();
            l.setStrokeWidth(3);
            l.setStartX(e.getFrom().getX());
            l.setStartY(e.getFrom().getY());
            l.setEndX(e.getTo().getX());
            l.setEndY(e.getTo().getY());
            lines.add(l);
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Canvas canvas= new Canvas();
        Group root2 = new Group(canvas);
        Scene scene = new Scene(root2,1500,500);
        Label fromB=new Label("From Vertex : ");
        Label toB=new Label("To Vertex :     ");
        TextField fromT = new TextField();
        TextField toT = new TextField();
        ObservableList<String> shapeChoices = FXCollections.observableArrayList("Square", "Plus", "Cross", "Triangle");
        Spinner<String> selection = new Spinner<String>();
        SpinnerValueFactory<String> valueFactory =  new SpinnerValueFactory.ListSpinnerValueFactory<String>(shapeChoices);
        valueFactory.setValue("Square");
        selection.setValueFactory(valueFactory);
        Button b=new Button("Get Dijkstra Paths");
        Button load=new Button("Load Dijkstra Query");
        b.setMaxWidth(Double.MAX_VALUE);
        load.setMaxWidth(Double.MAX_VALUE);
        HBox h1 =new HBox(),h2=new HBox(),h3=new HBox();
        h1.getChildren().addAll(fromB,fromT);
        h2.getChildren().addAll(toB,toT);
        h3.getChildren().addAll(new Label("Select Shape: "),selection);
        VBox v =new VBox();
        v.setSpacing(10);
        v.setAlignment(Pos.TOP_RIGHT);
        v.setPadding(new Insets(5, 20, 10, 1250));
        v.getChildren().addAll(h1,h2,h3,load,b);
        root2.getChildren().add(v);
        Alert alert = new Alert(Alert.AlertType.NONE);

        load.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                queries++;
                dijkstraFrom.add(fromT.getText());
                dijkstraTo.add(toT.getText());
                dijkstraShape.add(selection.getValue());
                fromT.clear();
                toT.clear();
            }
        });

        b.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Canvas cv = new Canvas();
                Group root3 = new Group(cv);
                Scene secondScene = new Scene(root3, 1400, 500);
                Stage newWindow = new Stage();
                newWindow.setScene(secondScene);
                while(queries>0) {
                    ArrayList<Circle> circlesCopy = Circopy(circles);
                    ArrayList<Line> linesCopy = Lincopy(lines);
                    showCopy(root3);
                    //                circles.copy();
                    ArrayList<Integer> dijkvertexList = new ArrayList<>();
                    ArrayList<Integer> dijkedgeList = new ArrayList<>();
                    double cost = 0;String str="";
                    try {
                        double[][] arr = graph.getAdj(graph.getVertex().size());
                        int i = -1, j = -1;
                        i = graph.retIndex(dijkstraFrom.get(queries - 1));
                        j = graph.retIndex(dijkstraTo.get(queries - 1));
                        if (i != -1 && j != -1)
                            cost = graph.getPath(arr, i, j, dijkvertexList, dijkedgeList);
                        System.out.println(graph.getPrint());
                        for (Circle c : circlesCopy) {
                            if (dijkedgeList.contains(circlesCopy.indexOf(c))) {
//                                c.setStroke(Color.VIOLET);
//                                c.setFill(Color.BLUEVIOLET);
                                System.out.println(circlesCopy.indexOf(c));
                            }
                        }
                        Shape sh = new Shape() {
                            @Override
                            public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
                                return super.getCssMetaData();
                            }
                        };
                        Path path = new Path();
                        PathTransition pathTransition = new PathTransition();
                        if(dijkstraShape.get(queries-1).compareTo("Square")==0){
                            sh = new Rectangle();
                            ((Rectangle) sh).setHeight(20);
                            ((Rectangle) sh).setWidth(20);
                            sh.setFill(Color.RED);
                            sh.setStroke(Color.RED);
                        }
                        else if(dijkstraShape.get(queries-1).compareTo("Plus")==0){
//                            sh = new Plus();
                            sh = new Plus(30,30).getPlus();
                            sh.setFill(Color.DARKBLUE);
                            sh.setStroke(Color.DARKBLUE);

                        }
                        else if(dijkstraShape.get(queries-1).compareTo("Cross")==0){
                            sh = new Cross(30,30).getCross();
                            sh.setRotate(45);
                            sh.setFill(Color.LIGHTGREEN);
                            sh.setStroke(Color.LIGHTGREEN);
                        }
                        else if(dijkstraShape.get(queries-1).compareTo("Triangle")==0){
//                            sh = new Triangle();
                            sh = new Triangle(25,25).getTriangle();
                            sh.setFill(Color.TRANSPARENT);
                            sh.setStroke(Color.YELLOW);
                            sh.setStrokeWidth(4);
                        }
                        for (int k = 0; k < (dijkedgeList.size() - 1); k++) {
                            Line t = getLine(vertexList.get(dijkedgeList.get(k)).getName(), vertexList.get(dijkedgeList.get(k + 1)).getName(), linesCopy);
//                            t.setFill(Color.BLUEVIOLET);
//                            t.setStroke(Color.BLUEVIOLET);
                            if(t!=null) {
                                t.setStrokeWidth(3);
                                path.getElements().add(new MoveTo(t.getStartX(), t.getStartY()));
                                path.getElements().add(new LineTo(t.getEndX(), t.getEndY()));
                                //                            pathTransition.setDelay(Duration.millis(5000));
                                pathTransition.setRate(0.1);
                                pathTransition.setNode(sh);
                                pathTransition.setPath(path);
                                pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                                pathTransition.setCycleCount(Integer.MAX_VALUE);
                            }
                        }
                        if(sh.getLayoutX()==0 && sh.getLayoutY()==0 && cost==0){
                            sh.setLayoutX(vertexList.get(i).getX()-10);
                            sh.setLayoutY(vertexList.get(i).getY()-10);
                        }
                        showCopy(root3);
                        pathTransition.play();
                        HBox h1 = new HBox();
                        //str+="\nDijkstra's Path Cost between vertex " + dijkstraFrom.get(queries - 1) + " and vertex " + dijkstraTo.get(queries - 1) + " is = " + cost;
//                        h1.getChildren().addAll(new Label(str));
                        VBox v = new VBox();
                        v.setAlignment(Pos.TOP_CENTER);
                        v.setPadding(new Insets(5, 20, 10, 800));
                        v.getChildren().addAll(h1);
                        root3.getChildren().add(v);
                        root3.getChildren().addAll(circlesCopy);
                        root3.getChildren().addAll(linesCopy);
                        root3.getChildren().addAll(sh);
                        showCopy(root3);
                        newWindow.setTitle("Dijkstra Path");
                        newWindow.setScene(secondScene);
                        newWindow.show();
                    } catch (VNFException | UnreachableException | ArrayIndexOutOfBoundsException e) {
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setTitle("");
                        alert.setContentText("No path is possible between vertex " + dijkstraFrom.get(queries - 1) + " and vertex " + dijkstraTo.get(queries - 1));
                        alert.show();
                        //                    e.printStackTrace();
                    }
                    queries--;
                }
                dijkstraTo = new ArrayList<>();
                dijkstraFrom = new ArrayList<>();
                dijkstraShape = new ArrayList<>();
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getText().compareTo("1")==0){
//                    System.out.println("Mode");
                    mode = 1;
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setContentText("Entered to Vertex Mode!");
                    alert.show();
                }
                if(keyEvent.getText().compareTo("2")==0){
//                    System.out.println("Mode");
                    mode = 2;
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setContentText("Entered to Edge Mode!");
                    alert.show();
                }
                System.out.println(keyEvent.getText());if(keyEvent.getText().compareTo("e")==0){
//                    System.out.println("Mode");
                    try {
                        graph.exportGraph("file");
                    } catch (FileException e) {
//                        e.printStackTrace();
                    }
                    System.out.println("exported");
                }
            }
        });

        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Released!!");
                double clickX=mouseEvent.getSceneX(),clickY=mouseEvent.getSceneY();
                int found =0;
                for(Circle cir : circles) {
                    if (mode ==1 && cir.contains(clickX, clickY)) {
                        found=1;
                        System.out.println("To Vertex " + vertexList.get(circles.indexOf(cir)).getName());
                        if(eventX==cir.getCenterX() && eventY==cir.getCenterY()) {
                            //Same to same Vertex
                            tid.setHeaderText("Vertex "+ vertexList.get(circles.indexOf(cir)).getName() +" selected. Press \"del\" or d key to delete.");
                            tid.setX(clickX);
                            tid.setY(clickY);
                            tid.setContentText(null);
                            tid.showAndWait();
                            String name=tid.getEditor().getText();
                            tid.getEditor().clear();eventStarted=-1;
                            if(name.compareTo("del")==0||name.compareTo("d")==0){
                                try {
                                    graph.delVertex(vertexList.get(circles.indexOf(cir)).getName());
                                    vertexList=graph.getVertex();
                                    edgeList=graph.getEdges();
                                    root2.getChildren().removeAll(circles);
                                    root2.getChildren().removeAll(lines);
                                    make();
                                    root2.getChildren().addAll(circles);
                                    root2.getChildren().addAll(lines);
                                }
                                catch (VNFException e){
                                    alert.setAlertType(Alert.AlertType.INFORMATION);
                                    alert.setTitle("");
                                    nullable=graph.getPrint();
                                    alert.setHeaderText(nullable);
                                    alert.show();
                                }
                                tid.getEditor().clear();
                                tempCircle = -1;
                            }
                            break;
                        }
                        else if (eventStarted == 0) {
                            tid.setHeaderText("Enter Weight");
                            tid.setX(clickX);
                            tid.setY(clickY);
                            tid.setContentText(null);
                            tid.showAndWait();
                            try {
                                if(tid.getEditor().getText().compareTo("")==0)
                                throw new Exception();
                                double d =Double.parseDouble(tid.getEditor().getText());
                                graph.addEdge(vertexList.get(tempCircle).getName(), vertexList.get(circles.indexOf(cir)).getName(), Double.parseDouble(tid.getEditor().getText()));
                                tid.getEditor().clear();

                                vertexList = graph.getVertex();
                                edgeList = graph.getEdges();
                                show(root2);
                                Line line = new Line(eventX, eventY, cir.getCenterX(), cir.getCenterY());
                                line.setStrokeWidth(3);
                                lines.add(line);
                                root2.getChildren().add(line);
                            }
                            catch (VNFException | ENFException e) {
//                                    e.printStackTrace();
                                alert.setAlertType(Alert.AlertType.INFORMATION);
                                alert.setTitle("");
                                nullable=graph.getPrint();
                                alert.setHeaderText(nullable);
                                tid.getEditor().clear();
                                alert.show();
                            }
                            catch (NumberFormatException e) {
//                                    e.printStackTrace();
                                alert.setAlertType(Alert.AlertType.INFORMATION);
                                alert.setTitle("");
                                alert.setHeaderText("Give weight in double value!");
                                tid.getEditor().clear();
                                alert.show();
                            }
                            catch (Exception e){
                                //
                            }
                            tempCircle = -1;
                            eventStarted = -1;
                        }
                        else {
                            tempCircle = -1;
                            eventStarted = -1;
                        }
                    }
                }/* Isko bhi*/
                if(startt==-1){
                    startt=1;
                }
                /* blah */
                else if(found==0 && mode==1){
                    System.out.println("Outside");
                    root2.getChildren().removeAll(circles);
                    root2.getChildren().removeAll(lines);
                    vertexList=graph.getVertex();
                    vertexList.get(tempCircle).setX(clickX);
                    vertexList.get(tempCircle).setY(clickY);
                    graph.setVertex(vertexList);
                    for(Circle cir : circles) {
                        if (cir.contains(eventX, eventY)) {
                            tempCircle=circles.indexOf(cir);
                            double tX=cir.getCenterX(),tY=cir.getCenterY();
                            cir.setCenterX(clickX);
                            cir.setCenterY(clickY);
                            for(Line line : lines) {
                                if (line.getStartX()==tX && line.getStartY()== tY) {
                                    line.setStartX(clickX);
                                    line.setStartY(clickY);
//                                        eventX=line.getEndX();eventY=line.getEndY();
                                }
                                if (line.getEndX()==tX && line.getEndY()== tY) {
                                    line.setEndX(clickX);
                                    line.setEndY(clickY);
//                                        eventX=line.getStartX();eventY=line.getStartY();
                                }
                            }
                            eventX=clickX;eventY=clickY;
                        }
                    }
                    root2.getChildren().addAll(circles);
                    root2.getChildren().addAll(lines);
                    tempCircle = -1;
                    eventStarted = -1;
                }
                if(mode==2){
                    found=0;
                    int indexOfLine=-1;
                    for(Line line : lines) {
                        if(line.contains(clickX,clickY)){
                            found=1;
                            tid.setHeaderText("Write \" del \" or d key to delete or set new weight to edit weight of the edge.\nCurrent weight is : "+edgeList.get(lines.indexOf(line)).getWeight());
                            tid.setX(clickX);
                            tid.setY(clickY);
                            tid.setContentText(null);
                            tid.showAndWait();
                            if(tid.getEditor().getText().toLowerCase().compareTo("del")==0 || tid.getEditor().getText().toLowerCase().compareTo("d")==0){
                                tid.close();
                                tid.getEditor().clear();
                                System.out.println(edgeList.get(lines.indexOf(line)).getFrom().getName()+" "+edgeList.get(lines.indexOf(line)).getTo().getName());
                                try {
                                    indexOfLine=lines.indexOf(line);
                                    graph.delEdge(edgeList.get(lines.indexOf(line)).getFrom().getName(),edgeList.get(lines.indexOf(line)).getTo().getName());
                                    edgeList=graph.getEdges();
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
                                catch (Exception e){
                                    System.out.println("Exception occured \n " +edgeList.get(lines.indexOf(line)).getFrom().getName()+" "+edgeList.get(lines.indexOf(line)).getTo().getName());
                                }
                            }
                            else if(tid.getEditor().getText().compareTo("")==0 || tid.getEditor().getText().isEmpty()){
                                //
                            }
                            else{
                                indexOfLine=lines.indexOf(line);
                                try {
                                    edgeList.get(indexOfLine).setWeight(Double.parseDouble(tid.getEditor().getText()));
                                    graph.setEdges(edgeList);
                                }
                                catch (NumberFormatException e){
                                    alert.setAlertType(Alert.AlertType.ERROR);
                                    alert.setHeaderText("Please give weight in double");
                                    alert.show();
                                }
                                tid.getEditor().clear();
                                indexOfLine=-1;
                            }
                            break;
                        }
                    }
                    root2.getChildren().removeAll(lines);
                    if(found==1 && indexOfLine!=-1) {
                        lines.remove(indexOfLine);indexOfLine=-1;found=0;
                    }
                    root2.getChildren().addAll(lines);
                }
                show(root2);
            }
        });

        scene.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double x=mouseEvent.getX(),y=mouseEvent.getY();
                System.out.println("Dragging!");

            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            }
        });

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int found =0;;
                double clickX = mouseEvent.getSceneX(), clickY = mouseEvent.getSceneY();
                for(Circle cir : circles){
                    if( cir.contains(clickX,clickY) && mode==1){
                        found=1;
                        System.out.println("From Vertex "+vertexList.get(circles.indexOf(cir)).getName());
                        if(eventStarted==-1){
                            eventStarted=0;
                            tempCircle=circles.indexOf(cir);
                            eventX=cir.getCenterX();
                            eventY=cir.getCenterY();
                        }
                        break;
                    }
                }/* isko bhi*/
                if(startt==0){
                    startt=-1;
                    root2.getChildren().removeAll(circles);
                    root2.getChildren().removeAll(lines);
                    make();
                    root2.getChildren().addAll(circles);
                    root2.getChildren().addAll(lines);
                    System.out.println("Hola");
                }
                /*oka*/
                else if(found==0 && mode==1)
                {
                    System.out.println("Pressed mouse");
                    System.out.println(clickX + " " + clickY);
                    Circle c = new Circle();
//                    Label l =new Label();
                    c.setRadius(25);
                    c.setCenterX(clickX);
                    c.setCenterY(clickY);
                    c.setFocusTraversable(true);
                    c.setFill(Color.TRANSPARENT);
                    c.setStroke(Color.BLACK);
                    c.setStrokeWidth(4);

                    tid.setHeaderText("Enter Vertex Name");
                    tid.setX(clickX);
                    tid.setY(clickY);
                    tid.setContentText(null);
                    tid.showAndWait();
                    try {
                        if(tid.getEditor().getText().compareTo("")==0){
                            throw new Exception();
                        }
                        graph.addVertex(tid.getEditor().getText(), clickX, clickY);tid.getEditor().clear();
                        tid.getEditor().clear();
                        circles.add(c);
                        show(root2);
                        vertexList=graph.getVertex();
//                        if(tid.getContentText().compareTo("")==0)
//                            throw new Exception();
                        root2.getChildren().add(c);
                    }
                    catch (VNFException e) {
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setTitle("");
                        nullable=graph.getPrint();
                        alert.setHeaderText(nullable);
//                    alert.setContentText(nullable);
                        alert.show();
                    }
                    catch (Exception e){
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setTitle("");
                        alert.setHeaderText("Please give a valid input!");
                    }

                    for (int i = 0; i < vertexList.size(); i++) {
                        System.out.println(vertexList.get(i).getName() + " " + vertexList.get(i).getX() + " " +
                                vertexList.get(i).getY());
                    }
                    for (int i = 0; i < edgeList.size(); i++) {
                        System.out.println(edgeList.get(i).getFrom().getName() + " " + edgeList.get(i).getTo().getName() + " " +
                                edgeList.get(i).getWeight());
                    }

                }
            }
        });

        primaryStage.setTitle("Question5");
        primaryStage.setScene(scene);
//        primaryStage.setScene(new Scene(tp, 800, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
