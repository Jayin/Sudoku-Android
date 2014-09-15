package io.github.jayin.sudoku.core;

import io.github.jayin.sudoku.core.Table.PendingNode;

import java.util.List;

/**
 * 数独 <strong>Usage</strong> TODO 根据给出的难度,自动生成一个数独矩阵
 * 
 * <pre>
 * // 解
 * Sudoku sudoku = new Sudoku();
 * sudoku.init(cur_Matrix).solve();
 * </pre>
 * 
 * <pre>
 * // 检查矩阵
 * new Sudoku.check(cur_Matrix);
 * </pre>
 * <p>
 * 实现思路
 * </p>
 * <p>
 * 1.计算出每行，每列，每模块的待填列表
 * </p>
 * <p>
 * 2.对待填列表排序，待填数较少的排在前面
 * </p>
 * <p>
 * 3.待填数为1的，直接填上
 * </p>
 * 
 * @author Jayin
 * 
 */
public class Sudoku {
	/** 数独表 */
	Table table;
	/** 待填点 */
	List<PendingNode> pendingNodes;
	/** 待填点 */
	boolean debug = false;
	/** 起始时间 */
	long startTime;
	long costTime;

	int[][] result = new int[Table.ROW][Table.ROW];

	public Sudoku() {
		this(false);
	}

	/**
	 * 是否开启调试模式
	 * 
	 * @param debug
	 */
	public Sudoku(boolean debug) {
		this.debug = debug;
		startTime = System.currentTimeMillis();
	}

	/**
	 * 检查数独是否正确
	 * 
	 * @param cur_table
	 * @return true if it's correct
	 */
	public boolean check(int[][] cur_table) {
		try {
			for (int i = 0; i < Table.ROW; i++)
				for (int j = 0; j < Table.ROW; j++)
					if (cur_table[i][j] == 0) {
						return false;
					}
			new Table(cur_table);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * 导入数独
	 * 
	 * @param cur_Matrix
	 * @return
	 * @throws Exception 
	 */
	public Sudoku init(int[][] cur_Matrix) throws Exception {
			table = new Table(cur_Matrix);
			pendingNodes = table.getPendingNodes();
			if (debug) {
				for (PendingNode node : table.getPendingNodes()) {
					System.out.println(node.toString());
				}
			}
	
		return this;
	}

	/**
	 * 开始解答
	 */
	public void solve() {
		solve(0);
	}

	/**
	 * 从第一待填点开始解答
	 * 
	 * @param cur
	 */
	private void solve(int cur) {
		if (cur == pendingNodes.size()) {
			costTime = System.currentTimeMillis() - startTime;
			if (debug){
				System.out.println("耗时: " + costTime / 1000.0 + " s");
			}
			int[][] res = table.getCurTable();
			if (check(res)) {
				// print
				for (int i = 0; i < Table.ROW; i++) {
					for (int j = 0; j < Table.ROW; j++) {
						System.out.print(res[i][j] + "");
						result[i][j] = res[i][j];
					}
					System.out.println();
				}
			} else {
				System.out.println("数独解答不正确");
			}

		} else {
			if (pendingNodes.get(cur).getPendingList().size() == 1) {
				PendingNode node = pendingNodes.get(cur);
				int x = node.getX();
				int y = node.getY();
				int v = node.getPendingList().get(0);
				int blockId = table.getBlockId(x, y);
				table.setCurTable(x, y, v);
				table.setRow(x, v, true);
				table.setColumn(y, v, true);
				table.setBlock(blockId, v, true);
				solve(cur + 1);
			} else {
				for (int i = 0; i < pendingNodes.get(cur).getPendingList()
						.size(); i++) {
					PendingNode node = pendingNodes.get(cur);
					int x = node.getX();
					int y = node.getY();
					int v = node.getPendingList().get(i);
					int blockId = table.getBlockId(x, y);
					if (table.enableRow(x, v) && table.enableColumn(y, v)
							&& table.enableBlock(blockId, v)) {
						table.setCurTable(x, y, v);
						table.setRow(x, v, true);
						table.setColumn(y, v, true);
						table.setBlock(blockId, v, true);
						solve(cur + 1);
						table.setCurTable(x, y, 0);
						table.setRow(x, v, false);
						table.setColumn(y, v, false);
						table.setBlock(blockId, v, false);
					}
				}
			}
		}
	}

	/**
	 * 获得当前矩阵
	 * 
	 * @return
	 */
	public int[][] getMatrix() {
		return result;
	}
	/**
	 * 获取耗时(单位ms)
	 * @return
	 */
	public long getCostTime(){
		return costTime;
	}
	
	/**
	 * 获取待填点(x,y)的信息
	 * @param x
	 * @param y
	 * @return
	 */
	public PendingNode getPendingNode(int x,int y){
		return table.getPendingNode(x, y);
	}
}