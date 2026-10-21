/**
 * RandomMaze.js - Random maze generator
 * Port of source.cspathfinder.RandomMazes
 */
class RandomMazeGenerator {
    constructor(width, height) {
        this.width = width;
        this.height = height;
        this.nodeGrid = [];

        // Create nodes
        for (let x = 0; x < width; x++) {
            this.nodeGrid[x] = [];
            for (let y = 0; y < height; y++) {
                this.nodeGrid[x][y] = new Node(x, y);
            }
        }

        // Create random walls with 40% density
        for (let x = 0; x < width; x++) {
            for (let y = 0; y < height; y++) {
                if (Math.random() < 0.4) {
                    this.nodeGrid[x][y].wallNode = true;
                }
            }
        }
    }

    getNodeGrid() {
        return this.nodeGrid;
    }
}
