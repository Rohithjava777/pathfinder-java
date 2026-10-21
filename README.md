# Pathfinding Visualizer

A web-based interactive pathfinding algorithm visualizer. Originally a JavaFX desktop application by [Maryan Prydatko](https://github.com/MaryanPrydatko), ported to a pure HTML/CSS/JavaScript web app for deployment on **Vercel**.

🔗 **Live Demo**: Deploy to Vercel and share the link!

![HTML5](https://img.shields.io/badge/HTML5-Canvas-orange)
![JavaScript](https://img.shields.io/badge/JavaScript-ES6-yellow)
![Vercel](https://img.shields.io/badge/Vercel-Ready-black)

## 🎯 Features

- **Multiple Pathfinding Algorithms:**
  - A* (A-Star) — Heuristic-based pathfinding
  - Dijkstra's Algorithm — Shortest path guarantee
  - Breadth-First Search (BFS) — Level-order traversal
  - Depth-First Search (DFS) — Stack-based exploration

- **Interactive Grid:**
  - Click and drag to draw walls
  - Adjustable grid/cell size
  - Set custom start and goal nodes
  - Erase walls with eraser tool

- **Maze Generation:**
  - DFS-based perfect maze generator
  - Random maze generator (40% wall density)
  - Save and load mazes (browser localStorage)

- **Real-Time Visualization:**
  - Color-coded nodes showing algorithm progress
  - Adjustable animation speed
  - Visual path reconstruction

## 🎨 Color Legend

| Color | Meaning |
|-------|---------|
| 🟢 Green | Start node |
| 🔴 Red | Goal/target node |
| ⬛ Dark | Wall nodes |
| 🟠 Orange | Nodes being explored |
| 🟣 Purple | Nodes in queue (BFS/DFS) |
| 🔵 Blue | Final path |

## 🚀 Deploy to Vercel

### One-Click Deploy

[![Deploy with Vercel](https://vercel.com/button)](https://vercel.com/new/clone?repository-url=https://github.com/MaryanPrydatko/pathfinder-java)

### Manual Deploy

1. **Install Vercel CLI:**
   ```bash
   npm i -g vercel
   ```

2. **Deploy:**
   ```bash
   cd pathfinder-java
   vercel
   ```

3. **Production deploy:**
   ```bash
   vercel --prod
   ```

### Local Development

```bash
# Serve locally
npx serve public

# Or simply open public/index.html in your browser
```

## 📖 How to Use

1. **Create a Grid** — Enter pixel size and click "Create Grid"
2. **Set Start** — Click "Set Start" then click on the grid
3. **Set Goal** — Click "Set Goal" then click on the grid
4. **Draw Walls** — Click "Draw Walls" and drag on the grid
5. **Choose Algorithm** — Select from the dropdown
6. **Visualise!** — Click the green button and watch the magic

### Keyboard Shortcuts

| Key | Action |
|-----|--------|
| `V` | Start visualization |
| `Esc` | Deselect tool / close modal |

## 📁 Project Structure

```
pathfinder-java/
├── public/                  # Static site (served by Vercel)
│   ├── index.html           # Main HTML page
│   ├── css/
│   │   └── style.css        # Styles
│   └── js/
│       ├── node.js          # Node data structure
│       ├── grid.js          # Grid management
│       ├── astar.js         # A* algorithm
│       ├── dijkstra.js      # Dijkstra's algorithm
│       ├── bfs.js           # BFS algorithm
│       ├── dfs.js           # DFS algorithm
│       ├── mazeDFS.js       # DFS maze generator
│       ├── randomMaze.js    # Random maze generator
│       └── app.js           # Main controller
├── vercel.json              # Vercel configuration
├── package.json             # Project metadata
└── README.md                # This file
```

## 🧮 Algorithm Details

### A* (A-Star)
Uses both distance from start (g-cost) and estimated distance to goal (h-cost).
**Formula:** `f(n) = g(n) + h(n)`

### Dijkstra's Algorithm
Guarantees shortest path by exploring nodes in order of distance from start.

### Breadth-First Search (BFS)
Explores level by level using a queue. Shortest path in unweighted graphs.

### Depth-First Search (DFS)
Explores deep using a stack. Does **not** guarantee shortest path.

## 👨‍💻 Credits

- **Original Java App**: [Maryan Prydatko](https://github.com/MaryanPrydatko)
- **Web Port**: Converted from JavaFX to HTML5 Canvas for web deployment

## 📄 License

This project was created as a high school computer science project.
