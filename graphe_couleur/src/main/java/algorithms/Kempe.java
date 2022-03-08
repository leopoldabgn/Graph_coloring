package algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import graphs.Graph;
import graphs.Vertex;

public class Kempe {

    private List<Color> remainsColor;
    //private static Graph kempeCGraph = new Graph("Kempe Chain");

    private void initColor(){
        Color tab [] = {Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW, Color.PINK};
        remainsColor = Arrays.asList(tab); 
    }

    public boolean moreThanFiveVertices(Vertex v) {
        return v.getVertices().size() >= 5;
    }

    public boolean allColored(ArrayList<Vertex> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getColor().equals(Color.WHITE))
                return false;
        }
        return true;
    }

    public boolean noMoreColor(Vertex v){
        this.initColor();
        int numOfRemainsColor = remainsColor.size();
        for(int i = 0 ; i < v.getVertices().size() ; i++){
            for(int j = 0 ; j < numOfRemainsColor ;  j++){
                if(v.getVertices().get(i).getColor().equals(remainsColor.get(j))){
                    remainsColor.remove(j);
                    numOfRemainsColor -= 1;
                }
            }
        }
        return numOfRemainsColor == 0;
    }
    //Si je me trompe pas kempeChain renvoie un graph qui est la chaine de kempe pour un vertex donné 
    //et son 1er voisin en gros. Mais c'est pas ce qu'on veut je crois vu qu'on veut partir d'un vertex avec 
    //5 voisins, tous de col differentes et en prendre 2 non voisins entre eux et créer la chaine à partir de 
    //ces 2 la. ??  
    public static Graph kempeChain(Vertex current) {
        Graph graph = new Graph("Kempe chain");
        kempeChainAux(graph, current, null);
        return graph;
    }

    public static void kempeChainAux(Graph graph, Vertex current, Color c2) {
        graph.addVertex(current);
        ArrayList<Vertex> vertices = current.getVertices();
        if(c2 == null) {
            if(vertices.isEmpty())
                return;
            c2 = vertices.get(0).getColor();//y a tjrs un voisin avec get(0) en id ? Donc les vertex ils ont un id pour le graph et un id pour chaque voisin ?
        }
            
        for(int i = 0 ; i < vertices.size() ; i++) {
            Vertex next = vertices.get(i);
            if(next.getColor() == c2 && 
               !graph.getVertices().contains(next)) {
                kempeChainAux(graph, next, current.getColor());
               }
        }
    }

    public static void reverseKempeChain(Graph graph) {
        ArrayList<Vertex> vertices = graph.getVertices();
        if(vertices.isEmpty())
            return;
        Color c1 = vertices.get(0).getColor();
        vertices = vertices.get(0).getVertices();
        if(vertices.isEmpty())
            return;
        Color c2 = vertices.get(0).getColor();

        for(Vertex v : graph.getVertices())
            v.setColor(v.getColor() == c1 ? c2 : c1);

    }
    
    //retourne l'id du vertex avec le moins de voisins dans un graph si il a 5 voisins
    //Rm : il faudrait faire fct qui verifie qu'un graph est planaire avant d'appeler cette fct.
    public static int isKempe(Graph graph){
        int minVertx = 0;
        int  idmin = 0;
        for(Vertex v : graph.getVertices()){
            int nbVertx = v.getVertices().size();
            int idV = v.getId();
            if( nbVertx < minVertx){
                minVertx = nbVertx;
                idmin = idV;
            }
        }
        if(minVertx < 5)
            return -1;
        return idmin;
    }

    public static Graph beginKempe(Graph graph){
        int idmin = isKempe(graph);
        if(idmin == -1)
            return graph;//on return le même graph mais en soit faudrait le colorier avec 5 couleurs ducoup c'est tt
        Vertex v = graph.getVertex(idmin);
        return kempeChain2(v, 0);
    }

    public static Graph kempeChain2(Vertex current, int id){
        ArrayList<Vertex> vertices = current.getVertices();
        Graph kempe_graph = new Graph("Kempe chain");
        Vertex v1 = null;
        Vertex v2 = null;
        if(id == 0){
            v1 = vertices.get(0);
            for(int i=1;i<vertices.size();i++){
                v2 = vertices.get(i);
                if(!v1.getVertices().contains(v2)){
                    break;
                }
            }
        } else {
            for(Vertex v : vertices){
                if(!(v.getId() == 0 || v.getId() == id)){
                    v1 = v;
                }
            }
            for(Vertex v : vertices){
                if(!(v.getId() == 0 || v.getId() == id || v == v1)){
                    if(!v1.getVertices().contains(v)){
                        v2 = v;
                    }
                }
            }
        }
        kempe_graph.addVertex(v1);
        Color c1 = v1.getColor();
        Color c2 = v2.getColor();
         
        kempe_graph(kempe_graph,v1,c1,c2);
        if(kempe_graph.getVertices().contains(v2))
            kempeChain2(current, v2.getId());

        return kempe_graph;
    }

    public static void kempe_graph(Graph kempe_graph, Vertex current, Color c1, Color c2){
        ArrayList<Vertex> vertices = current.getVertices();
        for(Vertex v : vertices){
            if(v.getColor() == c1 || v.getColor() == c2){
                kempe_graph.addVertex(v);
                kempe_graph(kempe_graph,v,c1,c2);
            }
        }
    }
  }
