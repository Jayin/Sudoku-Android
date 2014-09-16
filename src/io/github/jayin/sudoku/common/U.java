package io.github.jayin.sudoku.common;

public class U {
	public static void copyMatrix(int[][] to, int[][] from) {
		int row = from.length;
		for (int i = 0; i < row; i++)
			for (int j = 0; j < row; j++)
				to[i][j] = from[i][j];
	}

}
