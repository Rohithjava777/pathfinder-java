/**
 * AStar.js - A* pathfinding algorithm
 * Port of source.cspathfinder.aStar
 */
class AStar {
    constructor(nodeGrid, startNode, goalNode) {
        this.nodeGrid = nodeGrid;
        this.startNode = startNode;
        this.goalNode = goalNode;
        this.finalPath = null;
        this.openSet = [];
        this.neighbourList = [];
        this.stopSearch = true;
    }

    isPathSet() { return this.finalPath !== null; }

    isOnPath(x, y) {
        return this.isPathSet() && this.finalPath.some(n => n.x === x && n.y === y);
    }

    isNeighbour(x, y) {
        return this.neighbourList.some(n => n.x === x && n.y === y);
    }

    resetAll() {
        const width = this.nodeGrid.length;
        const height = this.nodeGrid[0].length;

        for (let x = 0; x < width; x++) {
            for (let y = 0; y < height; y++) {
                const node = this.nodeGrid[x][y];
                node.fCost = Infinity;
                node.gCost = Infinity;
                node.hCost = Math.abs(x - this.goalNode.x) + Math.abs(y - this.goalNode.y);
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
        this.startNode.gCost = 0;
        this.startNode.fCost = this.startNode.hCost;
        this.openSet = [this.startNode];
        this.stopSearch = false;
    }

    /**
     * Run one step of A*
     */
    run() {
        if (this.stopSearch) return;
        if (this.openSet.length === 0) return;

        // Find node with lowest fCost
        this.openSet.sort((a, b) => a.fCost - b.fCost);
        const currentNode = this.openSet.shift();
        this.neighbourList = [];

        for (const neighbour of currentNode.neighbourNodes) {
            if (neighbour.visited || neighbour.wallNode) continue;

            const tentativeGCost = currentNode.gCost + this._calculateDistance(
                neighbour.x, neighbour.y, currentNode
            );

            if (tentativeGCost < neighbour.gCost) {
                neighbour.cameFromNode = currentNode;
                neighbour.gCost = tentativeGCost;
                neighbour.fCost = neighbour.gCost + neighbour.hCost;

                if (!this.openSet.includes(neighbour)) {
                    this.neighbourList.push(neighbour);
                    this.openSet.push(neighbour);
                }
            }
        }

        currentNode.visited = true;

        if (currentNode.equals(this.goalNode)) {
            this.finalPath = this.retracePath(this.goalNode);
            this.stopSearch = true;
        }
    }

    stop() {
        this.stopSearch = true;
        this.openSet = [];
        this.neighbourList = [];
        this.finalPath = null;
    }

    _calculateDistance(x, y, node) {
        return Math.round(Math.sqrt(
            Math.pow(node.x - x, 2) + Math.pow(node.y - y, 2)
        ));
    }
}
