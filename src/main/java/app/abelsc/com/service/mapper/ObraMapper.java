package app.abelsc.com.service.mapper;

import app.abelsc.com.domain.Cliente;
import app.abelsc.com.domain.Empresa;
import app.abelsc.com.domain.Obra;
import app.abelsc.com.service.dto.ClienteDTO;
import app.abelsc.com.service.dto.EmpresaDTO;
import app.abelsc.com.service.dto.ObraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Obra} and its DTO {@link ObraDTO}.
 */
@Mapper(componentModel = "spring")
public interface ObraMapper extends EntityMapper<ObraDTO, Obra> {
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "empresaId")
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteId")
    ObraDTO toDto(Obra s);

    @Named("empresaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpresaDTO toDtoEmpresaId(Empresa empresa);

    @Named("clienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClienteDTO toDtoClienteId(Cliente cliente);
}
