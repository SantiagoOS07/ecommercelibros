# ECOMMERCE LIBROS

# Pruebas Avanzadas

Parte 0 · Pruebas del repositorio

¿Por qué probar el repositorio contra una base de datos de verdad (H2) y no con Mockito?

Con Mockito se simula y prueba el llamado al repositorio, ideal para las pruebas aisladas de cada clas, pero no se verifica que funcione como es debido. 
Al usar el H2 real se verfifca que el SQL, sus columnas y las restricciones que se establecieron, 
ademas de la consulta paginada funcionen correctamente.


¿Qué hace @Transactional en un test (deshace lo que se guardó al terminar)?

Lo que hace @Transactional como lo dice su nombre es ejecutar las pruebas en una transacccion, asi que los datos modificados en ese test de la base de datos no afectan otras pruebas. Ademas al finalizar el test todo se revierte automaticamente, lo que se conoce como rollback.


¿Qué es el mapper y por qué esta prueba también lo comprueba?

El mapper es una clase encargada de convertir las entidades a su version Entity, osea las entidades de dominio a su version de persistencia. Por ejemplo Libro a LibroEntity y devuelta, con sus metodo ToDomain y ToEntity. En esta prueba al guardar y leer tambien es necesario
validar que se convierte correctamente ya que esta conversion nos permite la insercion y modificacion de los datos guardados en la base de datos.



Parte 2 · @DataJpaTest contra @SpringBootTest

¿Qué levanta cada una (@DataJpaTest solo la capa de persistencia, @SpringBootTest la aplicación completa)?

El @DataJpaTest solo levanta la capa de persistenca, esto incluye las entidades y los repositorios JPA, pero no incluye los controllers, servicios ni seguridad. En cambio @SpringBootTest levanta la aplicacion
por completo.


¿Qué base de datos usa @DataJpaTest por defecto y cómo cambiarlo (pista: @AutoConfigureTestDatabase)?

@DataJpaTest reemplaza nuestra base y usa la H2. El control de si se realiza ese cambio o no, de acuerdo a la situacion lo realiza @AutoConfigureTestDatabase.


¿Qué pasa con @Transactional en cada una?

@DataJpaTest revierte automaticamente por si solo, por lo tanto no es necesario declararlo. En cambio en @SpringBootTest no se hace automaticamente, entonces nosotros debemos ponerlo en cada metodo o en la clase en general.


¿Qué cambió en Spring Boot 4 con el paquete de @DataJpaTest y por qué hace falta un starter de pruebas?

En Spring Boot 4 todo el soporte de rpuebas se dividio en diferente modulos. Y como estas clases ya no vienen incluidas en el starter de pruebas nos fue necesario añadir 
spring-boot-starter-data-jpa-test, spring-boot-starter-validation-test y spring-boot-starter-webmvc-test, ya que sin ellos ni @DataJpaTest o @AutoConfigureMockMvc funcionan correctamente.



Parte 3 · Pruebas de integración

¿ Qué es una prueba de integración y en qué se diferencia de una unitaria ?

Una prueba integracion recorre varias entidades o incluso toda la aplicacion. Desde el controller, pasa por casos de uso, repositorios hasta la base, ademas no hace uso de Mockito.
En cambio las pruebas unitarias examinan las entidades o simples unidades de codigo en test aislados usando Mocks, lo cual es mucho mas rapido pero no nos verifica su correcta integracion con 
las demas clases.


¿ Qué ventajas y desventajas tiene (velocidad, qué errores detecta que las otras no) ?

En terminos de velocidad las prubas unitarias ganan, ya que solo examinan clases aisladas, ademas que usan los Mocks, pero solo verifican los llamados a otras clases. Por 
otro lado las pruebas de integracion son mucho mas lentas, pero verifican las respuestas de los diferentes pasos desde los controllers hasta la base principal y su correcto funcionamiento.


¿ Cuándo conviene usar una y cuándo no ?

Lo mejor es usar ambos tipos de pruebas, primero evaluar el comportamiento de las clases de manera aislada, para ver su funcionamiento base. Ya luego de hacr estas pruebas 
podemos pasar a las pruebas de integracion y verificar su correcta integracion con las demas clases, no solo como llaman o que responden, si no como lo hacen, el proceso que hay detras y si
realmente se llevan a cabo los procesos esperados y se nos dan las respuestas correctas.