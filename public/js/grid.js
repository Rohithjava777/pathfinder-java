/**
 * Grid.js - Manages the grid of nodes
 * Port of source.cspathfinder.Grid
 */
class Grid {
    constructor(width, height) {
        this.width = width;
        this.height = height;
        this.nodeGrid = [];
        this.startNode = null;
        this.goalNode = null;
        this.finalPath = null;
        this.neighbourList = [];

        // Create nodes
        for (let x = 0; x < width; x++) {
            this.nodeGrid[x] = [];
            for (let y = 0; y < height; y++) {
                this.nodeGrid[x][y] = new Node(x, y);
            }
        }
    }

    /**
     * Initialize grid from a node array (e.g., from maze generators)
     */
    initialiseGridNodes(nodeGrid) {
        this.nodeGrid = nodeGrid;
        this.width = nodeGrid.length;
        this.height = nodeGrid[0].length;
        this.neighbourList = [];

        for (let x = 0; x < this.width; x++) {
            for (let y = 0; y < this.height; y++) {
                this.nodeGrid[x][y].setNeighbourNodes(this.width, this.height, this.nodeGrid);
            }
        }
    }

    setStartNode(x, y) {
        this.startNode = this.nodeGrid[x][y];
    }

    removeStartNode() {
        this.startNode = null;
    }

    setGoalNode(x, y) {
        this.goalNode = this.nodeGrid[x][y];
    }

    removeGoalNode() {
        this.goalNode = null;
    }

    setWallNode(x, y, isWall) {
        this.nodeGrid[x][y].wallNode = isWall;
    }

    isStartNode(x, y) {
        return this.startNode && this.nodeGrid[x][y].equals(this.startNode);
    }

    isGoalNode(x, y) {
        return this.goalNode && this.nodeGrid[x][y].equals(this.goalNode);
    }

    isWallNode(x, y) {
        return this.nodeGrid[x][y].wallNode;
    }

    isInGrid(x, y) {
        return x >= 0 && y >= 0 && x < this.width && y < this.height;
    }

    isPathSet() {
        return this.finalPath !== null;
    }

    getNodeGrid() {
        return this.nodeGrid;
    }

    /**
     * Export maze data as JSON
     */
    exportMaze(pixelSize) {
        const data = {
            pixelSize: pixelSize,
            width: this.width,
            height: this.height,
            walls: [],
            start: this.startNode ? { x: this.startNode.x, y: this.startNode.y } : null,
            goal: this.goalNode ? { x: this.goalNode.x, y: this.goalNode.y } : null
        };
        for (let x = 0; x < this.width; x++) {
            for (let y = 0; y < this.height; y++) {
                if (this.nodeGrid[x][y].wallNode) {
                    data.walls.push({ x, y });
                }
            }
        }
        return data;
    }

    /**
     * Import maze data from JSON
     */
    importMaze(data) {
        // Reconstruct grid
        this.width = data.width;
        this.height = data.height;
        this.nodeGrid = [];
        for (let x = 0; x < this.width; x++) {
            this.nodeGrid[x] = [];
            for (let y = 0; y < this.height; y++) {
                this.nodeGrid[x][y] = new Node(x, y);
            }
        }

        // Set walls
        for (const w of data.walls) {
            this.nodeGrid[w.x][w.y].wallNode = true;
        }

        // Set start/goal
        if (data.start) this.startNode = this.nodeGrid[data.start.x][data.start.y];
        if (data.goal) this.goalNode = this.nodeGrid[data.goal.x][data.goal.y];

        // Set neighbours
        this.initialiseGridNodes(this.nodeGrid);
    }
}
