package app.abelsc.com.service.mapper;

import static app.abelsc.com.domain.MaquinariaAsserts.*;
import static app.abelsc.com.domain.MaquinariaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaquinariaMapperTest {

    private MaquinariaMapper maquinariaMapper;

    @BeforeEach
    void setUp() {
        maquinariaMapper = new MaquinariaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMaquinariaSample1();
        var actual = maquinariaMapper.toEntity(maquinariaMapper.toDto(expected));
        assertMaquinariaAllPropertiesEquals(expected, actual);
    }
}
