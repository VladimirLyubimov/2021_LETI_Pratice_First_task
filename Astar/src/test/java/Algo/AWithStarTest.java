package Algo;

import Graph.MyGraph;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AWithStarTest {
    private MyGraph graph;

    @Before
    public void setUp() throws Exception {
        String[] edges = new String[] {"A B 2", "A C 2", "B D 2", "B E 2", "D F 3", "E H 3", "F I 4", "F J 2", "I K 3", "J L 1", "H J 2", "L K 4"};
        String[] vertexes = new String[] {"A 0 0", "B 2 0" , "C 0 2", "D 4 0", "E 2 2", "F 4 3", "I 8 3", "J 4 5", "H 2 5", "K 8 6", "L 4 6"};
        try{
            graph = new MyGraph(edges, vertexes);
        }
        catch (IndexOutOfBoundsException err){
        }
        graph.resetGraph();
        graph.resetStartFinish();
    }

    @Test
    public void doAlgoPathExist() {
        graph.setStart("A");
        graph.setFinish("K");
        String expected_path = "[A, B, D, F, I, K]";
        String actual_path;
        actual_path = AWithStar.doAlgo(graph).toString();
        assertEquals(expected_path, actual_path);
    }

    @Test
    public void doAlgoNoPath() {
        graph.setStart("C");
        graph.setFinish("A");
        String expected_path = "[No path!]";
        String actual_path = AWithStar.doAlgo(graph).toString();
        assertEquals(expected_path, actual_path);
    }

    @Test
    public void doAlgoSameStartFinish() {
        graph.setStart("A");
        graph.setFinish("A");
        ;
        String expected_path = "[Start and finish vertexes are same!]";
        String actual_path = AWithStar.doAlgo(graph).toString();
        assertEquals(expected_path, actual_path);
    }

    @Test
    public void doAlgoNoFinish() {
        graph.setStart("A");
        graph.setFinish("Ms");

        String expected_path = "[No path! Finish vertex doesn't exist!]";
        String actual_path = AWithStar.doAlgo(graph).toString();
        assertEquals(expected_path, actual_path);
    }

    @Test
    public void doAlgoNoStart() {
        graph.setStart("U");
        graph.setFinish("K");
        try{
            String actual_path = AWithStar.doAlgo(graph).toString();
            assertEquals(1,0);
        }
        catch (IndexOutOfBoundsException err){
            assertEquals(1,1);
        }
    }
}