package com.dalin

class Country {

    Date dateCreated
    Date lastUpdated

    String id
    String code
    String name

    static mapWith = 'mongo'

    static constraints = {
        code nullable: false, blank: false, unique: true
        name nullable: false, blank: false, unique: true
    }

    static mapping = {
        code index: true
        name index: true
    }
}
