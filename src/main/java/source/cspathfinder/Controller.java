package source.cspathfinder;

import java.awt.Desktop;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable {

    @FXML
    private Canvas canvas;
    @FXML
    private TextField gridInput;
    @FXML
    private TextField gridInput1;
    @FXML
    private ComboBox<String> algCombo;
    @FXML
    private ComboBox<String> mazesCombo;
    @FXML
    private Slider animationSpeedSlider;
    int animationSpeed = 15;
    private GraphicsContext gc;
    public static int pixelSize = 1;
    public  int widthOfWindow;
    public  int heightOfWindow;
    private aStar aStarAlgorithm;
    private BFS bfs;
    private DFS dfs;
    private Grid grid;
    private Dijkstras dijkstra;
    private String buttonVal = "";
    private final String[] algs = {"A star", "Dijkstra", "BFS", "DFS"};
    private final String[] mazes = {"MazeDFS", "Random Mazes"};
    private boolean isStartNodePlaced = false;
    private boolean isGoalNodePlaced = false;
    public Node StartNode;
    public Node GoalNode;
    public SaveMaze savemaze;
    public InputFile uploadmaze;

    //initialise the controller for the GUI, using overriding
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //initialise the grid
        init();
        //add choice of algorithms to the drop-down menu
        algCombo.getItems().addAll(algs);
        //add choice of mazes to the drop-down menu
        mazesCombo.getItems().addAll(mazes);
    }

    //method used to render the grid whenever anything changes
    private void renderAlg() {
        //if user chose the A star option
        if (algCombo.getValue().equals("A star")){
            //iterate through each node of the grid
            for (int x = 0; x < widthOfWindow; x++) {
                for (int y = 0; y < heightOfWindow; y++) {
                    //if the node is start node fill the grid box to green colour
                    if (grid.isStartNode(x, y)) gc.setFill(Color.GREEN);
                    //if the node is goal node fill the grid box to red colour
                    else if (grid.isGoalNode(x, y)) gc.setFill(Color.RED);
                    //if the node is path node fill the grid box to light sky blue colour
                    else if (aStarAlgorithm.isOnPath(x, y)) gc.setFill(Color.LIGHTSKYBLUE);
                    //if the node is neighbour node fill the grid box to orange colour
                    else if (aStarAlgorithm.isNeighbour(x, y)) gc.setFill(Color.ORANGE);
                    //if the node is start node fill the grid box to green colour
                    else continue;
                    //make every node a rectangle
                    gc.fillRect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
                }
            }
        }
        //if user chose the BFS option
        else if (algCombo.getValue().equals("BFS")){
            //iterate through each node of the grid
            for (int x = 0; x < widthOfWindow; x++) {
                for (int y = 0; y < heightOfWindow; y++) {
                    //if the node is start node fill the grid box to green colour
                    if (grid.isStartNode(x, y)) gc.setFill(Color.GREEN);
                    //if the node is goal node fill the grid box to red colour
                    else if (grid.isGoalNode(x, y)) gc.setFill(Color.RED);
                    //if the node is not fully explored node fill the grid box to medium purple colour
                    else if (bfs.isNotFullyExploredNode(x, y)) gc.setFill(Color.MEDIUMPURPLE);
                    //if the node is path node fill the grid box to light sky blue colour
                    else if (bfs.isOnPath(x, y)) gc.setFill(Color.LIGHTSKYBLUE);
                    //if the node is neighbour node fill the grid box to orange colour
                    else if (bfs.isNeighbour(x, y)) gc.setFill(Color.ORANGE);
                    //if the node is start node fill the grid box to green colour
                    else continue;
                    //make every node a rectangle
                    gc.fillRect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
                }
            }

        }
        //if user chose the DFS option
        else if (algCombo.getValue().equals("DFS")){
            //iterate through each node of the grid
            for (int x = 0; x < widthOfWindow; x++) {
                for (int y = 0; y < heightOfWindow; y++) {
                    //if the node is start node fill the grid box to green colour
                    if (grid.isStartNode(x, y)) gc.setFill(Color.GREEN);
                    //if the node is goal node fill the grid box to red colour
                    else if (grid.isGoalNode(x, y)) gc.setFill(Color.RED);
                    //if the node is not fully explored node fill the grid box to medium purple colour
                    else if (dfs.isNotFullyExploredNode(x, y)) gc.setFill(Color.MEDIUMPURPLE);
                    //if the node is path node fill the grid box to light sky blue colour
                     if (dfs.isOnPath(x, y)) gc.setFill(Color.LIGHTSKYBLUE);
                     //if the node is neighbour node fill the grid box to orange colour
                    else if (dfs.isNeighbour(x, y)) gc.setFill(Color.ORANGE);
                    //if the node is start node fill the grid box to green colour
                    else continue;
                    //make every node a rectangle
                    gc.fillRect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
                }
            }

        }
        //if user chose the Dijkstra option
        else if (algCombo.getValue().equals("Dijkstra")){
            //iterate through each node of the grid
            for (int x = 0; x < widthOfWindow; x++) {
                for (int y = 0; y < heightOfWindow; y++) {
                    //if the node is path node fill the grid box to light sky blue colour
                    if (dijkstra.isOnPath(x, y)) gc.setFill(Color.LIGHTSKYBLUE);
                    //if the node is neighbour node fill the grid box to orange colour
                    else if (dijkstra.isNeighbour(x, y)) gc.setFill(Color.ORANGE);
                    else continue;
                    //make every node a rectangle
                    gc.fillRect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
                    //if the node is start node fill the grid box to green colour
                    if (grid.isStartNode(x, y)) gc.setFill(Color.GREEN);
                    //if the node is goal node fill the grid box to red colour
                    else if (grid.isGoalNode(x, y)) gc.setFill(Color.RED);
                }
            }
        }
    }

    //method to reset the graph to default
    private void resetGraph() {
        //iterate through each node of the grid
        for (int x = 0; x < widthOfWindow; x++) {
            for (int y = 0; y < heightOfWindow; y++) {
                //if the node is wall node then fill the grid box to black colour
                if (grid.isWallNode(x, y)) gc.setFill(javafx.scene.paint.Color.BLACK);
                //if the node is start node fill the grid box to green colour
                else if (grid.isStartNode(x, y)) gc.setFill(javafx.scene.paint.Color.GREEN);
                //if the node is goal node fill the grid box to red colour
                else if (grid.isGoalNode(x, y)) gc.setFill(javafx.scene.paint.Color.RED);
                //if the node is anything else them fill the white colour
                else gc.setFill(javafx.scene.paint.Color.WHITE);
                //make every node a rectangle
                gc.fillRect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
            }
        }
        //if the node is wall node then fill the grid box to black colour
        gc.setFill(Color.BLACK);

        for (int x = 0; x < canvas.getWidth(); x += pixelSize) {
            gc.strokeLine(x, 0, x, canvas.getHeight());
        }
        for (int y = 0; y < canvas.getHeight(); y += pixelSize) {
            gc.strokeLine(0, y, canvas.getWidth(), y);
        }
    }

    //method to maze selector
    public void mazeChoice(){
        //set button value to mazechoice
        buttonVal = "mazeChoice";
        //check if the input is not null
        if (mazesCombo.getValue() != null) {
            //check if the choice is random maze
            if (mazesCombo.getValue().equals("Random Mazes")) {
                //clear grid
                clearGrid();
                //change the grid to the new grid with wall nodes representing a maze
                grid.initialiseGridNodes(new RandomMazes(widthOfWindow, heightOfWindow).getNodeGrid());
                //reset a graph
                resetGraph();

            } //check if the choice is DFS maze
            else if (mazesCombo.getValue().equals("MazeDFS")) {
                //clear the grid
                clearGrid();
                //change the grid to the new grid with wall nodes representing a maze
                grid.initialiseGridNodes(new MazeDFS(widthOfWindow, heightOfWindow).getNodeGrid());
                //reset the grid
                resetGraph();
            }
        }
        //if the input is null show an error message
        else {
            //error message initialiser
            Alert alert = new Alert(Alert.AlertType.ERROR);
            //set title
            alert.setTitle("Error");
            //set header text
            alert.setHeaderText("Value is either incorrect or not put into text field");
            //set text of the main content
            alert.setContentText("Please input correct value into a text field");
            //show alert and wait
            alert.showAndWait();
        }
    }

    //method to control uploading maze buttton
    public void UploadMazeBut(){
        //set button value to upload maze
        buttonVal = "UploadMaze";
        //check if file exists already
        if (!Files.exists(Path.of(gridInput1.getText()))) {
            //show alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            //set title
            alert.setTitle("Error");
            //set header text
            alert.setHeaderText("File name is either incorrect or does not exist");
            //set text of the main content
            alert.setContentText("Please check the file name and try again");
            //show alert and wait
            alert.showAndWait();
        }
        //if file doesn't exist already
        else{
            //pass node grid and a file of the name to InputFile class
            uploadmaze = new InputFile(grid.getNodeGrid(), gridInput1.getText());
            //read from the file
            uploadmaze.readFromFile();
            //update size of the grid depending on the metadata of the file
            pixelSize = uploadmaze.pixelSize1;
            //update the width of the grid
            widthOfWindow = (int) canvas.getWidth() / pixelSize;
            //update the height of the grid
            heightOfWindow = (int) canvas.getHeight() / pixelSize;
            //reset the graph
            resetGraph();
        }
    }

    //method to control the save maze button
    public void SaveMazeBut(){
        //set the button value to the save maze
        buttonVal = "saveMaze";
        //check if file exists already
        if (Files.exists(Path.of(gridInput1.getText()))) {
            //show alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            //set title
            alert.setTitle("File exists");
            //set header text
            alert.setHeaderText("File already exists. Do You want to override?");
            //dont allow user to resize the alert
            alert.setResizable(false);
            //set text of the main content
            alert.setContentText("Select okay to proceed or cancel.");
            //show and wait the alert
            Optional<ButtonType> result = alert.showAndWait();
            //make alert have a choice of buttons
            ButtonType button = result.orElse(ButtonType.CANCEL);
            //if user pressed ok then proceed
            if (button == ButtonType.OK) {
                //pass the grid, grid size and file name to SaveMaze class
                savemaze = new SaveMaze(grid.getNodeGrid(), pixelSize, gridInput1.getText());
                //write the maze to a file
                savemaze.writeToFile();
                //check if file was created
                if (Files.exists(Path.of(gridInput1.getText()))) {
                    //show alert
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    //set title
                    alert1.setTitle("File created");
                    //set header text
                    alert1.setHeaderText("File: " + gridInput1.getText() + " is successfully created");
                    //show and wait the alert
                    alert1.showAndWait();
                }
            }
            //if user selected cancel then close the alert
            else {
                //close the alert
                alert.close();
            }
        }
        //if file doesn't exist yet then create one
        else {
            //pass the grid, grid size and file name to SaveMaze class
            savemaze = new SaveMaze(grid.getNodeGrid(), pixelSize, gridInput1.getText());
            //write the maze to a file
            savemaze.writeToFile();
            //check if file was created
            if (Files.exists(Path.of(gridInput1.getText()))) {
                //show alert
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                //set title
                alert1.setTitle("File created");
                //set header text
                alert1.setHeaderText("File: " + gridInput1.getText() + " is successfully created");
                //show and wait the alert
                alert1.showAndWait();
            }
        }
    }


    //method to control the get help button
    @FXML
    private void getHelp() {
        //try and catch if there was an error
        try {
            //load the file image
            File imageFile = new File("/Users/maryanprydatko/PathfindingVis/StoryBoard.png");
            //open an image using default editor
            Desktop.getDesktop().open(imageFile);
            //if any error occurred catch it
        } catch (IOException ex) {
            //output the stack trace
            ex.printStackTrace();
        }
    }

    //method to control the create grid button
    public void createGrid() {
        //set button value to createGrid
        buttonVal = "createGrid";
        //get the input
        String input = gridInput.getText();
        //set pixel size to 1(default)
        pixelSize = 1;
        //change the width of the canvas
        widthOfWindow = (int) canvas.getWidth() / pixelSize;
        //change the height of the canvas
        heightOfWindow = (int) canvas.getHeight() / pixelSize;
        //create a new grid
        grid = new Grid(widthOfWindow,heightOfWindow);
        //initialise the nodes for new grid
        grid.initialiseGridNodes(new Grid(widthOfWindow,heightOfWindow).getNodeGrid());
        //reset the graph
        resetGraph();
        //check if the button value is createGrid
        if (buttonVal.equals("createGrid")) {
            //check if the input is integer
            if (isInputInteger(input)) {
                //change the pixes size to the one user inputted in
                pixelSize = Integer.parseInt(gridInput.getText());
                //change the width of the canvas
                widthOfWindow = (int) canvas.getWidth() / pixelSize;
                //change the height of the canvas
                heightOfWindow = (int) canvas.getHeight() / pixelSize;
                //create a new grid
                grid = new Grid(widthOfWindow,heightOfWindow);
                //initialise the nodes for new grid
                grid.initialiseGridNodes(new Grid(widthOfWindow,heightOfWindow).getNodeGrid());
                //reset the graph
                resetGraph();
            }
            //if the input is not an integer then show message
            else {
                //initialise the alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                //set title
                alert.setTitle("Error");
                //set header text
                alert.setHeaderText("Value is either incorrect or not put into text field");
                //set content text
                alert.setContentText("Please input correct value into a text field");
                //show and wait
                alert.showAndWait();
            }
        }
    }

    //method to check if the inout is an integer
    public boolean isInputInteger(String inputGrid) {
        //try and catch block to catch any errors
        try {
            //set value equal to passed
            int input = Integer.parseInt(inputGrid);
            //check if it is positive number
            return input > 0;
            //catch any errors
        } catch (NumberFormatException e) {
            //return false
            return false;
        }
    }


    //method to control the set walls button
    public void setWalls() {
        //set button value to drawWalls
        buttonVal = "drawWalls";
        //mouse event on the dragging mouse after a click
        canvas.setOnMouseDragged(mouseEvent -> {
            //check of the button value is drawWalls
            if (buttonVal.equals("drawWalls")) {
                //parse the mouse coordinates in terms of x of the grid
                int x = (int) mouseEvent.getX() / pixelSize;
                //parse the mouse coordinates in terms of y of the grid
                int y = (int) mouseEvent.getY() / pixelSize;
                //check if the node is on grid and is not a start node and a goal node
                if (grid.isInGrid(x, y) & !grid.isStartNode(x,y) & !grid.isGoalNode(x,y)) {
                    //set wall node to true
                    grid.setWallNode(x, y, true);
                    //check if path is set
                    if(grid.isPathSet())
                        //visualise the alg
                        visualise();
                }
                //reset the graph
                resetGraph();
            }
        });
    }

    //method to control the set the start node button
    public void setStartNode() {
        //set button value to setStart
        buttonVal = "setStart";
        //make a mouse event if the mouse was pressed on the grid
        canvas.setOnMousePressed(mouseEvent -> {
            //if the button is equal to set start
            if (buttonVal.equals("setStart")) {
                //parse the mouse coordinates in terms of x of the grid
                int x = (int) mouseEvent.getX() / pixelSize;
                //parse the mouse coordinates in terms of y of the grid
                int y = (int) mouseEvent.getY() / pixelSize;
                //check if start node is placed o not
                if (!isStartNodePlaced) {
                    //set start node
                    grid.SetStartNode(x, y);
                    //set start node
                    StartNode = grid.getStartNode();
                    //set start node placed flag to true
                    isStartNodePlaced = true;
                }
                //if node is already placed
                else {
                    //remove start node from the grid
                    grid.removeStartNode();
                    //set start node
                    grid.SetStartNode(x, y);
                    //set start node
                    StartNode = grid.getStartNode();
                    //set start node placed flag to fals
                    isStartNodePlaced = false;
                }
                //reset the graph
                resetGraph();
            }
        });
    }

    //method to control the set goal node button
    public void setGoalNode() {
        //set button value to setGoal
        buttonVal = "setGoal";
        //make a mouse event if the mouse was pressed on the grid
        canvas.setOnMousePressed(mouseEvent -> {
            //check if button value is setgoal
            if (buttonVal.equals("setGoal")) {
                //parse the mouse coordinates in terms of x of the grid
                int x = (int) mouseEvent.getX() / pixelSize;
                //parse the mouse coordinates in terms of y of the grid
                int y = (int) mouseEvent.getY() / pixelSize;
                //check if goal node was already placed
                if (!isGoalNodePlaced) {
                    //set goal node
                    grid.SetGoalNode(x, y);
                    //set goal node
                    isGoalNodePlaced = true;
                    //set goal node placed flag to true
                    GoalNode = grid.getGoalNode();
                    //reset the graph
                    resetGraph();
                }
                else {
                    //remove goal node from the grid
                    grid.removeGoalNode();
                    //set goal node
                    grid.SetGoalNode(x, y);
                    //set goal node
                    GoalNode = grid.getGoalNode();
                    //set goal node placed flag to fals
                    isGoalNodePlaced = false;
                    //reset the graph
                    resetGraph();
                }
            }
        });
    }

    //method to control the erase walls button
    public void eraseWalls() {
        //set button value to erase walls
        buttonVal = "eraseWalls";
        //reset graph
        resetGraph();

        //mouse event on the dragging mouse after a click
        canvas.setOnMouseDragged(mouseEvent -> {
            //check if the button value is erase walls
            if (buttonVal.equals("eraseWalls")) {
                //parse the mouse coordinates in terms of x of the grid
                int x = (int) mouseEvent.getX() / pixelSize;
                //parse the mouse coordinates in terms of y of the grid
                int y = (int) mouseEvent.getY() / pixelSize;
                //check if x and y coordinates are on grid
                if (grid.isInGrid(x, y)) {
                    //erase a wall node
                    grid.setWallNode(x,y, false);
                    if (grid.isPathSet()) visualise();
                }
                //reset the graph
                resetGraph();
            }
        });
    }

    //method to control algorithms drop-down menu
    public void visualise() {
        //set button value to alg choce
        buttonVal = "algChoice";
        //check if drop-down menu zero
        if (algCombo.getValue() == null) {
            //show alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Algorithm is not chosen");
            alert.setContentText("Please select the algorithm for your pathfinding visualisation");
            alert.showAndWait();
            //if dorp down menu is not zero proceed
        }else{
            //if choice is A star
            if (algCombo.getValue().equals("A star")){
                //visualise the A star
                visualiseAstar();
                //if choice is Dijkstra
            }else if (algCombo.getValue().equals("Dijkstra")){
                //visualise the Dijkstra
                visualiseDij();
                //if choice is BFS
            }else if (algCombo.getValue().equals("BFS")){
                //visualise the BFS
                visualiseBFS();
                //if choice is DFS
            }else if (algCombo.getValue().equals("DFS")){
                //visualise the DFS
                visualiseDFS();
            }
        }
    }

    //method to control the visualise button
    private void visualiseAstar() {
        //set button value to visualise
        buttonVal = "visualise";

        //check if goal or start node are null
        if (GoalNode == null || StartNode == null){
            //show alert if they are null
            Alert alert = new Alert(Alert.AlertType.ERROR);
            //set title
            alert.setTitle("Error");
            //set header
            alert.setHeaderText("Either goal or start node are not chosen, or both");
            //set content text
            alert.setContentText("Please choose you goal/start node by pressing `Set Goal/Start` ");
            //show and wait
            alert.showAndWait();
        }
        //if the goal and start node are placed
        else {
            //pass grid, start and goal nodes to the a star algorithm
            aStarAlgorithm = new aStar(grid.getNodeGrid(), StartNode, GoalNode);
            //reset all the values of the a star algorithm
            aStarAlgorithm.start();

            //initiate executor
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

            //loop through the algorithm at a fixed rate
            executor.scheduleAtFixedRate(() -> {
                //check if the path is not set and stop search flag is false
                if (!aStarAlgorithm.isPathSet() && !aStarAlgorithm.stopSearch) {
                    try {
                        //run the a star algorithm
                        aStarAlgorithm.run();
                        //render algorithm
                        renderAlg();
                        //catch any problems
                    } catch (Exception e) {
                        //print the problem
                        e.printStackTrace();
                        //stop looping
                        executor.shutdown();
                    }
                    //if the path is set and stop search flag is true
                } else {
                    //stoep the search
                    executor.shutdown();
                }
            }, 0, animationSpeed, TimeUnit.MILLISECONDS);
        }
    }

    private void visualiseBFS() {
        //set button value to visualisebfs
        buttonVal = "visualisebfs";

        //check if goal or start node are null
        if (GoalNode == null || StartNode == null){
            //show alert if they are null
            Alert alert = new Alert(Alert.AlertType.ERROR);
            //set title
            alert.setTitle("Error");
            //set header
            alert.setHeaderText("Either goal or start node are not chosen, or both");
            //set content text
            alert.setContentText("Please choose you goal/start node by pressing `Set Goal/Start` ");
            //show and wait
            alert.showAndWait();
        }
        //if the goal and start node are placed
        else {
            //pass grid, start and goal nodes to the bfs algorithm
            bfs = new BFS(grid.getNodeGrid(),StartNode,GoalNode);
            //reset all the values of the bfs algorithm
            bfs.start();

            //initiate executor
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

            //loop through the algorithm at a fixed rate
            executor.scheduleAtFixedRate(() -> {
                //check if the path is not set and stop search flag is false
                if (!bfs.isPathSet() && !bfs.stopSearch) {
                    try {
                        //run the bfs algorithm
                        bfs.run();
                        //render algorithm
                        renderAlg();
                        //catch any problems
                    } catch (Exception e) {
                        //print the problem
                        e.printStackTrace();
                        //stop looping
                        executor.shutdown();
                    }
                    //if the path is set and stop search flag is true
                } else {
                    //stop the search
                    executor.shutdown();
                }
            }, 0, animationSpeed, TimeUnit.MILLISECONDS);
        }
    }

    private void visualiseDFS(){
        //set button value to visualisedfs
        buttonVal = "visualisedfs";


        //check if goal or start node are null
        if (GoalNode == null || StartNode == null){
            //show alert if they are null
            Alert alert = new Alert(Alert.AlertType.ERROR);
            //set title
            alert.setTitle("Error");
            //set header
            alert.setHeaderText("Either goal or start node are not chosen, or both");
            //set content text
            alert.setContentText("Please choose you goal/start node by pressing `Set Goal/Start` ");
            //show and wait
            alert.showAndWait();
        }
        //if the goal and start node are placed
        else {
            //pass grid, start and goal nodes to the dfs algorithm
            dfs = new DFS(grid.getNodeGrid(), StartNode, GoalNode);
            //reset all the values of the dfs algorithm
            dfs.start();

            //initiate executor
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

            //loop through the algorithm at a fixed rate
            executor.scheduleAtFixedRate(() -> {
                //check if the path is not set and stop search flag is false
                if (!dfs.isPathSet() && !dfs.stopSearch) {
                    try {
                        //run the a star algorithm
                        dfs.run();
                        //render algorithm
                        renderAlg();
                        //catch any problems
                    } catch (Exception e) {
                        //print the problem
                        e.printStackTrace();
                        //stop looping
                        executor.shutdown();
                    }
                    //if the path is set and stop search flag is true
                } else {
                    //stoep the search
                    executor.shutdown();
                }
            }, 0, animationSpeed, TimeUnit.MILLISECONDS);
        }
    }

    private void visualiseDij(){
        //set button value to visualisedij
        buttonVal = "visualisedij";

        //check if goal or start node are null
        if (GoalNode == null || StartNode == null){
            //show alert if they are null
            Alert alert = new Alert(Alert.AlertType.ERROR);
            //set title
            alert.setTitle("Error");
            //set header
            alert.setHeaderText("Either goal or start node are not chosen, or both");
            //set content text
            alert.setContentText("Please choose you goal/start node by pressing `Set Goal/Start` ");
            //show and wait
            alert.showAndWait();
        }
        //if the goal and start node are placed
        else {
            //pass grid, start and goal nodes to the dijkstra's algorithm
            dijkstra = new Dijkstras(grid.getNodeGrid(), StartNode, GoalNode);
            //reset all the values of the dijkstra's algorithm
            dijkstra.start();

            //initiate executor
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

            //loop through the algorithm at a fixed rate
            executor.scheduleAtFixedRate(() -> {
                //check if the path is not set and stop search flag is false
                if (!dijkstra.isPathSet() && !dijkstra.stopSearch) {
                    try {
                        //run the a star algorithm
                        dijkstra.run();
                        //render algorithm
                        renderAlg();
                        //catch any problems
                    } catch (Exception e) {
                        //print the problem
                        e.printStackTrace();
                        //stop looping
                        executor.shutdown();
                    }
                    //if the path is set and stop search flag is true
                } else {
                    //stoep the search
                    executor.shutdown();
                }
            }, 0, animationSpeed, TimeUnit.MILLISECONDS);
        }
    }

    private void init(){
        //Set up the slider value
        animationSpeedSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                animationSpeed = (int) animationSpeedSlider.getValue());

        //set up the values of width and heigth of the Grid
        widthOfWindow = (int)canvas.getWidth() / pixelSize;
        heightOfWindow = (int)canvas.getHeight() / pixelSize;
        gc = canvas.getGraphicsContext2D();

        canvas.requestFocus();

        grid = new Grid(widthOfWindow,heightOfWindow);
        grid.initialiseGridNodes(new Grid(widthOfWindow,heightOfWindow).getNodeGrid());
        aStarAlgorithm = new aStar(grid.getNodeGrid(),StartNode,GoalNode);
        bfs = new BFS(grid.getNodeGrid(),StartNode,GoalNode);

        StartNode = null;
        GoalNode = null;

        resetGraph();

    }

    //method to control the clear grid button
    public void clearGrid() {
        //set button value to create grid
        buttonVal = "createGrid";
        //show a message to clear the grid wtih choice
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear Grid");
        alert.setHeaderText("Are you sure you want to clear the grid?");
        alert.setResizable(false);
        alert.setContentText("Select okay to proceed or cancel.");

        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        //if the button is OK
        if (button == ButtonType.OK) {
            //initialise the grid and clear it
            init();
        } else {
            //close the alert
            alert.close();
        }
    }
}
