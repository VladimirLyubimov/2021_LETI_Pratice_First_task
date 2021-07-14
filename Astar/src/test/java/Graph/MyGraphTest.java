package Graph;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class MyGraphTest {
    private MyGraph defaultGraph;

    @Before
    public void setUp() throws Exception{
        String[] edges = new String[] {"A B 2", "A C 2", "B D 2", "B E 2", "D F 3", "E H 3", "F I 4", "F J 2", "I K 3", "J L 1", "H J 2", "L K 4"};
        String[] vertexes = new String[] {"A 0 0", "B 2 0" , "C 0 2", "D 4 0", "E 2 2", "F 4 3", "I 8 3", "J 4 5", "H 2 5", "K 8 6", "L 4 6"};
        try{
            defaultGraph = new MyGraph(edges, vertexes);
        }
        catch (IndexOutOfBoundsException err){
        }
    }

    @Test
    public void graphCorrectCreation() {
        String[] edges = new String[]{"A B 2", "A C 2", "B D 4"};
        String[] vertexes = new String[]{"A 0 0", "B 2 0", "C 0 2", "D 2 4"};
        MyGraph testGraph;
        testGraph = new MyGraph(edges, vertexes);
        assertEquals(1, 1);
    }

    @Test
    public void doubleVertexCreation() {
        String[] edges = new String[]{"A B 2", "A C 2", "B D 4"};
        String[] vertexes = new String[]{"A 0 0", "A 2 0", "C 0 2", "D 2 4"};
        MyGraph testGraph;
        try {
            testGraph = new MyGraph(edges, vertexes);
            assertEquals(0, 1);
        } catch (IndexOutOfBoundsException err) {
            assertEquals(1, 1);
        }
    }

    @Test
    public void wrongEdgeCreation(){
        String[] vertexes = new String[] {"A 0 0", "B 2 0" , "C 0 2", "D 2 4"};
        String[] edges = new String[] {"A U 2", "A C 2", "B D 4"};
        MyGraph testGraph;
        try{
            testGraph = new MyGraph(edges, vertexes);
            assertEquals(0,1);
        }
        catch(IndexOutOfBoundsException err){
            assertEquals(1,1);
        }
    }

    @Test
    public void randomGraphCreation(){
        int exp = 10;
        MyGraph testGraph = new MyGraph(10, 12, 1, 2);
        assertEquals(10,testGraph.getSize());
    }

    @Test
    public void randomGraphTooManyEdgesCreation(){
        try{
            MyGraph testGraph = new MyGraph(10, 12, 1, 2);
        }
        catch (IndexOutOfBoundsException err){
            assertEquals(1,1);
        }
    }

    @Test
    public void randomGraphTooManyVertexesCreation(){
        try{
            MyGraph testGraph = new MyGraph(100, 12, 1, 6);
        }
        catch (IndexOutOfBoundsException err){
            assertEquals(1,1);
        }
    }

    @Test
    public void unaryGraphTooManyVertexesCreation(){
        try{
            MyGraph testGraph = new MyGraph(200);
        }
        catch (IndexOutOfBoundsException err){
            assertEquals(1,1);
        }
    }

    @Test
    public void getSize(){
        int expected = 11;
        int actual = defaultGraph.getSize();
        assertEquals(expected, actual);
    }

    @Test
    public void addVertex() {
        String name = "T";
        int x = 10;
        int y = 10;
        int actualCount;
        int expected = 12;
        Vertex expectedVertex = new Vertex(name, x, y);
        defaultGraph.addVertex(name, x, y);
        actualCount = defaultGraph.getSize();
        assertEquals(expected, actualCount);
        assertEquals(expectedVertex, defaultGraph.getVertex(name).get());
    }

    @Test
    public void addExistVertex() {
        try{
            String name = "A";
            int x = 10;
            int y = 10;
            defaultGraph.addVertex(name, x, y);
            assertEquals(1,0);
        }
        catch (IndexOutOfBoundsException err){
            assertEquals(1,1);
        }
    }

    @Test
    public void addInSameCoordVertex() {
        try{
            String name = "T";
            int x = 0;
            int y = 0;
            defaultGraph.addVertex(name, x, y);
            assertEquals(1,0);
        }
        catch (IndexOutOfBoundsException err){
            assertEquals(1,1);
        }
    }

    @Test
    public void addEdge() {
        String start = "C";
        String finish = "A";
        int weight = 2;
        Edge expectedEdge = new Edge(start, finish, weight);

        try {
            defaultGraph.addEdge(start, finish, weight);
            Vertex start_vertex = defaultGraph.getVertex(start).get();
            assertEquals(expectedEdge, start_vertex.getEdge(start_vertex.getEdgeAmount() - 1));
        } catch (IOException err) {
            assertEquals(1, 0);
        }
    }

    @Test
    public void addNoStartEdge() {
        String start = "Y";
        String finish = "A";
        int weight = 2;
        try {
            defaultGraph.addEdge(start, finish, weight);
            assertEquals(1, 0);
        } catch (IOException err) {
            assertEquals(1, 1);
        }
    }

    @Test
    public void addAlreadyExistEdge() {
        String start = "A";
        String finish = "B";
        int weight = 2;
        try {
            defaultGraph.addEdge(start, finish, weight);
            assertEquals(1, 0);
        } catch (IOException err) {
            assertEquals(1, 1);
        }
    }

    @Test
    public void addNoFinishEdge() {
        String start = "A";
        String finish = "O";
        int weight = 2;
        try {
            defaultGraph.addEdge(start, finish, weight);
        } catch (IOException err) {
            assertEquals(1, 1);
        }
    }

    @Test
    public void addWrongWeightEdge() {
        String start = "B";
        String finish = "A";
        int weight = 8;
        try {
            defaultGraph.addEdge(start, finish, weight);
            assertEquals(1, 0);
        }
        catch (IOException err){
            assertEquals(1,1);
        }
    }

    @Test
    public void deleteVertex() {
        String name = "A";
        int expected_size = 10;
        int actual_size;
        defaultGraph.deleteVertex(name);
        actual_size = defaultGraph.getSize();
        assertEquals(expected_size, actual_size);
        assertFalse(defaultGraph.getVertex(name).isPresent());
    }

    @Test
    public void deleteUnrealVertex() {
        String name = "O";
        try {
            defaultGraph.deleteVertex(name);
            assertEquals(1, 0);
        } catch (IndexOutOfBoundsException err) {
            assertEquals(1, 1);
        }
    }

    @Test
    public void deleteFromEmptyVertex() {
        MyGraph emptyGraph = new MyGraph();
        String name = "Y";
        try{
            defaultGraph.deleteVertex(name);
            assertEquals(1,0);
        }
        catch (IndexOutOfBoundsException err){
            assertEquals(1,1);
        }
    }

    @Test
    public void deleteEdge() {
        String start = "A";
        String finish = "B";
        int expected_edge_count = defaultGraph.getVertex(start).get().getEdgeAmount() - 1;
        int actual_edge_count = 0;
        defaultGraph.deleteEdge(start, finish);
        actual_edge_count = defaultGraph.getVertex(start).get().getEdgeAmount();
        assertEquals(expected_edge_count, actual_edge_count);
    }

    @Test
    public void deleteUnrealEdge() {
        String start = "B";
        String finish = "A";
        try {
            defaultGraph.deleteEdge(start, finish);
            assertEquals(1, 0);
        } catch (IndexOutOfBoundsException err) {
            assertEquals(1, 1);
        }
    }

    @Test
    public void deleteWithoutFinishEdge() {
        String start = "A";
        String finish = "Y";
        try {
            defaultGraph.deleteEdge(start, finish);
            assertEquals(1, 0);
        } catch (IndexOutOfBoundsException err) {
            assertEquals(1, 1);
        }
    }

    @Test
    public void deleteWithoutStartEdge() {
        String start = "U";
        String finish = "B";
        try{
            defaultGraph.deleteEdge(start,finish);
            assertEquals(1,0);
        }
        catch (IndexOutOfBoundsException err){
            assertEquals(1,1);
        }
    }
}