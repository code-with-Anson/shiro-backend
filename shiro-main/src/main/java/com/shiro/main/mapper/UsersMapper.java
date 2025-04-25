package com.shiro.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiro.main.domain.po.Users;
import com.shiro.main.enums.isDeletedEnum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
public interface UsersMapper extends BaseMapper<Users> {
    List<Users> findDeletedUsers();

    List<Users> findUsersByDeletedStatus(@Param("deletedStatus") isDeletedEnum deletedStatus);
}
