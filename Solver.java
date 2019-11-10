
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Solver {
	private static class Board {

		Random random = new Random();

		/**
		 * The row for each column, For example [3,7,0,4,6,1,5,2] represents
		 * 
		 * 
		 *     ..*.....
		 *     .....*..
		 *     .......*
		 *     *.......
		 *     ...*....
		 *     ......*.
		 *     ....*...
		 *     .*......
		 * 
		 */
		int[] queens;
		/**
		 * The queens on each row, r[0] is the number of queens on 1st row and so on.
		 */
		int[] r;
		/**
		 * The number of queens of the main diagonal. d1[0] is the number of queens on the 
		 * left lowest field of the board, and d1[1] is the number of the queens on the two
		 * fields up and right of d1[0], and so on. 
		 */
		int[] d1;
		/**
		 * The number of queens of the secondary diagonal. d2[0] is the number of queens on the 
		 * left uppermost  field of the board, and d1[1] is the number of the queens on the two
		 * fields down and right of d2[0], and so on. 
		 */
		int[] d2;

		/**
		 * Creates a new n x n board and randomly fills it with one queen in each
		 * column.
		 */
		Board(int n) {
			queens = new int[n];
			r = new int[n];
			d1 = new int[2 * n - 1];
			d2 = new int[2 * n - 1];
			scramble();
		}

		/**
		 * Randomly fills the board with one queen in each column. After that, fills the 
		 * arrays with the queens on the rows, on the main diagonal, and on the second diagonal
		 */
		void scramble() {
			for (int i = 0; i < queens.length; i++) {
				queens[i] = random.nextInt(queens.length);
			}
			fillRows();
			fillFirstDiagonal();
			fillSecondDiagonal();
		}
		/**
		 * Fills the array for the number of queens in each row.
		 */
		void fillRows() {
			Arrays.fill(r, 0);
			for (int i = 0; i < queens.length; i++) {
				int indexToIncrese = queens[i];
				r[indexToIncrese]++;
			}
		}
		
		/**
		 * Fills the array for the number of queens in each diagonal on the main diagonal.
		 */
		void fillFirstDiagonal() {
			Arrays.fill(d1, 0);
			for (int i = 0; i < queens.length; i++) {
				int indexDiagonal = i - queens[i] + queens.length - 1;
				d1[indexDiagonal]++;
			}
		}

		/**
		 * Fills the array for the number of queens in each diagonal on the secondary diagonal.
		 */
		void fillSecondDiagonal() {
			Arrays.fill(d2, 0);
			for (int i = 0; i < queens.length; i++) {
				int indexDiagonal = i + queens[i];
				d2[indexDiagonal]++;
			}
		}

		/**
		 * This method returns the column of the queen, that has the most conflicts with
		 * other queens. If there are more queens with equal number of conflicts, the method
		 * returns randomly one of the columns.
		 * @return  int, the index of the column of the queen with most conflicts.
		 */
		int getColWithMaxCnflicts() {
			ArrayList<Integer> cols = new ArrayList<Integer>();

			int max = 0;
			for (int i = 0; i < queens.length; i++) {
				int curr = r[queens[i]] + d1[i - queens[i] + queens.length - 1] + d2[i + queens[i]];
				if (curr > max) {
					max = curr;
					cols = new ArrayList<Integer>();
					cols.add(i);
				} else if (curr == max) {
					cols.add(i);
				}
			}
			return cols.get(random.nextInt(cols.size()));
		}
		
		/**
		 * This is method, which gives the safest position on a given column. If there are more than 1
		 * rows with equal number of conflicts, it returns randomly one of the rows.
		 * @param col, int - the index of the column we want to find the safest position on.
		 * @return int - the index of the row, which is the safest (least conflicts) in the given column
		 */
		int getRowWithMinConflicts(int col) {
			ArrayList<Integer> rows = new ArrayList<Integer>();

			int min = 200000;
			for (int i = 0; i < queens.length; i++) {
				int curr = r[i] + d1[col - i + queens.length - 1] + d2[col + i];
				if (curr < min) {
					min = curr;
					rows = new ArrayList<Integer>();
					rows.add(i);
				} else if (curr == min) {
					rows.add(i);
				}
			}
			return rows.get(random.nextInt(rows.size()));
		}

		/**
		 * This method returns if there are conflicts on the board (If two queens attack each other).
		 */
		boolean hasConflicts() {
			for (int i = 0; i < r.length; i++) {
				if (r[i] >= 2) {
					return true;
				}
			}
			for (int i = 0; i < d1.length; i++) {
				if (d1[i] >= 2) {
					return true;
				}
			}

			for (int i = 0; i < d2.length; i++) {
				if (d2[i] >= 2) {
					return true;
				}
			}
			return false;
		}
		/**
		 * Fills the board with N Queens, so that no two queens attack each other.
		 */
		void solve() {
			int i = 0;
			while (i <= 2 * queens.length) {
				i++;
				int col = getColWithMaxCnflicts();
				int row = getRowWithMinConflicts(col);
				queens[col] = row;
				fillRows();
				fillFirstDiagonal();
				fillSecondDiagonal();
				if (!hasConflicts()) {
					return;
				}
			}
			if (hasConflicts()) {
				scramble();
				solve();
			}
		}

		/**
		 * Prints the board
		 */
		void print() {
			for (int r = 0; r < queens.length; r++) {
				for (int c = 0; c < queens.length; c++) {
					System.out.print(queens[c] == r ? '*' : '.');
				}
				System.out.println();
			}
		}
	}

	/**
	 * Runs the application.
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int dimension = scanner.nextInt();
		Board board = new Board(dimension);
		long start = System.currentTimeMillis();
		board.solve();
		long stop = System.currentTimeMillis();
		System.out.println("Found in " + ((double)(stop-start))/1000 + "s.");
		board.print();
		scanner.close();
	}
}
