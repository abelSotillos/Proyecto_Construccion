package app.abelsc.com.service.dto;

import app.abelsc.com.domain.enumeration.EstadoMaquinaria;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link app.abelsc.com.domain.Maquinaria} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaquinariaDTO implements Serializable {

    private Long id;

    private String modelo;

    private EstadoMaquinaria estado;

    private Long precio;

    private EmpresaDTO empresa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public EstadoMaquinaria getEstado() {
        return estado;
    }

    public void setEstado(EstadoMaquinaria estado) {
        this.estado = estado;
    }

    public Long getPrecio() {
        return precio;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
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
        if (!(o instanceof MaquinariaDTO)) {
            return false;
        }

        MaquinariaDTO maquinariaDTO = (MaquinariaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, maquinariaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaquinariaDTO{" +
            "id=" + getId() +
            ", modelo='" + getModelo() + "'" +
            ", estado='" + getEstado() + "'" +
            ", precio=" + getPrecio() +
            ", empresa=" + getEmpresa() +
            "}";
    }
}
