# PoligonosDF - Algoritmo O(log N) 🎯

Este proyecto implementa un algoritmo de geometría computacional de alto rendimiento ($O(\log N)$) para determinar si un punto se encuentra dentro, fuera o en la frontera de un polígono convexo. La solución sigue estrictamente el proceso **Waterfall**: Requerimientos, Diseño, Implementación y Pruebas.

## 🛠️ Tecnologías Utilizadas
* **Backend:** Java 21 + Spring Boot (Lógica de búsqueda binaria y productos cruzados).
* **Frontend:** SvelteKit + Tailwind CSS (Interfaz gráfica reactiva y moderna).
* **Despliegue:** Docker & Docker Compose para ejecución en contenedores.

## ⚠️ Requisitos Previos Obligatorios

Para que los comandos de este proyecto funcionen, es indispensable cumplir con lo siguiente:

1. **Docker Desktop Instalado:** Debes tener instalada la versión más reciente de [Docker Desktop](https://www.docker.com/products/docker-desktop/).
2. **Docker Engine Activo:** La aplicación de Docker Desktop **debe estar abierta** antes de ejecutar cualquier comando. 
3. **Estado de la Ballena:** Asegúrate de que el icono de la ballena en tu barra de tareas esté quieto y en verde. Si Docker no está corriendo, la terminal arrojará errores de conexión al "pipe" del sistema.



## 🚀 Cómo ejecutar el proyecto (One-Click Start)

Una vez que Docker esté abierto y funcionando:

1. Abre una terminal en la raíz de este proyecto (donde se encuentra el archivo `docker-compose.yml`).
2. Ejecuta el siguiente comando para construir y levantar los servicios:
   ```bash
   docker-compose up --build