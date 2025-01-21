package app.abelsc.com.service.mapper;

import static app.abelsc.com.domain.PerfilUsuarioAsserts.*;
import static app.abelsc.com.domain.PerfilUsuarioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PerfilUsuarioMapperTest {

    private PerfilUsuarioMapper perfilUsuarioMapper;

    @BeforeEach
    void setUp() {
        perfilUsuarioMapper = new PerfilUsuarioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPerfilUsuarioSample1();
        var actual = perfilUsuarioMapper.toEntity(perfilUsuarioMapper.toDto(expected));
        assertPerfilUsuarioAllPropertiesEquals(expected, actual);
    }
}
