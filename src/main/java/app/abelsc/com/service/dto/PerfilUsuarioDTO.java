package app.abelsc.com.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link app.abelsc.com.domain.PerfilUsuario} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PerfilUsuarioDTO implements Serializable {

    private Long id;

    private AdminUserDTO user;

    private EmpresaDTO empresa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmpresaDTO getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaDTO empresa) {
        this.empresa = empresa;
    }

    public AdminUserDTO getUser() {
        return user;
    }

    public void setUser(AdminUserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PerfilUsuarioDTO)) {
            return false;
        }

        PerfilUsuarioDTO perfilUsuarioDTO = (PerfilUsuarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, perfilUsuarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerfilUsuarioDTO{" +
            "id=" + getId() +
            ", empresa=" + getEmpresa() +
            "}";
    }
}
