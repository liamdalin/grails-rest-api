package com.dalin

import grails.artefact.controller.RestResponder
import grails.artefact.controller.support.ResponseRenderer
import grails.converters.JSON
import grails.validation.ValidationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus

import static org.springframework.http.HttpStatus.*

trait ControllerSupport implements ResponseRenderer, RestResponder{

    static Logger log = LoggerFactory.getLogger(this.class)
    final String MIME_TYPE_JSON = 'application/json'

    def handleException(Exception ex) {
        log.error('Handled exception', ex)
        if (ex instanceof IllegalArgumentException) {
            renderResponse(BAD_REQUEST, [message: ex.message])
        } else if (ex instanceof ValidationException) {
            renderResponse(BAD_REQUEST, [message: ex.message])
        } else {
            log.info('Returning 500', ex)
            renderResponse(INTERNAL_SERVER_ERROR, [exception: ex.class.canonicalName, message: ex.message])
        }
    }

    /**
     * Render http response
     * @param httpStatus {@link org.springframework.http.HttpStatus} enum object
     * @param message body message data, MUST be compatible with grails markup converters.
     */
    def renderResponse(HttpStatus httpStatus, message) {
        if (message instanceof String || message instanceof GString) {
            message = [message: message.toString()]
        }
        this.render status: httpStatus, contentType: MIME_TYPE_JSON, text: (message as JSON)
    }
}
