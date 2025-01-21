package app.abelsc.com.service.mapper;

import app.abelsc.com.domain.Material;
import app.abelsc.com.domain.MaterialObra;
import app.abelsc.com.domain.Obra;
import app.abelsc.com.service.dto.MaterialDTO;
import app.abelsc.com.service.dto.MaterialObraDTO;
import app.abelsc.com.service.dto.ObraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MaterialObra} and its DTO {@link MaterialObraDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaterialObraMapper extends EntityMapper<MaterialObraDTO, MaterialObra> {
    @Mapping(target = "obra", source = "obra", qualifiedByName = "obraId")
    @Mapping(target = "material", source = "material", qualifiedByName = "materialId")
    MaterialObraDTO toDto(MaterialObra s);

    @Named("obraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ObraDTO toDtoObraId(Obra obra);

    @Named("materialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MaterialDTO toDtoMaterialId(Material material);
}
