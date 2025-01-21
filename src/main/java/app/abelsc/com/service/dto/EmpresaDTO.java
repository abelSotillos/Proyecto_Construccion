package app.abelsc.com.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link app.abelsc.com.domain.Empresa} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmpresaDTO implements Serializable {

    private Long id;

    private String nombre;

    private String nif;

    private String calle;

    private String telefono;

    private String provincia;

    private String poblacion;

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

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmpresaDTO)) {
            return false;
        }

        EmpresaDTO empresaDTO = (EmpresaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, empresaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpresaDTO{" +
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
