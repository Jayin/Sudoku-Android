package io.github.jayin.sudoku.common;

import io.github.jayin.sudoku.core.Table;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;

public class U {
	private static final String Sudoku_Folder = "sudoku" + File.separator;
	private static final String Suffix = ".su";

	public static void copyMatrix(int[][] to, int[][] from) {
		int row = from.length;
		for (int i = 0; i < row; i++)
			for (int j = 0; j < row; j++)
				to[i][j] = from[i][j];
	}

	public static int[][] readMatrix(Context context, int lv, int num) {
		int[][] rawMatrix = new int[Table.ROW][Table.ROW];
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(context.getAssets()
					.open(Sudoku_Folder + lv + File.separator + num + Suffix)));
			Log.d("debug", "file url = " + Sudoku_Folder + lv + File.separator
					+ num + Suffix);
			String lineString = null;
			int line = 0;
			while ((lineString = br.readLine()) != null) {
				String[] s = lineString.split(" ");
				for (int j = 0; j < s.length; j++) {
					rawMatrix[line][j] = Integer.parseInt(s[j]);
				}
				line++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return rawMatrix;
	}

	public static String readJsonFile(Context context, int lv) {
		String file_name = Sudoku_Folder + lv + File.separator + lv + ".json";
		String json = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(context.getAssets()
					.open(file_name)));
			String lineString = null;
			while ((lineString = br.readLine()) != null) {
				json += lineString;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return json;
	}

}
