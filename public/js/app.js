/**
 * app.js - Main application controller
 * Port of source.cspathfinder.Controller + Main
 */
(function () {
    'use strict';

    // ===== DOM Elements =====
    const canvas = document.getElementById('gridCanvas');
    const ctx = canvas.getContext('2d');

    const btnVisualise = document.getElementById('btnVisualise');
    const btnHelp = document.getElementById('btnHelp');
    const algSelect = document.getElementById('algSelect');
    const speedSlider = document.getElementById('speedSlider');

    const gridSizeInput = document.getElementById('gridSizeInput');
    const btnCreateGrid = document.getElementById('btnCreateGrid');
    const btnClearGrid = document.getElementById('btnClearGrid');
    const btnSetStart = document.getElementById('btnSetStart');
    const btnSetGoal = document.getElementById('btnSetGoal');
    const btnDrawWalls = document.getElementById('btnDrawWalls');
    const btnEraseWalls = document.getElementById('btnEraseWalls');

    const mazeSelect = document.getElementById('mazeSelect');
    const btnCreateMaze = document.getElementById('btnCreateMaze');

    const mazeNameInput = document.getElementById('mazeNameInput');
    const btnSaveMaze = document.getElementById('btnSaveMaze');
    const btnLoadMaze = document.getElementById('btnLoadMaze');

    const statusBar = document.getElementById('statusBar');

    const modalOverlay = document.getElementById('modalOverlay');
    const btnCloseModal = document.getElementById('btnCloseModal');

    const confirmOverlay = document.getElementById('confirmOverlay');
    const confirmTitle = document.getElementById('confirmTitle');
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmOk = document.getElementById('confirmOk');
    const confirmCancel = document.getElementById('confirmCancel');

    // ===== State =====
    let pixelSize = 20;
    let widthOfWindow = 0;
    let heightOfWindow = 0;
    let grid = null;
    let currentTool = '';  // setStart, setGoal, drawWalls, eraseWalls
    let animationSpeed = 50;
    let animationTimer = null;

    let aStarAlgorithm = null;
    let bfsAlgorithm = null;
    let dfsAlgorithm = null;
    let dijkstraAlgorithm = null;

    let isStartNodePlaced = false;
    let isGoalNodePlaced = false;

    // ===== Colors (matching the original Java app) =====
    const COLORS = {
        white: '#ffffff',
        black: '#1a1a2e',
        green: '#22c55e',
        red: '#ef4444',
        orange: '#f97316',
        lightSkyBlue: '#38bdf8',
        mediumPurple: '#a78bfa',
        gridLine: '#e2e2e2'
    };

    // ===== Initialization =====
    function init() {
        resizeCanvas();
        pixelSize = 20;
        widthOfWindow = Math.floor(canvas.width / pixelSize);
        heightOfWindow = Math.floor(canvas.height / pixelSize);

        grid = new Grid(widthOfWindow, heightOfWindow);
        grid.initialiseGridNodes(grid.getNodeGrid());

        isStartNodePlaced = false;
        isGoalNodePlaced = false;
        currentTool = '';

        clearToolHighlights();
        resetGraph();
        setStatus('Ready — Create a grid or start placing nodes');
    }

    function resizeCanvas() {
        const container = canvas.parentElement;
        const tabBar = container.querySelector('.tab-bar');
        const tabBarHeight = tabBar ? tabBar.offsetHeight : 30;

        canvas.width = container.clientWidth;
        canvas.height = container.clientHeight - tabBarHeight;
    }

    // ===== Rendering =====
    function resetGraph() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        for (let x = 0; x < widthOfWindow; x++) {
            for (let y = 0; y < heightOfWindow; y++) {
                if (grid.isWallNode(x, y)) {
                    ctx.fillStyle = COLORS.black;
                } else if (grid.isStartNode(x, y)) {
                    ctx.fillStyle = COLORS.green;
                } else if (grid.isGoalNode(x, y)) {
                    ctx.fillStyle = COLORS.red;
                } else {
                    ctx.fillStyle = COLORS.white;
                }
                ctx.fillRect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
            }
        }

        // Draw grid lines
        ctx.strokeStyle = COLORS.gridLine;
        ctx.lineWidth = 0.5;
        for (let x = 0; x <= canvas.width; x += pixelSize) {
            ctx.beginPath();
            ctx.moveTo(x, 0);
            ctx.lineTo(x, canvas.height);
            ctx.stroke();
        }
        for (let y = 0; y <= canvas.height; y += pixelSize) {
            ctx.beginPath();
            ctx.moveTo(0, y);
            ctx.lineTo(canvas.width, y);
            ctx.stroke();
        }
    }

    function renderAlgorithm() {
        const algo = algSelect.value;

        for (let x = 0; x < widthOfWindow; x++) {
            for (let y = 0; y < heightOfWindow; y++) {
                let color = null;

                if (algo === 'astar' && aStarAlgorithm) {
                    if (grid.isStartNode(x, y)) color = COLORS.green;
                    else if (grid.isGoalNode(x, y)) color = COLORS.red;
                    else if (aStarAlgorithm.isOnPath(x, y)) color = COLORS.lightSkyBlue;
                    else if (aStarAlgorithm.isNeighbour(x, y)) color = COLORS.orange;
                    else continue;
                } else if (algo === 'bfs' && bfsAlgorithm) {
                    if (grid.isStartNode(x, y)) color = COLORS.green;
                    else if (grid.isGoalNode(x, y)) color = COLORS.red;
                    else if (bfsAlgorithm.isNotFullyExploredNode(x, y)) color = COLORS.mediumPurple;
                    else if (bfsAlgorithm.isOnPath(x, y)) color = COLORS.lightSkyBlue;
                    else if (bfsAlgorithm.isNeighbour(x, y)) color = COLORS.orange;
                    else continue;
                } else if (algo === 'dfs' && dfsAlgorithm) {
                    if (grid.isStartNode(x, y)) color = COLORS.green;
                    else if (grid.isGoalNode(x, y)) color = COLORS.red;
                    else if (dfsAlgorithm.isNotFullyExploredNode(x, y)) color = COLORS.mediumPurple;
                    else if (dfsAlgorithm.isOnPath(x, y)) color = COLORS.lightSkyBlue;
                    else if (dfsAlgorithm.isNeighbour(x, y)) color = COLORS.orange;
                    else continue;
                } else if (algo === 'dijkstra' && dijkstraAlgorithm) {
                    if (dijkstraAlgorithm.isOnPath(x, y)) color = COLORS.lightSkyBlue;
                    else if (dijkstraAlgorithm.isNeighbour(x, y)) color = COLORS.orange;
                    else continue;

                    // Draw, then overlay start/goal
                    ctx.fillStyle = color;
                    ctx.fillRect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);

                    if (grid.isStartNode(x, y)) {
                        ctx.fillStyle = COLORS.green;
                        ctx.fillRect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
                    } else if (grid.isGoalNode(x, y)) {
                        ctx.fillStyle = COLORS.red;
                        ctx.fillRect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
                    }
                    continue;
                } else {
                    continue;
                }

                ctx.fillStyle = color;
                ctx.fillRect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
            }
        }
    }

    // ===== Tool Highlights =====
    const toolButtons = [btnSetStart, btnSetGoal, btnDrawWalls, btnEraseWalls];

    function clearToolHighlights() {
        toolButtons.forEach(b => b.classList.remove('active-tool'));
    }

    function setActiveTool(tool, button) {
        currentTool = tool;
        clearToolHighlights();
        if (button) button.classList.add('active-tool');
    }

    // ===== Status Bar =====
    function setStatus(msg) {
        statusBar.textContent = msg;
    }

    // ===== Alert / Confirm helpers =====
    function showAlert(msg) {
        alert(msg);
    }

    function showConfirm(title, message) {
        return new Promise((resolve) => {
            confirmTitle.textContent = title;
            confirmMessage.textContent = message;
            confirmOverlay.classList.add('show');

            const handleOk = () => { cleanup(); resolve(true); };
            const handleCancel = () => { cleanup(); resolve(false); };

            function cleanup() {
                confirmOverlay.classList.remove('show');
                confirmOk.removeEventListener('click', handleOk);
                confirmCancel.removeEventListener('click', handleCancel);
            }

            confirmOk.addEventListener('click', handleOk);
            confirmCancel.addEventListener('click', handleCancel);
        });
    }

    // ===== Visualisation =====
    function stopCurrentAnimation() {
        if (animationTimer) {
            clearInterval(animationTimer);
            animationTimer = null;
        }
    }

    function getAnimationDelay() {
        // Map slider (1-100) to delay (200ms - 1ms): higher slider = faster
        return Math.max(1, Math.round(201 - speedSlider.value * 2));
    }

    function visualise() {
        const algo = algSelect.value;
        if (!algo) {
            showAlert('Please select an algorithm from the dropdown');
            return;
        }
        if (!grid.startNode || !grid.goalNode) {
            showAlert('Please set both Start and Goal nodes before visualising');
            return;
        }

        stopCurrentAnimation();
        resetGraph();

        switch (algo) {
            case 'astar':   visualiseAStar(); break;
            case 'dijkstra': visualiseDijkstra(); break;
            case 'bfs':      visualiseBFS(); break;
            case 'dfs':      visualiseDFS(); break;
        }
    }

    function visualiseAStar() {
        aStarAlgorithm = new AStar(grid.getNodeGrid(), grid.startNode, grid.goalNode);
        aStarAlgorithm.start();
        setStatus('Running A* algorithm...');

        animationTimer = setInterval(() => {
            if (!aStarAlgorithm.isPathSet() && !aStarAlgorithm.stopSearch) {
                try {
                    aStarAlgorithm.run();
                    renderAlgorithm();
                } catch (e) {
                    console.error(e);
                    stopCurrentAnimation();
                    setStatus('A* error: ' + e.message);
                }
            } else {
                stopCurrentAnimation();
                renderAlgorithm();
                setStatus(aStarAlgorithm.isPathSet()
                    ? 'A* — Path found! Length: ' + aStarAlgorithm.finalPath.length
                    : 'A* — No path found');
            }
        }, getAnimationDelay());
    }

    function visualiseBFS() {
        bfsAlgorithm = new BFS(grid.getNodeGrid(), grid.startNode, grid.goalNode);
        bfsAlgorithm.start();
        setStatus('Running BFS algorithm...');

        animationTimer = setInterval(() => {
            if (!bfsAlgorithm.isPathSet() && !bfsAlgorithm.stopSearch) {
                try {
                    bfsAlgorithm.run();
                    renderAlgorithm();
                } catch (e) {
                    console.error(e);
                    stopCurrentAnimation();
                    setStatus('BFS error: ' + e.message);
                }
            } else {
                stopCurrentAnimation();
                renderAlgorithm();
                setStatus(bfsAlgorithm.isPathSet()
                    ? 'BFS — Path found! Length: ' + bfsAlgorithm.finalPath.length
                    : 'BFS — No path found');
            }
        }, getAnimationDelay());
    }

    function visualiseDFS() {
        dfsAlgorithm = new DFS(grid.getNodeGrid(), grid.startNode, grid.goalNode);
        dfsAlgorithm.start();
        setStatus('Running DFS algorithm...');

        animationTimer = setInterval(() => {
            if (!dfsAlgorithm.isPathSet() && !dfsAlgorithm.stopSearch) {
                try {
                    dfsAlgorithm.run();
                    renderAlgorithm();
                } catch (e) {
                    console.error(e);
                    stopCurrentAnimation();
                    setStatus('DFS error: ' + e.message);
                }
            } else {
                stopCurrentAnimation();
                renderAlgorithm();
                setStatus(dfsAlgorithm.isPathSet()
                    ? 'DFS — Path found! Length: ' + dfsAlgorithm.finalPath.length
                    : 'DFS — No path found');
            }
        }, getAnimationDelay());
    }

    function visualiseDijkstra() {
        dijkstraAlgorithm = new Dijkstra(grid.getNodeGrid(), grid.startNode, grid.goalNode);
        dijkstraAlgorithm.start();
        setStatus('Running Dijkstra\'s algorithm...');

        animationTimer = setInterval(() => {
            if (!dijkstraAlgorithm.isPathSet() && !dijkstraAlgorithm.stopSearch) {
                try {
                    dijkstraAlgorithm.run();
                    renderAlgorithm();
                } catch (e) {
                    console.error(e);
                    stopCurrentAnimation();
                    setStatus('Dijkstra error: ' + e.message);
                }
            } else {
                stopCurrentAnimation();
                renderAlgorithm();
                setStatus(dijkstraAlgorithm.isPathSet()
                    ? 'Dijkstra — Path found! Length: ' + dijkstraAlgorithm.finalPath.length
                    : 'Dijkstra — No path found');
            }
        }, getAnimationDelay());
    }

    // ===== Canvas Mouse Events =====
    function getGridCoords(e) {
        const rect = canvas.getBoundingClientRect();
        const mx = e.clientX - rect.left;
        const my = e.clientY - rect.top;
        return {
            x: Math.floor(mx / pixelSize),
            y: Math.floor(my / pixelSize)
        };
    }

    let isMouseDown = false;

    canvas.addEventListener('mousedown', (e) => {
        isMouseDown = true;
        const { x, y } = getGridCoords(e);
        if (!grid.isInGrid(x, y)) return;

        if (currentTool === 'setStart') {
            if (isStartNodePlaced) {
                grid.removeStartNode();
            }
            grid.setStartNode(x, y);
            isStartNodePlaced = true;
            resetGraph();
            setStatus('Start node placed at (' + x + ', ' + y + ')');
        } else if (currentTool === 'setGoal') {
            if (isGoalNodePlaced) {
                grid.removeGoalNode();
            }
            grid.setGoalNode(x, y);
            isGoalNodePlaced = true;
            resetGraph();
            setStatus('Goal node placed at (' + x + ', ' + y + ')');
        } else if (currentTool === 'drawWalls') {
            if (!grid.isStartNode(x, y) && !grid.isGoalNode(x, y)) {
                grid.setWallNode(x, y, true);
                resetGraph();
            }
        } else if (currentTool === 'eraseWalls') {
            grid.setWallNode(x, y, false);
            resetGraph();
        }
    });

    canvas.addEventListener('mousemove', (e) => {
        if (!isMouseDown) return;
        const { x, y } = getGridCoords(e);
        if (!grid.isInGrid(x, y)) return;

        if (currentTool === 'drawWalls') {
            if (!grid.isStartNode(x, y) && !grid.isGoalNode(x, y)) {
                grid.setWallNode(x, y, true);
                resetGraph();
            }
        } else if (currentTool === 'eraseWalls') {
            grid.setWallNode(x, y, false);
            resetGraph();
        }
    });

    canvas.addEventListener('mouseup', () => { isMouseDown = false; });
    canvas.addEventListener('mouseleave', () => { isMouseDown = false; });

    // Touch support for mobile
    canvas.addEventListener('touchstart', (e) => {
        e.preventDefault();
        const touch = e.touches[0];
        canvas.dispatchEvent(new MouseEvent('mousedown', {
            clientX: touch.clientX, clientY: touch.clientY
        }));
    }, { passive: false });

    canvas.addEventListener('touchmove', (e) => {
        e.preventDefault();
        const touch = e.touches[0];
        canvas.dispatchEvent(new MouseEvent('mousemove', {
            clientX: touch.clientX, clientY: touch.clientY
        }));
    }, { passive: false });

    canvas.addEventListener('touchend', (e) => {
        e.preventDefault();
        canvas.dispatchEvent(new MouseEvent('mouseup'));
    }, { passive: false });

    // ===== Button Event Listeners =====

    btnVisualise.addEventListener('click', visualise);

    btnHelp.addEventListener('click', () => {
        modalOverlay.classList.add('show');
    });

    btnCloseModal.addEventListener('click', () => {
        modalOverlay.classList.remove('show');
    });

    modalOverlay.addEventListener('click', (e) => {
        if (e.target === modalOverlay) modalOverlay.classList.remove('show');
    });

    // About menu item
    document.getElementById('menuAbout').addEventListener('click', () => {
        modalOverlay.classList.add('show');
    });

    btnCreateGrid.addEventListener('click', () => {
        const input = gridSizeInput.value;
        stopCurrentAnimation();

        if (input && !isNaN(input) && parseInt(input) > 0) {
            pixelSize = parseInt(input);
        } else if (!input) {
            pixelSize = 20; // default
        } else {
            showAlert('Please enter a valid positive number for grid size');
            return;
        }

        widthOfWindow = Math.floor(canvas.width / pixelSize);
        heightOfWindow = Math.floor(canvas.height / pixelSize);
        grid = new Grid(widthOfWindow, heightOfWindow);
        grid.initialiseGridNodes(grid.getNodeGrid());

        isStartNodePlaced = false;
        isGoalNodePlaced = false;
        clearToolHighlights();
        currentTool = '';
        resetGraph();
        setStatus('Grid created: ' + widthOfWindow + '×' + heightOfWindow + ' cells (' + pixelSize + 'px each)');
    });

    btnClearGrid.addEventListener('click', async () => {
        const ok = await showConfirm('Clear Grid', 'Are you sure you want to clear the grid?');
        if (ok) {
            stopCurrentAnimation();
            init();
            setStatus('Grid cleared');
        }
    });

    btnSetStart.addEventListener('click', () => {
        setActiveTool('setStart', btnSetStart);
        setStatus('Click on the grid to place Start node');
    });

    btnSetGoal.addEventListener('click', () => {
        setActiveTool('setGoal', btnSetGoal);
        setStatus('Click on the grid to place Goal node');
    });

    btnDrawWalls.addEventListener('click', () => {
        setActiveTool('drawWalls', btnDrawWalls);
        setStatus('Click and drag on the grid to draw walls');
    });

    btnEraseWalls.addEventListener('click', () => {
        setActiveTool('eraseWalls', btnEraseWalls);
        setStatus('Click and drag on the grid to erase walls');
    });

    btnCreateMaze.addEventListener('click', () => {
        const mazeType = mazeSelect.value;
        if (!mazeType) {
            showAlert('Please select a maze type from the dropdown');
            return;
        }

        stopCurrentAnimation();

        if (mazeType === 'mazeDFS') {
            const generator = new MazeDFSGenerator(widthOfWindow, heightOfWindow);
            grid.initialiseGridNodes(generator.getNodeGrid());
            grid.startNode = null;
            grid.goalNode = null;
            isStartNodePlaced = false;
            isGoalNodePlaced = false;
            resetGraph();
            setStatus('DFS Maze generated');
        } else if (mazeType === 'random') {
            const generator = new RandomMazeGenerator(widthOfWindow, heightOfWindow);
            grid.initialiseGridNodes(generator.getNodeGrid());
            grid.startNode = null;
            grid.goalNode = null;
            isStartNodePlaced = false;
            isGoalNodePlaced = false;
            resetGraph();
            setStatus('Random Maze generated (40% wall density)');
        }
    });

    // ===== Save / Load Maze (using localStorage) =====
    btnSaveMaze.addEventListener('click', async () => {
        const name = mazeNameInput.value.trim();
        if (!name) {
            showAlert('Please enter a name for the maze');
            return;
        }

        const key = 'maze_' + name;
        if (localStorage.getItem(key)) {
            const ok = await showConfirm('File exists', 'Maze "' + name + '" already exists. Overwrite?');
            if (!ok) return;
        }

        const data = grid.exportMaze(pixelSize);
        localStorage.setItem(key, JSON.stringify(data));
        setStatus('Maze "' + name + '" saved successfully');
    });

    btnLoadMaze.addEventListener('click', () => {
        const name = mazeNameInput.value.trim();
        if (!name) {
            showAlert('Please enter the name of the maze to load');
            return;
        }

        const key = 'maze_' + name;
        const raw = localStorage.getItem(key);
        if (!raw) {
            showAlert('Maze "' + name + '" not found. Available mazes: ' +
                Object.keys(localStorage).filter(k => k.startsWith('maze_')).map(k => k.slice(5)).join(', ') || 'none');
            return;
        }

        stopCurrentAnimation();

        const data = JSON.parse(raw);
        pixelSize = data.pixelSize || 20;
        widthOfWindow = data.width;
        heightOfWindow = data.height;

        grid.importMaze(data);

        isStartNodePlaced = !!grid.startNode;
        isGoalNodePlaced = !!grid.goalNode;

        resetGraph();
        setStatus('Maze "' + name + '" loaded');
    });

    // ===== Speed slider label =====
    speedSlider.addEventListener('input', () => {
        animationSpeed = parseInt(speedSlider.value);
    });

    // ===== Window resize =====
    window.addEventListener('resize', () => {
        resizeCanvas();
        widthOfWindow = Math.floor(canvas.width / pixelSize);
        heightOfWindow = Math.floor(canvas.height / pixelSize);
        // Rebuild grid to fit new canvas (loses current state)
        grid = new Grid(widthOfWindow, heightOfWindow);
        grid.initialiseGridNodes(grid.getNodeGrid());
        isStartNodePlaced = false;
        isGoalNodePlaced = false;
        resetGraph();
    });

    // ===== Keyboard shortcuts =====
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            currentTool = '';
            clearToolHighlights();
            setStatus('Tool deselected');
            modalOverlay.classList.remove('show');
            confirmOverlay.classList.remove('show');
        }
        if (e.key === 'v' || e.key === 'V') {
            if (document.activeElement.tagName !== 'INPUT') {
                visualise();
            }
        }
    });

    // ===== Start =====
    init();
})();
