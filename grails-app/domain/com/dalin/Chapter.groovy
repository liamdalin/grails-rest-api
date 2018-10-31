package com.dalin

class Chapter {

    Date dateCreated
    Date lastUpdated

    String id
    String title

    static mapWith = 'mongo'

    static belongsTo = [book: Book]

    static constraints = {
        title nullable: false, blank: false, unique: true
        book nullable: true
    }

    static mapping = {
        title index: true
    }
}
