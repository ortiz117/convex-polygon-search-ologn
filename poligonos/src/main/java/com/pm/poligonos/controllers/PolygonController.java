package com.pm.poligonos.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pm.poligonos.models.Point;
import com.pm.poligonos.services.GeometryService;

@RestController
@RequestMapping("/api/polygon")
@CrossOrigin(origins = "*") // Permite la comunicación con SvelteKit
public class PolygonController {

    private final GeometryService geometryService;

    public PolygonController(GeometryService geometryService) {
        this.geometryService = geometryService;
    }

    public record Request(List<Point> vertices, Point point) {}
    public record Response(boolean inside, String message) {}

    @PostMapping("/check")
    public Response check(@RequestBody Request req) {
        boolean res = geometryService.isInside(req.vertices(), req.point());
        return new Response(res, res ? "Dentro" : "Fuera");
    }
}