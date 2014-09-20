package io.github.jayin.sudoku.common;

import io.github.jayin.sudoku.core.Table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.view.View;

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
				System.out.println(lineString);
				String[] s = lineString.trim().split(" ");
				for (int j = 0; j < s.length; j++) {
					rawMatrix[line][j] = Integer.parseInt(s[j]);
				}
				line++;
				//only read Line 0-8
				if(line == 9){
					break;
				}
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
	
	public static String getSDPath(){ 
	       File sdDir = null; 
	       boolean sdCardExist = Environment.getExternalStorageState()   
	                           .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在 
	       if   (sdCardExist)   
	       {                               
	         sdDir = Environment.getExternalStorageDirectory();//获取跟目录 
	      }   
	       return sdDir.toString(); 
	}
	
    public static String getAppSDPath(){
    	String path = getSDPath() + File.separator + "Sudoku" +File.separator;
    	File f = new File(path);
    	if(!f.exists()){
    		f.mkdirs();
    	}
    	return path;
    }
    
    public static String getScreenShotPath(){
    	return getAppSDPath() + "screenshot.png";
    }
    /**
     * 截取view视图
     * @param v
     */
	public static void screenShot(View v) {
		Bitmap bmp  = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
		v.draw(new Canvas(bmp ));

		String fname = getScreenShotPath();
		try {
			bmp.compress(CompressFormat.PNG, 100, new FileOutputStream(fname));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	

}
