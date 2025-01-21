package app.abelsc.com.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Empleado.
 */
@Entity
@Table(name = "empleado")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "dni")
    private String dni;

    @Column(name = "salario")
    private Long salario;

    @Column(name = "fecha_contratacion")
    private Instant fechaContratacion;

    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empleado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Empleado nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return this.dni;
    }

    public Empleado dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Long getSalario() {
        return this.salario;
    }

    public Empleado salario(Long salario) {
        this.setSalario(salario);
        return this;
    }

    public void setSalario(Long salario) {
        this.salario = salario;
    }

    public Instant getFechaContratacion() {
        return this.fechaContratacion;
    }

    public Empleado fechaContratacion(Instant fechaContratacion) {
        this.setFechaContratacion(fechaContratacion);
        return this;
    }

    public void setFechaContratacion(Instant fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Empleado empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empleado)) {
            return false;
        }
        return getId() != null && getId().equals(((Empleado) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empleado{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", dni='" + getDni() + "'" +
            ", salario=" + getSalario() +
            ", fechaContratacion='" + getFechaContratacion() + "'" +
            "}";
    }
}
