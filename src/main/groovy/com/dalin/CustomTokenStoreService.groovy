package com.dalin

import com.dalin.security.AuthenticationToken
import com.dalin.security.Role
import com.dalin.security.User
import com.dalin.security.UserRole
import grails.plugin.springsecurity.rest.token.storage.TokenNotFoundException
import grails.plugin.springsecurity.rest.token.storage.TokenStorageService
import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomTokenStoreService implements TokenStorageService{

    @Override
    UserDetails loadUserByToken(String tokenValue) throws TokenNotFoundException {
        UserDetails userDetails
        if (tokenValue) {
            Collection<GrantedAuthority> authorities = new ArrayList<>()

            def authenticationToken = AuthenticationToken.findByTokenValue(tokenValue)
            if (authenticationToken) {
                def userName = authenticationToken.username
                def user = User.findByUsername(userName)
                UserRole.findByUser(user).each {userRole ->
                    def role = Role.findById(userRole.role.id)
                    authorities << new SimpleGrantedAuthority(role.authority)
                }

                userDetails = new GrailsUser(userName, user.password, user.enabled, !user.accountExpired, true,
                        !user.accountLocked, authorities, tokenValue)
            }
            else {
                def userName = UUID.randomUUID().toString()
                authorities << new SimpleGrantedAuthority('ROLE_USER')
                userDetails = new GrailsUser(userName, 'X', true, true,
                        true, true, authorities, tokenValue)
            }
        }

        return userDetails
    }

    @Override
    void storeToken(String tokenValue, UserDetails principal) {
        User.withTransaction {
            User user = new User()
            user.username = principal.username
            user.enabled = principal.enabled
            user.accountLocked = !principal.accountNonLocked

        }

    }

    @Override
    void removeToken(String tokenValue) throws TokenNotFoundException {

    }
}