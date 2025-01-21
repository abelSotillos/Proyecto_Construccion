package app.abelsc.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import app.abelsc.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialObraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialObraDTO.class);
        MaterialObraDTO materialObraDTO1 = new MaterialObraDTO();
        materialObraDTO1.setId(1L);
        MaterialObraDTO materialObraDTO2 = new MaterialObraDTO();
        assertThat(materialObraDTO1).isNotEqualTo(materialObraDTO2);
        materialObraDTO2.setId(materialObraDTO1.getId());
        assertThat(materialObraDTO1).isEqualTo(materialObraDTO2);
        materialObraDTO2.setId(2L);
        assertThat(materialObraDTO1).isNotEqualTo(materialObraDTO2);
        materialObraDTO1.setId(null);
        assertThat(materialObraDTO1).isNotEqualTo(materialObraDTO2);
    }
}
