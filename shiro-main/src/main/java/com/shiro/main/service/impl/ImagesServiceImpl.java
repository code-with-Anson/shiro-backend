package com.shiro.main.service.impl;

import com.shiro.main.domain.po.Images;
import com.shiro.main.mapper.ImagesMapper;
import com.shiro.main.service.IImagesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图片表，存储账单或周期计费相关的图片附件 服务实现类
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Service
public class ImagesServiceImpl extends ServiceImpl<ImagesMapper, Images> implements IImagesService {

}
