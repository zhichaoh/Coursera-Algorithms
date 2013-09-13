import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Fast {
	
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
        Map<String, Boolean> printedMap = new HashMap<String, Boolean>();
        for(int i=0;i<points.size();i++) {
        	List<Point> tmpPoints = new ArrayList<Point>();
        	for(int j=i+1;j<points.size();j++) tmpPoints.add(points.get(j));
    		Collections.sort(tmpPoints, points.get(i).SLOPE_ORDER);
    		for(int j=0;j<tmpPoints.size();){
        		double slote = points.get(i).slopeTo(tmpPoints.get(j));
    			List<Point> nList = new ArrayList<Point>(); 
    			nList.add(points.get(i)); nList.add(tmpPoints.get(j));
    			j++;
    			while(j<tmpPoints.size() && points.get(i).slopeTo(tmpPoints.get(j))==slote) {
    				nList.add(tmpPoints.get(j));
    				j ++;
        		} 
        		if(nList.size()>=4) {
        			boolean printedFlag = false;
        			for(int k=0;k<nList.size()-1;k++) {
        				String seg = nList.get(k).toString()+","+nList.get(k+1).toString();
        				if(!printedMap.containsKey(seg)) {
        					printedMap.put(seg, true);
        					printedFlag = true;
        				}
        			}
        			if(printedFlag){
						Collections.sort(nList);
						for(int k=0;k<nList.size()-1;k++) System.out.print(nList.get(k)+" -> ");
						System.out.print(nList.get(nList.size()-1)+"\n");
						points.get(i).drawTo(nList.get(nList.size()-1));
        			}
        		}
        	}
        }
	}
}