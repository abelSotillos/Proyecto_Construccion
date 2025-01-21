package app.abelsc.com.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link app.abelsc.com.domain.Empleado} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmpleadoDTO implements Serializable {

    private Long id;

    private String nombre;

    private String dni;

    private Long salario;

    private Instant fechaContratacion;

    private EmpresaDTO empresa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Long getSalario() {
        return salario;
    }

    public void setSalario(Long salario) {
        this.salario = salario;
    }

    public Instant getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Instant fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public EmpresaDTO getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaDTO empresa) {
        this.empresa = empresa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmpleadoDTO)) {
            return false;
        }

        EmpleadoDTO empleadoDTO = (EmpleadoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, empleadoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpleadoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", dni='" + getDni() + "'" +
            ", salario=" + getSalario() +
            ", fechaContratacion='" + getFechaContratacion() + "'" +
            ", empresa=" + getEmpresa() +
            "}";
    }
}
