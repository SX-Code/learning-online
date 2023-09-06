package com.swx.orders.model.vo;

import com.swx.orders.model.po.XcPayRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 支付记录dto
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class PayRecordVO extends XcPayRecord {

    //二维码
    private String qrcode;

}
