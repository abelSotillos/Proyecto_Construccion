package app.abelsc.com.domain;

import app.abelsc.com.domain.enumeration.EstadoMaquinaria;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Maquinaria.
 */
@Entity
@Table(name = "maquinaria")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Maquinaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "modelo")
    private String modelo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoMaquinaria estado;

    @Column(name = "precio")
    private Long precio;

    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Maquinaria id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return this.modelo;
    }

    public Maquinaria modelo(String modelo) {
        this.setModelo(modelo);
        return this;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public EstadoMaquinaria getEstado() {
        return this.estado;
    }

    public Maquinaria estado(EstadoMaquinaria estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoMaquinaria estado) {
        this.estado = estado;
    }

    public Long getPrecio() {
        return this.precio;
    }

    public Maquinaria precio(Long precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Maquinaria empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Maquinaria)) {
            return false;
        }
        return getId() != null && getId().equals(((Maquinaria) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Maquinaria{" +
            "id=" + getId() +
            ", modelo='" + getModelo() + "'" +
            ", estado='" + getEstado() + "'" +
            ", precio=" + getPrecio() +
            "}";
    }
}
