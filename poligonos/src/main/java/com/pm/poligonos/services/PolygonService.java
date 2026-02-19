package com.pm.poligonos.services;

import java.util.List;
import com.pm.poligonos.models.Point;

public class PolygonService {

    public boolean pointInConvexPolygon(List<Point> polygon, Point q) {
    int n = polygon.size();
    if (n < 3) return false; 

    Point p0 = polygon.get(0);
    Point qT = q.minus(p0); 
    
    // Vectores de los extremos
    Point v1 = polygon.get(1).minus(p0);
    Point vn = polygon.get(n - 1).minus(p0);

    // 1. Validaciones básicas de orientación
    if (v1.crossProduct(qT) < 0) return false;
    if (vn.crossProduct(qT) > 0) return false;

    // 2. CASO ESPECIAL: Punto sobre la línea P0-P1
    // Si el producto cruz es 0, están alineados. Usamos squareLength para ver si está dentro del segmento.
    if (v1.crossProduct(qT) == 0) {
        return v1.squareLength() >= qT.squareLength();
    }

    // 3. Búsqueda binaria de la "rebanada"
    int l = 1, r = n - 1;
    while (r - l > 1) {
        int mid = (l + r) / 2;
        if (polygon.get(mid).minus(p0).crossProduct(qT) >= 0) l = mid;
        else r = mid;
    }
    
    // 4. Verificar si está dentro del triángulo final
    return isPointInTriangle(p0, polygon.get(l), polygon.get(l + 1), q);
}

    // Método auxiliar para checar si un punto q está dentro del triángulo abc
    private boolean isPointInTriangle(Point a, Point b, Point c, Point q) {
        // Usamos la técnica de la suma de áreas o signos de productos cruzados
        double areaTotal = Math.abs(b.minus(a).crossProduct(c.minus(a)));
        double areaSuma = Math.abs(a.minus(q).crossProduct(b.minus(q))) +
                          Math.abs(b.minus(q).crossProduct(c.minus(q))) +
                          Math.abs(c.minus(q).crossProduct(a.minus(q)));

        // Si la suma de las áreas de los 3 triángulos formados con q es igual al área total, está dentro
        return Math.abs(areaTotal - areaSuma) < 1e-9;
    }
}