import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class PointSET {
	
	private Set<Point2D> pointSet;
	
	public PointSET() {
		// construct an empty set of points
		pointSet = new TreeSet<Point2D>();
	}
	
	public boolean isEmpty() {
		// is the set empty?
		return pointSet.isEmpty();
	}
	
	public int size() {
		// number of points in the set
		return pointSet.size();
	}
	
	public void insert(Point2D p) {
		// add the point p to the set (if it is not already in the set)
		pointSet.add(p);
	}
	
	public boolean contains(Point2D p) {
		// does the set contain the point p?
		return pointSet.contains(p);
	}
	
	public void draw() {
		// draw all of the points to standard draw
		for(Point2D p : pointSet){
			p.draw();
		}
	}
	
	public Iterable<Point2D> range(RectHV rect) {
		// all points in the set that are inside the rectangle
		List<Point2D> pointList = new LinkedList<Point2D>();
		for(Point2D p : pointSet){
			if(rect.distanceTo(p)==0D) pointList.add(p);
		}
		return pointList;
	}
	
	public Point2D nearest(Point2D p) {
		// a nearest neighbor in the set to p; null if set is empty
		if(pointSet==null || pointSet.size()==0) return null;
		Point2D nn = null;
		double dist = Double.MAX_VALUE;
		for(Point2D cp : pointSet) {
			double cd = cp.distanceTo(p);
			if(cd<dist) {
				dist = cd; nn = cp;
			}
		}
		return nn;
	}
}
