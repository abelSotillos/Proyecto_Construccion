package app.abelsc.com.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MaquinariaObraTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MaquinariaObra getMaquinariaObraSample1() {
        return new MaquinariaObra().id(1L).horas(1L);
    }

    public static MaquinariaObra getMaquinariaObraSample2() {
        return new MaquinariaObra().id(2L).horas(2L);
    }

    public static MaquinariaObra getMaquinariaObraRandomSampleGenerator() {
        return new MaquinariaObra().id(longCount.incrementAndGet()).horas(longCount.incrementAndGet());
    }
}
