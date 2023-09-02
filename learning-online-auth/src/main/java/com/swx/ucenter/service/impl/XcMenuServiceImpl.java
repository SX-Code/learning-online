package com.swx.ucenter.service.impl;

import com.swx.ucenter.model.po.XcMenu;
import com.swx.ucenter.mapper.XcMenuMapper;
import com.swx.ucenter.service.XcMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-31
 */
@Service
public class XcMenuServiceImpl extends ServiceImpl<XcMenuMapper, XcMenu> implements XcMenuService {

    /**
     * 根据用户ID查询用户权限列表
     *
     * @param userId 用户ID
     * @return List<com.swx.ucenter.model.po.XcMenu> 权限列表
     */
    @Override
    public List<String> selectPermissionCodeByUserId(String userId) {
        return baseMapper.selectPermissionCodeByUserId(userId);
    }
}
