/**
 * BFS.js - Breadth-First Search algorithm
 * Port of source.cspathfinder.BFS
 */
class BFS {
    constructor(nodeGrid, startNode, goalNode) {
        this.nodeGrid = nodeGrid;
        this.startNode = startNode;
        this.goalNode = goalNode;
        this.finalPath = null;
        this.openSet = [];  // Used as a queue (FIFO)
        this.neighbourList = new Set();
        this.stopSearch = true;
    }

    isPathSet() { return this.finalPath !== null; }

    isOnPath(x, y) {
        return this.isPathSet() && this.finalPath.some(n => n.x === x && n.y === y);
    }

    isNeighbour(x, y) {
        const node = this.nodeGrid[x][y];
        return node.visited && this.neighbourList.has(node);
    }

    isNotFullyExploredNode(x, y) {
        const node = this.nodeGrid[x][y];
        return this.neighbourList.has(node) && !node.visited && !node.wallNode && !node.fullyExplored;
    }

    resetAll() {
        const width = this.nodeGrid.length;
        const height = this.nodeGrid[0].length;
        for (let x = 0; x < width; x++) {
            for (let y = 0; y < height; y++) {
                const node = this.nodeGrid[x][y];
                node.cameFromNode = null;
                node.visited = false;
            }
        }
        this.stop();
    }

    retracePath(goalNode) {
        const path = [];
        let currentNode = goalNode;
        while (currentNode !== null) {
            path.push(currentNode);
            currentNode = currentNode.cameFromNode;
        }
        path.reverse();
        return path;
    }

    start() {
        this.resetAll();
        this.openSet = [];
        this.startNode.distanceFromStart = 0;
        this.openSet.push(this.startNode);
        this.stopSearch = false;
    }

    run() {
        if (this.stopSearch) return;
        if (this.openSet.length === 0) return;

        // Dequeue (FIFO)
        const currentNode = this.openSet.shift();

        for (const neighbour of currentNode.neighbourNodes) {
            if (neighbour.visited || neighbour.wallNode) continue;
            const distance = currentNode.distanceFromStart + 1;
            neighbour.distanceFromStart = distance;
            neighbour.cameFromNode = currentNode;
            this.openSet.push(neighbour);
            this.neighbourList.add(neighbour);
        }

        currentNode.visited = true;

        if (currentNode.equals(this.goalNode)) {
            this.stopSearch = true;
            this.finalPath = this.retracePath(this.goalNode);
        }
    }

    stop() {
        this.stopSearch = true;
        this.openSet = [];
        this.neighbourList = new Set();
        this.finalPath = null;
    }
}
