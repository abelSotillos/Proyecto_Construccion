package app.abelsc.com.service.mapper;

import static app.abelsc.com.domain.MaterialObraAsserts.*;
import static app.abelsc.com.domain.MaterialObraTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaterialObraMapperTest {

    private MaterialObraMapper materialObraMapper;

    @BeforeEach
    void setUp() {
        materialObraMapper = new MaterialObraMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMaterialObraSample1();
        var actual = materialObraMapper.toEntity(materialObraMapper.toDto(expected));
        assertMaterialObraAllPropertiesEquals(expected, actual);
    }
}
