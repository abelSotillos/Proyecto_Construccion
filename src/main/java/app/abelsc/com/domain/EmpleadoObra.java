package app.abelsc.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A EmpleadoObra.
 */
@Entity
@Table(name = "empleado_obra")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmpleadoObra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "horas")
    private Long horas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "empresa", "cliente" }, allowSetters = true)
    private Obra obra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "empresa" }, allowSetters = true)
    private Empleado empleado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmpleadoObra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHoras() {
        return this.horas;
    }

    public EmpleadoObra horas(Long horas) {
        this.setHoras(horas);
        return this;
    }

    public void setHoras(Long horas) {
        this.horas = horas;
    }

    public Obra getObra() {
        return this.obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

    public EmpleadoObra obra(Obra obra) {
        this.setObra(obra);
        return this;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public EmpleadoObra empleado(Empleado empleado) {
        this.setEmpleado(empleado);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmpleadoObra)) {
            return false;
        }
        return getId() != null && getId().equals(((EmpleadoObra) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpleadoObra{" +
            "id=" + getId() +
            ", horas=" + getHoras() +
            "}";
    }
}
