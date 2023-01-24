import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Minesweeper (text-based)
 * @author Joshua Knott
 * 01/22-23/2023
 *
 */

public class Main {
	// checks if a certain coordinate exists on the grid of tiles
	public static boolean exists(int y, int x, int height, int width) {
		if (y >= 0 && y < height && x >= 0 && x < width) {
			return true;
		}
		return false;
	}
	
	// displays the grid of tiles all nice and neat
	public static void showTiles(Tile[][] arr, int height, int width) {
		// shows x-indices at top
		System.out.print(" ");
		for (int n = 0; n < width; n++) {
			System.out.printf("%3d", n + 1 );
		}
		System.out.println();
		
		// shows y-indices, all tiles in 2d array, y-indices again
		for (int y = 0; y < height; y++) {
			System.out.printf("%2d|", y + 1);
			for (int x = 0; x < width - 1; x++) {
				System.out.print((arr[y][x].getAppearance()) + "  ");
			}
			System.out.print((arr[y][width - 1].getAppearance()));
			System.out.printf("|%2d\n", y + 1);
		}
		
		// shows x-indices at bottom
		System.out.print(" ");
		for (int n = 0; n < width; n++) {
			System.out.printf("%3d", n + 1 );
		}
		System.out.println();
	}
	
	// recursive function that uncovers all neighboring squares of 'zero value' tiles
	public static void uncover(Tile[][] arr, int height, int width, int yi, int xi) {
		int kx, ky;
		for (int dy = -1; dy <= 1; dy++) {
			for (int dx = -1; dx <= 1; dx++) {
				ky = arr[yi][xi].y + dy;
				kx = arr[yi][xi].x + dx;
				if (exists(ky, kx, height, width) && (arr[ky][kx].isCovered)) {
					arr[ky][kx].isCovered = false;
					if (arr[ky][kx].numBombNeighbors == 0) {
						uncover(arr, height, width, ky, kx);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		final int TILES_H = 10;
		final int TILES_W = 20;
		int cy, cx;
		boolean gameRunning = true;
		double bombDensity = 0.25;
		String moveStr;
		int move_x, move_y;
		char move_action;
		int move_diff;
		boolean valid_move;
		boolean valid_diff = false;
		Tile[][] tiles = new Tile[TILES_H][TILES_W];
		List<String> inputs;
		
		try (Scanner sc = new Scanner(System.in)) {
			
			// choose difficulty - difficulty is proportional to bomb density
			System.out.print("Choose difficulty:\n[1] Easy\n[2] Normal\n[3] Hard\n> ");		
			
			while (!valid_diff) {
				move_diff = sc.nextInt();
				if (move_diff < 1 || move_diff > 3) {
					System.out.println("Invalid option - try again.");
				} 
				if (move_diff == 1) {
					bombDensity = 0.125;
				} else if (move_diff == 2) {
					bombDensity = 0.25;
				} else {
					bombDensity = 0.35;
				}
				valid_diff = true;
			}
			
			
			// initialize each object in the 2d array
			for (int y = 0; y < TILES_H; y++) {
				for (int x = 0; x < TILES_W; x++) {
					tiles[y][x] = new Tile(y, x, Math.random() <= bombDensity);
				}
			}
			
			
			// calculate the number of bomb neighbors for all tiles
			for (int y = 0; y < TILES_H; y++) {
				for (int x = 0; x < TILES_W; x++) {
					for (int dy = -1; dy <= 1; dy++) {
						for (int dx = -1; dx <= 1; dx++) {
							cx = x + dx;
							cy = y + dy;
							if (exists(cy, cx, TILES_H, TILES_W)) {
								if (tiles[cy][cx].isBomb) {
									tiles[y][x].numBombNeighbors++;
								}
							}
						}
					}
				}
			}
			
			// the whole running game
			while (gameRunning) {
				valid_move = false;
				showTiles(tiles, TILES_H, TILES_W);
				System.out.print("Enter as: <x_pos, y_pos, action>\t(action: f = flag, d = dig).\n> ");
				
				while (!valid_move) {
					moveStr = sc.next();
					
					inputs = Arrays.asList(moveStr.split(","));
					move_x = Integer.parseInt(inputs.get(0)) - 1;
					move_y = Integer.parseInt(inputs.get(1)) - 1;
					move_action = inputs.get(2).charAt(0);
					
					// if the move input is valid, go to the tile selected and do the action to it
					if (exists(move_y, move_x, TILES_H, TILES_W)/* && (move_action == 'f' || move_action == 'd')*/) {
						valid_move = true;
						if (move_action == 'f') { // if user wants to put a flag down
							tiles[move_y][move_x].hasFlag = !tiles[move_y][move_x].hasFlag;
						} else { // otherwise, if user wants to dig at that spot
							tiles[move_y][move_x].isCovered = false;
							if (tiles[move_y][move_x].isBomb) {
								gameRunning = false;
								System.out.println("\nYou hit a bomb! You lose! :(");
								System.exit(0);
							}
							if (tiles[move_y][move_x].numBombNeighbors == 0) {							
								uncover(tiles, TILES_H, TILES_W, move_y, move_x);
							}
						}
					} else {
						System.out.print("Invalid input, try again.\n> ");
					}
				}	
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}	
}