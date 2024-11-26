![video_spider](https://socialify.git.ci/chensoul/spring-security-5-samples/image?forks=1&issues=1&language=1&name=1&owner=1&stargazers=1&theme=Light)

# <font size="6p">spring-security-5-samples</font> <font size="5p">  | [English](README.md)</font>

<p align="left">
 <a href="https://github.com/chensoul/spring-security-5-samples/workflows/maven.yml"><img src="https://github.com/chensoul/spring-security-5-samples/actions/workflows/maven.yml/badge.svg"></a>
 <a href="/pom.xml"><img src="https://img.shields.io/badge/Spring%20Boot%20Version-2.7.18-blue"></a>
 <a href="/pom.xml"><img src="https://img.shields.io/badge/Java%20Version-11-blue"></a>
	<a href="https://github.com/chensoul/spring-security-5-samples/network/members"><img src="https://img.shields.io/github/forks/chensoul/spring-security-5-samples?style=flat-square&logo=GitHub"></a>
	<a href="https://github.com/chensoul/spring-security-5-samples/watchers"><img src="https://img.shields.io/github/watchers/chensoul/spring-security-5-samples?style=flat-square&logo=GitHub"></a>
	<a href="https://github.com/chensoul/spring-security-5-samples/issues"><img src="https://img.shields.io/github/issues/chensoul/spring-security-5-samples.svg?style=flat-square&logo=GitHub"></a>
	<a href="https://github.com/chensoul/spring-security-5-samples/blob/main/LICENSE"><img src="https://img.shields.io/github/license/chensoul/spring-security-5-samples.svg?style=flat-square"></a>
</p>

基于 Maven 构建的 Spring Security 5 示例。

## 说明

1. PasswordEncoder 通常和 UserDetailsService 一起配置，配置 UserDetailsService 时，需要指定 PasswordEncoder。
2. 配置 Spring Security 有两种方式，一是继承 WebSecurityConfigurerAdapter 类（在 Spring Security 5.7.0-M2
   之后已删除，参考[文档](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)
   ），二是使用
   @Bean 注解装配 Bean。不建议混合使用两种方式，推荐使用第二种方式。
3. CSRF
4. OAuth2 Token 有两种格式：Opaque，不透明。不存储数据的令牌。要实现授权，资源服务器通常需要调用授权服务器，提供不透明的令牌，并获取详细信息。No
   Opaque，非不透明，存储数据的令牌，使后端能够立即实现授权。JWT 是最常用的非不透明令牌实现。
5. OAuth2 漏洞：
    - CSRF
    - Stealing client credentials
    - 重放令牌
    - 令牌劫持，参考[文档](https://blog.intothesymmetry.com/2015/06/on-oauth-token-hijacks-for-fun-and.html)
6. Spring Security 测试
    - @WithMockUser
    - @WithUserDetails
    - 自定义注解 @WithCustomUser：使用 @WithSecurityContext

## TODO

- MFA

## 其他

## 生成 HTTPS 证书

生成字签名证书：

```bash
openssl req -newkey rsa:2048 -x509 -keyout key.pem -out cert.pem -days 365
# 输入密码为123456

openssl pkcs12 -export -in cert.pem -inkey key.pem -out certificate.p12
-name "certificate"
```

修改 spring boot 配置

```properties
server.ssl.key-store-type=PKCS12
server.ssl.key-store=classpath:certificate.p12
server.ssl.key-store-password=12345
```

### 生成 Jwt 证书

JRE 提供了一个简单的证书管理工具——keytool。它位于您的JRE_HOME\bin目录下。以下代码中的命令生成一个自签名证书并将其放入
PKCS12 KeyStore 中。除了 KeyStore 的类型之外，您还需要设置其有效期、别名以及文件名。在开始生成过程之前，keytool会要求您输入密码和一些其他信息，如下所示：

```bash
keytool -genkeypair -alias mytest -keyalg RSA -keysize 2048 \
    -storetype PKCS12 -keystore mytest.p12 -storepass mypass \
    -dname "CN=WebServer,OU=Unit,O=Organization,L=City,S=State,C=CN" -validity 3650
```

导出公钥文件：

```bash
keytool -list -rfc --keystore mytest.p12 -storepass mypass | \
    openssl x509 -inform pem -pubkey > public.key
```

导出私钥文件：

```bash
keytool -importkeystore -srckeystore mytest.p12 -srcstorepass mypass \
    -destkeystore private.p12 -deststoretype PKCS12 \
    -deststorepass mypass -destkeypass mytest

#输入 storepass 密码 
openssl pkcs12 -in private.p12 -nodes -nocerts -out private.key
```

## 参考

- 《Spring Security in Action, 2nd
  Edition》 [code](https://manning-content.s3.amazonaws.com/download/9/cdd7a3a-1962-44d0-b637-59a805d0e18c/spring_security_in_action_source_code.zip)
- https://github.com/spring-attic/spring-security-oauth2-boot/tree/main/samples
- https://github.com/chensoul/spring-security-oauth2-boot-examples
- https://github.com/eugenp/learn-spring-security/tree/lsso-module5/logout-with-oauth-and-oidc-end
- https://github.com/OpenDocCN/freelearn-javaweb-zh/blob/f12853ff9d621f45414c5f67af4c8d1d04484f1a/docs/ms-sprcld/ms-sprcld_12.md
- https://coursehunters.online/t/learn-spring-security-oauth-the-master-class-part-1
- https://coursehunters.online/t/learn-spring-security-oauth-the-master-class-part-2
- https://coursehunters.online/t/learn-spring-security-oauth-the-master-class-part-3
- https://coursehunters.online/t/learn-spring-security-oauth-the-master-class-part-4
- https://coursehunters.online/t/learn-spring-security-oauth-the-certification-class-part-1
- https://coursehunters.online/t/learn-spring-security-oauth-the-certification-class-part-2
- https://coursehunters.online/t/learn-spring-security-oauth-the-certification-class-part-3
- https://coursehunters.online/t/learn-spring-security-oauth-the-certification-class-part-4
- https://github.com/spring-tips/auth0
- https://github.com/spring-tips/webauthn-and-passkeys
- https://github.com/spring-tips/spring-security-one-time-token
- https://github.com/atquil/spring-security/
- https://github.com/Kehrlann/spring-security-architecture-workshop
- https://github.com/becoderpavy/spring_boot_tutorial.git
- https://github.com/docodebyself/JWT-Authentication-and-Authorization-in-Spring-Security---Spring-Boot-REST-API
- https://dimitri.codes/spring-security-compromisedpasswordchecker/

## 贡献

非常欢迎[提出请求](https://help.github.com/articles/creating-a-pull-request) 。

## 许可

learn-spring-authorization-server 是在 [Apache 2.0 许可](https://www.apache.org/licenses/LICENSE-2.0.html)
下发布的开源软件 。

 



