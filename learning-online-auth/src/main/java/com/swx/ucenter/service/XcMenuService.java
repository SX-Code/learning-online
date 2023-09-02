package com.swx.ucenter.service;

import com.swx.ucenter.model.po.XcMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-31
 */
public interface XcMenuService extends IService<XcMenu> {

    /**
     * 根据用户ID查询用户权限列表
     *
     * @param userId 用户ID
     * @return List<com.swx.ucenter.model.po.XcMenu> 权限列表
     */
    List<String> selectPermissionCodeByUserId(String userId);
}
