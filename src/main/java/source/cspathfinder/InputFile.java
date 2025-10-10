package source.cspathfinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InputFile {
    private final String fileName;
    public Node[][] nodeGrid;
    public int pixelSize1;

    //initialise the input file
    public InputFile(Node[][] nodeGrid, String fileName) {
        //set filename
        this.fileName = fileName;
        //set nodegrid
        this.nodeGrid = nodeGrid;
    }

    //method to read from the file
    public void readFromFile() {
        try {
            //read a maze from a file using buffer reader
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            // Read the first line to get the pixelSize
            pixelSize1 = Integer.parseInt(reader.readLine());

            String line;
            //line counter
            int y = 0;
            //keep on looping through the file and set wall on each 1 in the file
            while ((line = reader.readLine()) != null && y < nodeGrid[0].length) {
                //iterate through each 0 and 1 in the file line
                for (int x = 0; x < line.length() && x < nodeGrid.length; x++) {
                    //set wall node at the place where it read 1 in the file
                    nodeGrid[x][y].setWallNode(line.charAt(x) == '1');
                }
                //increment a line count
                y++;
            }
            //close a reader
            reader.close();
            //catch any errors
        } catch (IOException e) {
            //print the stack trace
            e.printStackTrace();
        }
    }
}
