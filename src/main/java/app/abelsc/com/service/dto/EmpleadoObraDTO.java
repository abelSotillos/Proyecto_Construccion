package app.abelsc.com.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link app.abelsc.com.domain.EmpleadoObra} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmpleadoObraDTO implements Serializable {

    private Long id;

    private Long horas;

    private ObraDTO obra;

    private EmpleadoDTO empleado;

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

    public EmpleadoDTO getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoDTO empleado) {
        this.empleado = empleado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmpleadoObraDTO)) {
            return false;
        }

        EmpleadoObraDTO empleadoObraDTO = (EmpleadoObraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, empleadoObraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpleadoObraDTO{" +
            "id=" + getId() +
            ", horas=" + getHoras() +
            ", obra=" + getObra() +
            ", empleado=" + getEmpleado() +
            "}";
    }
}
