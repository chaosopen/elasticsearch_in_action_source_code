package cn.chaosopen.es.chapter5.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
@TableName("sku_info")
public class SkuInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("spu_id")
    private Integer spuId;

    @TableField("category_name")
    private String categoryName;

    @TableField("product_name")
    private String productName;

    @TableField("price")
    private BigDecimal price;

    @TableField("images")
    private String images;

    @TableField("attrs")
    private String attrs;

    @TableField("created_by")
    private String createdBy;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("updated_by")
    private String updatedBy;

    @TableField("updated_time")
    private LocalDateTime updatedTime;

    @TableField("is_deleted")
    private Integer isDeleted;
}