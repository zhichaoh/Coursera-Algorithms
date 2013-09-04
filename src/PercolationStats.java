public class PercolationStats {

	private double mean = 0.0;

	private double std = 0.0;

	private double upperConf = 0.0;

	private double lowerConf = 0.0;

	public PercolationStats(int N, int T) { // perform T independent
											// computational experiments on an
											// N-by-N grid
		if (N <= 0 || T <= 0)
			throw new java.lang.IllegalArgumentException();
		double[] res = new double[T];
		for (int t = 0; t < T; t++) {
			Percolation perObj = new Percolation(N);
			for (int c = 0; c < N * N; c++) {
				int pt = (int) (Math.random() * N * N);
				int i = pt / N + 1, j = pt % N + 1;
				while (perObj.isOpen(i, j)) {
					pt = (int) (Math.random() * N * N);
					i = pt / N + 1;
					j = pt % N + 1;
				}
				perObj.open(i, j);
				if (perObj.percolates()) {
					res[t] = ((double) c) / (N * N);
					break;
				}
			}
		}
		// Mean
		for (int t = 0; t < T; t++)
			mean += res[t];
		mean /= T;
		// Std
		for (int t = 0; t < T; t++)
			std += (res[t] - mean) * (res[t] - mean);
		std /= (T - 1);
		std = Math.sqrt(std);
		// confidence
		lowerConf = mean - 1.96 * std / Math.sqrt(T + 0.0);
		upperConf = mean + 1.96 * std / Math.sqrt(T + 0.0);
	}

	public double mean() { // sample mean of percolation threshold
		return mean;
	}

	public double stddev() { // sample standard deviation of percolation
								// threshold
		return std;
	}

	public double confidenceLo() { // returns lower bound of the 95% confidence
									// interval
		return lowerConf;
	}

	public double confidenceHi() { // returns upper bound of the 95% confidence
									// interval
		return upperConf;
	}

	public static void main(String[] args) { // test client, described below
		PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]),
				Integer.parseInt(args[1]));
		System.out.println("mean                    = " + ps.mean());
		System.out.println("stddev                  = " + ps.stddev());
		System.out.println("95% confidence interval = " + ps.confidenceLo()
				+ ", " + ps.confidenceHi());
	}
}
