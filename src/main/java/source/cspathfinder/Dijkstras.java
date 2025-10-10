package source.cspathfinder;

import java.util.*;

public class Dijkstras {
    public final Node[][] nodeGrid;
    public Node startNode = null;
    public Node goalNode = null;
    public List<Node> finalPath = null;
    public PriorityQueue<Node> openSet;
    public Set<Node> closedSet;
    public volatile boolean stopSearch = true;

    //initialisation of the passed variables
    public Dijkstras(Node[][] nodeGrid, Node startNode, Node goalNode) {
        //initialise the node grid
        this.nodeGrid = nodeGrid;
        //initialise the priority queue
        openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getDistanceFromStart));
        //initialise the start node
        this.startNode = startNode;
        //initialise the hash set
        closedSet = new HashSet<>();
        //initialise the goal node
        this.goalNode = goalNode;
    }

    //checker method if the node is neighbour
    public boolean isNeighbour(int x, int y) {return  openSet.contains(nodeGrid[x][y]);}

    //method to check if path is set
    public boolean isPathSet() {
        return finalPath != null;
    }

    //method to check if node is on path
    public boolean isOnPath(int x, int y) {
        return isPathSet() && finalPath.contains(nodeGrid[x][y]);
    }

    //method to reset all the variable to the default
    public void resetAll() {
        //iterate through each node
        for (Node[] nodes : nodeGrid) {
            for (int y = 0; y < nodeGrid[0].length; y++) {
                //set came from node to null
                nodes[y].setCameFromNode(null);
                //set nodes to be unvisited
                nodes[y].setVisited(false);
                //set distance from start to a max value
                nodes[y].setDistanceFromStart(Integer.MAX_VALUE);
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
        openSet.offer(startNode);
        //set stop search flag to false
        stopSearch = false;
    }

    //method to run the algorithm
    public void run() {
        //check if the stop search flag is false
        if (!stopSearch) {
            //check of priority queue is empty
            if (!openSet.isEmpty()) {
                //set current node to last node from priority queue also remove it from there
                Node currentNode = openSet.poll();
                //check if goal node is reached
                if (currentNode.equals(goalNode)) {
                    //set stop search flag to true
                    stopSearch = true;
                    //set final path
                    finalPath = retracePath(goalNode);
                    return;
                }
                //iterate through neighbour nodes of the grid
                for (Node neighbour : currentNode.getNeighbourNodes()) {
                    //if neighbour is a wall then continue
                    if (neighbour.getWallNode()) continue;
                    //calculate the tentative distance from current node to the goal node
                    int distance = currentNode.getDistanceFromStart() + 1;
                    //if tentative distance is smaller than actual distance from start node to goal
                    if (distance < neighbour.getDistanceFromStart()) {
                        //change distance to current node
                        neighbour.setDistanceFromStart(distance);
                        //change the node to current
                        neighbour.setCameFromNode(currentNode);
                        //check if priority queue does not have neighbour
                        if (!openSet.contains(neighbour)) {
                            //add neighbour to priority queue
                            openSet.offer(neighbour);
                        } else {
                            //remove neighbour from priority queue
                            openSet.remove(neighbour);
                            //add next neighbour to priority queue
                            openSet.offer(neighbour);
                        }
                    }
                }
                //add current node to the hash set
                closedSet.add(currentNode);
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
