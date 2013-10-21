public class Solver {
	
	private class Node implements Comparable<Node> {
		private Board board;
		private int numMoves;
		private Node previous;
		public Node(Board board, Node previous){
			this.board = board;
			this.previous = previous;
			if(previous==null) this.numMoves = 0;
			else this.numMoves = previous.numMoves + 1;
		}
		@Override
		public int compareTo(Node o) {
			return (this.board.manhattan() - o.board.manhattan()) + (this.numMoves - o.numMoves);
		}
		
	};
	
	private Node lastNode = null;
	
    public Solver(Board initial) {
    	// find a solution to the initial board (using the A* algorithm)  	
    	MinPQ<Node> Q = new MinPQ<Node>();
    	MinPQ<Node> SQ = new MinPQ<Node>();
    	Q.insert(new Node(initial, null));
    	SQ.insert(new Node(initial.twin(), null));
    	
    	boolean sSolvable = false;
    	boolean solvable = false;
    	while(!solvable && !sSolvable) { //!solvable && 
    		lastNode = expand(Q);
    		solvable = (lastNode!=null);
    		sSolvable = (expand(SQ)!=null);
    	}
    }
    
    private Node expand(MinPQ<Node> queue){
    	if(queue.isEmpty()) return null;
    	Node current = queue.delMin();
    	//StdOut.println(current.board);
    	if(current.board.isGoal()) return current;
    	
    	for(Board b : current.board.neighbors()) {
    		if(current.previous==null || !b.equals(current.previous.board)) {
    			queue.insert(new Node(b, current));
    		}
    	}
    	return null;
    }
    
    public boolean isSolvable() {
    	// is the initial board solvable?
    	return lastNode!=null;
    }
    
    public int moves() {
    	// min number of moves to solve initial board; -1 if no solution
    	if(lastNode!=null) return lastNode.numMoves;
    	else return -1;
    }
    
    public Iterable<Board> solution() {
    	// sequence of boards in a shortest solution; null if no solution
    	if(lastNode!=null){
    		Stack<Board> res = new Stack<Board>();
    		for(Node tail=lastNode; tail!=null; tail = tail.previous){
    			res.push(tail.board);
    		}
    		return res;
    	}
    	else return null;
    }
	
	public static void main(String[] args) { 
	    // create initial board from file
	    In in = new In("./8puzzle/puzzle12.txt");
	    int N = in.readInt();
	    int[][] blocks = new int[N][N];
	    for (int i = 0; i < N; i++)
	        for (int j = 0; j < N; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);

	    // solve the puzzle
	    Solver solver = new Solver(initial);

	    // print solution to standard output
	    if (!solver.isSolvable())
	        StdOut.println("No solution possible");
	    else {
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board : solver.solution())
	            StdOut.println(board);
	    }
	}
}
