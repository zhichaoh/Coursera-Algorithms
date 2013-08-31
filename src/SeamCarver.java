import java.awt.Color;

public class SeamCarver {

	private Picture pic;

	private double[][] energy;

	public SeamCarver(Picture picture) {
		pic = new Picture(picture);
		energy = new double[pic.height()][pic.width()];
	}

	public Picture picture() { 
		// current picture
		return pic;
	}

	public int width() { 
		// width of current picture
		return pic.width();
	}

	public int height() {
		// height of current picture
		return pic.height();
	}

	public double energy(int x, int y) throws IndexOutOfBoundsException { 
		// energy of pixel at column x and row y
		if (x < 0 || y < 0 || x >= pic.width() || y >= pic.height())
			throw new IndexOutOfBoundsException();
		else if (x == 0 || y == 0 || x == pic.width() - 1
				|| y == pic.height() - 1)
			return 195075.0;
		else {
			Color xp1y = pic.get(x + 1, y);
			Color xn1y = pic.get(x - 1, y);
			Color xyp1 = pic.get(x, y + 1);
			Color xyn1 = pic.get(x, y - 1);
			double detX = (xn1y.getRed() - xp1y.getRed())
					* (xn1y.getRed() - xp1y.getRed())
					+ (xn1y.getBlue() - xp1y.getBlue())
					* (xn1y.getBlue() - xp1y.getBlue())
					+ (xn1y.getGreen() - xp1y.getGreen())
					* (xn1y.getGreen() - xp1y.getGreen());
			double detY = (xyn1.getRed() - xyp1.getRed())
					* (xyn1.getRed() - xyp1.getRed())
					+ (xyn1.getBlue() - xyp1.getBlue())
					* (xyn1.getBlue() - xyp1.getBlue())
					+ (xyn1.getGreen() - xyp1.getGreen())
					* (xyn1.getGreen() - xyp1.getGreen());
			return detX + detY;
		}
	}

	private void dfsMinPath(int x, int y, double[][] sumEnergy,
			double[][] energy, int[][] steps, boolean horizontal) {
		if ((horizontal && x == pic.width() - 1)
				|| (!horizontal && y == pic.height() - 1)) {
			sumEnergy[y][x] = energy[y][x];
			steps[y][x] = -1;
			return;
		}
		double minPath = Double.MAX_VALUE;
		int bestMv = 0;
		for (int mv = -1; mv <= 1; mv++) {
			if (horizontal) {
				int py = y + mv;
				if (py >= pic.height() || py < 0)
					continue;
				if (steps[py][x + 1] == 0)
					dfsMinPath(x + 1, py, sumEnergy, energy, steps, horizontal);
				if (sumEnergy[py][x + 1] < minPath) {
					minPath = sumEnergy[py][x + 1];
					bestMv = py;
				}
			} else {
				int px = x + mv;
				if (px >= pic.width() || px < 0)
					continue;
				if (steps[y + 1][px] == 0)
					dfsMinPath(px, y + 1, sumEnergy, energy, steps, horizontal);
				if (sumEnergy[y + 1][px] < minPath) {
					minPath = sumEnergy[y + 1][px];
					bestMv = px;
				}
			}
		}
		steps[y][x] = bestMv;
		sumEnergy[y][x] = energy[y][x] + minPath;
	}

	private void calSumEnergy() {
		for (int j = 0; j < pic.height(); j++)
			for (int i = 0; i < pic.width(); i++) {
				energy[j][i] = this.energy(i, j);
			}
	}

	public int[] findHorizontalSeam() { 
		// sequence of indices for horizontal seam
		int[][] steps = new int[pic.height()][pic.width()];
		double[][] sumEnergy = new double[pic.height()][pic.width()];
		this.calSumEnergy();
		for (int y = 0; y < this.height(); y++)
			this.dfsMinPath(0, y, sumEnergy, energy, steps, true);
		int[] ht = new int[pic.width()];
		double bestEnergy = Double.MAX_VALUE;
		for (int y = 0; y < this.height(); y++) {
			if (sumEnergy[y][0] < bestEnergy) {
				bestEnergy = sumEnergy[y][0];
				ht[0] = y;
			}
		}
		for (int x = 1; x < this.width(); x++) {
			ht[x] = steps[ht[x - 1]][x - 1];
		}
		return ht;
	}

	public int[] findVerticalSeam() { 
		// sequence of indices for vertical seam
		int[][] steps = new int[pic.height()][pic.width()];
		double[][] sumEnergy = new double[pic.height()][pic.width()];
		this.calSumEnergy();
		for (int x = 0; x < this.width(); x++)
			this.dfsMinPath(x, 0, sumEnergy, energy, steps, false);
		int[] ht = new int[pic.height()];
		double bestEnergy = Double.MAX_VALUE;
		for (int x = 0; x < this.width(); x++) {
			if (sumEnergy[0][x] < bestEnergy) {
				bestEnergy = sumEnergy[0][x];
				ht[0] = x;
			}
		}
		for (int y = 1; y < this.height(); y++)
			ht[y] = steps[y - 1][ht[y - 1]];
		return ht;
	}

	public void removeHorizontalSeam(int[] a) throws IllegalArgumentException {
		// remove horizontal seam from picture
		if (a.length != pic.width())
			throw new IllegalArgumentException();
		Picture cPic = new Picture(pic.width(), pic.height() - 1);
		for (int i = 0; i < pic.width(); i++) {
			for (int j = 0; j < pic.height(); j++) {
				if (j == a[i])
					continue;
				int pt = j;
				if (pt > a[i])
					pt--;
				cPic.set(i, pt, this.pic.get(i, j));
			}
		}
		this.pic = cPic;
	}

	public void removeVerticalSeam(int[] a) throws IllegalArgumentException {
		// remove vertical seam from picture
		if (a.length != pic.height())
			throw new IllegalArgumentException();
		Picture cPic = new Picture(pic.width() - 1, pic.height());
		for (int j = 0; j < pic.height(); j++) {
			for (int i = 0; i < pic.width(); i++) {
				if (i == a[j])
					continue;
				int pt = i;
				if (pt > a[j])
					pt--;
				cPic.set(pt, j, pic.get(i, j));
			}
		}
		this.pic = cPic;
	}
}
