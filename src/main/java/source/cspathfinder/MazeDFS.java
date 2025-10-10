package source.cspathfinder;

import java.util.List;
import java.util.Stack;

public class MazeDFS {
    private final Node[][] nodeGrid;
    private final Stack<Node> stack;
    private final int width;
    private final int height;

    //initialise the maze dfs generator
    public MazeDFS(int width, int height) {
        //set width
        this.width = width;
        //set height
        this.height = height;
        //set a node grid
        nodeGrid = new Node[width][height];
        //initialise a stack
        stack = new Stack<>();
        //iterate through each node on the grid
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //set node
                nodeGrid[x][y] = new Node(x, y);
                //set wall node
                nodeGrid[x][y].setWallNode(true);
            }
        }
        //generate a maze starting from 0, 0 position
        generateMaze(0,0);
    }

    //method to generate a maze
    private void generateMaze(int x, int y) {
        //set current node to be visited
        nodeGrid[x][y].setVisitedMaze(true);
        //remove a wall node
        nodeGrid[x][y].setWallNode(false);

        //initialise a list of neighbours
        List<Node> neighbors = List.of(getUnvisitedNeighbors(x, y));

        //keep on looping until no neighbours left
        while (!neighbors.isEmpty()) {
            //select next done on a random
            Node next = neighbors.get((int) (Math.random() * neighbors.size()));
            //get x coordinate of node
            int nextX = next.getX();
            //get y coordinate of node
            int nextY = next.getY();

            //check if the next node is not visited
            if (!next.isVisitedMaze()) {
                //remove a wall node
                nodeGrid[(x + nextX) / 2][(y + nextY) / 2].setWallNode(false);
                //using recursion proceed to next node
                generateMaze(nextX, nextY);
            }
            //update a list of neighbours
            neighbors = List.of(getUnvisitedNeighbors(x, y));
        }
    }

    //method to get unvisited node
    private Node[] getUnvisitedNeighbors(int x, int y) {
        //set 4 neighbours
        Node[] neighbors = new Node[4];
        //count to 0
        int count = 0;

        //
        if (x > 1 && !nodeGrid[x - 2][y].getVisitedMaze()) {
            neighbors[count++] = nodeGrid[x - 2][y];
        }

        if (x < nodeGrid.length - 2 && !nodeGrid[x + 2][y].getVisitedMaze()) {
            neighbors[count++] = nodeGrid[x + 2][y];
        }

        if (y > 1 && !nodeGrid[x][y - 2].getVisitedMaze()) {
            neighbors[count++] = nodeGrid[x][y - 2];
        }

        if (y < nodeGrid[0].length - 2 && !nodeGrid[x][y + 2].getVisitedMaze()) {
            neighbors[count++] = nodeGrid[x][y + 2];
        }

        Node[] result = new Node[count];
        System.arraycopy(neighbors, 0, result, 0, count);

        return result;

    }

    //getter for a node grid
    public Node[][] getNodeGrid() {
        return nodeGrid;
    }
}

