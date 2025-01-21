package app.abelsc.com.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PerfilUsuarioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PerfilUsuario getPerfilUsuarioSample1() {
        return new PerfilUsuario().id(1L);
    }

    public static PerfilUsuario getPerfilUsuarioSample2() {
        return new PerfilUsuario().id(2L);
    }

    public static PerfilUsuario getPerfilUsuarioRandomSampleGenerator() {
        return new PerfilUsuario().id(longCount.incrementAndGet());
    }
}
