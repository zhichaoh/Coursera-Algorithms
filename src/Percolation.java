public class Percolation {

	private boolean[] mat = null;

	private int N = 0;

	private WeightedQuickUnionUF qfObj = null;

	private WeightedQuickUnionUF qfObjEx = null;

	private final int[] mx = { -1, 0, 1, 0 };

	private final int[] my = { 0, -1, 0, 1 };

	public Percolation(int N) { // create N-by-N grid, with all sites blocked
		mat = new boolean[N * N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				mat[i * N + j] = false;
		this.N = N;
		qfObj = new WeightedQuickUnionUF(N * N + 2);
		qfObjEx = new WeightedQuickUnionUF(N * N + 1);
	}

	public void open(int i, int j) { // open site (row i, column j) if it is not
										// already
		if (i < 1 || j < 1 || i > N || j > N)
			throw new java.lang.IndexOutOfBoundsException();
		int ni = i - 1, nj = j - 1;
		if (!mat[ni * N + nj]) {
			mat[ni * N + nj] = true;
			for (int m = 0; m < 4; m++) {
				int mi = ni + mx[m];
				int mj = nj + my[m];
				if (mi >= 0 && mj >= 0 && mi < N && mj < N && mat[mi * N + mj]) {
					qfObj.union(ni * N + nj, mi * N + mj);
					qfObjEx.union(ni * N + nj, mi * N + mj);
				}
			}
			if (i == 1) {
				qfObj.union(ni * N + nj, N * N);
				qfObjEx.union(ni * N + nj, N * N);
			}
			if (i == N)
				qfObj.union(ni * N + nj, N * N + 1);
		}
	}

	public boolean isOpen(int i, int j) { // is site (row i, column j) open?
		if (i < 1 || j < 1 || i > N || j > N)
			throw new java.lang.IndexOutOfBoundsException();
		int ni = i - 1, nj = j - 1;
		return mat[ni * N + nj];
	}

	public boolean isFull(int i, int j) { // is site (row i, column j) full?
		if (i < 1 || j < 1 || i > N || j > N)
			throw new java.lang.IndexOutOfBoundsException();
		int ni = i - 1, nj = j - 1;
		return mat[ni * N + nj] && qfObjEx.connected(ni * N + nj, N * N);
	}

	public boolean percolates() { // does the system percolate?
		return qfObj.connected(N * N, N * N + 1);
	}
}
