package com.pm.poligonos.models;

/**
 * Usamos un record para tener x() y y() automáticamente y que sea inmutable.
 */
public record Point(double x, double y) {
    
    // Método para restar puntos (vectores)
    public Point minus(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    // El producto cruz de dos vectores
    public double crossProduct(Point other) {
        return (this.x * other.y) - (this.y * other.x);
    }

    // Magnitud al cuadrado para comparar distancias
    public double squareLength() {
        return (this.x * this.x) + (this.y * this.y);
    }
}