package com.shiro.backend.service.impl;

import com.shiro.backend.domain.po.Bills;
import com.shiro.backend.mapper.BillsMapper;
import com.shiro.backend.service.IBillsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账单记录表 服务实现类
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Service
public class BillsServiceImpl extends ServiceImpl<BillsMapper, Bills> implements IBillsService {

}
