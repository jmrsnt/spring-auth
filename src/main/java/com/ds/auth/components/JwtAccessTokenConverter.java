package com.ds.auth.components;

import com.ds.auth.configurations.KeyPairConfig;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.stereotype.Component;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

@Component
class JwtAccessTokenConverter extends org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter {

    KeyPair keyPair;
    UserAuthenticationConverter userAuthenticationConverter;
    private final JsonParser objectMapper;
    private final RsaSigner signer;
    private final Map<String, String> customHeader;

    public JwtAccessTokenConverter(KeyPair keyPair, UserAuthenticationConverter authenticationConverter) {
        this.keyPair = keyPair;
        this.userAuthenticationConverter = authenticationConverter;
        super.setKeyPair(keyPair);
        this.objectMapper = JsonParserFactory.create();
        this.signer = new RsaSigner((RSAPrivateKey) this.keyPair.getPrivate());
        this.customHeader = new HashMap<>() {{ put("kid", KeyPairConfig.KID); }};
    }

    @Override
    public String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String content;
        Map<String, Object> mapContent;

        try {
            mapContent = new HashMap<>(getAccessTokenConverter().convertAccessToken(accessToken, authentication));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot convert access token to JSON", ex);
        }

        Long nowTimeSecs = System.currentTimeMillis() / 1000;
        Long delta = ((Long) mapContent.get("exp")) - nowTimeSecs;
        Long expTimeSecs = nowTimeSecs + delta;

        mapContent.replace("exp", expTimeSecs);
        mapContent.put("iat", nowTimeSecs);

        content = objectMapper.formatMap(mapContent);

        return JwtHelper.encode(content, signer, customHeader).getEncoded();
    }

    @Override
    public AccessTokenConverter getAccessTokenConverter() {
        DefaultAccessTokenConverter converter = new DefaultAccessTokenConverter();
        converter.setUserTokenConverter(userAuthenticationConverter);
        return converter;
    }

}
