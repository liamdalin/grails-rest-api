package com.dalin

class Book {

    Date dateCreated
    Date lastUpdated

    String id
    String title
    String introduction
    Author author

    static mapWith = 'mongo'

    static belongsTo = [author: Author]

    static constraints = {
        title nullable: false, blank: false
        author nullable: true
        introduction nullable: true
    }

    static mapping = {
        title index: true
    }
}
