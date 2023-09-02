package com.swx.ucenter.mapper;

import com.swx.ucenter.model.po.XcMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sw-code
 * @since 2023-08-31
 */
public interface XcMenuMapper extends BaseMapper<XcMenu> {

    @Select("SELECT code FROM xc_menu WHERE id IN(SELECT menu_id FROM xc_permission WHERE role_id IN(SELECT role_id FROM xc_user_role WHERE user_id = #{userId}))")
    List<String> selectPermissionCodeByUserId(@Param("userId") String userId);
}
