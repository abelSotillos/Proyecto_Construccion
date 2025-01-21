package app.abelsc.com.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link app.abelsc.com.domain.MaterialObra} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialObraDTO implements Serializable {

    private Long id;

    private Long cantidad;

    private ObraDTO obra;

    private MaterialDTO material;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public ObraDTO getObra() {
        return obra;
    }

    public void setObra(ObraDTO obra) {
        this.obra = obra;
    }

    public MaterialDTO getMaterial() {
        return material;
    }

    public void setMaterial(MaterialDTO material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialObraDTO)) {
            return false;
        }

        MaterialObraDTO materialObraDTO = (MaterialObraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, materialObraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialObraDTO{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", obra=" + getObra() +
            ", material=" + getMaterial() +
            "}";
    }
}
