package app.abelsc.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import app.abelsc.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaquinariaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaquinariaDTO.class);
        MaquinariaDTO maquinariaDTO1 = new MaquinariaDTO();
        maquinariaDTO1.setId(1L);
        MaquinariaDTO maquinariaDTO2 = new MaquinariaDTO();
        assertThat(maquinariaDTO1).isNotEqualTo(maquinariaDTO2);
        maquinariaDTO2.setId(maquinariaDTO1.getId());
        assertThat(maquinariaDTO1).isEqualTo(maquinariaDTO2);
        maquinariaDTO2.setId(2L);
        assertThat(maquinariaDTO1).isNotEqualTo(maquinariaDTO2);
        maquinariaDTO1.setId(null);
        assertThat(maquinariaDTO1).isNotEqualTo(maquinariaDTO2);
    }
}
