package app.abelsc.com.service.mapper;

import static app.abelsc.com.domain.EmpleadoObraAsserts.*;
import static app.abelsc.com.domain.EmpleadoObraTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmpleadoObraMapperTest {

    private EmpleadoObraMapper empleadoObraMapper;

    @BeforeEach
    void setUp() {
        empleadoObraMapper = new EmpleadoObraMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmpleadoObraSample1();
        var actual = empleadoObraMapper.toEntity(empleadoObraMapper.toDto(expected));
        assertEmpleadoObraAllPropertiesEquals(expected, actual);
    }
}
