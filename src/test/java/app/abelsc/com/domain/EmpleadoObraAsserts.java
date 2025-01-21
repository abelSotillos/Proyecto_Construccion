package app.abelsc.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EmpleadoObraAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadoObraAllPropertiesEquals(EmpleadoObra expected, EmpleadoObra actual) {
        assertEmpleadoObraAutoGeneratedPropertiesEquals(expected, actual);
        assertEmpleadoObraAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadoObraAllUpdatablePropertiesEquals(EmpleadoObra expected, EmpleadoObra actual) {
        assertEmpleadoObraUpdatableFieldsEquals(expected, actual);
        assertEmpleadoObraUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadoObraAutoGeneratedPropertiesEquals(EmpleadoObra expected, EmpleadoObra actual) {
        assertThat(expected)
            .as("Verify EmpleadoObra auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadoObraUpdatableFieldsEquals(EmpleadoObra expected, EmpleadoObra actual) {
        assertThat(expected)
            .as("Verify EmpleadoObra relevant properties")
            .satisfies(e -> assertThat(e.getHoras()).as("check horas").isEqualTo(actual.getHoras()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadoObraUpdatableRelationshipsEquals(EmpleadoObra expected, EmpleadoObra actual) {
        assertThat(expected)
            .as("Verify EmpleadoObra relationships")
            .satisfies(e -> assertThat(e.getObra()).as("check obra").isEqualTo(actual.getObra()))
            .satisfies(e -> assertThat(e.getEmpleado()).as("check empleado").isEqualTo(actual.getEmpleado()));
    }
}
