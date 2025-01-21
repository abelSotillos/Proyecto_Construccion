package app.abelsc.com.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ObraTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Obra getObraSample1() {
        return new Obra().id(1L).nombre("nombre1").direccion("direccion1").coste(1L).costePagado(1L);
    }

    public static Obra getObraSample2() {
        return new Obra().id(2L).nombre("nombre2").direccion("direccion2").coste(2L).costePagado(2L);
    }

    public static Obra getObraRandomSampleGenerator() {
        return new Obra()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .coste(longCount.incrementAndGet())
            .costePagado(longCount.incrementAndGet());
    }
}
