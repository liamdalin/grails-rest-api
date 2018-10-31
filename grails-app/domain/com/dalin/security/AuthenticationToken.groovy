package com.dalin.security

class AuthenticationToken {

    String id
    String tokenValue
    String username

    static mapWith = 'mongo'

    static mapping = {
        version false
    }


    static constraints = {
    }
}
