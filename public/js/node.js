/**
 * Node.js - Represents a single cell in the grid
 * Port of source.cspathfinder.Node
 */
class Node {
    constructor(x, y) {
        this.x = x;
        this.y = y;
        this.cameFromNode = null;
        this.fCost = Infinity;
        this.hCost = Infinity;
        this.gCost = Infinity;
        this.distanceFromStart = Infinity;
        this.visited = false;
        this.visitedMaze = false;
        this.neighbourNodes = [];
        this.fullyExplored = false;
        this.wallNode = false;
    }

    /**
     * Set neighbour nodes (up, right, down, left)
     */
    setNeighbourNodes(width, height, grid) {
        this.neighbourNodes = [];
        if (this.y > 0) this.neighbourNodes.push(grid[this.x][this.y - 1]);
        if (this.x < width - 1) this.neighbourNodes.push(grid[this.x + 1][this.y]);
        if (this.y < height - 1) this.neighbourNodes.push(grid[this.x][this.y + 1]);
        if (this.x > 0) this.neighbourNodes.push(grid[this.x - 1][this.y]);
    }

    /**
     * Reset the node to default state (preserving walls)
     */
    reset() {
        this.cameFromNode = null;
        this.fCost = Infinity;
        this.hCost = Infinity;
        this.gCost = Infinity;
        this.distanceFromStart = Infinity;
        this.visited = false;
        this.fullyExplored = false;
    }

    equals(other) {
        if (!other) return false;
        return this.x === other.x && this.y === other.y;
    }
}
