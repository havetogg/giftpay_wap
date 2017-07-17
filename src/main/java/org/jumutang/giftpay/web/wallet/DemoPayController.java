package org.jumutang.giftpay.web.wallet;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jumutang.caranswer.wechat.CodeMess;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.entity.WCPreOrderRequest;
import org.jumutang.giftpay.entity.WCPreOrderResponse;
import org.jumutang.giftpay.model.BalanceModel;
import org.jumutang.giftpay.model.OilRecordModel;
import org.jumutang.giftpay.model.PayInfoModel;
import org.jumutang.giftpay.model.UserOilInfoModel;
import org.jumutang.giftpay.tools.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RuanYJ on 2017/5/5.
 */
@Controller
@RequestMapping(value = "/giftpay/demopay", method = {RequestMethod.GET, RequestMethod.POST})
public class DemoPayController extends BaseController{


}
