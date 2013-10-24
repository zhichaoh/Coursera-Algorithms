import java.util.Set;
import java.util.TreeSet;


public class KdTree {

	private Node root;
	
	private int numNodes;
	
	private class Node implements Comparable<Node> {
		private Point2D point;
		private Node left, right;
		private RectHV rect;
		private boolean flag;
		public Node(Point2D point, boolean flag, RectHV rect){
			this.point = new Point2D(point.x(), point.y());
			this.flag = flag;
			this.left = null;
			this.right = null;
			this.rect = rect;
		}

		@Override
		public int compareTo(Node o) {
			if(flag) {
				if(this.point.x()>o.point.x()) return 1;
				else if(Math.abs(this.point.x()-o.point.x())<1e-20) return 0;
				else return -1;
			} else {
				if(this.point.y()>o.point.y()) return 1;
				else if(Math.abs(this.point.y()-o.point.y())<1e-20) return 0;
				else return -1;
			}
		}
	};
	
	private Point2D searchRec(Node root, Node p) {
		if(root.point.equals(p.point)) return root.point;
		if(root.compareTo(p)>0) {
			if(root.left==null) return null;
			else return searchRec(root.left, p);
		} else {
			if(root.right==null) return null;
			else return searchRec(root.right, p);
		}
		
	}
	
	private boolean insertRec(Node root, Point2D p){
		if(root.point.equals(p)) return false;
		Node newNode = new Node(p, !root.flag, null);
		if(root.compareTo(newNode)>0){
			RectHV newRect = null;
			if(root.flag) newRect = new RectHV(root.rect.xmin(), root.rect.ymin(), root.point.x(), root.rect.ymax());
			else newRect = new RectHV(root.rect.xmin(), root.rect.ymin(), root.rect.xmax(), root.point.y());
			if(root.left!=null) return insertRec(root.left, p);
			else root.left = new Node(p, !root.flag, newRect);
			return true;
		} else {
			RectHV newRect = null;
			if(root.flag) newRect = new RectHV(root.point.x(), root.rect.ymin(), root.rect.xmax(), root.rect.ymax());
			else newRect = new RectHV(root.rect.xmin(),  root.point.y(), root.rect.xmax(), root.rect.ymax());
			if(root.right!=null) return insertRec(root.right, p);
			else root.right = new Node(p, !root.flag, newRect);
			return true;
		} 
	}
	
	private void drawRec(Node root, double minX, double maxX, double minY, double maxY) {
		if (root == null) return;

		StdDraw.setPenRadius(.001);
		if (root.flag) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(root.point.x(), minY, root.point.x(), maxY);
			drawRec(root.left, minX, root.point.x(), minY, maxY);
			drawRec(root.right, root.point.x(), maxX, minY, maxY);
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(minX, root.point.y(), maxX, root.point.y());
			drawRec(root.left, minX, maxX, minY, root.point.y());
			drawRec(root.right, minX, maxX, root.point.y(), maxY);
		}
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(.01);
		root.point.draw();
	}
	
	private void rangeRec(Node root, RectHV rect, Set<Point2D> set) {
		if (root == null) return;
		if (root.rect.intersects(rect)) {
			if (rect.contains(root.point)) set.add(root.point);
			rangeRec(root.left, rect, set);
			rangeRec(root.right, rect, set);
		}
	}
	
/*	private Node nearestRec(Point2D point, Node root, double dist) {
		if (root.rect.distanceSquaredTo(point) >= dist) return root;
		Node min = root;
		dist = root.point.distanceSquaredTo(point);

		if (root.left != null && root.left.rect.distanceSquaredTo(point) < dist) {
			Node left_min = nearestRec(point, root.left, dist);
			double left_dist = point.distanceSquaredTo(left_min.point);
			if(left_dist<dist) {
				dist = left_dist;
				min = left_min;
			}
		}
		if (root.right != null && root.right.rect.distanceSquaredTo(point) < dist) {
			Node right_min = nearestRec(point, root.right, dist);
			if(point.distanceSquaredTo(right_min.point)<dist){
				min = right_min;
			}
		}
		return min;
	}*/
	
	private Point2D nearestHelper(Node node, RectHV rect, double x, double y, Point2D nearestPointCandidate) {

        Point2D nearestPoint = nearestPointCandidate;

        if (node != null) {
            Point2D queryPoint = new Point2D(x, y);

            if (nearestPoint == null || queryPoint.distanceSquaredTo(nearestPoint) > rect.distanceSquaredTo(queryPoint)) {

                Point2D nodePoint = new Point2D(node.point.x(), node.point.y());

                if (nearestPoint == null) {
                    nearestPoint = nodePoint;
                } else {
                    if (queryPoint.distanceSquaredTo(nearestPoint) > queryPoint.distanceSquaredTo(nodePoint)) {
                        nearestPoint = nodePoint;
                    }
                }

                if (node.flag) {
                    if (x <= node.point.x()) {
                        if(node.left!=null) nearestPoint = nearestHelper(node.left, node.left.rect, x, y, nearestPoint);
                        if(node.right!=null) nearestPoint = nearestHelper(node.right, node.right.rect, x, y, nearestPoint);
                    } else {
                    	if(node.right!=null) nearestPoint = nearestHelper(node.right, node.right.rect, x, y, nearestPoint);
                        if(node.left!=null) nearestPoint = nearestHelper(node.left, node.left.rect, x, y, nearestPoint);
                    }
                } else {
                    if (y <= node.point.y()) {
                    	if(node.left!=null) nearestPoint = nearestHelper(node.left, node.left.rect, x, y, nearestPoint);
                        if(node.right!=null) nearestPoint = nearestHelper(node.right, node.right.rect, x, y, nearestPoint);
                    } else {
                    	if(node.right!=null) nearestPoint = nearestHelper(node.right, node.right.rect, x, y, nearestPoint);
                    	if(node.left!=null) nearestPoint = nearestHelper(node.left, node.left.rect, x, y, nearestPoint);
                    }
                }
            }
        }
        return nearestPoint;
    }

    public Point2D nearest(Point2D p) {
        return nearestHelper(root, new RectHV(0,0,1,1), p.x(), p.y(), null);
    }
	
	public KdTree() {
		// construct an empty set of points
		this.root = null;
		this.numNodes = 0;
	}
	
	public boolean isEmpty() {
		// is the set empty?
		return numNodes==0;
	}
	
	public int size() {
		// number of points in the set
		return this.numNodes;
	}
	
	public void insert(Point2D p) {
		// add the point p to the set (if it is not already in the set)
		if(this.numNodes==0) {
			this.root = new Node(p,true,new RectHV(0,0,1,1));
			this.numNodes ++;
		}
		else {
			if(insertRec(root, p)) this.numNodes++;
		}
		
	}
	
	public boolean contains(Point2D p) {
		// does the set contain the point p?
		if(isEmpty()) return false;
		return searchRec(root, new Node(p, true, null))!=null;
	}
	
	public void draw() {
		// draw all of the points to standard draw
		if (isEmpty()) return;
		drawRec(root, 0, 1, 0, 1);
	}
	
	public Iterable<Point2D> range(RectHV rect) {
		// all points in the set that are inside the rectangle
		Set<Point2D> pointList = new TreeSet<Point2D>();
		rangeRec(root, rect, pointList);
		return pointList;
	}
	
/*	public Point2D nearest(Point2D p) {
		// a nearest neighbor in the set to p; null if set is empty
		if(this.numNodes==0) return null;
		return nearestRec(p, root, root.point.distanceSquaredTo(p)).point;
	}*/
}
