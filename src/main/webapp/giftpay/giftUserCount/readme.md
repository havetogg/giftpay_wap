1.用户根据链接地址，静默授权获取用户的openId。
2.查询用户表中当前用户是否存在手机号码。条件openId。
3.用户不存在手机号码，update当前用户的手机号码。如果存在进行下一步操作
4.根据页面地址，新增渠道表中的数据到数据库，作为当前用户访问进入的统计代码