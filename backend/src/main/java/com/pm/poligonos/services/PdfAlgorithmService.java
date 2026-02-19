package com.pm.poligonos.services;

import com.pm.poligonos.models.Point;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PdfAlgorithmService {

    // Traducción de la función lexComp del PDF
    private boolean lexComp(Point p1, Point p2) {
        return p1.x() < p2.x() || (p1.x() == p2.x() && p1.y() < p2.y());
    }

    private double crossProduct(Point a, Point b) {
        return a.x() * b.y() - a.y() * b.x();
    }

    private int sgn(double val) {
        return val > 0 ? 1 : (val == 0 ? 0 : -1);
    }

    // Traducción de pointInTriangle del PDF
    private boolean pointInTriangle(Point a, Point b, Point c, Point p) {
        double s1 = Math.abs(crossProduct(new Point(b.x()-a.x(), b.y()-a.y()), new Point(c.x()-a.x(), c.y()-a.y())));
        double s2 = Math.abs(crossProduct(new Point(a.x()-p.x(), a.y()-p.y()), new Point(b.x()-p.x(), b.y()-p.y()))) +
                    Math.abs(crossProduct(new Point(b.x()-p.x(), b.y()-p.y()), new Point(c.x()-p.x(), c.y()-p.y()))) +
                    Math.abs(crossProduct(new Point(c.x()-p.x(), c.y()-p.y()), new Point(a.x()-p.x(), a.y()-p.y())));
        return Math.abs(s1 - s2) < 0.0001; // Tolerancia para decimales
    }

    public boolean isInsidePdfAlgorithm(List<Point> originalPoints, Point query) {
        int n = originalPoints.size();
        List<Point> points = new ArrayList<>(originalPoints);

        // PREPARE: Buscar el menor lexicográfico (p0)
        int pos = 0;
        for (int i = 1; i < n; i++) {
            if (lexComp(points.get(i), points.get(pos))) {
                pos = i;
            }
        }
        Collections.rotate(points, -pos);

        Point p0 = points.get(0);
        List<Point> seq = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            seq.add(new Point(points.get(i).x() - p0.x(), points.get(i).y() - p0.y()));
        }

        Point pt = new Point(query.x() - p0.x(), query.y() - p0.y());

        if (n < 3) return false;

        // Casos borde
        if (crossProduct(seq.get(0), pt) != 0 && sgn(crossProduct(seq.get(0), pt)) != sgn(crossProduct(seq.get(0), seq.get(n - 2))))
            return false;
        if (crossProduct(seq.get(n - 2), pt) != 0 && sgn(crossProduct(seq.get(n - 2), pt)) != sgn(crossProduct(seq.get(n - 2), seq.get(0))))
            return false;

        if (crossProduct(seq.get(0), pt) == 0) {
            double sqrLen0 = seq.get(0).x() * seq.get(0).x() + seq.get(0).y() * seq.get(0).y();
            double sqrLenPt = pt.x() * pt.x() + pt.y() * pt.y();
            return sqrLen0 >= sqrLenPt;
        }

        // Búsqueda Binaria
        int l = 0, r = n - 2;
        while (r - l > 1) {
            int mid = (l + r) / 2;
            if (crossProduct(seq.get(mid), pt) >= 0) {
                l = mid;
            } else {
                r = mid;
            }
        }

        return pointInTriangle(seq.get(l), seq.get(l + 1), new Point(0, 0), pt);
    }
}