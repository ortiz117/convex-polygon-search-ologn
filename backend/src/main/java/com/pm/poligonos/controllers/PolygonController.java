package com.pm.poligonos.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pm.poligonos.models.Point;
import com.pm.poligonos.services.GeometryService;
import com.pm.poligonos.services.PdfAlgorithmService;

@RestController
@RequestMapping("/api/polygon")
@CrossOrigin(origins = "*") // Permite la comunicación con SvelteKit
public class PolygonController {

    private final GeometryService geometryService;
    private final PdfAlgorithmService pdfAlgorithmService;

    // Inyectar ambos servicios en el constructor
    public PolygonController(GeometryService geometryService, PdfAlgorithmService pdfAlgorithmService) {
        this.geometryService = geometryService;
        this.pdfAlgorithmService = pdfAlgorithmService;
    }

    public record Request(List<Point> vertices, Point point) {}
    public record Response(boolean inside, String message) {}

    @PostMapping("/check")
    public Response check(@RequestBody Request req) {
        boolean res = geometryService.isInside(req.vertices(), req.point());
        return new Response(res, res ? "Dentro" : "Fuera");
    }

    @PostMapping("/check-pdf")
    public Response checkPdf(@RequestBody Request req) {
        boolean inside = pdfAlgorithmService.isInsidePdfAlgorithm(req.vertices(), req.point());
        return new Response(inside, inside ? "TARGET INSIDE (PDF Algo)" : "OUT OF BOUNDS (PDF Algo)");
    }
}