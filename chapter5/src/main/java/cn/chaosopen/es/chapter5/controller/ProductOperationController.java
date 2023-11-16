package cn.chaosopen.es.chapter5.controller;

import cn.chaosopen.es.chapter5.converter.SkuInfoConverter;
import cn.chaosopen.es.chapter5.dto.SkuInfoDTO;
import cn.chaosopen.es.chapter5.entity.SkuInfo;
import cn.chaosopen.es.chapter5.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ProductOperationController {

    @Autowired
    private SkuInfoService skuInfoService;

    @Resource
    private SkuInfoConverter skuInfoConverter;

    @RequestMapping("/addProduct")
    public boolean addProduct(@RequestBody SkuInfoDTO skuInfoDTO) {
        SkuInfo skuInfo = skuInfoConverter.dtoToDo(skuInfoDTO);
        skuInfoService.save(skuInfo);
        return true;
    }

    @RequestMapping("/updateProduct")
    public boolean updateProduct(@RequestBody SkuInfoDTO skuInfoDTO) {
        SkuInfo skuInfo = skuInfoConverter.dtoToDo(skuInfoDTO);
        skuInfoService.updateById(skuInfo);
        return true;
    }

    @RequestMapping("/deleteProduct")
    public boolean deleteProduct(@RequestBody SkuInfoDTO skuInfoDTO) {
        skuInfoService.removeById(skuInfoDTO.getId());
        return true;
    }
}
