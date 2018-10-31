package com.dalin

class Author {

    Date dateCreated
    Date lastUpdated

    String id
    String name

    static hasMany = [books: Book]

    static mapWith = 'mongo'

    static constraints = {
        name nullable: false, blank: false, unique: true
        books nullable: true
    }

    static mapping = {
        name index: true
        books cascade: "all-delete-orphan"
        books lazy: false
    }
}
