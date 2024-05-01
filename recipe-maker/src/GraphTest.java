//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//
//import org.junit.Test;
//
//public class GraphTest {
//
//    @Test
//    public void testTopologicalSort() {
//        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
//        graph.put(1, new ArrayList<>(Arrays.asList(2, 3)));
//        graph.put(2, new ArrayList<>(Arrays.asList(4)));
//        graph.put(3, new ArrayList<>(Arrays.asList(4)));
//        graph.put(4, new ArrayList<>());
//
//        Graph g = new Graph(graph, 1);
//        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 2, 4));
//        assertEquals(expected, g.getTopSort());
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testCyclicGraph() {
//        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
//        graph.put(1, new ArrayList<>(Arrays.asList(2)));
//        graph.put(2, new ArrayList<>(Arrays.asList(3)));
//        graph.put(3, new ArrayList<>(Arrays.asList(1)));
//
//        new Graph(graph, 1);
//    }
//
//    @Test
//    public void testSingleNodeGraph() {
//        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
//        graph.put(1, new ArrayList<>());
//
//        Graph g = new Graph(graph, 1);
//        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1));
//        assertEquals(expected, g.getTopSort());
//    }
//
//    @Test
//    public void testDisconnectedGraph() {
//        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
//        graph.put(1, new ArrayList<>(Arrays.asList(2)));
//        graph.put(2, new ArrayList<>());
//        graph.put(3, new ArrayList<>());
//        graph.put(4, new ArrayList<>());
//
//        Graph g = new Graph(graph, 1);
//        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(4, 3, 1, 2));
//        assertEquals(expected, g.getTopSort());
//    }
//
//    @Test (expected = IllegalArgumentException.class)
//    public void testDisconnectedGraphWithCycle() {
//        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
//        graph.put(1, new ArrayList<>(Arrays.asList(2)));
//        graph.put(2, new ArrayList<>(Arrays.asList(3)));
//        graph.put(3, new ArrayList<>(Arrays.asList(1)));
//        graph.put(4, new ArrayList<>());
//
//        Graph g = new Graph(graph, 1);
//        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 2, 4));
//        assertEquals(expected, g.getTopSort());
//    }
//
//    @Test
//    public void testComplexTopologicalSort() {
//        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
//        graph.put(1, new ArrayList<>(Arrays.asList(2, 3)));
//        graph.put(2, new ArrayList<>(Arrays.asList(4)));
//        graph.put(3, new ArrayList<>(Arrays.asList(4, 5)));
//        graph.put(4, new ArrayList<>(Arrays.asList(6)));
//        graph.put(5, new ArrayList<>(Arrays.asList(6)));
//        graph.put(6, new ArrayList<>(Arrays.asList(7)));
//        graph.put(7, new ArrayList<>());
//
//        Graph g = new Graph(graph, 1);
//        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 5, 2, 4, 6, 7));
//        assertEquals(expected, g.getTopSort());
//    }
//}
//
//
