package com.swx.checkcode.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.swx.checkcode.model.CheckCodeParamsDTO;
import com.swx.checkcode.model.CheckCodeResultVO;
import com.swx.checkcode.service.AbstractCheckCodeService;
import com.swx.checkcode.service.CheckCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 图片验证码生成器
 */
@Slf4j
@Service("PicCheckCodeService")
public class PicCheckCodeServiceImpl extends AbstractCheckCodeService implements CheckCodeService {

    private final DefaultKaptcha kaptcha;

    public PicCheckCodeServiceImpl(DefaultKaptcha kaptcha) {
        this.kaptcha = kaptcha;
    }

    @Resource(name="NumberLetterCheckCodeGenerator")
    @Override
    public void setCheckCodeGenerator(CheckCodeGenerator checkCodeGenerator) {
        this.checkCodeGenerator = checkCodeGenerator;
    }

    @Resource(name="UUIDKeyGenerator")
    @Override
    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }


    @Resource(name="RedisCheckCodeStore")
    @Override
    public void setCheckCodeStore(CheckCodeStore checkCodeStore) {
        this.checkCodeStore = checkCodeStore;
    }

    /**
     * 生成验证码
     *
     * @param dto 生成验证码参数
     * @return com.swx.checkcode.model.CheckCodeResultDTO 验证码结果
     */
    @Override
    public CheckCodeResultVO generate(CheckCodeParamsDTO dto) {
        GenerateResult generate = generate(dto, 4, "checkcode:", 60);
        String key = generate.getKey();
        String code = generate.getCode();
        String pic = createPic(code);
        CheckCodeResultVO resultDTO = new CheckCodeResultVO();
        resultDTO.setAliasing(pic);
        resultDTO.setKey(key);
        return resultDTO;
    }

    private String createPic(String code) {
        // 生成图片验证码
        ByteArrayOutputStream outputStream = null;
        BufferedImage image = kaptcha.createImage(code);

        outputStream = new ByteArrayOutputStream();
        String imgBase64Encoder = null;
        try {
            // 对字节数组Base64编码
            ImageIO.write(image, "png", outputStream);
            imgBase64Encoder = "data:image/png;base64," + Base64Utils.encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            log.error("图片验证码生成错误", e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                log.error("流关闭失败", e);
            }
        }
        return imgBase64Encoder;
    }

}
