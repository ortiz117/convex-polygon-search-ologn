### 1. Diagrama de Clases

```mermaid
classDiagram
    class Point {
        +double x
        +double y
        +minus(Point p) Point
        +crossProduct(Point v1, Point v2) double
    }
    class Request {
        +List~Point~ vertices
        +Point point
    }
    class Response {
        +boolean isInside
        +String message
    }
    class GeometryService {
        +isInside(List~Point~ vertices, Point q) boolean
    }
    class PolygonController {
        -GeometryService geometryService
        +check(Request req) Response
    }

    PolygonController --> GeometryService : usa
    PolygonController ..> Request : recibe
    PolygonController ..> Response : devuelve
    GeometryService ..> Point : procesa
    Request "1" *-- "many" Point : contiene
```

---

### 2. Diagrama de Secuencia

```mermaid
sequenceDiagram
    participant U as Usuario
    participant F as SvelteKit (Frontend)
    participant C as Controller (Backend)
    participant S as GeometryService

    U->>F: Clic en coordenadas
    F->>C: POST /api/polygon/check (JSON)
    C->>S: isInside(vertices, point)
    S->>S: Algoritmo O(log N)
    S-->>C: boolean (Resultado)
    C-->>F: JSON {isInside, message}
    F->>U: Actualiza UI (Verde/Rojo)
```

---

### 3. Diagrama de Arquitectura

```mermaid
graph LR
    subgraph "Docker Network"
        FE[Frontend Container: 5173]
        BE[Backend Container: 8080]
    end
    User((Usuario)) -->|Navegador| FE
    FE -->|API Rest| BE
```