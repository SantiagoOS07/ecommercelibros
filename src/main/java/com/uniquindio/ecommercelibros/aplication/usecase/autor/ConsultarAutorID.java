import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultarAutorID {

    private final AutorRepository autorRepository;

    public ConsultarAutorID(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Autor> consultar(String idAutor) {
        return autorRepository.buscarPorIdAutor(idAutor);
    }
}