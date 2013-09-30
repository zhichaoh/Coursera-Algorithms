

public class Board {
	
	private int N;
	
	private int[][] board;
	
	private int[][] goal;
	
	private int[][] copyBoard(int[][] src) {
		int[][] tar = new int[src.length][src.length];
		for(int i=0;i<src.length;i++)
			for(int j=0;j<src.length;j++) tar[i][j]=src[i][j];
		return tar;
	}
	
	private int outPlace(int[][] src, int[][] tar) {
		int cnt = 0;
		for(int i=0;i<src.length;i++)
			for(int j=0;j<src.length;j++)
				if(src[i][j]!=tar[i][j]) cnt++;
		return cnt;
	}
	
    public Board(int[][] blocks) {
    	// construct a board from an N-by-N array of blocks (where blocks[i][j] = block in row i, column j)
    	this.N = blocks.length;
    	this.board = copyBoard(blocks);
    	this.goal = new int[N][N];
    	for(int i=0;i<N*N-1;i++) {
    		int r = i/N;
    		int c = i%N;
    		this.goal[r][c] = i+1;
    	}
    }
                                           
    public int dimension() {
    	// board dimension N
    	return N;
    }
    
    public int hamming() {
    	// number of blocks out of place
    	return outPlace(this.board, this.goal);
    }
    
    public int manhattan() {
    	// sum of Manhattan distances between blocks and goal
    	int cnt = 0;
    	for(int i=0;i<N*N;i++) {
    		int r = i/N;
    		int c = i%N;
    		int nr = this.board[r][c]/N;
    		int nc = this.board[r][c]%N;
    		if(this.board[r][c]!=0)
    			cnt += Math.abs(nr-r)+Math.abs(nc-c);
    	}
    	return cnt;
    }
    
    public boolean isGoal() {
    	// is this board the goal board?
    	for(int i=0;i<N*N;i++) {
    		int r = i/N;
    		int c = i%N;
    		if(this.board[r][c]!=i) return false;
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
    				break;
    			}
    		}
    		if(flag) break;
    	}
    	return new Board(newBoard);
    }
    
    public boolean equals(Object y) {
    	// does this board equal y?
    	if(y==null) return false;
    	if(!(y instanceof Board)) return false;
    	return this.outPlace(board, ((Board)y).board)==0;
    }
    public Iterable<Board> neighbors() {
    	// all neighboring boards
    }
    public String toString() {
    	// string representation of the board (in the output format specified below)
    }
}