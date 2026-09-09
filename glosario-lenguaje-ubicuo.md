# Glosario del Lenguaje Ubicuo - LibMark / Libros Fisicos

## Conceptos Centrales

### Autor
**Definición:** Es la persona que escribio el libro

**Sinónimos aceptados:** Escritor
**No usar:** Creador, Productor

**Ejemplo de uso en código:**
\`\`\`java
[ public List filtrarPorAutor(String autor){ return libroRepository.findByAutor(autor); } ]
\`\`\`

---

### Fecha de publicacion
**Definición:** Es el dia, mes y año en que una edicion del libro sale oficialmente al mercado. 

**Sinónimos aceptados:** Fecha de aparicion
**No usar:** Fecha

**Ejemplo de uso en código:**
\`\`\`java
[ public void agregarLibro(int idLibro, String nombreLibro, String autor){ Libro libro = new Libro(idLibro, nombreLibro, autor);
libroRepository.save(libro); } ]
\`\`\`

---

### Número de paginas

**Definición:** Es el numero de paginas que tiene el libro

**Sinónimos aceptados:** Paginas
**No usar:** Cantidad

**Ejemplo de uso en código:**
\`\`\`java
[ public List filtrarPorPaginas(int min, int max){ return libroRepository.findByPaginasBetween(min, max); } ]
\`\`\`

---

### Edición
**Definición:** Es la versión específica de un libro publicada por una editorial

**Sinónimos aceptados:** Publicación
**No usar:** Versión, impresión

**Ejemplo de uso en código:**
\`\`\`java
[ public List filtrarPorEdicion(String edicion){ return libroRepository.findByEdicion(edicion); } ]
\`\`\`

---

### ISBN
**Definición:** Es el número único que identifica una edición específica de un libro publicado

**Sinónimos aceptados:** Número ISBN, código ISBN
**No usar:** Código libro, número libro

**Ejemplo de uso en código:**
\`\`\`java
[ public Libro buscarPorISBN(String isbn){ return libroRepository.findByIsbn(isbn); } ]
\`\`\`

---

## Anti-patrones (Términos a EVITAR en nuestro proyecto)

| No usar | Usar |
|---|---|
| Nombre | Título |
| Código | ISBN |
| Versión | Edición |
| Fecha | Fecha de publicación |
| Páginas | Número de páginas |
| Escritor | Autor |
| Artículo | Libro |