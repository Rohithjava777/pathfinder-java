package source.cspathfinder;

import java.util.*;

public class aStar {

    public final Node[][] nodeGrid;
    public Node startNode = null;
    public Node goalNode = null;
    public static List<Node> finalPath = null;
    public PriorityQueue<Node> openSet ;
    public static List<Node> neighbourList = null;
    public volatile boolean stopSearch = true;

    //initialisation of the variables
    public aStar(Node[][] nodeGrid, Node startNode, Node goalNode){
        // set nodegrid to the passed node grid
        this.nodeGrid = nodeGrid;
        //initialise the priority queue
        openSet = new PriorityQueue<>(Comparator.comparing(Node::getFCost));
        //initialise the array list
        neighbourList = new ArrayList<>();
        // set startNode to the passed node grid
        this.startNode = startNode;
        // set goalNode to the passed node grid
        this.goalNode = goalNode;
    }

    //getter method for the start node
    public Node getStartNode(){return startNode;}

    //getter method for the goal node
    public Node getGoalNode(){return goalNode;}

    //checker if the path is set
    public boolean isPathSet() {return finalPath != null;}

    //checker of the node present on the path
    public boolean isOnPath(int x, int y) {return isPathSet() && finalPath.contains(nodeGrid[x][y]);}

    //checker if the node is a neighbour node to the current node
    public boolean isNeighbour(int x, int y) {return  neighbourList.contains(nodeGrid[x][y]);}

    //method to reset all the values to default
    public void resetAll(){
        //iterate through every node on the grid
        for (int x = 0; x < getGraphWidth(); x++) {
            for (int y = 0; y < getGraphHeight(); y++) {
                //set f cost to positive infinity
                nodeGrid[x][y].setFCost((int) Double.POSITIVE_INFINITY);
                //set g cost to positive infinity
                nodeGrid[x][y].setGCost((int) Double.POSITIVE_INFINITY);
                //set h cost to distance between current node and goal node
                nodeGrid[x][y].setHCost(Math.abs(x - goalNode.getX()) + Math.abs(y - goalNode.getY()));
                //set starting goal node to null
                nodeGrid[x][y].setCameFromNode(null);
            }
        }
        //stop all the processes
        stop();
    }

    //getter method for the grid width
    private int getGraphWidth() {
        return nodeGrid.length;
    }

    //getter method for the grid height
    private int getGraphHeight() {
        return nodeGrid[0].length;
    }

    //method to retrace the path from start to the goal node
    private List<Node> retracePath(Node goalNode) {
        //initialise new array list for the final path
        List<Node> path = new ArrayList<>();
        //set current node to the goal node
        Node currentNode = goalNode;
        //while current node is not null keep on looping
        while (currentNode != null) {
            //add current node to the path
            path.add(currentNode);
            //set current node to the next node
            currentNode = currentNode.getCameFromNode();
        }
        //reverse path
        Collections.reverse(path);
        //return path
        return path;
    }

    //method used to reset all the values to default
    public void start() {
        //reset all values
        resetAll();
        //add start node to the priority queue
        openSet.add(startNode);
        //set the stop search flag to false
        stopSearch = false;
    }

    //method to run the a star algorithm
    public void run() {
        //check if the stop search flag true then don't run the algorithm
        if(!stopSearch){
            //check if openset is empty. If not don't run the algorithm
            if (!openSet.isEmpty()) {
                //retrieve and remove the first element of Priority queue
                Node currentNode = openSet.poll();
                //clear the neighbours List
                neighbourList.clear();

                //iterate through the current nodes
                for (Node neighbour : currentNode.getNeighbourNodes() ) {
                    // if the node is visited or is a wall node then continue( skip that node since it is either visited or a wall)
                    if (neighbour.getVisited() || neighbour.getWallNode()) continue;
                    //calculate the tentative g cost for the a star algorithm
                    int tentativeGCost = currentNode.getGCost() + calculateDistance(neighbour.getX(), neighbour.getY(), currentNode);
                    //if a tentative g cost is better than the actual g cost of a neighbour then change node
                    if (tentativeGCost < neighbour.getGCost()) {
                        //change the node
                        neighbour.setCameFromNode(currentNode);
                        //change a g cost to the tentative g cost
                        neighbour.setGCost(tentativeGCost);
                        //calculate the f cost for the current node
                        neighbour.setFCost(currentNode.getGCost() + neighbour.getHCost());

                        if (!openSet.contains(neighbour)) {
                            neighbourList.add(neighbour);
                            openSet.add(neighbour);
                        }
                    }
                }
                //set current node to a visited
                currentNode.setVisited(true);
                //iff a goal node is reached draw a path
                if (currentNode.equals(goalNode)){
                    //pass the final path from start to goal node to draw it
                    finalPath = retracePath(goalNode);
                    //set flag to true since the goal node has been reached
                    stopSearch = true;
                }

            }
        }
    }

    //method to reset all the values and stop the algorithm if needed
    public void stop() {
        //set the stop search flag to true to stop search
        stopSearch = true;
        //clear the priority queue
        openSet.clear();
        //clear the list of neighbours
        neighbourList.clear();
        //reset the final path
        finalPath = null;
    }

    //method that is used to calculate the distance between two nodes using pythagorean theorem
    public static int calculateDistance(int x, int y, Node goalNode){
        //using the formula to calculate ( sqrt(x^2 + y^2) )
        return (int) Math.round(Math.sqrt((Math.pow((goalNode.getX() - x),2) + (Math.pow((goalNode.getY() - y),2)))));
    }
}


