package source.cspathfinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class Node {

    private final int x;
    private final int y;
    private Node cameFromNode = null;
    private int fCost;
    private int hCost;
    private int gCost;
    private int distanceFromStart;
    private boolean visited;
    private boolean visitedMaze;
    private final List<Node> neighbourNode;
    private boolean fullyExplored;
    private boolean wallNode;

    //Initialise node class
    public Node(int x, int y) {
        //initialise x values
        this.x = x;
        //initialise y values
        this.y = y;
        //set h cost to positive infinity
        this.hCost = (int) Double.POSITIVE_INFINITY;
        //set g cost to positive infinity
        this.gCost = (int) Double.POSITIVE_INFINITY;
        //set f cost to positive infinity
        this.fCost = (int) Double.POSITIVE_INFINITY;
        //set neighbour node to arraylist, with set capacity of 4
        this.neighbourNode = new ArrayList<>(4);
        //set wall node to false
        this.wallNode = false;
        //set fully explored to false
        fullyExplored = false;
    }

    //get x coordinates
    public int getX(){return x;}
    //get y coordinates
    public int getY(){return y;}

    //Get h(n) values
    public int getHCost(){return hCost;}
    //Get g(n) values
    public int getGCost(){return gCost;}
    //Get f(n) values
    public int getFCost(){return fCost;}

    //Set h(n) values
    public void setHCost(int hCost){this.hCost = hCost;}
    //Set g(n) values
    public void setGCost(int gCost){this.gCost = gCost;}
    //Set f(n) values
    public void setFCost(int fCost){this.fCost = fCost;}

    //Used in aStar.isWallNode for checking is the Node is wall
    public boolean getWallNode(){return wallNode;}

    //Set Wall Node
    public void setWallNode(boolean wallNode){this.wallNode = wallNode;}
    //set came from node
    public void setCameFromNode(Node cameFromNode){this.cameFromNode = cameFromNode;}
    //get came from node
    public Node getCameFromNode(){return cameFromNode;}
    //getter for fully explored node
    public boolean isFullyExplored() {
        return fullyExplored;
    }
    //set visited for a maze
    public void setVisitedMaze(boolean visitedMaze) {
        this.visitedMaze = visitedMaze;
    }
    //set visited for a maze
    public boolean isVisitedMaze() {return visitedMaze;}
    //set visited
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    //getter for a node

    public Node getNode() {
        return this;
    }

    //getter for a visited node in maze
    public boolean getVisitedMaze(){return visitedMaze;}
    //getter for a visited node
    public boolean getVisited() {
        return visited;
    }
    //set distance from a start to goal
    public void setDistanceFromStart(int distance) {
        this.distanceFromStart = distance;
    }
    //getter for a distance from start to goal
    public int getDistanceFromStart() {
        return distanceFromStart;
    }
    //set neighbour nodes
    public void setNeighbourNodes(int width, int height, Node[][] grid) {
        //
        if (y > 0) neighbourNode.add(grid[x][y - 1]);
        if (x < width - 1) neighbourNode.add(grid[x + 1][y]);
        if (y < height - 1) neighbourNode.add(grid[x][y + 1]);
        if (x > 0) neighbourNode.add(grid[x - 1][y]);
    }
    //getter of neighbour nodes
    public List<Node> getNeighbourNodes(){return neighbourNode;}

    //node equals
    @Override
    public boolean equals(Object o) {
        //check if node is equal to object
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node nodes = (Node) o;
        //check if nodes are equal to each other
        return x == nodes.x &&
                y == nodes.y &&
                fCost == nodes.fCost &&
                gCost == nodes.gCost &&
                hCost == nodes.hCost &&
                Objects.equals(cameFromNode, nodes.cameFromNode) &&
                Objects.equals(neighbourNode, nodes.neighbourNode) &&
                Objects.equals(wallNode, nodes.wallNode);
    }

}