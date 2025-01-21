package app.abelsc.com.domain;

import static app.abelsc.com.domain.EmpleadoObraTestSamples.*;
import static app.abelsc.com.domain.EmpleadoTestSamples.*;
import static app.abelsc.com.domain.ObraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import app.abelsc.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmpleadoObraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmpleadoObra.class);
        EmpleadoObra empleadoObra1 = getEmpleadoObraSample1();
        EmpleadoObra empleadoObra2 = new EmpleadoObra();
        assertThat(empleadoObra1).isNotEqualTo(empleadoObra2);

        empleadoObra2.setId(empleadoObra1.getId());
        assertThat(empleadoObra1).isEqualTo(empleadoObra2);

        empleadoObra2 = getEmpleadoObraSample2();
        assertThat(empleadoObra1).isNotEqualTo(empleadoObra2);
    }

    @Test
    void obraTest() {
        EmpleadoObra empleadoObra = getEmpleadoObraRandomSampleGenerator();
        Obra obraBack = getObraRandomSampleGenerator();

        empleadoObra.setObra(obraBack);
        assertThat(empleadoObra.getObra()).isEqualTo(obraBack);

        empleadoObra.obra(null);
        assertThat(empleadoObra.getObra()).isNull();
    }

    @Test
    void empleadoTest() {
        EmpleadoObra empleadoObra = getEmpleadoObraRandomSampleGenerator();
        Empleado empleadoBack = getEmpleadoRandomSampleGenerator();

        empleadoObra.setEmpleado(empleadoBack);
        assertThat(empleadoObra.getEmpleado()).isEqualTo(empleadoBack);

        empleadoObra.empleado(null);
        assertThat(empleadoObra.getEmpleado()).isNull();
    }
}
