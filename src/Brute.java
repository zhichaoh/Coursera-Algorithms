import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Brute {

	private static List<Point> readList(String fileName) throws IOException {
		Scanner input = new Scanner(new File(fileName));
		int numPoints = input.nextInt();
		List<Point> points = new ArrayList<Point>();
		for(int i=0;i<numPoints;i++) {
			int x = input.nextInt();
			int y = input.nextInt();
			Point point = new Point(x, y);
			points.add(point);
		}
		input.close();
		return points;
	}
	
	public static void main(String[] args) throws IOException {
		String fileName = args[0];
		List<Point> points = readList(fileName); 
		Collections.sort(points);
		StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for(int i=0;i<points.size();i++) points.get(i).draw();
        for(int i=0;i<points.size();i++){
			for(int j=i+1;j<points.size();j++){
				for(int k=j+1;k<points.size();k++){
					for(int l=k+1;l<points.size();l++){
						double sloteIJ = points.get(i).slopeTo(points.get(j));
						double sloteJK = points.get(j).slopeTo(points.get(k));
						double sloteKL = points.get(k).slopeTo(points.get(l));
						double sloteLI = points.get(l).slopeTo(points.get(i));
						if(sloteIJ==sloteJK && sloteJK==sloteKL && sloteKL==sloteLI && sloteIJ==sloteLI) {
							System.out.print(points.get(i)+" -> ");
							System.out.print(points.get(j)+" -> ");
							System.out.print(points.get(k)+" -> ");
							System.out.print(points.get(l)+"\n");
							points.get(i).drawTo(points.get(l));
						}
					}
				}
			}
		}
	}

}
