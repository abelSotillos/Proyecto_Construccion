package app.abelsc.com.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MaterialTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Material getMaterialSample1() {
        return new Material().id(1L).nombre("nombre1").descripcion("descripcion1").precio(1L).stock(1L);
    }

    public static Material getMaterialSample2() {
        return new Material().id(2L).nombre("nombre2").descripcion("descripcion2").precio(2L).stock(2L);
    }

    public static Material getMaterialRandomSampleGenerator() {
        return new Material()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .precio(longCount.incrementAndGet())
            .stock(longCount.incrementAndGet());
    }
}
