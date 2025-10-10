package source.cspathfinder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveMaze {
    private final String fileName;
    public Node[][] nodeGrid;
    private final int pixelSize;

    //initialise the passed values
    public SaveMaze(Node[][] nodeGrid, int pixelSize, String fileName) {
        //set node grid
        this.nodeGrid = nodeGrid;
        //set pixel size
        this.pixelSize = pixelSize;
        //set file name
        this.fileName = fileName;
    }

    public void writeToFile() {
        try {
            // Create a new file if it doesn't exist
            if (!Files.exists(Path.of(fileName))) {
                Files.createFile(Path.of(fileName));
            }

            //open file with the name from input
            FileWriter fileWriter = new FileWriter(fileName);
            // Write the maze to the file
            fileWriter.write(pixelSize + System.lineSeparator());
            //iterate through each node
            for (int y = 0; y < nodeGrid[0].length; y++) {
                for (Node[] nodes : nodeGrid) {
                    //if wall node then output 1
                    if (nodes[y].getWallNode()) {
                        fileWriter.write("1");
                        //if not a wall node output 0
                    } else {
                        fileWriter.write("0");
                    }
                }
                //jump to the next line
                fileWriter.write(System.lineSeparator());
            }
            //clos the writer
            fileWriter.close();
            //catch any problems and print stack trace
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
