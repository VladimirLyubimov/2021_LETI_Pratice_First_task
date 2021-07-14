import Graph.MyGraph;
import GUI.Gui;

public class Main {
    public static void main(String[] args) {
        String[] edges = new String[] {"AGH B 2", "AGH C 2", "B D 2", "B E 2", "D F 3", "E H 3", "F I 4", "F J 2", "I K 3", "J L 1", "H J 2", "L K 4"};
        String[] vertexes = new String[] {"AGH 0 0", "B 2 0" , "C 0 2", "D 4 0", "E 2 2", "F 4 3", "I 8 3", "J 4 5", "H 2 5", "K 8 6", "L 4 6"};
        MyGraph graph;
        try{
            graph = new MyGraph(edges, vertexes);
        }
        catch (IndexOutOfBoundsException err){
            System.out.println("Error!");
            return;
        }
        Gui gui = new Gui(graph);
    }
}
