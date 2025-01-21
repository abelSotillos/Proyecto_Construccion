package app.abelsc.com.service.mapper;

import static app.abelsc.com.domain.MaquinariaObraAsserts.*;
import static app.abelsc.com.domain.MaquinariaObraTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaquinariaObraMapperTest {

    private MaquinariaObraMapper maquinariaObraMapper;

    @BeforeEach
    void setUp() {
        maquinariaObraMapper = new MaquinariaObraMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMaquinariaObraSample1();
        var actual = maquinariaObraMapper.toEntity(maquinariaObraMapper.toDto(expected));
        assertMaquinariaObraAllPropertiesEquals(expected, actual);
    }
}
