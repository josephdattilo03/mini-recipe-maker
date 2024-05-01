import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class Graph {
    /*------------------------------
      FIELDS
      ------------------------------*/
    private HashMap<Square, ArrayList<Square>> graph;
    private LinkedList<Square> stack;
    private ArrayList<Square> discovered;
    private HashMap<Square, Boolean> seen;
    private HashMap<Square, Boolean> seeing;
    private HashMap<Square, Integer> startTime;
    private HashMap<Square, Integer> finishTime;
    private boolean cyclic = false;
    private int currTime = 0;
    boolean hasImage;

    /*------------------------------
      METHODS
      ------------------------------*/

    // Constructor
    public Graph(HashMap<Square, ArrayList<Square>> graph, Square source) {
        this.graph = graph;
        this.discovered = new ArrayList<>();
        this.stack = new LinkedList<>();
        this.seen = new HashMap<>();
        this.seeing = new HashMap<>();
        this.startTime = new HashMap<>();
        this.finishTime = new HashMap<>();
        this.hasImage = false;

        for (Square key : this.graph.keySet()) {
            this.seen.put(key, false);
            this.seeing.put(key, false);
        }

        helper(source);

        for (Square done : this.graph.keySet()) {
            if (!this.seen.get(done)) {
                helper(done);
            }
        }

        if (this.cyclic) {
            throw new IllegalArgumentException("Information given contains a cycle");
        }

    }

    private void helper(Square top) {
        this.stack.add(top);

        while (!this.stack.isEmpty()) {
            Square newTop = this.stack.peekLast();

            if (!this.seen.get(newTop)) {
                this.seen.put(newTop, true);
                this.seeing.put(newTop, true);
                this.currTime++;
                this.startTime.put(newTop, this.currTime);
            }

            boolean allNeighTrav = true;
            for (Square neigh : this.graph.get(newTop)) {
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

    public ArrayList<Square> getDiscovered() {
        return this.discovered;
    }

    private static void downloadImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();

        // write to output buffer
        try (InputStream in = connection.getInputStream();
             FileOutputStream out = new FileOutputStream(destinationFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    public void txtFile(String title, String description) {
        ArrayList<Square> topSort = topSort();
        File file = new File( title.toLowerCase().replace(" ", "")+ ".txt");



        try (PrintWriter pWriter = new PrintWriter(file)) {
            pWriter.println(title);
            pWriter.println("\n\n");
            pWriter.println(description);
            pWriter.println("\n\n Steps:\n\n");

            for (Square item : topSort) {
                pWriter.println(item.getTitle());
                pWriter.println(item.getDescription());
                pWriter.println("\n\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            String searchSimplyURL = "https://www.simplyrecipes.com/search?q="
                    + URLEncoder.encode(title, StandardCharsets.UTF_8.toString());
            System.out.println(URLEncoder.encode(title, StandardCharsets.UTF_8.toString()));
            Document doc = Jsoup.connect(searchSimplyURL).get();
            Elements results = doc.getElementsByClass("card-list__item");

            for (Element result : results) {
                System.out.println(result.text());
                if (result.text().toLowerCase().contains(title.toLowerCase())) {
                    Elements img = result.getElementsByClass("card__image");
                    if (img.size() > 0) {
                        String imageURL = img.first().attr("data-src");
                        downloadImage(imageURL, title.toLowerCase().replace(" ", "-") + ".jpg");
                        this.hasImage = true;
                        break;
                    }
                }
            }




        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Square> getTopSort() {
        return topSort();
    }



    private ArrayList<Square> topSort() {
        if (this.cyclic) {
            throw new IllegalArgumentException("Information given contains a cycle");
        }

        HashMap<Square, Integer> finishTimeCopy = new HashMap<>(this.finishTime);
        ArrayList<Square> topSort = new ArrayList<>();

        while (!finishTimeCopy.isEmpty()) {
            Square maxEntryKey = null;
            int maxEntryVal = Integer.MIN_VALUE;

            for (Map.Entry<Square, Integer> node : finishTimeCopy.entrySet()) {
                if (node.getValue() > maxEntryVal) {
                    maxEntryKey = node.getKey();
                    maxEntryVal = node.getValue();
                }
            }

            topSort.add(maxEntryKey);
            finishTimeCopy.remove(maxEntryKey);
        }

        return topSort;
    }
    public boolean graphHasImage() {
        return this.hasImage;
    }
}
