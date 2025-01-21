package app.abelsc.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import app.abelsc.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaquinariaObraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaquinariaObraDTO.class);
        MaquinariaObraDTO maquinariaObraDTO1 = new MaquinariaObraDTO();
        maquinariaObraDTO1.setId(1L);
        MaquinariaObraDTO maquinariaObraDTO2 = new MaquinariaObraDTO();
        assertThat(maquinariaObraDTO1).isNotEqualTo(maquinariaObraDTO2);
        maquinariaObraDTO2.setId(maquinariaObraDTO1.getId());
        assertThat(maquinariaObraDTO1).isEqualTo(maquinariaObraDTO2);
        maquinariaObraDTO2.setId(2L);
        assertThat(maquinariaObraDTO1).isNotEqualTo(maquinariaObraDTO2);
        maquinariaObraDTO1.setId(null);
        assertThat(maquinariaObraDTO1).isNotEqualTo(maquinariaObraDTO2);
    }
}
