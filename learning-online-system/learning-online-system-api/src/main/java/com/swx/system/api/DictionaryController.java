package com.swx.system.api;

import com.swx.system.model.po.Dictionary;
import com.swx.system.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统字典接口类
 */
@Api("系统字典接口类")
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @ApiOperation("查询所有字典")
    @GetMapping("/all")
    public List<Dictionary> queryAll() {
        return dictionaryService.list();
    }

    @GetMapping("/code/{code}")
    public Dictionary getByCode(@PathVariable("code") String code) {
        return dictionaryService.getByCode(code);
    }
}
