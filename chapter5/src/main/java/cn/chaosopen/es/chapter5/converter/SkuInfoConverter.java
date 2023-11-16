package cn.chaosopen.es.chapter5.converter;

import cn.chaosopen.es.chapter5.dto.SkuInfoDTO;
import cn.chaosopen.es.chapter5.entity.SkuInfo;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(builder = @Builder(disableBuilder = true), componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface SkuInfoConverter {

    SkuInfo dtoToDo(SkuInfoDTO skuInfoDTO);

}
