package com.uniquindio.ecommercelibros.aplication.usecase.editorial;

import com.uniquindio.ecommercelibros.domain.entity.Editorial;
import com.uniquindio.ecommercelibros.domain.repository.EditorialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListarEditoriales {

    private final EditorialRepository editorialRepository;

    public ListarEditoriales(EditorialRepository editorialRepository) {
        this.editorialRepository = editorialRepository;
    }

    @Transactional(readOnly = true)
    public List<Editorial> listar() {
        return editorialRepository.listarTodas();
    }
}