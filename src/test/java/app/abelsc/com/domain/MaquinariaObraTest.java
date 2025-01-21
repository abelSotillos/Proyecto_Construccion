package app.abelsc.com.domain;

import static app.abelsc.com.domain.MaquinariaObraTestSamples.*;
import static app.abelsc.com.domain.MaquinariaTestSamples.*;
import static app.abelsc.com.domain.ObraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import app.abelsc.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaquinariaObraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaquinariaObra.class);
        MaquinariaObra maquinariaObra1 = getMaquinariaObraSample1();
        MaquinariaObra maquinariaObra2 = new MaquinariaObra();
        assertThat(maquinariaObra1).isNotEqualTo(maquinariaObra2);

        maquinariaObra2.setId(maquinariaObra1.getId());
        assertThat(maquinariaObra1).isEqualTo(maquinariaObra2);

        maquinariaObra2 = getMaquinariaObraSample2();
        assertThat(maquinariaObra1).isNotEqualTo(maquinariaObra2);
    }

    @Test
    void obraTest() {
        MaquinariaObra maquinariaObra = getMaquinariaObraRandomSampleGenerator();
        Obra obraBack = getObraRandomSampleGenerator();

        maquinariaObra.setObra(obraBack);
        assertThat(maquinariaObra.getObra()).isEqualTo(obraBack);

        maquinariaObra.obra(null);
        assertThat(maquinariaObra.getObra()).isNull();
    }

    @Test
    void maquinariaTest() {
        MaquinariaObra maquinariaObra = getMaquinariaObraRandomSampleGenerator();
        Maquinaria maquinariaBack = getMaquinariaRandomSampleGenerator();

        maquinariaObra.setMaquinaria(maquinariaBack);
        assertThat(maquinariaObra.getMaquinaria()).isEqualTo(maquinariaBack);

        maquinariaObra.maquinaria(null);
        assertThat(maquinariaObra.getMaquinaria()).isNull();
    }
}
