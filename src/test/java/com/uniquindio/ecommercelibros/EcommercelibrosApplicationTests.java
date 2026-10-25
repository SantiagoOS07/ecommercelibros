package com.uniquindio.ecommercelibros;

import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
class EcommercelibrosApplicationTests {

	private static final String ISBN = "9780306406157";

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private LibroRepository libroRepository;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("Registrar un libro lo guarda y se puede consultar por el Location")
	void registrarYConsultar_recorreElSistemaCompleto() throws Exception {
		// Arrange
		String body = json(ISBN, "Fundamentos de programación");

		// Act
		MvcResult creado = mockMvc.perform(post("/api/libros")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn();

		// Assert
		assertThat(libroRepository.buscarPorISBN(ISBN)).isPresent();
		mockMvc.perform(get(creado.getResponse().getHeader("Location")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.isbn").value(ISBN));
	}

	@Test
	@DisplayName("Un ISBN repetido falla y no guarda el segundo libro")
	void registrarIsbnDuplicado_noGuardaElSegundo() throws Exception {
		// Arrange
		mockMvc.perform(post("/api/libros").contentType(MediaType.APPLICATION_JSON)
						.content(json(ISBN, "Primer libro del ISBN")))
				.andExpect(status().isCreated());

		// Act + Assert
		mockMvc.perform(post("/api/libros").contentType(MediaType.APPLICATION_JSON)
						.content(json(ISBN, "Segundo libro mismo ISBN")))
				.andExpect(status().is4xxClientError());
		assertThat(libroRepository.listarTodosLosLibros()).hasSize(1);
	}

	@Test
	@DisplayName("Consultar un id que no existe responde 404")
	void consultarIdInexistente_responde404() throws Exception {
		mockMvc.perform(get("/api/libros/{id}", UUID.randomUUID()))
				.andExpect(status().isNotFound());
	}

	private String json(String isbn, String titulo) {
		return """
                {
                  "titulo": "%s",
                  "idAutor": "%s",
                  "isbn": "%s",
                  "descripcion": "Descripción de prueba",
                  "numeroPaginas": 320,
                  "precio": 45000.50,
                  "moneda": "COP",
                  "stock": 12,
                  "edicion": "Primera edición",
                  "idEditorial": "%s",
                  "fechaPublicacion": "2020-05-10",
                  "categoria": "NOMBRE_REAL_DE_CATEGORIA",
                  "imagen": "https://imagenes.test/libro.png",
                  "estado": "NOMBRE_REAL_DE_ESTADO"
                }
                """.formatted(titulo, UUID.randomUUID(), isbn, UUID.randomUUID());
	}
}