package app.abelsc.com.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Empresa.
 */
@Entity
@Table(name = "empresa")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "nif")
    private String nif;

    @Column(name = "calle")
    private String calle;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "poblacion")
    private String poblacion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empresa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Empresa nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNif() {
        return this.nif;
    }

    public Empresa nif(String nif) {
        this.setNif(nif);
        return this;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getCalle() {
        return this.calle;
    }

    public Empresa calle(String calle) {
        this.setCalle(calle);
        return this;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Empresa telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getProvincia() {
        return this.provincia;
    }

    public Empresa provincia(String provincia) {
        this.setProvincia(provincia);
        return this;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPoblacion() {
        return this.poblacion;
    }

    public Empresa poblacion(String poblacion) {
        this.setPoblacion(poblacion);
        return this;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empresa)) {
            return false;
        }
        return getId() != null && getId().equals(((Empresa) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empresa{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", nif='" + getNif() + "'" +
            ", calle='" + getCalle() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", provincia='" + getProvincia() + "'" +
            ", poblacion='" + getPoblacion() + "'" +
            "}";
    }
}
