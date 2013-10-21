import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Board {
	
	private int N;
	
	private int[][] board;
	
	private int[][] copyBoard(int[][] src) {
		int[][] tar = new int[src.length][src.length];
		for(int i=0;i<src.length;i++)
			for(int j=0;j<src.length;j++) tar[i][j]=src[i][j];
		return tar;
	}

	
    public Board(int[][] blocks) {
    	// construct a board from an N-by-N array of blocks (where blocks[i][j] = block in row i, column j)
    	this.N = blocks.length;
    	this.board = copyBoard(blocks);
    }
                                           
    public int dimension() {
    	// board dimension N
    	return N;
    }
    
    public int hamming() {
    	// number of blocks out of place
    	int value = 0;
    	for(int i=0;i<N*N;i++) {
    		int r = i/N;
    		int c = i%N;
    		if(this.board[r][c]!=0 && this.board[r][c] != (i+1)) value++;
    	}
    	return value;
    }
    
    public int manhattan() {
    	// sum of Manhattan distances between blocks and goal
    	int cnt = 0;
    	for(int i=0;i<N*N;i++) {    		
    		int r = i/N;
    		int c = i%N;
    		if(this.board[r][c]==0) continue;
    		int nr = (this.board[r][c]-1)/N;
    		int nc = (this.board[r][c]-1)%N;
    		cnt += Math.abs(nr-r)+Math.abs(nc-c);
    	}
    	return cnt;
    }
    
    public boolean isGoal() {
    	// is this board the goal board?
    	for(int i=0;i<N*N-1;i++) {
    		int r = i/N;
    		int c = i%N;
    		if(this.board[r][c]-1!=i) return false;
    	}
    	return true;
    }
    
    public Board twin() {
    	// a board obtained by exchanging two adjacent blocks in the same row
    	int[][] newBoard = copyBoard(this.board);
    	boolean flag = false;
    	for(int i=0;i<N;i++){
    		for(int j=0;j<N-1;j++){
    			if(newBoard[i][j]>0 && newBoard[i][j+1]>0) {
    				int tmp = newBoard[i][j];
    				newBoard[i][j] = newBoard[i][j+1];
    				newBoard[i][j+1] = tmp;
    				flag = true;
    				break;
    			}
    		}
    		if(flag) break;
    	}
    	return new Board(newBoard);
    }
    
    public boolean equals(Object y) {
    	// does this board equal y?
    	if(y==this) return true;
    	if(y==null) return false;
    	if(!(y instanceof Board)) return false;
    	if(((Board)y).N!=this.N) return false;
    	for (int r = 0; r < N; r++)
            if (!Arrays.equals(this.board[r], ((Board)y).board[r]))
                return false;
    	return true;
    }
    
    public Iterable<Board> neighbors() {
    	// all neighboring boards
    	List<Board> boards = new ArrayList<Board>();
    	int[] mx = {1,-1,0,0};
    	int[] my = {0,0,1,-1};
    	boolean foundFlag = false;
    	for(int i=0;i<N;i++){
    		for(int j=0;j<N;j++){
    			if(this.board[i][j]==0) {
    				for(int k=0;k<4;k++){
    					int ni = i + mx[k];
    					int nj = j + my[k];
    					if(ni>=0 && nj>=0 && ni<N && nj<N) {
    						int[][] newB = copyBoard(this.board);
    						newB[i][j] = this.board[ni][nj];
    						newB[ni][nj] = this.board[i][j];
    						Board newBoard = new Board(newB);
    						boards.add(newBoard);
    					}
    				}
    				foundFlag = true;
    				break;
    			}
    		}
    		if(foundFlag) break;
    	}
    	return boards;
    }
    
    public String toString() {
    	// string representation of the board (in the output format specified below)
    	StringBuilder str = new StringBuilder();
    	str.append(N+"\n");
    	for(int i=0;i<N;i++) {
    		for(int j=0;j<N;j++) str.append(this.board[i][j]+" ");
    		str.append("\n");
    	}
    	return str.toString();
    }
}