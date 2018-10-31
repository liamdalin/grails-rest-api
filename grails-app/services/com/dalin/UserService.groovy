package com.dalin

import com.dalin.security.User
import grails.gorm.transactions.Transactional


class UserService {

    def passwordEncoder

    def isUserValid(Map params) {
        def user = User.findByUsername(params.username)

        if (passwordEncoder.isPasswordValid(user.password, params.password, null)) {
            log.info "the password of ${params.username} match with it in DB"
            return true
        }

        return false
    }

    @Transactional
    def createUser(Map params) {
        def user = new User(params)

        user.validate()
        if(user.hasErrors()) {
            log.info(user.errors)
            throw IllegalArgumentException(user.errors.toString())
        }

        user.save(falilOnError: true)

        return user
    }
}
