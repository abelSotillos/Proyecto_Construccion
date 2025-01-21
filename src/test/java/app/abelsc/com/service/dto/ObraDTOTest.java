package app.abelsc.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import app.abelsc.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ObraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ObraDTO.class);
        ObraDTO obraDTO1 = new ObraDTO();
        obraDTO1.setId(1L);
        ObraDTO obraDTO2 = new ObraDTO();
        assertThat(obraDTO1).isNotEqualTo(obraDTO2);
        obraDTO2.setId(obraDTO1.getId());
        assertThat(obraDTO1).isEqualTo(obraDTO2);
        obraDTO2.setId(2L);
        assertThat(obraDTO1).isNotEqualTo(obraDTO2);
        obraDTO1.setId(null);
        assertThat(obraDTO1).isNotEqualTo(obraDTO2);
    }
}
