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
    private ArrayList<Integer> discovered;
    private HashMap<Integer, Boolean> seen;
    //maps an integer to the string ingredient
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
        this.startTime = new HashMap<>();
        this.finishTime = new HashMap<>();

        //for each key, make the value false at the start as none have been seen
        for (Integer key : this.graph.keySet()) {
            this.seen.put(key, false);
        }

        this.stack.add(source);
        helper(source);

        for (Integer done : this.graph.keySet()) {
            if (!this.seen.get(done)) {
                this.stack.add(done);
                helper(done);
            }
        }
    }

    //meant to do the iterations to add necessary elements
    //to stack and discovered
    private void helper(Integer top) {
        //setting source node to say it has been seen if it hasn't already
        this.seen.put(top, true);
        this.currTime++;
        this.startTime.put(top, this.currTime);

        //loop until queue is incomplete and be able to start
        //if it's the first node
        while (!(this.stack.isEmpty())) {
            //pull from top of the stack and add to discovered
            int newTop = this.stack.pollLast();
            this.discovered.add(newTop);

            //iterate through neighbors of the source node
            for (int i = 0; i < this.graph.get(newTop).size(); i++) {
                //set temp to neighbor i
                int temp = this.graph.get(newTop).get(i);
                //if i isn't in seen, set it to seen, and add
                //to top of the stack
                if (this.seen.containsKey(temp) && !(this.seen.get(temp))) {
                    this.seen.put(temp, true);
                    this.stack.add(temp);
                }
            }
        }
        this.currTime++;
        this.finishTime.put(top, this.currTime);
    }

    public ArrayList<Integer> getDiscovered() {
        return this.discovered;
    }

    public void txtFile(){
        ArrayList<Integer> topSort = getTopSort();
        File file = new File("orderedRecipe.txt");

        try (PrintWriter pWriter = new PrintWriter(file)){
            for (Integer integer : topSort) {
                pWriter.println(integer);
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }

    private ArrayList<Integer> getTopSort() {
        if (this.cyclic) {
            throw new IllegalArgumentException("Information given contains a cycle");
        }

        //create a copy so we can remove entries
        HashMap<Integer, Integer>  finishTimeCopy = new HashMap<>(finishTime);
        ArrayList<Integer> topSort = new ArrayList<>();

        //sees which val is the biggest and then adds to topSort and deletes entry
        //repeats until copy of HashMap is empty
        while (!finishTimeCopy.isEmpty()) {
            //key or value could never be this value in practice
            int maxEntryKey = Integer.MIN_VALUE;
            int maxEntryVal = Integer.MIN_VALUE;

            for (HashMap.Entry<Integer, Integer> node : this.finishTime.entrySet()) {
                if (node.getValue() > maxEntryVal) {
                    maxEntryKey = node.getKey();
                    maxEntryVal = node.getValue();
                }

            }

            topSort.add(maxEntryKey);
            //remove entry once we've added it to top sort
            finishTimeCopy.remove(maxEntryVal);
        }

        return topSort;
    }
}
