package com.pm.poligonos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pm.poligonos.models.Point;

@Service
public class GeometryService {

    public boolean isInside(List<Point> vertices, Point q) {
        int n = vertices.size();
        if (n < 3) return false;

        Point p0 = vertices.get(0);
        Point qRel = q.minus(p0);

        // 1. Verificación de bordes exteriores (O(1))
        Point v1 = vertices.get(1).minus(p0);
        Point vn = vertices.get(n - 1).minus(p0);

        if (v1.crossProduct(qRel) < 0) return false;
        if (vn.crossProduct(qRel) > 0) return false;

        // Caso especial: sobre la línea p0-p1
        if (v1.crossProduct(qRel) == 0) {
            return v1.squareLength() >= qRel.squareLength();
        }

        // 2. Búsqueda binaria del sector (O(log N))
        int low = 1, high = n - 1;
        while (high - low > 1) {
            int mid = (low + high) / 2;
            if (vertices.get(mid).minus(p0).crossProduct(qRel) >= 0) {
                low = mid;
            } else {
                high = mid;
            }
        }

        // 3. Prueba final en el triángulo detectado
        return isPointInTriangle(p0, vertices.get(low), vertices.get(low + 1), q);
    }

    private boolean isPointInTriangle(Point a, Point b, Point c, Point p) {
        // Basado en la suma de áreas de sub-triángulos
        double totalArea = Math.abs(b.minus(a).crossProduct(c.minus(a)));
        double areaSum = Math.abs(a.minus(p).crossProduct(b.minus(p))) +
                         Math.abs(b.minus(p).crossProduct(c.minus(p))) +
                         Math.abs(c.minus(p).crossProduct(a.minus(p)));
        
        return Math.abs(totalArea - areaSum) < 1e-9;
    }
}