/**
 * MazeDFS.js - DFS-based perfect maze generator
 * Port of source.cspathfinder.MazeDFS
 */
class MazeDFSGenerator {
    constructor(width, height) {
        this.width = width;
        this.height = height;
        this.nodeGrid = [];

        // Create nodes, all walls
        for (let x = 0; x < width; x++) {
            this.nodeGrid[x] = [];
            for (let y = 0; y < height; y++) {
                this.nodeGrid[x][y] = new Node(x, y);
                this.nodeGrid[x][y].wallNode = true;
            }
        }

        // Generate maze starting from (0, 0)
        this._generateMaze(0, 0);
    }

    _generateMaze(x, y) {
        this.nodeGrid[x][y].visitedMaze = true;
        this.nodeGrid[x][y].wallNode = false;

        let neighbors = this._getUnvisitedNeighbors(x, y);

        while (neighbors.length > 0) {
            const next = neighbors[Math.floor(Math.random() * neighbors.length)];
            const nextX = next.x;
            const nextY = next.y;

            if (!next.visitedMaze) {
                // Remove wall between current and next
                this.nodeGrid[Math.floor((x + nextX) / 2)][Math.floor((y + nextY) / 2)].wallNode = false;
                this._generateMaze(nextX, nextY);
            }

            neighbors = this._getUnvisitedNeighbors(x, y);
        }
    }

    _getUnvisitedNeighbors(x, y) {
        const neighbors = [];

        if (x > 1 && !this.nodeGrid[x - 2][y].visitedMaze) {
            neighbors.push(this.nodeGrid[x - 2][y]);
        }
        if (x < this.nodeGrid.length - 2 && !this.nodeGrid[x + 2][y].visitedMaze) {
            neighbors.push(this.nodeGrid[x + 2][y]);
        }
        if (y > 1 && !this.nodeGrid[x][y - 2].visitedMaze) {
            neighbors.push(this.nodeGrid[x][y - 2]);
        }
        if (y < this.nodeGrid[0].length - 2 && !this.nodeGrid[x][y + 2].visitedMaze) {
            neighbors.push(this.nodeGrid[x][y + 2]);
        }

        return neighbors;
    }

    getNodeGrid() {
        return this.nodeGrid;
    }
}
