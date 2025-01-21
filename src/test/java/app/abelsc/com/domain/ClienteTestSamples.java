package app.abelsc.com.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cliente getClienteSample1() {
        return new Cliente()
            .id(1L)
            .nombre("nombre1")
            .apellidos("apellidos1")
            .nif("nif1")
            .direccion("direccion1")
            .telefono("telefono1")
            .email("email1");
    }

    public static Cliente getClienteSample2() {
        return new Cliente()
            .id(2L)
            .nombre("nombre2")
            .apellidos("apellidos2")
            .nif("nif2")
            .direccion("direccion2")
            .telefono("telefono2")
            .email("email2");
    }

    public static Cliente getClienteRandomSampleGenerator() {
        return new Cliente()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellidos(UUID.randomUUID().toString())
            .nif(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
