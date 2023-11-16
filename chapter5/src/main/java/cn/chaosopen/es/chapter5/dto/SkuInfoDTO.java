package cn.chaosopen.es.chapter5.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;

@Data
@Builder
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
public class SkuInfoDTO {

    private Integer id;

    private Integer spuId;

    private String categoryName;

    private String productName;

    private BigDecimal price;

    private String images;

    private String attrs;
}
