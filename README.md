# 苍穹外卖 Sky Take-Out

苍穹外卖是一个基于 **Spring Boot** 技术栈开发的外卖订餐系统，主要面向商家端和用户端，具备点餐、下单、支付、订单管理等功能，适用于学习和实践企业级后端开发。

## ✨ 项目亮点

- 使用 Spring Boot 构建后端应用
- 支持用户下单与商家接单流程
- 实现订单状态流转与自动超时处理
- 引入 AOP 实现统一日志记录与接口性能分析
- 可扩展，结构清晰，适合二次开发与教学

## 🧰 技术栈

| 技术 | 描述 |
|------|------|
| Spring Boot | 后端框架 |
| MyBatis-Plus | ORM 持久层框架 |
| Redis | 缓存与并发优化 |
| MySQL | 数据库 |
| Maven | 构建工具 |
| Lombok | 简化代码 |
| AOP | 统一日志、权限控制等切面处理 |

## 📁 项目结构简要说明

```bash
sky-take-out/
├── common/        # 公共工具类与配置
├── service/       # 核心业务逻辑层
├── controller/    # 接口控制层
├── mapper/        # 数据访问层
├── pojo/vo/dto/   # 实体类与数据传输对象
└── resources/     # 配置文件与资源
