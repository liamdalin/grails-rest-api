package com.dalin


import grails.rest.*
import grails.converters.*

import static org.springframework.http.HttpStatus.*

class UserController implements ControllerSupport {
	static responseFormats = ['json', 'xml']

    def userService

    def index() { }

    def save() {
        def requestParamsMap = request.JSON

        def user = userService.createUser(requestParamsMap)

        respond user, [status: CREATED]
    }
}
