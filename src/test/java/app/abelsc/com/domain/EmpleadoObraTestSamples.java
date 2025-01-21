package app.abelsc.com.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class EmpleadoObraTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EmpleadoObra getEmpleadoObraSample1() {
        return new EmpleadoObra().id(1L).horas(1L);
    }

    public static EmpleadoObra getEmpleadoObraSample2() {
        return new EmpleadoObra().id(2L).horas(2L);
    }

    public static EmpleadoObra getEmpleadoObraRandomSampleGenerator() {
        return new EmpleadoObra().id(longCount.incrementAndGet()).horas(longCount.incrementAndGet());
    }
}
