package app.abelsc.com.domain;

import static app.abelsc.com.domain.EmpleadoTestSamples.*;
import static app.abelsc.com.domain.EmpresaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import app.abelsc.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmpleadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Empleado.class);
        Empleado empleado1 = getEmpleadoSample1();
        Empleado empleado2 = new Empleado();
        assertThat(empleado1).isNotEqualTo(empleado2);

        empleado2.setId(empleado1.getId());
        assertThat(empleado1).isEqualTo(empleado2);

        empleado2 = getEmpleadoSample2();
        assertThat(empleado1).isNotEqualTo(empleado2);
    }

    @Test
    void empresaTest() {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        Empresa empresaBack = getEmpresaRandomSampleGenerator();

        empleado.setEmpresa(empresaBack);
        assertThat(empleado.getEmpresa()).isEqualTo(empresaBack);

        empleado.empresa(null);
        assertThat(empleado.getEmpresa()).isNull();
    }
}
