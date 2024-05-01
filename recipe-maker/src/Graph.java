import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Graph {
    /*------------------------------
      FIELDS
      ------------------------------*/
    private HashMap<Integer, ArrayList<Integer>> graph;
    private LinkedList<Integer> stack;

    //discovered not at moment it was added to stack
    //discovered moment it was removed
    private ArrayList<Integer> discovered;
    private HashMap<Integer, Boolean> seen;
    //maps an integer to the string ingredient
    private HashMap<Integer, Boolean> seeing;
    //private HashMap<Integer, String> strMap;
    private HashMap<Integer, Integer> startTime;
    private HashMap<Integer, Integer> finishTime;
    private Boolean cyclic = false;
    private int currTime = 0;

    /*------------------------------
      METHODS
      ------------------------------*/

    //constructor
    public Graph(HashMap<Integer, ArrayList<Integer>> graph, Integer source) {
        //assigning graph and the other fields
        this.graph = graph;
        this.discovered = new ArrayList<>();
        this.stack = new LinkedList<>();
        this.seen = new HashMap<>();
        //helps detect cycles
        this.seeing = new HashMap<>();
        this.startTime = new HashMap<>();
        this.finishTime = new HashMap<>();

        //for each key, make the value false at the start as none have been seen
        for (Integer key : this.graph.keySet()) {
            this.seen.put(key, false);
            this.seeing.put(key, false);
        }

        helper(source);

        for (Integer done : this.graph.keySet()) {
            if (!this.seen.get(done)) {
                helper(done);
            }
        }

        if (this.cyclic) {
            throw new IllegalArgumentException("Information given contains a cycle");
        }

        txtFile();
    }

    //meant to do the iterations to add necessary elements
    //to stack and discovered
    private void helper(Integer top) {
        this.stack.add(top);

        while(!this.stack.isEmpty()){
            int newTop = this.stack.peekLast();

            if (!this.seen.get(newTop)) {
                this.seen.put(newTop, true);
                this.seeing.put(newTop, true);
                this.currTime++;
                this.startTime.put(newTop, this.currTime);
            }

            boolean allNeighTrav = true;
            for(Integer neigh : this.graph.get(newTop)) {
                if (this.seeing.get(neigh)) {
                    this.cyclic = true;
                    throw new IllegalArgumentException("Information given contains a cycle");
                }
                if (!this.seen.get(neigh)) {
                    this.stack.add(neigh);
                    allNeighTrav = false;
                    break;
                }
            }

            if (allNeighTrav) {
                this.stack.removeLast();
                this.seeing.put(newTop, false);
                this.discovered.add(newTop);
                this.currTime++;
                this.finishTime.put(newTop, this.currTime);
            }
        }
    }

    public ArrayList<Integer> getDiscovered() {
        return this.discovered;
    }

    private void txtFile(){
        ArrayList<Integer> topSort = topSort();
        File file = new File("orderedRecipe.txt");

        for (Integer integer : topSort) {
            System.out.println(integer);
        }

        try (PrintWriter pWriter = new PrintWriter(file)){
            for (Integer integer : topSort) {
                pWriter.println(integer);
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public ArrayList<Integer> getTopSort() {
        return topSort();
    }
    private ArrayList<Integer> topSort() {
        if (this.cyclic) {
            throw new IllegalArgumentException("Information given contains a cycle");
        }

        //create a copy so we can remove entries
        HashMap<Integer, Integer>  finishTimeCopy = new HashMap<>(this.finishTime);
        ArrayList<Integer> topSort = new ArrayList<>();

        //sees which val is the biggest and then adds to topSort and deletes entry
        //repeats until copy of HashMap is empty
        while (!finishTimeCopy.isEmpty()) {
            //key or value could never be this value in practice
            int maxEntryKey = Integer.MIN_VALUE;
            int maxEntryVal = Integer.MIN_VALUE;

            for (HashMap.Entry<Integer, Integer> node : finishTimeCopy.entrySet()) {
                if (node.getValue() > maxEntryVal) {
                    maxEntryKey = node.getKey();
                    maxEntryVal = node.getValue();
                }

            }

            topSort.add(maxEntryKey);
            //remove entry once we've added it to top sort
            finishTimeCopy.remove(maxEntryKey);
        }

        return topSort;
    }
}
