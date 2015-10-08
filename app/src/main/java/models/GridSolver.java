package models;

/**
 * Created by pushkar on 9/26/15.
 */
public class GridSolver {

    private int[][] grid;
    private boolean solvable;
    private int dimension;
    private int miniGridDimension;

    public GridSolver(int[][] grid) {
        this.grid = getDeepCopy(grid);
        this.dimension = grid.length;
        this.miniGridDimension = (int) Math.sqrt(dimension);
    }

    public GridSolver(String initialValues) {
        this.dimension = (int) Math.sqrt(initialValues.length());
        this.miniGridDimension = (int) Math.sqrt(dimension);
        this.grid = stringToBoard(initialValues);
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int[][] getSolution() {
        solvable = solverHelper();
        return grid;
    }

    private boolean solverHelper() {
        int[] zeroPosition = new int[2];
        boolean noZerosLeft = !findNextZero(zeroPosition);

        if(noZerosLeft) {
            return true;
        }

        int row = zeroPosition[0];
        int col = zeroPosition[1];

        for(int num = 1; num <= 9; num++) {
            grid[row][col] = num;
            if(gridIsValid(row, col)) {
                if(solverHelper())
                    return true;
            }
        }
        grid[row][col] = 0;
        return false;
    }

    private boolean findNextZero(int[] zeroPosition) {
        for(int r = 0; r < dimension; r++) {
            for(int c = 0; c < dimension; c++) {
                if(grid[r][c] == 0) {
                    zeroPosition[0] = r;
                    zeroPosition[1] = c;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean gridIsValid(int row, int col) {
        return rowColValid(row, col) && miniGridValid(row, col);
    }

    private boolean rowColValid(int row, int col) {
        int value = grid[row][col];

        for(int c = 0; c < dimension; c++) {
            if(c != col && grid[row][c] == value)
                return false;
        }
        for(int r = 0; r < dimension; r++) {
            if(r != row && grid[r][col] == value)
                return false;
        }
        return true;
    }

    private boolean miniGridValid(int row, int col) {
        int value = grid[row][col];

        int startRow = (row / miniGridDimension) * miniGridDimension;
        int startCol = (col / miniGridDimension) * miniGridDimension;

        for(int r = startRow; r < startRow + miniGridDimension; r++) {
            for(int c = startCol; c < startCol + miniGridDimension; c++) {
                if(!(r == row && c == col) && grid[r][c] == value)
                    return false;
            }
        }
        return true;
    }

    private int[][] getDeepCopy(int[][] inputGrid) {
        int[][] deepCopy = new int[inputGrid.length][inputGrid[0].length];

        for(int r = 0; r < inputGrid.length; r++) {
            for(int c = 0; c < inputGrid[0].length; c++) {
                deepCopy[r][c] = inputGrid[r][c];
            }
        }
        return deepCopy;
    }

    private int[][] stringToBoard(String values) {
        int[][] grid = new int[dimension][dimension];
        for(int i = 0; i < dimension; i++) {
            for(int j = 0; j < dimension; j++) {
                grid[i][j] = Integer.parseInt(String.valueOf(values.charAt((i * dimension) + j)));
            }
        }
        return grid;
    }
}
