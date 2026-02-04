# 智能个人知识库管理系统

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.6-brightgreen" alt="Spring Boot Version">
  <img src="https://img.shields.io/badge/Java-17-orange" alt="Java Version">
  <img src="https://img.shields.io/badge/License-MIT-blue" alt="License">
  <img src="https://img.shields.io/badge/Build-Maven-success" alt="Build Tool">
</p>

## 📋 项目简介

智能个人知识库管理系统是一个基于Spring Boot 3开发的现代化知识管理平台后端服务。该系统提供了完整的知识文档管理、分类管理、用户权限控制等功能，支持RESTful API接口，具备完善的API文档和安全认证机制。

## 🏗️ 技术架构

### 核心技术栈
- **Spring Boot 3.2.6** - 主框架
- **Java 17** - 开发语言
- **MyBatis-Plus 3.5.7** - ORM框架
- **MySQL** - 关系型数据库
- **Redis** - 缓存和会话存储
- **Spring Security + JWT** - 安全认证
- **Knife4j 4.3.0** - API文档(swagger增强)
- **Lombok** - 代码简化工具
- **Hutool 5.8.25** - 工具类库
- **FastJSON2 2.0.48** - JSON处理

### 架构特点
- 📦 分层架构设计（Controller-Service-Mapper）
- 🔐 基于JWT的无状态认证
- 🔄 Redis缓存提升性能
- 📝 完整的异常处理机制
- 🛠️ 统一响应格式封装
- 📊 详细的API文档支持

## 📁 项目结构

```
src/main/java/com/bujian/aipersnonknowledge/
├── config/                 # 配置类
│   ├── CorsConfig.java    # 跨域配置
│   ├── SecurityConfig.java # 安全配置
│   ├── SwaggerConfig.java # API文档配置
│   └── ...
├── controller/            # 控制层
│   ├── UserController.java
│   ├── DocumentController.java
│   ├── CategoryController.java
│   └── KnowledgeController.java
├── service/              # 业务逻辑层
│   ├── impl/            # 实现类
│   └── *.java          # 接口定义
├── mapper/              # 数据访问层
├── entity/              # 实体类
├── dto/                 # 数据传输对象
├── vo/                  # 视图对象
├── util/                # 工具类
└── exception/           # 异常处理

src/main/resources/
├── mapper/             # MyBatis XML映射文件
├── application.yml     # 主配置文件
├── application-dev.yml # 开发环境配置
└── application-prod.yml # 生产环境配置
```

## 🚀 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 1. 克隆项目
```bash
git clone https://github.com/nanying77/intelligent-knowledge-base-api.git
cd intelligent-knowledge-base-api
```

### 2. 数据库配置
创建数据库并执行初始化脚本：
```sql
CREATE DATABASE knowledge_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改 `application-dev.yml` 中的数据库连接配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/knowledge_platform?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 3. Redis配置
确保Redis服务正常运行，默认配置：
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
```

### 4. 编译运行
```bash
# 清理并编译
mvn clean compile

# 运行应用
mvn spring-boot:run

# 或者打包运行
mvn package
java -jar target/knowledge-platform-backend-1.0.0.jar
```

### 5. 访问验证
- 应用地址：http://localhost:8080/api/
- API文档：http://localhost:8080/api/doc.html
- 健康检查：http://localhost:8080/api/actuator/health

## 🔧 配置说明

### 多环境配置
项目支持多环境配置：
- `dev` - 开发环境（默认）
- `prod` - 生产环境

切换环境：
```bash
# 开发环境
mvn spring-boot:run -Pdev

# 生产环境
mvn spring-boot:run -Pprod
```

### 主要配置项
```yaml
# 应用配置
server:
  port: 8080

# JWT配置
jwt:
  secret: your-secret-key
  expiration: 86400000  # 24小时

# MyBatis-Plus配置
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

## 📊 API文档

项目集成Knife4j提供在线API文档，启动后访问：
```
http://localhost:8080/api/doc.html
```

主要API模块：
- 🔐 用户认证接口
- 📚 文档管理接口  
- 🏷️ 分类管理接口
- 🧠 知识库接口

## 🔒 安全机制

### 认证流程
1. 用户登录获取JWT Token
2. 请求时在Header中携带Token
3. 服务端验证Token有效性
4. 返回相应数据或401未授权

### 权限控制
- 基于角色的访问控制(RBAC)
- 方法级别的安全注解
- URL级别的访问控制

## 🛠️ 开发规范

### 代码规范
- 使用Lombok减少样板代码
- 统一异常处理机制
- 规范化的日志记录
- 完善的参数校验

### 提交规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 重构
test: 测试相关
chore: 构建过程或辅助工具的变动
```

## 📈 性能优化

### 已实现优化
- ✅ Redis缓存热点数据
- ✅ 数据库连接池优化
- ✅ MyBatis-Plus分页查询
- ✅ 异步处理非关键业务
- ✅ gzip压缩响应数据

### 待优化方向
- [ ] 数据库读写分离
- [ ] Elasticsearch全文检索
- [ ] 消息队列异步处理
- [ ] 微服务架构拆分

## 🐛 常见问题

### 1. 启动失败
检查数据库连接配置是否正确，确保MySQL和Redis服务正常运行。

### 2. API文档无法访问
确认Knife4j依赖是否正确引入，检查Swagger配置类。

### 3. 跨域问题
检查CorsConfig配置，确保允许的域名和请求头正确设置。

## 🤝 贡献指南

欢迎提交Issue和Pull Request！

1. Fork本项目
2. 创建feature分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

## 📄 许可证

本项目采用MIT许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👨‍💻 作者

**南璎:(Nanying)** - [GitHub](https://github.com/nanying77)

## 🙏 致谢

- [Spring Boot](https://spring.io/projects/spring-boot)
- [MyBatis-Plus](https://baomidou.com/)
- [Knife4j](https://doc.xiaominfo.com/)
- [Hutool](https://hutool.cn/)

---
> 💡 **提示**: 如需更多帮助，请查看[Wiki](wiki)或提交[Issue](issues)