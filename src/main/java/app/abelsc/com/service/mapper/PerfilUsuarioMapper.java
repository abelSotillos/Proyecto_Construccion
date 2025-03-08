package app.abelsc.com.service.mapper;

import app.abelsc.com.domain.Empresa;
import app.abelsc.com.domain.PerfilUsuario;
import app.abelsc.com.service.dto.EmpresaDTO;
import app.abelsc.com.service.dto.PerfilUsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PerfilUsuario} and its DTO {@link PerfilUsuarioDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface PerfilUsuarioMapper extends EntityMapper<PerfilUsuarioDTO, PerfilUsuario> {
    @Mapping(target = "empresa", source = "empresa")
    @Mapping(target = "user", source = "usuario")
    PerfilUsuarioDTO toDto(PerfilUsuario s);

    @Named("empresaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    EmpresaDTO toDtoEmpresaId(Empresa empresa);
}
