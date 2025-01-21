package app.abelsc.com.service.dto;

import app.abelsc.com.domain.enumeration.EstadoObra;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link app.abelsc.com.domain.Obra} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ObraDTO implements Serializable {

    private Long id;

    private String nombre;

    private String direccion;

    private Instant fechaInicio;

    private Instant fechaFin;

    private Long coste;

    private EstadoObra estado;

    private Long costePagado;

    private EmpresaDTO empresa;

    private ClienteDTO cliente;

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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Instant getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Instant fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getCoste() {
        return coste;
    }

    public void setCoste(Long coste) {
        this.coste = coste;
    }

    public EstadoObra getEstado() {
        return estado;
    }

    public void setEstado(EstadoObra estado) {
        this.estado = estado;
    }

    public Long getCostePagado() {
        return costePagado;
    }

    public void setCostePagado(Long costePagado) {
        this.costePagado = costePagado;
    }

    public EmpresaDTO getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaDTO empresa) {
        this.empresa = empresa;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ObraDTO)) {
            return false;
        }

        ObraDTO obraDTO = (ObraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, obraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ObraDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", coste=" + getCoste() +
            ", estado='" + getEstado() + "'" +
            ", costePagado=" + getCostePagado() +
            ", empresa=" + getEmpresa() +
            ", cliente=" + getCliente() +
            "}";
    }
}
