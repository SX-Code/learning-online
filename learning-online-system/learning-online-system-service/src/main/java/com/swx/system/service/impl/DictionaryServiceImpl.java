package com.swx.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.system.mapper.DictionaryMapper;
import com.swx.system.model.po.Dictionary;
import com.swx.system.service.DictionaryService;
import org.springframework.stereotype.Service;

@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    /**
     * 根据code获取字典
     *
     * @param code 字典code
     * @return 指定字典
     */
    @Override
    public Dictionary getByCode(String code) {
        return getOne(Wrappers.<Dictionary>lambdaQuery().eq(Dictionary::getCode, code));
    }
}
