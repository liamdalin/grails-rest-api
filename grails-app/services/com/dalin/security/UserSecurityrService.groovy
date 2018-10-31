package com.dalin.security

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.generation.TokenGenerator

class UserSecurityrService {

    SpringSecurityService springSecurityService
    TokenGenerator tokenGenerator

    def getTokenForUser(String username, refreshToken = false) {
        AuthenticationToken token = AuthenticationToken.findByUsername(username)
        if (token && (!refreshToken)) {
            return token.tokenValue
        }

        AccessToken accessToken = tokenGenerator.generateAccessToken(springSecurityService.principal)
        String tokenValue = accessToken.accessToken

        AuthenticationToken.withTransaction {
            if (token) {
                token.refresh()
                token.tokenValue = tokenValue
                token.save(failOnError: true)
            } else {
                token = new AuthenticationToken(username: username, tokenValue: tokenValue).save(flush: true, failOnError: true)
            }
        }
        return tokenValue
    }
}
