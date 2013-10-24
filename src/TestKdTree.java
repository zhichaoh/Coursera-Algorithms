import java.util.Iterator;
import java.util.Arrays;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Ignore;

public class TestKdTree {
    private KdTree k;

    @Before
    public void set_up() {
        k = new KdTree();
    }

    @After
    public void tear_down() {}

    private Point2D[] get_points() {
        return new Point2D[]{
            new Point2D(0.3, 0.2),
            new Point2D(0.2, 0.2),
            new Point2D(0.1, 0.1),
            new Point2D(0.2, 0.1),
            new Point2D(0.3, 0.3)
        };
    }

    @Test
    public void test_empty() {
        assertEquals(0, k.size());
        assertTrue(k.isEmpty());
    }

    @Test
    public void test_insert_first() {
        k.insert(new Point2D(0.1, 0.1));
        assertEquals(1, k.size());
        assertFalse(k.isEmpty());
    }

    @Test
    public void test_insert_distinct() {
        Point2D[] points = get_points();
        for (Point2D point : points) k.insert(point);
        assertEquals(points.length, k.size());
    }

    @Test
    public void test_insert_same() {
        for (int i = 0; i < 5; i++) k.insert(new Point2D(0.1, 0.1));
        assertEquals(1, k.size());
    }

    @Test
    public void test_empty_contains() {
        assertFalse(k.contains(new Point2D(0.1, 0.1)));
    }

    @Test
    public void test_single_contains() {
        k.insert(new Point2D(0.1, 0.1));
        assertEquals(1, k.size());
        assertTrue(k.contains(new Point2D(0.1, 0.1)));
    }

    @Test
    public void test_multiple_contains() {
        Point2D[] points = get_points();
        for (Point2D point : points) k.insert(point);
        for (Point2D point : points) assertTrue(k.contains(point));
    }

    @Test
    public void test_not_empty_not_contains() {
        Point2D[] points = get_points();
        for (Point2D point : points) k.insert(point);

        assertFalse(k.contains(new Point2D(0.4, 0.4)));
        assertFalse(k.contains(new Point2D(0.0, 0.0)));
    }

    @Test
    public void test_empty_range() {
        Iterator<Point2D> iter = k.range(new RectHV(0, 0, 1, 1)).iterator();
        assertFalse(iter.hasNext());
    }

    @Test
    public void test_single_range() {
        Point2D p = new Point2D(1, 1);
        k.insert(p);

        Iterator<Point2D> iter = k.range(new RectHV(0, 0, 1, 1)).iterator();
        assertTrue(iter.hasNext());
        assertTrue(p.equals(iter.next()));
        assertFalse(iter.hasNext());
    }

    @Test
    public void test_multiple_range() {
        Point2D[] points = get_points();
        for (Point2D point : points) k.insert(point);

        Iterator<Point2D> iter = k.range(new RectHV(0, 0, 1, 1)).iterator();

        int cnt = 0;
        while (iter.hasNext()) {
            cnt++;
            Point2D p = iter.next();
            Arrays.asList(points).contains(p);
        }
        assertFalse(iter.hasNext());
        assertEquals(points.length, cnt);
    }

    @Test
    public void test_selected_range() {
        Point2D[] points = new Point2D[]{
            new Point2D(.5, .5),
            new Point2D(.7, .8),
            new Point2D(.1, .1),
            new Point2D(1, 0),
            new Point2D(.3, .9),
        };
        for (Point2D point : points) k.insert(point);
        assertEquals(5, k.size());

        Iterator<Point2D> iter = k.range(new RectHV(.6, .6, .8, .8)).iterator();
        assertTrue(iter.hasNext());
        iter.next().equals(points[1]);
        assertFalse(iter.hasNext());

        iter = k.range(new RectHV(0, 0, .6, .6)).iterator();
        assertTrue(iter.hasNext());
        iter.next().equals(points[2]);
        assertTrue(iter.hasNext());
        iter.next().equals(points[0]);
        assertFalse(iter.hasNext());
    }

    @Test
    public void test_not_empty_no_range() {
        Point2D[] points = get_points();
        for (Point2D point : points) k.insert(point);

        Iterator<Point2D> iter = k.range(new RectHV(.4, .4, 1, 1)).iterator();
        assertFalse(iter.hasNext());
    }

    @Test
    public void test_empty_nearest() {
        assertNull(k.nearest(new Point2D(0, 0)));
    }

    @Test
    public void test_nearest_single() {
        Point2D p = new Point2D(0, 0);
        k.insert(p);
        assertTrue(p.equals(k.nearest(new Point2D(1, 1))));
        assertTrue(p.equals(k.nearest(p)));
    }

    @Test
    public void test_nearest_multiple_exact() {
        Point2D[] points = get_points();
        for (Point2D point : points) k.insert(point);

        for (Point2D point : points) assertTrue(point.equals(k.nearest(point)));
    }

    @Test
    public void test_nearest_multiple() {
        Point2D[] points = new Point2D[]{
            new Point2D(.5, .5),
            new Point2D(.7, .8),
            new Point2D(.1, .1),
            new Point2D(1, 0),
            new Point2D(.3, .9),
        };
        for (Point2D point : points) k.insert(point);

        assertTrue(points[0].equals(k.nearest(new Point2D(.6, .6))));
        assertTrue(points[3].equals(k.nearest(new Point2D(.6, 0))));
        assertTrue(points[4].equals(k.nearest(new Point2D(0, 1))));
        assertTrue(points[2].equals(k.nearest(new Point2D(.2, .2))));
        assertTrue(points[1].equals(k.nearest(new Point2D(1, 1))));
    }
}