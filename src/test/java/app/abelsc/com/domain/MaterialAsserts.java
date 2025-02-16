package app.abelsc.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class MaterialAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialAllPropertiesEquals(Material expected, Material actual) {
        assertMaterialAutoGeneratedPropertiesEquals(expected, actual);
        assertMaterialAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialAllUpdatablePropertiesEquals(Material expected, Material actual) {
        assertMaterialUpdatableFieldsEquals(expected, actual);
        assertMaterialUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialAutoGeneratedPropertiesEquals(Material expected, Material actual) {
        assertThat(expected)
            .as("Verify Material auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialUpdatableFieldsEquals(Material expected, Material actual) {
        assertThat(expected)
            .as("Verify Material relevant properties")
            .satisfies(e -> assertThat(e.getNombre()).as("check nombre").isEqualTo(actual.getNombre()))
            .satisfies(e -> assertThat(e.getDescripcion()).as("check descripcion").isEqualTo(actual.getDescripcion()))
            .satisfies(e -> assertThat(e.getPrecio()).as("check precio").isEqualTo(actual.getPrecio()))
            .satisfies(e -> assertThat(e.getStock()).as("check stock").isEqualTo(actual.getStock()))
            .satisfies(e -> assertThat(e.getUnidadMedida()).as("check unidadMedida").isEqualTo(actual.getUnidadMedida()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialUpdatableRelationshipsEquals(Material expected, Material actual) {
        assertThat(expected)
            .as("Verify Material relationships")
            .satisfies(e -> assertThat(e.getEmpresa()).as("check empresa").isEqualTo(actual.getEmpresa()));
    }
}
