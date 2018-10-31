package com.dalin.helper

class UserHelper {

    static def springSecurityService

    static def encodePassword(String password) {
        return springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }
}
