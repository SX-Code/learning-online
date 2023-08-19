package com.swx.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.system.model.po.Dictionary;

public interface DictionaryService extends IService<Dictionary> {
    /**
     * 根据code获取字典
     *
     * @param code 字典code
     * @return 指定字典
     */
    Dictionary getByCode(String code);
}
