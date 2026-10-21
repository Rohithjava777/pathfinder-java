/**
 * Dijkstra.js - Dijkstra's shortest path algorithm
 * Port of source.cspathfinder.Dijkstras
 */
class Dijkstra {
    constructor(nodeGrid, startNode, goalNode) {
        this.nodeGrid = nodeGrid;
        this.startNode = startNode;
        this.goalNode = goalNode;
        this.finalPath = null;
        this.openSet = [];
        this.closedSet = new Set();
        this.stopSearch = true;
    }

    isPathSet() { return this.finalPath !== null; }

    isOnPath(x, y) {
        return this.isPathSet() && this.finalPath.some(n => n.x === x && n.y === y);
    }

    isNeighbour(x, y) {
        return this.openSet.some(n => n.x === x && n.y === y);
    }

    resetAll() {
        const width = this.nodeGrid.length;
        const height = this.nodeGrid[0].length;
        for (let x = 0; x < width; x++) {
            for (let y = 0; y < height; y++) {
                const node = this.nodeGrid[x][y];
                node.cameFromNode = null;
                node.visited = false;
                node.distanceFromStart = Infinity;
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

        // Sort by distance from start
        this.openSet.sort((a, b) => a.distanceFromStart - b.distanceFromStart);
        const currentNode = this.openSet.shift();

        if (currentNode.equals(this.goalNode)) {
            this.stopSearch = true;
            this.finalPath = this.retracePath(this.goalNode);
            return;
        }

        for (const neighbour of currentNode.neighbourNodes) {
            if (neighbour.wallNode) continue;
            const distance = currentNode.distanceFromStart + 1;

            if (distance < neighbour.distanceFromStart) {
                neighbour.distanceFromStart = distance;
                neighbour.cameFromNode = currentNode;

                const idx = this.openSet.indexOf(neighbour);
                if (idx === -1) {
                    this.openSet.push(neighbour);
                }
                // If already in open set, the sort will fix the order next iteration
            }
        }

        this.closedSet.add(currentNode);
    }

    stop() {
        this.stopSearch = true;
        this.openSet = [];
        this.closedSet = new Set();
        this.finalPath = null;
    }
}
