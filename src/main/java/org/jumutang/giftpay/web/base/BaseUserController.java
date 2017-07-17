package org.jumutang.giftpay.web.base;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by RuanYJ on 2017/1/14.
 */

/**
 * 用户基本等
 */
@Controller
@RequestMapping(value = "/user", method = {RequestMethod.GET, RequestMethod.POST})
public class BaseUserController {

}
