package com.swx.checkcode.controller;

import com.swx.checkcode.model.CheckCodeParamsDTO;
import com.swx.checkcode.model.CheckCodeResultVO;
import com.swx.checkcode.service.CheckCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "验证码服务接口", tags = "验证码服务接口")
@RestController
public class CheckCodeController {

    private final CheckCodeService picCheckCodeService;

    public CheckCodeController(CheckCodeService picCheckCodeService) {
        this.picCheckCodeService = picCheckCodeService;
    }

    @ApiOperation(value="生成验证信息", notes="生成验证信息")
    @PostMapping(value = "/pic")
    public CheckCodeResultVO generatePicCheckCode(CheckCodeParamsDTO dto){
        return picCheckCodeService.generate(dto);
    }

    @ApiOperation(value="校验", notes="校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "业务名称", required = true, dataType = "String", paramType="query"),
            @ApiImplicitParam(name = "key", value = "验证key", required = true, dataType = "String", paramType="query"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType="query")
    })
    @PostMapping(value = "/verify")
    public Boolean verify(String key, String code){
        return picCheckCodeService.verify(key, code);
    }
}
