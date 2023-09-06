package com.swx.orders.api;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.swx.base.exception.BizException;
import com.swx.orders.config.AlipayConfig;
import com.swx.orders.model.dto.AddOrderDTO;
import com.swx.orders.model.dto.PayStatusDTO;
import com.swx.orders.model.po.XcPayRecord;
import com.swx.orders.model.vo.PayRecordVO;
import com.swx.orders.service.OrderService;
import com.swx.orders.service.XcPayRecordService;
import com.swx.orders.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Api(value = "订单支付接口", tags = "订单支付接口")
@RestController
public class OrderController {

    @Value("${pay.alipay.APP_ID}")
    private String APP_ID;
    @Value("${pay.alipay.APP_PRIVATE_KEY}")
    private String APP_PRIVATE_KEY;
    @Value("${pay.alipay.ALIPAY_PUBLIC_KEY}")
    private String ALIPAY_PUBLIC_KEY;
    @Value("${pay.alipay.NOTIFY_URL}")
    private String NOTIFY_URL;
    private final OrderService orderService;
    private final XcPayRecordService xcPayRecordService;

    public OrderController(OrderService orderService, XcPayRecordService xcPayRecordService) {
        this.orderService = orderService;
        this.xcPayRecordService = xcPayRecordService;
    }

    @ApiOperation("生成支付二维码")
    @PostMapping("/generatepaycode")
    public PayRecordVO generatePayCode(@RequestBody @Validated AddOrderDTO dto) {
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        String userId = user.getId();
        return orderService.createOrder(userId, dto);
    }

    @ApiOperation("扫码下单接口")
    @RequestMapping("/requestpay")
    public void requestPay(String payNo, HttpServletResponse httpResponse) throws IOException {
        // 判断支付记录号是否存在
        XcPayRecord xcPayRecord = xcPayRecordService.getOneByPayNo(payNo);
        if (xcPayRecord == null) {
            throw new BizException("支付记录不存在");
        }
        if (xcPayRecord.getStatus().equals("601002")) {
            // 该支付记录已成功支付
            throw new BizException("已支付，无需重复支付");
        }
        // 发起支付请求
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, APP_ID, APP_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        // 异步接收地址，仅支持http/https，公网可访问
        request.setNotifyUrl(NOTIFY_URL);
        /******必传参数******/
        JSONObject bizContent = new JSONObject();
        //商户订单号，商家自定义，保持唯一性
        bizContent.put("out_trade_no", payNo);
        //支付金额，最小值0.01元
        bizContent.put("total_amount", xcPayRecord.getTotalPrice());
        //订单标题，不可使用特殊符号
        bizContent.put("subject", xcPayRecord.getOrderName());

        /******可选参数******/
        //手机网站支付默认传值FAST_INSTANT_TRADE_PAY
        bizContent.put("product_code", "QUICK_WAP_WAY");

        request.setBizContent(bizContent.toString());
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new BizException("拉取支付宝支付失败");
        }
        httpResponse.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
    }

    @ApiOperation("查询支付结果")
    @RequestMapping("/payresult")
    public PayRecordVO payResult(String payNo) {
        return orderService.queryRequestPay(payNo);
    }

    @RequestMapping("/paynotify")
    public void paynotify(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
        // 获取支付宝POST过来的反馈信息
        HashMap<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iterator = requestParams.keySet().iterator(); iterator.hasNext();) {
            String name = iterator.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 解决乱码
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE); //调用SDK验证签名
        if (signVerified) {
            // 商户订单号
            String outTradNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 交易状态
            String tradeStatus = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 交易金额
            String totalAmount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 验证成功
            if (tradeStatus.equals("TRADE_SUCCESS")) {
                PayStatusDTO payStatusDTO = new PayStatusDTO();
                payStatusDTO.setOut_trade_no(outTradNo);
                payStatusDTO.setTrade_no(tradeNo);
                payStatusDTO.setTrade_status(tradeStatus);
                payStatusDTO.setApp_id(APP_ID);
                payStatusDTO.setTotal_amount(totalAmount);
                orderService.saveAlipayStatus(payStatusDTO);
            }
            response.getWriter().write("success");
        } else {
            response.getWriter().write("fail");
        }
    }
}
