import java.util.Arrays;
import java.util.Comparator;

public class BurrowsWheeler {

	private static int[] argsort(final char[] a, final boolean ascending) {
		Integer[] indexes = new Integer[a.length];
		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = i;
		}
		Arrays.sort(indexes, new Comparator<Integer>() {
			@Override
			public int compare(final Integer i1, final Integer i2) {
				return (ascending ? 1 : -1) * Float.compare(a[i1], a[i2]);
			}
		});
		return asArray(indexes);
	}

	@SafeVarargs
	private static <T extends Number> int[] asArray(final T... a) {
		int[] b = new int[a.length];
		for (int i = 0; i < b.length; i++) {
			b[i] = a[i].intValue();
		}
		return b;
	}

	// apply Burrows-Wheeler encoding, reading from standard input and writing
	// to standard output
	public static void encode() {

		StringBuilder str = new StringBuilder();
		while (!BinaryStdIn.isEmpty())
			str = str.append(BinaryStdIn.readChar());
		CircularSuffixArray csa = new CircularSuffixArray(str.toString());
		for (int i = 0; i < csa.length(); i++) {
			if (csa.index(i) == 0) {
				BinaryStdOut.write(i, 32);
				// System.out.printf("%c%c%c%c",0,0,0,i);
				break;
			}
		}
		for (int i = 0; i < csa.length(); i++) {
			// System.out.print(str.charAt((csa.index(i) - 1 + csa.length()) %
			// csa.length()));
			BinaryStdOut.write(str.charAt((csa.index(i) - 1 + csa.length())
					% csa.length()));
		}
		BinaryStdIn.close();
		BinaryStdOut.close();
	}

	// apply Burrows-Wheeler decoding, reading from standard input and writing
	// to standard output
	public static void decode() {
		StringBuilder str = new StringBuilder();
		int s = BinaryStdIn.readInt(32);
		while (!BinaryStdIn.isEmpty())
			str = str.append(BinaryStdIn.readChar());
		int next[] = new int[str.length()];
		// System.out.println(s+" "+str.length());
		// build next array
		int[] index = argsort(str.toString().toCharArray(), true);
		for (int i = 0; i < str.length(); i++) {
			next[i] = index[i];
			// System.out.println(next[i]);
		}

		// output
		int idx = 0;
		int pt = next[s];
		while (idx < str.length() - 1) {
			BinaryStdOut.write(str.charAt(pt));
			// System.out.print(str.charAt(pt));
			pt = next[pt];
			idx++;
		}
		BinaryStdOut.write(str.charAt(s));
		// System.out.print(str.charAt(s));
		BinaryStdIn.close();
		BinaryStdOut.close();
	}

	// if args[0] is '-', apply Burrows-Wheeler encoding
	// if args[0] is '+', apply Burrows-Wheeler decoding
	public static void main(String[] args) {
		if (args[0].equals("-"))
			encode();
		else if (args[0].equals("+"))
			decode();
		else
			return;
	}
}
