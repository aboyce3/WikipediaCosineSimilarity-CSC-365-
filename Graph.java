import java.io.Serializable;
import java.util.*;

public class Graph implements Serializable {
    ArrayList<WikiPage> allPages;
    ArrayList<Edge> allEdges;
    HashMap<WikiPage, ArrayList<WikiPage>> connectedComponents;
    HashMap<WikiPage, ArrayList<Edge>> edgeMap;
    HashMap<WikiPage, ArrayList<WikiPage>> pageMap = new HashMap<>();
    int numEdges;
    String mainURL;

    public Graph(ArrayList<WikiPage> all, int numEdges, HashMap<WikiPage, ArrayList<Edge>> edgeMap, String url) {
        allPages = all;
        mainURL = url;
        this.edgeMap = edgeMap;
        this.numEdges = numEdges;
        allEdges = new ArrayList<>();
        for(WikiPage p : allPages)
            if (p.edges != null) allEdges.addAll(p.edges);

    }

    public String dijkstraShortestPath(WikiPage start, WikiPage end) {

        //Initialize output
        String output = "";

        //Initialize HashMaps
        HashMap<WikiPage, WikiPage> changedAt = new HashMap<>();
        changedAt.put(start, null);
        HashMap<WikiPage, Double> shortestPathMap = new HashMap<>();

        for (WikiPage node : allPages) {
            //if node == start, add to the map with 0.0 distance, all else positive INF
            if (node == start)
                shortestPathMap.put(start, 0.0);
            else shortestPathMap.put(node, Double.POSITIVE_INFINITY);
        }

        for (Edge edge : start.edges) {
            //Add edge to the map of the shortest path and add to changed at
            shortestPathMap.put(edge.dest, edge.weight);
            changedAt.put(edge.dest, start);
        }

        //set start to visited
        start.setVisited(true);

        while (true) {
            //current node = the closest node in the path map
            WikiPage currentNode = closestUnvisited(shortestPathMap);

            //if current node == null then there isn't a path
            if (currentNode == null)
                return "There isn't a path from " + start.URL.substring(30) + " to " + end.URL.substring(30);

            if (currentNode == end) {
                output += "The path with the smallest weight from "
                        + start.URL.substring(30) + " to " + end.URL.substring(30) + " is: \n";
                WikiPage child = end;
                String path = end.URL.substring(30) + (end.medioid ? "(Medioid)" : "");

                while (true) {

                    //runs until nothing is left in the path
                    WikiPage parent = changedAt.get(child);
                    if (parent == null) {
                        break;
                    }

                    //append to ouput the path and then set child = to the parent node
                    path = parent.URL.substring(30) + (parent.medioid ? "(Medioid)" : "") + " -> " + path;
                    child = parent;
                }

                //appends the path to our output
                output += path + "\n";

                //Returns the cost of the path (add the cosine similarity together)
                return output + ("The path costs: " + shortestPathMap.get(end));
            }

            //Set current node to visited
            currentNode.setVisited(true);

            //Iterate through edges
            for (Edge edge : currentNode.edges) {

                //if visited then skip
                if (edge.dest.isVisited()){}

                //if the weight of the current node and the edges weight combined is less than the edges destination weight,
                //add the edges destination to the shortest path map with a weight of the current nodes + the edges
                //weight
                else if (shortestPathMap.get(currentNode) + edge.weight < shortestPathMap.get(edge.dest)) {
                    shortestPathMap.put(edge.dest, shortestPathMap.get(currentNode) + edge.weight);
                    changedAt.put(edge.dest, currentNode);
                }
            }
        }
    }

    private WikiPage closestUnvisited(HashMap<WikiPage, Double> shortestPathMap) {

        //set short distance to INF to start
        double shortestDistance = Double.POSITIVE_INFINITY;

        //initialize the output
        WikiPage closestReachableNode = null;
        for (WikiPage node : allPages) {

            //If visited already, continue
            if (node.isVisited())
                continue;

            //set current distance equal to the visiting nodes distance
            double currentDistance = shortestPathMap.get(node);

            //if INF then continue
            if (currentDistance == Double.POSITIVE_INFINITY)
                continue;

            //if current distance is less than the shortest distance then set the shortest distance equal to the current
            // distance and set the closest reachable node equal to the node that we're viewing in the loop
            if (currentDistance < shortestDistance) {
                shortestDistance = currentDistance;
                closestReachableNode = node;
            }
        }
        return closestReachableNode;
    }

    public boolean hasPath(WikiPage start, WikiPage end) {

        HashMap<WikiPage, WikiPage> changedAt = new HashMap<>();
        changedAt.put(start, null);
        HashMap<WikiPage, Double> shortestPathMap = new HashMap<>();

        for (WikiPage node : allPages) {
            //if node == start, add to the map with 0.0 distance, all else positive INF
            if (node == start)
                shortestPathMap.put(start, 0.0);
            else shortestPathMap.put(node, Double.POSITIVE_INFINITY);
        }

        for (Edge edge : start.edges) {
            //Add edge to the map of the shortest path and add to changed at
            shortestPathMap.put(edge.dest, edge.weight);
            changedAt.put(edge.dest, start);
        }

        //set start to visited
        start.setVisited(true);

        while (true) {
            //current node = the closest node in the path map
            WikiPage currentNode = closestUnvisited(shortestPathMap);

            //if current node == null then there isn't a path
            if (currentNode == null)
                return false;

            if (currentNode == end) {
                WikiPage child = end;

                while (true) {

                    //runs until nothing is left in the path
                    WikiPage parent = changedAt.get(child);
                    if (parent == null) {
                        break;
                    }

                    //append to ouput the path and then set child = to the parent node
                    child = parent;
                }

                //Returns the cost of the path (add the cosine similarity together)
                return true;
            }

            //Set current node to visited
            currentNode.setVisited(true);

            //Iterate through edges
            for (Edge edge : currentNode.edges) {

                //if visited then skip
                if (edge.dest.isVisited()){}

                //if the weight of the current node and the edges weight combined is less than the edges destination weight,
                //add the edges destination to the shortest path map with a weight of the current nodes + the edges
                //weight
                else if (shortestPathMap.get(currentNode) + edge.weight < shortestPathMap.get(edge.dest)) {
                    shortestPathMap.put(edge.dest, shortestPathMap.get(currentNode) + edge.weight);
                    changedAt.put(edge.dest, currentNode);
                }
            }
        }
    }

    public int numDisjoints(String s){
        WikiPage main = null;
        for(WikiPage p : allPages)
            if(p.URL.equals(s)){
                main = p;
                break;
            }
        pageMap.put(main, new ArrayList<>());
        for(int i = 0; i < allPages.size()-1;i++){
            if(allPages.get(i) != main){
               if(hasPath(main,allPages.get(i))){
                   pageMap.get(main).add(allPages.get(i));
                   resetVisited();
               }
            }
        }
        return pageMap.size();
        }

    public void resetVisited(){
        for(WikiPage p : allPages)
            p.setVisited(false);
    }
}
