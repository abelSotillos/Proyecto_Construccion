package app.abelsc.com.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmpresaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Empresa getEmpresaSample1() {
        return new Empresa()
            .id(1L)
            .nombre("nombre1")
            .nif("nif1")
            .calle("calle1")
            .telefono("telefono1")
            .provincia("provincia1")
            .poblacion("poblacion1");
    }

    public static Empresa getEmpresaSample2() {
        return new Empresa()
            .id(2L)
            .nombre("nombre2")
            .nif("nif2")
            .calle("calle2")
            .telefono("telefono2")
            .provincia("provincia2")
            .poblacion("poblacion2");
    }

    public static Empresa getEmpresaRandomSampleGenerator() {
        return new Empresa()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .nif(UUID.randomUUID().toString())
            .calle(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .provincia(UUID.randomUUID().toString())
            .poblacion(UUID.randomUUID().toString());
    }
}
