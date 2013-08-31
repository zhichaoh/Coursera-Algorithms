import java.io.IOException;

public class MoveToFront {

	private static final int R = 256;
	
	// apply move-to-front encoding, reading from standard input and writing to
	// standard output
	public static void encode() throws IOException {
		char[] chars = radixList();
		char count, ch, tmpin, tmpout;
		while (!BinaryStdIn.isEmpty()) {
			ch = BinaryStdIn.readChar();
			for (count = 0, tmpout = chars[0]; ch != chars[count]; count++) {
				tmpin = chars[count];
				chars[count] = tmpout;
				tmpout = tmpin;
			}
			chars[count] = tmpout;
			BinaryStdOut.write(count);
			chars[0] = ch;
		}
		BinaryStdOut.close();
	}

	// apply move-to-front decoding, reading from standard input and writing to
	// standard output
	public static void decode() throws IOException {
		char[] chars = radixList();
		char count, ch;
		while (!BinaryStdIn.isEmpty()) {
			count = BinaryStdIn.readChar();
			for (ch = chars[count]; count > 0; chars[count] = chars[--count]);
			chars[count] = ch; // assert count == 0;
			BinaryStdOut.write(ch);
		}
		BinaryStdOut.close();
	}
	
	private static char[] radixList() {
		char[] rl = new char[R];
		for (char i = 0; i < R; rl[i] = i++);
		return rl;
	}

	// if args[0] is '-', apply move-to-front encoding
	// if args[0] is '+', apply move-to-front decoding
	public static void main(String[] args) throws IOException {
		if (args[0].equals("-"))
			encode();
		else if (args[0].equals("+"))
			decode();
		else
			return;
	}
}
