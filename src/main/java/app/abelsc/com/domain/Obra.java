package app.abelsc.com.domain;

import app.abelsc.com.domain.enumeration.EstadoObra;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Obra.
 */
@Entity
@Table(name = "obra")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Obra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "fecha_inicio")
    private Instant fechaInicio;

    @Column(name = "fecha_fin")
    private Instant fechaFin;

    @Column(name = "coste")
    private Long coste;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoObra estado;

    @Column(name = "coste_pagado")
    private Long costePagado;

    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Obra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Obra nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Obra direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Instant getFechaInicio() {
        return this.fechaInicio;
    }

    public Obra fechaInicio(Instant fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaFin() {
        return this.fechaFin;
    }

    public Obra fechaFin(Instant fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(Instant fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getCoste() {
        return this.coste;
    }

    public Obra coste(Long coste) {
        this.setCoste(coste);
        return this;
    }

    public void setCoste(Long coste) {
        this.coste = coste;
    }

    public EstadoObra getEstado() {
        return this.estado;
    }

    public Obra estado(EstadoObra estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoObra estado) {
        this.estado = estado;
    }

    public Long getCostePagado() {
        return this.costePagado;
    }

    public Obra costePagado(Long costePagado) {
        this.setCostePagado(costePagado);
        return this;
    }

    public void setCostePagado(Long costePagado) {
        this.costePagado = costePagado;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Obra empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Obra cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Obra)) {
            return false;
        }
        return getId() != null && getId().equals(((Obra) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Obra{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", coste=" + getCoste() +
            ", estado='" + getEstado() + "'" +
            ", costePagado=" + getCostePagado() +
            "}";
    }
}
