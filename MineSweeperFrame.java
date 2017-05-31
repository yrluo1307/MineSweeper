import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MineSweeperFrame {
	public static final int NUM_OF_ROWS = 16;
	public static final int NUM_OF_COLS = 16;
	public static final int NUM_OF_MINES = 40;
	public static final int SIZE_OF_GRID = 16; // 16px per grid
	
	public static void main(String[] args) throws IOException {
		MineSweeperFrame f = new MineSweeperFrame();
	}
	
	MineSweeper ms;
	boolean started;
	JFrame f;
	JButton[][] buttons;
	int unexplored;
	long time1;  // start time
	ImageIcon[] icons;
	
	public MineSweeperFrame() throws IOException {
		f = new JFrame("MineSweeper");
		f.setSize(NUM_OF_COLS * SIZE_OF_GRID, NUM_OF_ROWS * SIZE_OF_GRID);
		ms = new MineSweeper(NUM_OF_ROWS, NUM_OF_COLS, NUM_OF_MINES);
		started = false;
		buttons = new JButton[NUM_OF_ROWS][NUM_OF_COLS];
		
		// Initialize icons:
		icons = new ImageIcon[11];
		for (int i = 0; i < 10; i++) {
			Image img = ImageIO.read(new File("2000px-Minesweeper_" + i + ".svg.png"));
			Image newimg = img.getScaledInstance(SIZE_OF_GRID - 1, SIZE_OF_GRID - 1, 
					java.awt.Image.SCALE_SMOOTH);
			icons[i] = new ImageIcon(newimg);
		}
		icons[10] = new ImageIcon(ImageIO.read(new File("flag.png")).
				getScaledInstance(SIZE_OF_GRID - 1, SIZE_OF_GRID - 1, java.awt.Image.SCALE_SMOOTH));
		for (int i = 0; i < NUM_OF_ROWS; i++) {
			for (int j = 0; j < NUM_OF_COLS; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].setBounds(j * SIZE_OF_GRID, i * SIZE_OF_GRID, SIZE_OF_GRID, SIZE_OF_GRID);
				buttons[i][j].setIcon(icons[9]);
				f.add(buttons[i][j]);
				buttons[i][j].addActionListener(new ButtonHandler(i, j, ms));
			}
		}
		f.setLayout(null);
		f.setVisible(true);
		f.setSize(NUM_OF_COLS * SIZE_OF_GRID, (1 + NUM_OF_ROWS) * SIZE_OF_GRID + 5);
		unexplored = NUM_OF_ROWS * NUM_OF_COLS - NUM_OF_MINES;
		time1 = 0;
	}
	
	class ButtonHandler implements ActionListener {
		private int row, col;
	    	private MineSweeper game;

	    	public ButtonHandler(int r, int c, MineSweeper g) {
	        	row = r;
	        	col = c;
	        	game = g;
	    	}

	    	public void actionPerformed(ActionEvent event) {
	    		JButton b = (JButton) event.getSource();
			int row = b.getY() / SIZE_OF_GRID;
			int col = b.getX() / SIZE_OF_GRID;
			click(row, col);
	    	}
	    
		private void click(int row, int col) {
			if (!started) {
				started = true;
				time1 = System.currentTimeMillis();
			}
			
			if (!game.states[row][col]) {
				game.states[row][col] = true;
				int info = game.grids[row][col];
				if (info == -1) {
					JOptionPane.showMessageDialog(null, "GAME OVER!!");
					System.exit(0);
				} else {
					unexplored--;
					buttons[row][col].setIcon(icons[info]);
					if (info == 0) {
						for (int i = -1; i <= 1; i++) {
							for (int j = -1; j <= 1; j++) {
								if (i != 0 || j != 0) {
									try {
										click(row + i, col + j);
									} catch (IndexOutOfBoundsException e) {
										// do nothing
									}
								}
							}
						}
					}
				}
			}
			
			if (unexplored == 0) {
				long time2 = System.currentTimeMillis();
				for (int mine : game.mines) {
					int x = mine % game.w;
					int y = mine / game.w;
					buttons[y][x].setIcon(icons[10]);
				}
				JOptionPane.showMessageDialog(null, "You win in " + (time2 - time1) / 1000.0 + " seconds!");
				System.exit(0);
			}
		}
	}
}
