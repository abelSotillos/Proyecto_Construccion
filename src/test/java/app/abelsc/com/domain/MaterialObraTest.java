package app.abelsc.com.domain;

import static app.abelsc.com.domain.MaterialObraTestSamples.*;
import static app.abelsc.com.domain.MaterialTestSamples.*;
import static app.abelsc.com.domain.ObraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import app.abelsc.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialObraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialObra.class);
        MaterialObra materialObra1 = getMaterialObraSample1();
        MaterialObra materialObra2 = new MaterialObra();
        assertThat(materialObra1).isNotEqualTo(materialObra2);

        materialObra2.setId(materialObra1.getId());
        assertThat(materialObra1).isEqualTo(materialObra2);

        materialObra2 = getMaterialObraSample2();
        assertThat(materialObra1).isNotEqualTo(materialObra2);
    }

    @Test
    void obraTest() {
        MaterialObra materialObra = getMaterialObraRandomSampleGenerator();
        Obra obraBack = getObraRandomSampleGenerator();

        materialObra.setObra(obraBack);
        assertThat(materialObra.getObra()).isEqualTo(obraBack);

        materialObra.obra(null);
        assertThat(materialObra.getObra()).isNull();
    }

    @Test
    void materialTest() {
        MaterialObra materialObra = getMaterialObraRandomSampleGenerator();
        Material materialBack = getMaterialRandomSampleGenerator();

        materialObra.setMaterial(materialBack);
        assertThat(materialObra.getMaterial()).isEqualTo(materialBack);

        materialObra.material(null);
        assertThat(materialObra.getMaterial()).isNull();
    }
}
