package app.abelsc.com.domain;

import static app.abelsc.com.domain.EmpresaTestSamples.*;
import static app.abelsc.com.domain.PerfilUsuarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import app.abelsc.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PerfilUsuarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerfilUsuario.class);
        PerfilUsuario perfilUsuario1 = getPerfilUsuarioSample1();
        PerfilUsuario perfilUsuario2 = new PerfilUsuario();
        assertThat(perfilUsuario1).isNotEqualTo(perfilUsuario2);

        perfilUsuario2.setId(perfilUsuario1.getId());
        assertThat(perfilUsuario1).isEqualTo(perfilUsuario2);

        perfilUsuario2 = getPerfilUsuarioSample2();
        assertThat(perfilUsuario1).isNotEqualTo(perfilUsuario2);
    }

    @Test
    void empresaTest() {
        PerfilUsuario perfilUsuario = getPerfilUsuarioRandomSampleGenerator();
        Empresa empresaBack = getEmpresaRandomSampleGenerator();

        perfilUsuario.setEmpresa(empresaBack);
        assertThat(perfilUsuario.getEmpresa()).isEqualTo(empresaBack);

        perfilUsuario.empresa(null);
        assertThat(perfilUsuario.getEmpresa()).isNull();
    }
}
