import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private static final int INITIAL_CONST = 2;
    
    private boolean wasALine;
    private final Point[] points;
    private LineSegment[] lSegments;
    private int nSegments;
    
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        checkNice(points);

        this.points = points.clone();
        Arrays.sort(this.points);
        
        nSegments = 0;
        lSegments = new LineSegment[INITIAL_CONST];
        wasALine = false;

        bruteCollinearPoints();
    }

    public int numberOfSegments()   {   return nSegments;           }
    public LineSegment[] segments() {   return lSegments.clone();   }

    private void bruteCollinearPoints() {
        for (int i = 0; i < points.length - 3; i++) {
            Point p = points[i];

            for (int j = i + 1; j < points.length - 2; j++) {
                Point q = points[j];
                double slope = p.slopeTo(q);

                for (int k = j + 1; k < points.length - 1; k++) {
                    Point r = points[k];
                    if (slope != p.slopeTo(r)) continue;

                    for (int m = k + 1; m < points.length; m++) {    
                        Point s = points[m];
                        if (slope == p.slopeTo(s)) {
                            addLine(nSegments++, new LineSegment(p, s));
                            wasALine = true;
                        }
                        if (wasALine) break;
                    }
                    if (wasALine) break;
                }
                wasALine = false;
            }
        }
        resize(nSegments, nSegments);
    }

    private static void checkNice(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++)
                if (points[j] == null || points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();       
        }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
