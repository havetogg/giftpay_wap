/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2015/12/9 15:17:18                           */
/*==============================================================*/


drop table if exists t_authorize;

drop table if exists t_businessinfo;

drop table if exists t_gascard;

drop table if exists t_gascardchangerecord;

drop table if exists t_gaspackages;

drop table if exists t_maintenance;

drop table if exists t_orderinfo;

drop table if exists t_redinfo;

drop table if exists t_redpickdetail;

drop table if exists t_redpickinfo;

drop table if exists t_source;

drop table if exists t_userinfo;

drop table if exists t_userloginrecord;

/*==============================================================*/
/* Table: t_authorize                                           */
/*==============================================================*/
create table t_authorize
(
   c_authorizeid        varchar(32) not null comment '授权主键',
   c_userid             varchar(32) comment '用户主键',
   c_nikename           varchar(100) comment '授权昵称',
   c_sex                varchar(1) comment '授权性别',
   c_province           varchar(100) comment '授权省份',
   c_city               varchar(100) comment '授权地市',
   c_country            varchar(100) comment '授权国家',
   c_headimgurl         varchar(200) comment '授权头像',
   c_privilege          varchar(200) comment '特权信息',
   c_createtime         datetime not null comment '创建时间',
   c_updatetime         datetime comment '修改时间',
   c_deletetime         datetime comment '删除时间',
   c_wechatcode         varchar(200) comment '微信授权id',
   primary key (c_authorizeid)
);

alter table t_authorize comment '用户授权登录';

/*==============================================================*/
/* Table: t_businessinfo                                        */
/*==============================================================*/
create table t_businessinfo
(
   c_businessid         varchar(40) not null comment '合作id',
   c_context            varchar(200),
   c_usertel            varchar(20) comment '用户联系电话',
   c_username           varchar(100) comment '联系人',
   c_addtime            datetime comment '添加时间',
   primary key (c_businessid)
);

alter table t_businessinfo comment '商务合作';

/*==============================================================*/
/* Table: t_gascard                                             */
/*==============================================================*/
create table t_gascard
(
   c_gascardid          varchar(32) not null comment '加油卡信息主键',
   c_userid             varchar(32) comment '用户主键',
   c_gascardnumber      varchar(30) not null comment '加油卡号',
   c_buildtel           varchar(11) not null comment '加油卡绑定的手机号',
   c_builduname         varchar(50) not null comment '加油卡绑定的用户姓名',
   c_createtime         datetime not null comment '创建时间',
   c_updatetime         datetime comment '修改时间',
   c_deletetime         datetime comment '删除时间',
   primary key (c_gascardid)
);

alter table t_gascard comment '用户加油卡信息';

/*==============================================================*/
/* Table: t_gascardchangerecord                                 */
/*==============================================================*/
create table t_gascardchangerecord
(
   c_changeid           varchar(32) not null comment '加油卡变动主键',
   c_gascardid          varchar(32) comment '加油卡信息主键',
   c_oldgascard         varchar(30) not null comment '旧的加油卡号',
   c_newgascard         varchar(30) not null comment '新的加油卡号',
   c_createtime         datetime not null comment '创建时间',
   c_updatetime         datetime comment '修改时间',
   c_deletetime         datetime comment '删除时间',
   primary key (c_changeid)
);

alter table t_gascardchangerecord comment '加油卡变动记录表';

/*==============================================================*/
/* Table: t_gaspackages                                         */
/*==============================================================*/
create table t_gaspackages
(
   c_gaspackagesid      varchar(32) not null comment '加油卡套餐主键',
   c_gaspackagesname    varchar(100),
   c_price              double(10,2) not null comment '套餐价格',
   c_monthcount         integer not null comment '套餐返还的月份数量',
   c_monthmoney         double(10,2) not null comment '每月返还的金额',
   c_allmoney           double(10,2) not null comment '套餐总返还金额',
   c_createtime         datetime not null comment '套餐创建时间',
   c_updatetime         datetime comment '修改时间',
   c_deletetime         datetime comment '删除时间',
   primary key (c_gaspackagesid)
);

alter table t_gaspackages comment '加油卡套餐表';

/*==============================================================*/
/* Table: t_maintenance                                         */
/*==============================================================*/
create table t_maintenance
(
   c_maintenanceid      varchar(32) not null,
   c_usertel            varchar(11),
   c_cartype            varchar(100),
   c_servicetype        varchar(50),
   c_addtime            datetime,
   primary key (c_maintenanceid)
);

alter table t_maintenance comment '代办保养';

/*==============================================================*/
/* Table: t_orderinfo                                           */
/*==============================================================*/
create table t_orderinfo
(
   c_orderid            varchar(32) not null comment '订单主键、订单号',
   c_gaspackagesid      varchar(32) comment '加油卡套餐主键',
   c_userid             varchar(32) comment '用户主键',
   c_price              double(10,2) not null comment '订单价格',
   c_businessorderid    varchar(32) comment '第三方订单号',
   c_status             varchar(1) comment '订单支付状态
            2：待支付
            1：支付成功
            0：支付失败
            ',
   c_result             varchar(500) comment '第三方支付渠道返回通知',
   c_createtime         datetime not null comment '订单创建时间',
   c_updatetime         datetime comment '订单修改时间',
   c_deletetime         datetime comment '订单删除时间',
   c_payurl             varchar(2000) comment '支付链接',
   primary key (c_orderid)
);

alter table t_orderinfo comment '购买订单表';

/*==============================================================*/
/* Table: t_redinfo                                             */
/*==============================================================*/
create table t_redinfo
(
   c_redid              varchar(32) not null comment '红包主键',
   c_userid             varchar(32) comment '用户主键',
   c_orderid            varchar(32) comment '订单主键、订单号',
   c_redname            varchar(200) not null comment '红包名称',
   c_redmoney           double(10,2) not null comment '红包金额',
   c_starttime          datetime not null,
   c_endtime            datetime not null comment '红包有效期结束时间',
   c_createtime         datetime not null comment '红包创建时间',
   c_updatetime         datetime comment '红包修改时间',
   c_deletetime         datetime comment '红包删除时间',
   c_preferential       double(10,2),
   primary key (c_redid)
);

alter table t_redinfo comment '红包信息表';

/*==============================================================*/
/* Table: t_redpickdetail                                       */
/*==============================================================*/
create table t_redpickdetail
(
   c_redpickdetailid    varchar(32) not null comment '红包提取详情',
   c_redpickid          varchar(32) comment '红包提取信息主键',
   c_redid              varchar(32) comment '红包主键',
   c_createtime         datetime not null comment '创建时间',
   c_updatetime         datetime comment '修改时间',
   c_deletetime         datetime comment '删除时间',
   primary key (c_redpickdetailid)
);

alter table t_redpickdetail comment '红包提取详情';

/*==============================================================*/
/* Table: t_redpickinfo                                         */
/*==============================================================*/
create table t_redpickinfo
(
   c_redpickid          varchar(32) not null comment '红包提取信息主键',
   c_userid             varchar(32) comment '用户主键',
   c_redmoney           double(10,2),
   c_rednumber          varchar(3),
   c_status             varchar(1) comment '提取状态
            1：提取成功
            0：提出失败
            2: 创建红包',
   c_result             varchar(500) comment '第三方提现返回的结果集信息',
   c_businessorderid    varchar(32) comment '第三方订单号',
   c_pickmeno           varchar(100) comment '提取用途备注',
   c_createtime         datetime not null,
   c_updatetime         datetime comment '修改时间',
   c_deletetime         datetime comment '删除时间',
   primary key (c_redpickid)
);

alter table t_redpickinfo comment '红包提取信息表';

/*==============================================================*/
/* Table: t_source                                              */
/*==============================================================*/
create table t_source
(
   c_sourceid           varchar(32) not null comment '来源主键',
   c_name               varchar(100) not null comment '来源名称',
   primary key (c_sourceid)
);

alter table t_source comment '用户来源表';

/*==============================================================*/
/* Table: t_userinfo                                            */
/*==============================================================*/
create table t_userinfo
(
   c_userid             varchar(32) not null comment '用户主键',
   c_sourceid           varchar(32) comment '来源主键',
   c_utel               varchar(11) not null comment '用户注册手机号',
   c_recommendcode      varchar(11) comment '用户推荐码',
   c_createtime         datetime not null comment '用户注册时间',
   c_updatetime         datetime comment '用户修改时间',
   c_deletetime         datetime comment '用户删除时间',
   primary key (c_userid)
);

alter table t_userinfo comment '用户信息表';

/*==============================================================*/
/* Table: t_userloginrecord                                     */
/*==============================================================*/
create table t_userloginrecord
(
   c_loginrecordid      varchar(32) not null comment '用户登录记录主键',
   c_userid             varchar(32) comment '用户主键',
   c_createtime         datetime not null comment '登录记录创建时间',
   c_updatetime         datetime,
   c_deletetime         datetime,
   primary key (c_loginrecordid)
);

alter table t_userloginrecord comment '用户登录记录表';

