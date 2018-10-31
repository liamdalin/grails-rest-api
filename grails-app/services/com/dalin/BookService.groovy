package com.dalin

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import grails.gorm.transactions.Transactional

@Transactional
class BookService {

    int intValue
    String stringValue
    Object service
    def bookIdsFromAuthor = []

    def getStringValue() {
        return stringValue
    }

    def getByAggregation() {
        def queryPipeLine = new BasicDBList()


        def matchAuthor = new BasicDBObject('author', '5b3665783d30818b813b0e2f')
        def matchTitle = new BasicDBObject('title', 'bookTitle8')
        def matchAndList = new BasicDBList()
        matchAndList << matchAuthor
        matchAndList << matchTitle

        def matchCondition = new BasicDBObject('$match', new BasicDBObject('$and', matchAndList))

        queryPipeLine.add(matchCondition)

        def book = Book.collection.aggregate(queryPipeLine).first()
        return book
    }

    def save(Map params) {
//        def book = new Book(params)
//        book.validate()
//        if (book.hasErrors()){
//            log.info("save book error: ${book.errors}")
//            throw IllegalArgumentException(book.errors.toString())
//        }

        String bookId = params.id
        def book
        if (bookIdsFromAuthor.contains(bookId)) {
            book = Book.findById(bookId)
            book.introduction = bookId
            book.save(failOnError: true, flush: true)
            log.debug "save introduction into book of ${book.id}"
            synchronized(this) {
                log.debug 'notifyAll!!!!!!'
                notifyAll()
            }
        }


        return book
    }
}
