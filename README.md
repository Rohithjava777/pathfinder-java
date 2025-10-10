# Pathfinding Visualization

A JavaFX desktop application that visualizes various pathfinding algorithms in real-time. This project demonstrates the behavior of different search algorithms on customizable grids with interactive maze generation.

![Java](https://img.shields.io/badge/Java-18-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-18.0.2-blue)
![Maven](https://img.shields.io/badge/Maven-3.8+-green)

## 🎯 Features

- **Multiple Pathfinding Algorithms:**
  - A* (A-Star) - Heuristic-based pathfinding
  - Dijkstra's Algorithm - Shortest path guarantee
  - Breadth-First Search (BFS) - Level-order traversal
  - Depth-First Search (DFS) - Stack-based exploration

- **Interactive Grid:**
  - Click and drag to draw walls
  - Adjustable grid size
  - Set custom start and goal nodes
  - Erase walls with eraser tool

- **Maze Generation:**
  - DFS-based maze generator
  - Random maze generator
  - Save and load custom mazes

- **Real-Time Visualization:**
  - Color-coded nodes showing algorithm progress
  - Adjustable animation speed
  - Visual path reconstruction

## 🎨 Color Scheme

- **Green**: Start node
- **Red**: Goal/target node
- **Black**: Wall nodes
- **Orange**: Nodes being explored (neighbors)
- **Medium Purple**: Nodes in exploration queue (BFS/DFS)
- **Light Sky Blue**: Final path from start to goal

## 🎬 Demo Videos

Watch the pathfinding visualizer in action:

- **[Demo Video 1](https://www.youtube.com/watch?v=1MaM9EF9VbY)** - Pathfinding algorithms demonstration
- **[Demo Video 2](https://www.youtube.com/watch?v=LbLRZao_hqw)** - Interactive features and maze generation

## 🚀 Getting Started

### Prerequisites

- Java 18 or higher
- Maven 3.8+
- JavaFX 18.0.2

### Installation & Running

1. **Clone the repository:**
   ```bash
   git clone https://github.com/prydatkoo/pathfinder-java.git
   cd pathfinder-java
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn javafx:run
   ```

## 📖 How to Use

1. **Create a Grid:**
   - Enter desired pixel size in the text field
   - Click "Create Grid" to initialize

2. **Set Start and Goal:**
   - Click "Set Start Node" and click on the grid to place the start point
   - Click "Set Goal Node" and click on the grid to place the target point

3. **Draw Walls:**
   - Click "Set Walls" and drag on the grid to create obstacles
   - Use "Erase Walls" to remove obstacles

4. **Generate Mazes (Optional):**
   - Select "MazeDFS" or "Random Mazes" from the dropdown
   - Click to generate automated maze patterns

5. **Choose Algorithm:**
   - Select an algorithm from the dropdown menu (A*, Dijkstra, BFS, or DFS)

6. **Visualize:**
   - Click "Visualize" to watch the algorithm find the path
   - Adjust the animation speed slider for faster/slower visualization

7. **Save/Load Mazes:**
   - Enter a filename in the text field
   - Click "Save Maze" to save your current grid configuration
   - Click "Upload Maze" to load a previously saved maze

## 🧮 Algorithm Details

### A* (A-Star)
Uses both distance from start (g-cost) and estimated distance to goal (h-cost) to find the optimal path efficiently. Combines the benefits of Dijkstra's and greedy best-first search.

**Formula:** `f(n) = g(n) + h(n)`

### Dijkstra's Algorithm
Guarantees the shortest path by exploring nodes in order of their distance from the start. Does not use heuristics, making it slower but reliable.

### Breadth-First Search (BFS)
Explores nodes level by level using a queue. Guarantees the shortest path in unweighted graphs.

### Depth-First Search (DFS)
Explores as far as possible along each branch using a stack. Does not guarantee the shortest path but can be useful for maze solving.

## 📁 Project Structure

```
PathfindingVis/
├── src/
│   └── main/
│       ├── java/
│       │   └── source/cspathfinder/
│       │       ├── Main.java              # Application entry point
│       │       ├── Controller.java        # GUI controller and logic
│       │       ├── Node.java              # Node data structure
│       │       ├── Grid.java              # Grid management
│       │       ├── aStar.java             # A* implementation
│       │       ├── Dijkstras.java         # Dijkstra's implementation
│       │       ├── BFS.java               # BFS implementation
│       │       ├── DFS.java               # DFS implementation
│       │       ├── MazeDFS.java           # DFS maze generator
│       │       ├── RandomMazes.java       # Random maze generator
│       │       ├── SaveMaze.java          # Save maze functionality
│       │       └── InputFile.java         # Load maze functionality
│       └── resources/
│           └── source/cspathfinder/
│               └── Scene.fxml             # JavaFX UI layout
├── pom.xml                                # Maven configuration
└── README.md                              # This file
```

## 🛠️ Technologies Used

- **Java 18** - Core programming language
- **JavaFX 18.0.2** - GUI framework for desktop application
- **Maven** - Dependency management and build tool
- **FXML** - XML-based UI layout

## 🎓 Educational Value

This project demonstrates:
- **Data Structures:** Graphs, Priority Queues, Stacks, Queues, Hash Sets
- **Algorithms:** Pathfinding, maze generation, heuristics
- **Design Patterns:** MVC architecture
- **GUI Development:** Event handling, real-time visualization
- **Software Engineering:** Modular code structure, separation of concerns

## 📝 Key Features Implementation

### Node Class
Each grid cell is represented as a `Node` object containing:
- Coordinates (x, y)
- Cost values (f-cost, g-cost, h-cost)
- Neighbor references
- State flags (visited, wall, explored)

### Visualization Engine
Uses `ScheduledExecutorService` to run algorithms step-by-step with configurable delays, providing smooth real-time visualization of the search process.

### Maze Generation
- **DFS Maze:** Creates perfect mazes (one path between any two points) using recursive backtracking
- **Random Maze:** Generates random wall patterns with 40% wall density

## 🎯 Future Enhancements

- Add more algorithms (Greedy Best-First, Jump Point Search)
- Implement weighted nodes for terrain costs
- Add diagonal movement support
- Export visualizations as GIF/video
- Performance metrics (nodes explored, path length, time taken)

## 👨‍💻 Author

**Maryan Prydatko**
- Email: maryanko777@gmail.com
- GitHub: [@prydatkoo](https://github.com/prydatkoo)

## 📄 License

This project was created as a high school computer science project.

## 🙏 Acknowledgments

This project was developed during high school to explore and visualize fundamental pathfinding algorithms used in computer science, robotics, and game development.

---

**Note:** This application requires a display and cannot run in headless mode due to JavaFX requirements.

