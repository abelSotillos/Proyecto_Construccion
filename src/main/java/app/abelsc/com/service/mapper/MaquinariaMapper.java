package app.abelsc.com.service.mapper;

import app.abelsc.com.domain.Empresa;
import app.abelsc.com.domain.Maquinaria;
import app.abelsc.com.service.dto.EmpresaDTO;
import app.abelsc.com.service.dto.MaquinariaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Maquinaria} and its DTO {@link MaquinariaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaquinariaMapper extends EntityMapper<MaquinariaDTO, Maquinaria> {
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "empresaId")
    MaquinariaDTO toDto(Maquinaria s);

    @Named("empresaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpresaDTO toDtoEmpresaId(Empresa empresa);
}
