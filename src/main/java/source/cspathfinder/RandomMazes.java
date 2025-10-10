package source.cspathfinder;
import java.util.Random;

public class RandomMazes {

    private Node[][] nodeGrid;

    //method to create random mazes
    public RandomMazes(int width, int height) {
        //initialise the node grid
        nodeGrid = new Node[width][height];
        //iterate through every node
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //place nodes
                nodeGrid[x][y] = new Node(x, y);
            }
        }

        Random rand = new Random();
        //creating random walls, iterate through nodes
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //if the double is below 0.4
                if (rand.nextDouble() < 0.4 ) {
                    //place a wall node
                    nodeGrid[x][y].setWallNode(true);
                }
            }
        }
    }

    //getter for the node grid
    public Node[][] getNodeGrid() {
        return nodeGrid;
    }
}




