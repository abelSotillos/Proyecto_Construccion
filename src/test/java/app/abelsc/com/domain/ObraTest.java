package app.abelsc.com.domain;

import static app.abelsc.com.domain.ClienteTestSamples.*;
import static app.abelsc.com.domain.EmpresaTestSamples.*;
import static app.abelsc.com.domain.ObraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import app.abelsc.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ObraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Obra.class);
        Obra obra1 = getObraSample1();
        Obra obra2 = new Obra();
        assertThat(obra1).isNotEqualTo(obra2);

        obra2.setId(obra1.getId());
        assertThat(obra1).isEqualTo(obra2);

        obra2 = getObraSample2();
        assertThat(obra1).isNotEqualTo(obra2);
    }

    @Test
    void empresaTest() {
        Obra obra = getObraRandomSampleGenerator();
        Empresa empresaBack = getEmpresaRandomSampleGenerator();

        obra.setEmpresa(empresaBack);
        assertThat(obra.getEmpresa()).isEqualTo(empresaBack);

        obra.empresa(null);
        assertThat(obra.getEmpresa()).isNull();
    }

    @Test
    void clienteTest() {
        Obra obra = getObraRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        obra.setCliente(clienteBack);
        assertThat(obra.getCliente()).isEqualTo(clienteBack);

        obra.cliente(null);
        assertThat(obra.getCliente()).isNull();
    }
}
