package source.cspathfinder;

import java.util.*;

public class BFS {
    public final Node[][] nodeGrid;
    public Node startNode = null;
    public Node goalNode = null;
    public List<Node> finalPath = null;
    public Queue<Node> openSet;
    public Set<Node> neighbourList = null;
    public volatile boolean stopSearch = true;

    //initialisation of the passed variables
    public BFS(Node[][] nodeGrid, Node startNode, Node goalNode) {
        //set node grid to the passed node grid
        this.nodeGrid = nodeGrid;
        //intialise the linked list
        openSet = new LinkedList<>();
        //set start node to the passed start node
        this.startNode = startNode;
        //initialise the new hash set for the neighbour nodes
        neighbourList = new HashSet<>();
        //set goal node to the passed goal node
        this.goalNode = goalNode;
    }

    //checker method if the node is neighbour
    public boolean isNeighbour(int x, int y) {
        //translate the values to the grid prospective
        Node node = nodeGrid[x][y];
        //return if the node is neighbour
        return node.getVisited() && neighbourList.contains(node);
    }

    //method to check if the node is fully explored
    public boolean isNotFullyExploredNode(int x, int y) {
        //translate the values to the grid prospective
        Node node = nodeGrid[x][y];
        //return if the node is fully explored or not
        return neighbourList.contains(node) && !node.getVisited() && !node.getWallNode() && !node.isFullyExplored();
    }

    //method to check if the path is set
    public boolean isPathSet() {
        return finalPath != null;
    }

    //method to check if the current node is on the path
    public boolean isOnPath(int x, int y) {
        return isPathSet() && finalPath.contains(nodeGrid[x][y]);
    }

    //method to reset all the variable to the default
    public void resetAll() {
        //iterate through each node
        for (int x = 0; x < nodeGrid.length; x++) {
            for (int y = 0; y < nodeGrid[0].length; y++) {
                //set came from node to null
                nodeGrid[x][y].setCameFromNode(null);
                //set nodes to be unvisited
                nodeGrid[x][y].setVisited(false);
            }
        }
        //stop all the processes
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

    //method to run the breadth-first search algorithm
    public void run() {
        //check if the stop search flag is false
        if (!stopSearch) {
            //check if linked list is empty
            if (!openSet.isEmpty()) {
                //retrieve and remove the first element of Priority queue
                Node currentNode = openSet.poll();
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
                    openSet.add(neighbour);
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

