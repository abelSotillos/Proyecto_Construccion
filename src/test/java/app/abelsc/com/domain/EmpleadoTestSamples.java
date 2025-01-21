package app.abelsc.com.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmpleadoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Empleado getEmpleadoSample1() {
        return new Empleado().id(1L).nombre("nombre1").dni("dni1").salario(1L);
    }

    public static Empleado getEmpleadoSample2() {
        return new Empleado().id(2L).nombre("nombre2").dni("dni2").salario(2L);
    }

    public static Empleado getEmpleadoRandomSampleGenerator() {
        return new Empleado()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .dni(UUID.randomUUID().toString())
            .salario(longCount.incrementAndGet());
    }
}
