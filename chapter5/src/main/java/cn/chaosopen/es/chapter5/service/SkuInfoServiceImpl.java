package cn.chaosopen.es.chapter5.service;

import cn.chaosopen.es.chapter5.entity.SkuInfo;
import cn.chaosopen.es.chapter5.mapper.SkuInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {
}
