package app.abelsc.com.domain;

import static app.abelsc.com.domain.EmpresaTestSamples.*;
import static app.abelsc.com.domain.MaquinariaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import app.abelsc.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaquinariaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Maquinaria.class);
        Maquinaria maquinaria1 = getMaquinariaSample1();
        Maquinaria maquinaria2 = new Maquinaria();
        assertThat(maquinaria1).isNotEqualTo(maquinaria2);

        maquinaria2.setId(maquinaria1.getId());
        assertThat(maquinaria1).isEqualTo(maquinaria2);

        maquinaria2 = getMaquinariaSample2();
        assertThat(maquinaria1).isNotEqualTo(maquinaria2);
    }

    @Test
    void empresaTest() {
        Maquinaria maquinaria = getMaquinariaRandomSampleGenerator();
        Empresa empresaBack = getEmpresaRandomSampleGenerator();

        maquinaria.setEmpresa(empresaBack);
        assertThat(maquinaria.getEmpresa()).isEqualTo(empresaBack);

        maquinaria.empresa(null);
        assertThat(maquinaria.getEmpresa()).isNull();
    }
}
