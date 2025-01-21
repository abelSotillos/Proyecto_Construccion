package app.abelsc.com.service.mapper;

import static app.abelsc.com.domain.EmpleadoAsserts.*;
import static app.abelsc.com.domain.EmpleadoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmpleadoMapperTest {

    private EmpleadoMapper empleadoMapper;

    @BeforeEach
    void setUp() {
        empleadoMapper = new EmpleadoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmpleadoSample1();
        var actual = empleadoMapper.toEntity(empleadoMapper.toDto(expected));
        assertEmpleadoAllPropertiesEquals(expected, actual);
    }
}
