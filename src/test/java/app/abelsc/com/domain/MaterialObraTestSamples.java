package app.abelsc.com.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MaterialObraTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MaterialObra getMaterialObraSample1() {
        return new MaterialObra().id(1L).cantidad(1L);
    }

    public static MaterialObra getMaterialObraSample2() {
        return new MaterialObra().id(2L).cantidad(2L);
    }

    public static MaterialObra getMaterialObraRandomSampleGenerator() {
        return new MaterialObra().id(longCount.incrementAndGet()).cantidad(longCount.incrementAndGet());
    }
}
