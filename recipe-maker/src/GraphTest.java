import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;

public class GraphTest {

    //cycle should throw error
    @Test(expected = IllegalArgumentException.class)
    public void cycle() {
        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
        ArrayList<Integer> set1 = new ArrayList<Integer>();
        set1.add(2);
        ArrayList<Integer> set2 = new ArrayList<Integer>();
        set2.add(3);
        ArrayList<Integer> set3 = new ArrayList<Integer>();
        set3.add(1);
        graph.put(1, set1);
        graph.put(2, set2);
        graph.put(3, set3);
        new Graph(graph, 1);
    }

    //no cycle should work normally
    @Test
    public void acyclic() {
        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
        ArrayList<Integer> set1 = new ArrayList<Integer>();
        set1.add(2);
        ArrayList<Integer> set2 = new ArrayList<Integer>();
        set2.add(3);
        ArrayList<Integer> set3 = new ArrayList<Integer>();
        set3.add(4);
        graph.put(1, set1);
        graph.put(2, set2);
        graph.put(3, set3);
        graph.put(4, new ArrayList<>());
        Graph graph1 = new Graph(graph, 1);
    }

    @Test
    public void topSort() {
        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
        ArrayList<Integer> set1 = new ArrayList<Integer>();
        set1.add(2);
        ArrayList<Integer> set2 = new ArrayList<Integer>();
        set2.add(3);
        graph.put(1, set1);
        graph.put(2, set2);
        graph.put(3, new ArrayList<>());
        Graph graph1 = new Graph(graph, 1);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        expected.add(3);
        assertEquals(expected, graph1.getTopSort());
    }

    @Test
    public void discovered() {
        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
        ArrayList<Integer> set1 = new ArrayList<Integer>();
        set1.add(2);
        ArrayList<Integer> set2 = new ArrayList<Integer>();
        set2.add(3);
        graph.put(1, set1);
        graph.put(2, set2);
        graph.put(3, new ArrayList<>());
        Graph graph1 = new Graph(graph, 1);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(3);
        expected.add(2);
        expected.add(1);
        assertEquals(expected, graph1.getDiscovered());
    }
}
