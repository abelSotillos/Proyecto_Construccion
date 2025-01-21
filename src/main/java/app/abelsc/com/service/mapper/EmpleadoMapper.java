package app.abelsc.com.service.mapper;

import app.abelsc.com.domain.Empleado;
import app.abelsc.com.domain.Empresa;
import app.abelsc.com.service.dto.EmpleadoDTO;
import app.abelsc.com.service.dto.EmpresaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Empleado} and its DTO {@link EmpleadoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmpleadoMapper extends EntityMapper<EmpleadoDTO, Empleado> {
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "empresaId")
    EmpleadoDTO toDto(Empleado s);

    @Named("empresaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpresaDTO toDtoEmpresaId(Empresa empresa);
}
