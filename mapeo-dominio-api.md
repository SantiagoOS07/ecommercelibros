# Mapeo Dominio -> API

## Autor

| Operación del dominio (caso de uso) | Método HTTP | Endpoint |
|---|---|---|
| RegistrarAutor.registrar(idAutor, nombreAutor, biografia, nacionalidad) | POST | /autores |
| ListarAutores.listar() | GET | /autores |
| ConsultarAutorID.consultar(idAutor) | GET | /autores/{idAutor} |
| ConsultarAutorNombre.buscar(nombreAutor) | GET | /autores/nombre/{nombreAutor} |
| ActualizarAutor.actualizarNombre(idAutor, nuevoNombre) | PUT | /autores/{idAutor}/nombre |
| ActualizarAutor.actualizarBiografia(idAutor, nuevaBiografia) | PUT | /autores/{idAutor}/biografia |
| ActualizarAutor.agregarNacionalidad(idAutor, nacionalidad) | POST | /autores/{idAutor}/nacionalidades |
| ActualizarAutor.eliminarNacionalidad(idAutor, nacionalidad) | DELETE | /autores/{idAutor}/nacionalidades/{nacionalidad} |
| EliminarAutor.eliminar(idAutor) | DELETE | /autores/{idAutor} |

## Libro

| Operación del dominio (caso de uso) | Método HTTP | Endpoint |
|---|---|---|
| RegistrarLibroUseCase.registrarLibro(...) | POST | /libros |
| ListarLibros.listar() | GET | /libros |
| ConsultarLibroID.ConsultarLibroPorID(idLibro) | GET | /libros/{idLibro} |
| BuscarLibroISBN.buscar(isbn) | GET | /libros/isbn/{isbn} |
| BuscarLibroNombre.buscar(nombre) | GET | /libros/nombre/{nombre} |
| BuscarLibroAutor.buscar(nombreAutor) | GET | /libros?autor={nombreAutor} |
| ActualizarLibro.actualizarTitulo(idLibro, nuevoTitulo) | PUT | /libros/{idLibro}/titulo |
| ActualizarLibro.actualizarDescripcion(idLibro, nuevaDescripcion) | PUT | /libros/{idLibro}/descripcion |
| ActualizarLibro.actualizarNumeroPaginas(idLibro, nuevoNumeroPaginas) | PUT | /libros/{idLibro}/numero-paginas |
| ActualizarLibro.actualizarPrecio(idLibro, nuevoPrecio) | PUT | /libros/{idLibro}/precio |
| ActualizarLibro.actualizarStock(idLibro, nuevoStock) | PUT | /libros/{idLibro}/stock |
| ActualizarLibro.actualizarEdicion(idLibro, nuevaEdicion) | PUT | /libros/{idLibro}/edicion |
| ActualizarLibro.actualizarEditorial(idLibro, nuevoIdEditorial) | PUT | /libros/{idLibro}/editorial |
| ActualizarLibro.actualizarFechaPublicacion(idLibro, nuevaFechaPublicacion) | PUT | /libros/{idLibro}/fecha-publicacion |
| ActualizarLibro.actualizarCategoria(idLibro, nuevaCategoria) | PUT | /libros/{idLibro}/categoria |
| ActualizarLibro.actualizarImagen(idLibro, nuevaImagen) | PUT | /libros/{idLibro}/imagen |
| Libro.cambiarEstado(AGOTADO) via ActualizarLibro.actualizarEstado | PUT | /libros/{idLibro}/agotar |
| Libro.cambiarEstado(DISPONIBLE) via ActualizarLibro.actualizarEstado | PUT | /libros/{idLibro}/reactivar |
| Libro.cambiarEstado(DESCONTINUADO) via ActualizarLibro.actualizarEstado | PUT | /libros/{idLibro}/descontinuar |
| EliminarLibro.eliminar(idLibro) | DELETE | /libros/{idLibro} |