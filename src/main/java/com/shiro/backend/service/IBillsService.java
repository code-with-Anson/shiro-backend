package com.shiro.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.backend.domain.dto.BillsDTO;
import com.shiro.backend.domain.po.Bills;

/**
 * <p>
 * 账单记录表 服务类
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
public interface IBillsService extends IService<Bills> {
    void saveBills(BillsDTO billsDTO);
}
