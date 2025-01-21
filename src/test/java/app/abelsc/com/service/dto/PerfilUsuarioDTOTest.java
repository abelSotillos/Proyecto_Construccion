package app.abelsc.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import app.abelsc.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PerfilUsuarioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerfilUsuarioDTO.class);
        PerfilUsuarioDTO perfilUsuarioDTO1 = new PerfilUsuarioDTO();
        perfilUsuarioDTO1.setId(1L);
        PerfilUsuarioDTO perfilUsuarioDTO2 = new PerfilUsuarioDTO();
        assertThat(perfilUsuarioDTO1).isNotEqualTo(perfilUsuarioDTO2);
        perfilUsuarioDTO2.setId(perfilUsuarioDTO1.getId());
        assertThat(perfilUsuarioDTO1).isEqualTo(perfilUsuarioDTO2);
        perfilUsuarioDTO2.setId(2L);
        assertThat(perfilUsuarioDTO1).isNotEqualTo(perfilUsuarioDTO2);
        perfilUsuarioDTO1.setId(null);
        assertThat(perfilUsuarioDTO1).isNotEqualTo(perfilUsuarioDTO2);
    }
}
