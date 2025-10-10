package source.cspathfinder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Grid {
    public Node[][] nodeGrid;
    public Node startNode;
    public Node goalNode;
    public PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparing(Node::getFCost));
    public volatile boolean stopSearch = false;
    int widthArray, heightArray;
    private aStar astar;
    public List<Node> finalPath1 = null;
    public List<Node> neighbourList1 = null;

    //intialise the grid class
    public Grid(int width, int height) {
        //set node grid
        this.nodeGrid = new Node[width][height];
        //set a priority queue
        openSet = new PriorityQueue<>(Comparator.comparing(Node::getFCost));
        //set an array list
        neighbourList1 = new ArrayList<>();

        //iterate through each node on the frid
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //place on each x and y coordinates a new node
                nodeGrid[x][y] = new Node(x, y);
            }
        }

    }

    //method to initialise the grid nodes
    public void initialiseGridNodes(Node[][] nodeGrid){
        //set the node grid
        this.nodeGrid = nodeGrid;
        //set the width array
         widthArray = nodeGrid.length;
         //set the array list
        neighbourList1 = new ArrayList<>();
        //set the height array
         heightArray = nodeGrid[0].length;
         //iterate through each node
        for (Node[] nodes : nodeGrid) {
            for (int y = 0; y < heightArray; y++) {
                //set neighbour nodes
                nodes[y].setNeighbourNodes(widthArray, heightArray, nodeGrid);
            }
        }
    }

    //method to set a start node
    public void SetStartNode(int x, int y){
        startNode = nodeGrid[x][y];
        //add open node to the priority queue
        openSet.add(startNode);
    }

    public void removeStartNode(){
        //remove start node from priority queue
        openSet.remove(startNode);
        //set start node to null
        startNode = null;
    }

    //set goal node
    public void SetGoalNode(int x, int y) {
        goalNode = nodeGrid[x][y];
    }

    //remove goal node
    public void removeGoalNode(){
        goalNode = null;
    }

    //method to set a wall node
    public void setWallNode (int x, int y, boolean wallNode){nodeGrid[x][y].setWallNode(wallNode);}

    //method to get a start node
    public Node getStartNode(){return startNode;}

    //method to get a goal node
    public Node getGoalNode(){return goalNode;}

    //method to get a wall node
    public boolean getWallNode(int x, int y){return nodeGrid[x][y].getWallNode();}

    //method to check if node is a start node
    public boolean isStartNode(int x, int y){return nodeGrid[x][y].equals(startNode);}

    //method to check if node is a goal node
    public boolean isGoalNode(int x, int y){return nodeGrid[x][y].equals(goalNode);}

    //method to check is node is a wall node
    public boolean isWallNode(int x, int y){return nodeGrid[x][y].getWallNode();}

    //method to check if node is in grid
    public boolean isInGrid(int x, int y) {
        return x < getGraphWidth() && y < getGraphHeight() && x >= 0 && y >= 0;
    }

    //method to get a graph width
    public int getGraphWidth() {
        return nodeGrid.length;
    }

    //method to get a grapg height
    public int getGraphHeight() {
        return nodeGrid[0].length;
    }

    //method to check if a path is set
    public boolean isPathSet() {return finalPath1 != null;}

    //method to check if node is on the path
    public boolean isOnPath(int x, int y) {return isPathSet() && finalPath1.contains(nodeGrid[x][y]);}

    //method to check if node is a neighbour node
    public boolean isNeighbour(int x, int y) {return  neighbourList1.contains(nodeGrid[x][y]);}

    //method to get a node grid
    public Node[][] getNodeGrid() {
        return nodeGrid;
    }

}
