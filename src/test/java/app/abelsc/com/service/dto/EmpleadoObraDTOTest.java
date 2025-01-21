package app.abelsc.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import app.abelsc.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmpleadoObraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmpleadoObraDTO.class);
        EmpleadoObraDTO empleadoObraDTO1 = new EmpleadoObraDTO();
        empleadoObraDTO1.setId(1L);
        EmpleadoObraDTO empleadoObraDTO2 = new EmpleadoObraDTO();
        assertThat(empleadoObraDTO1).isNotEqualTo(empleadoObraDTO2);
        empleadoObraDTO2.setId(empleadoObraDTO1.getId());
        assertThat(empleadoObraDTO1).isEqualTo(empleadoObraDTO2);
        empleadoObraDTO2.setId(2L);
        assertThat(empleadoObraDTO1).isNotEqualTo(empleadoObraDTO2);
        empleadoObraDTO1.setId(null);
        assertThat(empleadoObraDTO1).isNotEqualTo(empleadoObraDTO2);
    }
}
