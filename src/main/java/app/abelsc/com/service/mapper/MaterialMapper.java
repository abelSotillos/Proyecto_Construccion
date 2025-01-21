package app.abelsc.com.service.mapper;

import app.abelsc.com.domain.Empresa;
import app.abelsc.com.domain.Material;
import app.abelsc.com.service.dto.EmpresaDTO;
import app.abelsc.com.service.dto.MaterialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Material} and its DTO {@link MaterialDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaterialMapper extends EntityMapper<MaterialDTO, Material> {
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "empresaId")
    MaterialDTO toDto(Material s);

    @Named("empresaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpresaDTO toDtoEmpresaId(Empresa empresa);
}
