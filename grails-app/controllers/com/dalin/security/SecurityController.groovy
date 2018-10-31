package com.dalin.security

import com.dalin.ControllerSupport
import grails.rest.*
import grails.converters.*
import org.springframework.http.HttpStatus

class SecurityController implements ControllerSupport {
	static responseFormats = ['json', 'xml']

    UserSecurityrService userSecurityService

    def seclogin() {
        def requestParamsMap = request.JSON
        String username = requestParamsMap.username
        String password = requestParamsMap.password

        if ((!username) || (!password)) {
            render status: HttpStatus.UNAUTHORIZED
            return
        }

        User user = User.findByUsernameAndPassword(username, password)
        if (!user)
        {
            new User(username, password).save(flush: true)
        }
        String tokenValue
        if (user) {
            tokenValue = userSecurityService.getTokenForUser(username)
        }

        render status: HttpStatus.OK,
                contentType: 'application/json',
                text: ([
                        status: 'ok',
                        message: [
                                username: user.username,
                                tokenValue: tokenValue
                        ]
                ] as JSON)

    }

    def index() { }
}
