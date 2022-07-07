import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private static final int INITIAL_CONST = 2;

    private final Point[] points;
    private Point p;
    private int nSegments;
    private int sloperController;
    private LineSegment[] lSegments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        checkNice(points);

        this.points = points.clone();
        lSegments = new LineSegment[INITIAL_CONST];
        p = new Point(-1, -1);

        fastCollinearPoints();
    }

    public int numberOfSegments()   {   return nSegments;           }
    public LineSegment[] segments() {   return lSegments.clone();   }
    
    private static void checkNice(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++)
                if (points[j] == null || points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();       
        }
    }
    
    private void fastCollinearPoints() {
        for (Point point : points) {
            p = nextMinP(point);
            Arrays.sort(points, p.slopeOrder());
            sloperController = 0;
            sloper(0);
        }
        resize(nSegments, nSegments);
    }

    private boolean less(Point p1, Point p2)          {   return p1.compareTo(p2) < 0;  }
    private boolean equalOrLess(Point p1, Point p2)   {   return p1.compareTo(p2) <= 0; }


    private Point nextMinP(Point newP) {
        for (Point point : points)
            if (equalOrLess(newP, p) || (less(point, newP) && less(p, point)))
                newP = point;
        return newP;
    }

    private void sloper(int i) {
        if (sloperController >= points.length) return;

        int newI = getNewI(i);
        sloperController = newI;

        Arrays.sort(points, i, newI);
        if (less(p, points[i]) && newI - i >= 3)
            addLine(nSegments++, new LineSegment(p, points[newI - 1]));
        sloper(newI);
        sloperController = i;
    }

    private int getNewI(int i) {
        while (i + 1 < points.length) {
            i++;
            if (i == points.length || p.slopeTo(points[i - 1]) != p.slopeTo(points[i])) 
                return i;
        }
        return ++i;
    }
 
    private void addLine(int index, LineSegment lineSegment) {
        if (index == lSegments.length) resize(index, lSegments.length * 2);
        lSegments[index] = lineSegment;
    }

    private void resize(int index, int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < index; i++) {
            copy[i] = lSegments[i];
        }
        lSegments = copy;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
