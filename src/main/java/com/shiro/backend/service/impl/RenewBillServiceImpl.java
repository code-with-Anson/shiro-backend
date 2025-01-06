package com.shiro.backend.service.impl;

import com.shiro.backend.domain.po.RenewBill;
import com.shiro.backend.mapper.RenewBillMapper;
import com.shiro.backend.service.IRenewBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 周期计费表，存储循环计费信息 服务实现类
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Service
public class RenewBillServiceImpl extends ServiceImpl<RenewBillMapper, RenewBill> implements IRenewBillService {

}
