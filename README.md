# 1.“Shiro-汐落” 是什么？

Shiro是软件的英文名，汐落是软件的中文名，下文中统一使用汐落来代指本软件
汐落是一款个人财务管理软件，目前已经初步完成了后端的基础功能，前端完成了移动端网页，桌面端还没有开始写，汐落目前已经实现了以下功能：

## 账单记录

这是汐落的基本功能
用户可以记录自己的账单，收入以及支出都可以记录
收入和支出可以选择对应的类型，比如：“饮食”，“购物”支出，“工资”，“兼职”收入

## 循环付费商品记录

这是一套和账单记录分离的系统，用来帮助用户记录自己的循环付费商品
可以记录商品的名称，记录开始时间，结束时间，循环周期以及自动续费状态，用户选择付费周期并输入周期所需金额，汐落会自动帮助用户换算该商品的平均月付和平均年付价格，方便用户管理自己的付费情况

## 数据展示

展示用户记录的账单情况，支持按月，按年指定查看账单

（其他数据功能开发中）

## 个人信息

用户可以更改自己的各项信息，比如邮箱，姓名，性别等，也可以通过点击头像的方式上传新的头像



# 2.汐落的立意

汐落，灵感源自日语 **shiro**，意为白色，象征着一种清晰与纯净的生活态度。
我相信，记账不该是复杂的，而是一种自然规律，就像潮水的涨落，让生活变得有序且宁静。
在汐落，每一笔记账，都是在为你的生活增添一种清晰的美感。



# 3.汐落-Backend说明文档

## 技术选型

后端使用了Springboot2.7 + MybatisPlus + Knife4j(openapi2版本) + MySQL +Redis

Redis在这里与SpringCache搭配使用，用于管理查询账单数据的缓存，以及用户邮箱登录的验证码



## 数据库创建

```sql
CREATE DATABASE `shiro_local` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
```

### users:

```sql
-- shiro_local.users definition

CREATE TABLE `users` (
  `id` bigint NOT NULL COMMENT '用户ID，主键，是项目中唯一一个由MybatisPlus使用雪花算法生成的id属性，其他都是自增',
  `name` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称，1到10个字符',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '加密后的密码',
  `sex` int NOT NULL DEFAULT '2' COMMENT '性别，0是男性，1是女性，预留扩展数值支持其他选项',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用于存放用户的邮箱，邮箱是用户登录以及找回密码的重要凭据',
  `avatar` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'https://shiro-1305030750.cos.ap-shanghai.myqcloud.com/15.jpg' COMMENT '用户头像URL，默认URL由后端管理',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除字段，标记是否被删除',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
```

### bills：

```sql
-- shiro_local.bills definition

CREATE TABLE `bills` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '账单ID，自增，主键',
  `userid` bigint NOT NULL COMMENT '用户ID，外键，关联user表的id',
  `amount` decimal(10,2) NOT NULL COMMENT '金额，可以为正数或负数',
  `type` int NOT NULL COMMENT '账单类型，收入或支出，使用整数表示',
  `category_id` bigint NOT NULL COMMENT '交易分类ID，用来记录交易的分类',
  `detail` text COLLATE utf8mb4_unicode_ci COMMENT '消费详情，可为空，用于记录备注信息',
  `date` date NOT NULL COMMENT '账单日期，精确到年月日，由用户前端输入',
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '逻辑删除字段，标记是否被删除',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账单记录表';
```

### category
```sql
-- shiro_local.category definition

CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类的名称',
  `user_id` bigint NOT NULL COMMENT '外键，用于关联users表单的id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认当前时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间，自动更新为当前时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账单分类表';
```

### renew_bill
```sql
-- shiro_local.renew_bill definition

CREATE TABLE `renew_bill` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '周期计费记录ID，主键，自增',
  `user_id` bigint NOT NULL COMMENT '循环账单关联的用户id',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '周期计费商品名称',
  `details` text COLLATE utf8mb4_unicode_ci COMMENT '周期计费商品备注',
  `category_id` bigint NOT NULL COMMENT '循环计费商品分类ID，用来记录对应的分类',
  `cycle` int NOT NULL COMMENT '计费周期，数字含义由前端解析',
  `beginning` date NOT NULL COMMENT '循环付费开始时间',
  `ending` date NOT NULL COMMENT '循环付费结束时间',
  `renew` int NOT NULL DEFAULT '0' COMMENT '是否自动续费，数字含义由前端解析',
  `cost` decimal(10,2) NOT NULL COMMENT '周期计费的费用',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除字段，标记是否被删除',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='周期计费表，存储循环计费信息';
```

### renew_category
```sql
-- shiro_local.renew_category definition

CREATE TABLE `renew_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `name` varchar(10) NOT NULL COMMENT '分类名称',
  `user_id` bigint NOT NULL COMMENT '外键，关联users表的id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认当前时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间，自动更新为当前时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='循环付费账单分类表';
```


### images
```sql
-- shiro_local.images definition

CREATE TABLE `images` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '图片ID，主键，自增',
  `bill_id` bigint DEFAULT NULL COMMENT '关联账单表的ID，外键，可以为空',
  `renew_id` bigint DEFAULT NULL COMMENT '关联周期计费表的ID，外键，可以为空',
  `url` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片地址，存储后端返回的URL',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除字段，标记是否被删除',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  KEY `bill_id` (`bill_id`),
  KEY `renew_id` (`renew_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图片表，存储账单或周期计费相关的图片附件';
```

### 外键配置：

tips:

这里会先进行外键检查和删除，删除的时候因为还没有创建外键所以会报错，跳过就可以，没有问题的

```sql
-- 先查看外键约束
SELECT CONSTRAINT_NAME 
FROM information_schema.TABLE_CONSTRAINTS 
WHERE TABLE_SCHEMA = 'shiro_local' 
AND TABLE_NAME = '表名' 
AND CONSTRAINT_TYPE = 'FOREIGN KEY';

-- 然后删除现有的外键约束
ALTER TABLE bills DROP FOREIGN KEY fk_bills_users;
ALTER TABLE bills DROP FOREIGN KEY fk_bills_category;
ALTER TABLE category DROP FOREIGN KEY fk_category_users;
ALTER TABLE renew_category DROP FOREIGN KEY fk_renew_category_users;
ALTER TABLE renew_bill DROP FOREIGN KEY fk_renew_bill_users;
ALTER TABLE renew_bill DROP FOREIGN KEY fk_renew_bill_category;
ALTER TABLE images DROP FOREIGN KEY fk_images_bills;
ALTER TABLE images DROP FOREIGN KEY fk_images_renew;

-- 添加新的外键约束（带有级联删除）
ALTER TABLE category ADD CONSTRAINT fk_category_users 
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE renew_category ADD CONSTRAINT fk_renew_category_users 
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE bills ADD CONSTRAINT fk_bills_users 
FOREIGN KEY (userid) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE renew_bill ADD CONSTRAINT fk_renew_bill_users 
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE bills ADD CONSTRAINT fk_bills_category 
FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE;

ALTER TABLE renew_bill ADD CONSTRAINT fk_renew_bill_category 
FOREIGN KEY (category_id) REFERENCES renew_category(id) ON DELETE CASCADE;

ALTER TABLE images ADD CONSTRAINT fk_images_bills 
FOREIGN KEY (bill_id) REFERENCES bills(id) ON DELETE CASCADE;

ALTER TABLE images ADD CONSTRAINT fk_images_renew 
FOREIGN KEY (renew_id) REFERENCES renew_bill(id) ON DELETE CASCADE;
```
