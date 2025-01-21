package app.abelsc.com.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MaquinariaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Maquinaria getMaquinariaSample1() {
        return new Maquinaria().id(1L).modelo("modelo1").precio(1L);
    }

    public static Maquinaria getMaquinariaSample2() {
        return new Maquinaria().id(2L).modelo("modelo2").precio(2L);
    }

    public static Maquinaria getMaquinariaRandomSampleGenerator() {
        return new Maquinaria().id(longCount.incrementAndGet()).modelo(UUID.randomUUID().toString()).precio(longCount.incrementAndGet());
    }
}
