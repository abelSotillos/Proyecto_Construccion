package app.abelsc.com.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link app.abelsc.com.domain.MaquinariaObra} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaquinariaObraDTO implements Serializable {

    private Long id;

    private Long horas;

    private ObraDTO obra;

    private MaquinariaDTO maquinaria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHoras() {
        return horas;
    }

    public void setHoras(Long horas) {
        this.horas = horas;
    }

    public ObraDTO getObra() {
        return obra;
    }

    public void setObra(ObraDTO obra) {
        this.obra = obra;
    }

    public MaquinariaDTO getMaquinaria() {
        return maquinaria;
    }

    public void setMaquinaria(MaquinariaDTO maquinaria) {
        this.maquinaria = maquinaria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaquinariaObraDTO)) {
            return false;
        }

        MaquinariaObraDTO maquinariaObraDTO = (MaquinariaObraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, maquinariaObraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaquinariaObraDTO{" +
            "id=" + getId() +
            ", horas=" + getHoras() +
            ", obra=" + getObra() +
            ", maquinaria=" + getMaquinaria() +
            "}";
    }
}
