package com.pizzamdp.services;

import com.pizzamdp.entities.Provider;
import com.pizzamdp.entities.User;
import com.pizzamdp.repositories.UserRepository;
import com.pizzamdp.security.oidc.CustomOidcUser;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    public CustomOidcUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        String email = oidcUser.getEmail();

        User user = userRepository.findByUsername(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .username(email)
                            .provider(Provider.GOOGLE)
                            .roles(Collections.singletonList("CLIENTE"))
                            .build();
                    return userRepository.save(newUser);
                });

        return new CustomOidcUser(oidcUser, user);
    }
}
