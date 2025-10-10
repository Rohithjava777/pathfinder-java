package source.cspathfinder;

import java.util.*;

public class DFS {
    public final Node[][] nodeGrid;
    public Node startNode = null;
    public Node goalNode = null;
    public List<Node> finalPath = null;
    public Stack<Node> openSet;
    public Set<Node> neighbourList = null;
    public volatile boolean stopSearch = true;

    //initialisation of the passed variables
    public DFS(Node[][] nodeGrid, Node startNode, Node goalNode) {
        //initialise node grid
        this.nodeGrid = nodeGrid;
        //initialise the stack
        openSet = new Stack<>();
        //initialise start node
        this.startNode = startNode;
        //initialise the hash set
        neighbourList = new HashSet<>();
        //initialise goal node
        this.goalNode = goalNode;
    }

    //check if node is neighbour
    public boolean isNeighbour(int x, int y) {
        //initialise node
        Node node = nodeGrid[x][y];
        //return if node is neighbour
        return node.getVisited() && neighbourList.contains(node);
    }

    //method to check if path is set
    public boolean isPathSet() {
        return finalPath != null;
    }

    //method to check if node is on path
    public boolean isOnPath(int x, int y) {
        return isPathSet() && finalPath.contains(nodeGrid[x][y]);
    }

    //check if node is fully explored
    public boolean isNotFullyExploredNode(int x, int y) {
        //initialise node grid
        Node node = nodeGrid[x][y];
        //return if node is explored or not
        return neighbourList.contains(node) && !node.getVisited() && !node.getWallNode() && !node.isFullyExplored();
    }

    //method reset all nodes
    public void resetAll() {
        //iterate through nodes on node grid
        for (Node[] nodes : nodeGrid) {
            for (int y = 0; y < nodeGrid[0].length; y++) {
                //set nodes came from node to null
                nodes[y].setCameFromNode(null);
                //set node to visited false
                nodes[y].setVisited(false);
            }
        }
        //stop any algorithms
        stop();
    }

    //method to retrace the path from start node to the current node
    private List<Node> retracePath(Node goalNode) {
        //initialise the array list for the final path
        List<Node> path = new ArrayList<>();
        //set current node to the goal node
        Node currentNode = goalNode;
        // keep on looping until the current node is null
        while (currentNode != null) {
            //add current node to the array list
            path.add(currentNode);
            //set current node
            currentNode = currentNode.getCameFromNode();
        }
        //reverse the path
        Collections.reverse(path);
        //return the path
        return path;
    }

    //method to use before the start of the algorithm to reset all values
    public void start() {
        //reset values
        resetAll();
        //clear the linked list
        openSet.clear();
        //set distance from start to 0
        startNode.setDistanceFromStart(0);
        //add start node to the linked list
        openSet.add(startNode);
        //set stop search flag to false
        stopSearch = false;
    }

    //method to run the algorithm
    public void run() {
        //check if the stop search flag is false
        if (!stopSearch) {
            //check if linked list is empty
            if (!openSet.isEmpty()) {
                //pop a current node from the stack
                Node currentNode = openSet.pop();
                //iterate through the current nodes
                for (Node neighbour : currentNode.getNeighbourNodes()) {
                    // if the node is visited or is a wall node then continue( skip that node since it is either visited or a wall)
                    if (neighbour.getVisited() || neighbour.getWallNode()) continue;
                    //calculate the distance from current node to the goal node
                    int distance = currentNode.getDistanceFromStart() + 1;
                    //set distance to the neighbour
                    neighbour.setDistanceFromStart(distance);
                    //go to the next node
                    neighbour.setCameFromNode(currentNode);
                    //add neighbour to the linked list
                    openSet.push(neighbour);
                    //add neighbour to the neighbour hash set
                    neighbourList.add(neighbour);
                }
                //set current node to be visited
                currentNode.setVisited(true);
                //check if goal is reached
                if (currentNode.equals(goalNode)) {
                    //set stop search flag to true
                    stopSearch = true;
                    //retrace the path from the start node to the goal node
                    finalPath = retracePath(goalNode);
                }
            }
        }
    }

    //method to stop the algorithm
    public void stop() {
        //flag stop search is set no true since we want to stop the algorithm
        stopSearch = true;
        //clear the linked list
        openSet.clear();
        //clear the final path
        finalPath = null;
    }
}