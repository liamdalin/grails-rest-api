package com.dalin


import grails.rest.*
import grails.converters.*

import static org.springframework.http.HttpStatus.*

class BookController implements ControllerSupport {
	static responseFormats = ['json', 'xml']

    def bookService

    def index() {
        respond(bookService.getByAggregation())
    }

    def save() {
        def params = request.JSON

        respond bookService.save(params), [status: CREATED]
    }
}
