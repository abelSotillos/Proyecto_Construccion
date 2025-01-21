package app.abelsc.com.service.mapper;

import app.abelsc.com.domain.Empleado;
import app.abelsc.com.domain.EmpleadoObra;
import app.abelsc.com.domain.Obra;
import app.abelsc.com.service.dto.EmpleadoDTO;
import app.abelsc.com.service.dto.EmpleadoObraDTO;
import app.abelsc.com.service.dto.ObraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmpleadoObra} and its DTO {@link EmpleadoObraDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmpleadoObraMapper extends EntityMapper<EmpleadoObraDTO, EmpleadoObra> {
    @Mapping(target = "obra", source = "obra", qualifiedByName = "obraId")
    @Mapping(target = "empleado", source = "empleado", qualifiedByName = "empleadoId")
    EmpleadoObraDTO toDto(EmpleadoObra s);

    @Named("obraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ObraDTO toDtoObraId(Obra obra);

    @Named("empleadoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpleadoDTO toDtoEmpleadoId(Empleado empleado);
}
