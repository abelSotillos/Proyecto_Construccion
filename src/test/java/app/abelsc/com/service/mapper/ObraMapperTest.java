package app.abelsc.com.service.mapper;

import static app.abelsc.com.domain.ObraAsserts.*;
import static app.abelsc.com.domain.ObraTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObraMapperTest {

    private ObraMapper obraMapper;

    @BeforeEach
    void setUp() {
        obraMapper = new ObraMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getObraSample1();
        var actual = obraMapper.toEntity(obraMapper.toDto(expected));
        assertObraAllPropertiesEquals(expected, actual);
    }
}
