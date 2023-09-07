package com.swx.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

/**
 * 授权服务配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final AuthorizationServerTokenServices authorizationServerTokenServicesCustom;

    public AuthorizationServer(AuthenticationManager authenticationManager, AuthorizationServerTokenServices authorizationServerTokenServicesCustom) {
        this.authenticationManager = authenticationManager;
        this.authorizationServerTokenServicesCustom = authorizationServerTokenServicesCustom;
    }

    /**
     * 客户端详情服务
     *
     * @param clients 客户端
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("XcWebApp")// 客户端密钥
                .secret(new BCryptPasswordEncoder().encode("XcWebApp"))// 客户端密匙
                .resourceIds("learning-online") // 资源列表
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token") // 该client允许的授权类型
                .scopes("all") // 允许的授权范围
                .autoApprove(false) // false跳转到授权页面
                .redirectUris("http://www.51xuecheng.cn"); // 客户端接收授权码的重定向地址
    }

    /**
     * 令牌端点的访问配置
     *
     * @param endpoints 令牌端点
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager) // 认证管理器
                .tokenServices(authorizationServerTokenServicesCustom) // 令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 令牌端点的安全配置
     *
     * @param security 令牌端点的安全
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()") // oauth/token_key是公开
                .checkTokenAccess("permitAll()") // oauth/check_token公开
                .allowFormAuthenticationForClients() // 表单认证（申请令牌）
        ;
    }
}
