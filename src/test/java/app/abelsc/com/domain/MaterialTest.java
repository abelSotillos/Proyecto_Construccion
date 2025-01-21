package app.abelsc.com.domain;

import static app.abelsc.com.domain.EmpresaTestSamples.*;
import static app.abelsc.com.domain.MaterialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import app.abelsc.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Material.class);
        Material material1 = getMaterialSample1();
        Material material2 = new Material();
        assertThat(material1).isNotEqualTo(material2);

        material2.setId(material1.getId());
        assertThat(material1).isEqualTo(material2);

        material2 = getMaterialSample2();
        assertThat(material1).isNotEqualTo(material2);
    }

    @Test
    void empresaTest() {
        Material material = getMaterialRandomSampleGenerator();
        Empresa empresaBack = getEmpresaRandomSampleGenerator();

        material.setEmpresa(empresaBack);
        assertThat(material.getEmpresa()).isEqualTo(empresaBack);

        material.empresa(null);
        assertThat(material.getEmpresa()).isNull();
    }
}
