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
**No usar:** fecha

**Ejemplo de uso en código:**
\`\`\`java
[ public void agregarLibro(int idLibro, String nombreLibro, String autor){ Libro libro = new Libro(idLibro, nombreLibro, autor);
libroRepository.save(libro); } ]
\`\`\`

---

### Número de paginas

**Definición:** Es el numero de paginas que tiene el libro

**Sinónimos aceptados:** paginas
**No usar:** cantidad

**Ejemplo de uso en código:**
\`\`\`java
[ public List filtrarPorPaginas(int min, int max){ return libroRepository.findByPaginasBetween(min, max); } ]
\`\`\`
---

### [Término 4]
[repitan la misma estructura]

---

### [Término 5]
[repitan la misma estructura]

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