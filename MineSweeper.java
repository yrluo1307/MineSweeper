import java.util.Random;

class MineSweeper {
	int h, w;
	int unexplored;
	int[][] grids;
	boolean[][] states;  // false: unclicked; true: clicked
	int[] mines;
	Random r;

	// Create a new instance of Minesweeper game:
	public MineSweeper(int nh, int nw, int m) {
		// Initialize:
		h = nh;
		w = nw;
		unexplored = h * w - m;
		r = new Random();
		grids = new int[h][w];
		states = new boolean[h][w];
		
		// Set mines randomly:
		mines = r.ints(0, h * w).distinct().limit(m).sorted().toArray();
		for (int loc : mines) {
			int x = loc % w;
			int y = loc / w;
			grids[y][x] = -2;
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					try {
						if (grids[y + i][x + j] != -1) {
							grids[y + i][x + j]++;
						}
					} catch (IndexOutOfBoundsException e) {
						// do nothing
					}
				}
			}
		}
	}
}
