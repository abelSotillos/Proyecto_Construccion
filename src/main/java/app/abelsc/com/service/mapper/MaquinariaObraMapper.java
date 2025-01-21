package app.abelsc.com.service.mapper;

import app.abelsc.com.domain.Maquinaria;
import app.abelsc.com.domain.MaquinariaObra;
import app.abelsc.com.domain.Obra;
import app.abelsc.com.service.dto.MaquinariaDTO;
import app.abelsc.com.service.dto.MaquinariaObraDTO;
import app.abelsc.com.service.dto.ObraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MaquinariaObra} and its DTO {@link MaquinariaObraDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaquinariaObraMapper extends EntityMapper<MaquinariaObraDTO, MaquinariaObra> {
    @Mapping(target = "obra", source = "obra", qualifiedByName = "obraId")
    @Mapping(target = "maquinaria", source = "maquinaria", qualifiedByName = "maquinariaId")
    MaquinariaObraDTO toDto(MaquinariaObra s);

    @Named("obraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ObraDTO toDtoObraId(Obra obra);

    @Named("maquinariaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MaquinariaDTO toDtoMaquinariaId(Maquinaria maquinaria);
}
