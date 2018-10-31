package com.dalin

import grails.events.EventPublisher
import grails.events.annotation.Publisher
import grails.gorm.transactions.Transactional
import org.hibernate.SessionFactory

import static grails.async.Promises.task

@Transactional
class AuthorService implements EventPublisher {

    def bookService

//    @Publisher('testSession')
//    def testSessionPublisher(String name) {
//        return name
//    }

    SessionFactory sessionFactory

    def get(Map params) {
        String name = params.name
//        def author = Author.findByName(name, [fetch: [books: 'select']])

        log.debug "sessionFactory.statistics ${sessionFactory.statistics.toString()}"
        def session = sessionFactory.getCurrentSession()
        log.debug 'notify testSession'

        Book book = new Book(title: 'bb2').save(failOnError: true, flush: true)
        log.debug "new book is created!!! The id is ${book.id}"
        bookService.bookIdsFromAuthor << book.id

        synchronized(bookService) {
            while (!book.introduction) {
                log.debug "AuthorService start to wait!!, book id is ${book.id}"
                bookService.wait(1800000)
                log.debug 'AuthorService awaked!!'

                Book.withNewSession {
                    book = Book.findById(book.id)
                    if (book.introduction) {
                        log.debug "book ${book.id} introduction is ${book.introduction}"
                    }
                }

            }
        }

        if (!book) {
            return null
        }

        def author = Author.get('5b2d32f93d308174c56c5860')

//        notify('testSession', author)

        log.debug "AuthorService session ${session.toString()}"

//        def author = Author.findByName(name)
//
//        List nameList = []
//        def books = author?.books
//        books.each {nameList << it.title}
//        println nameList
//
////        println bookService.intValue
////        println bookService.getStringValue()
////        println bookService.service.intValue
//
//        author.name = author.name + '1'
//        author.save()
//        throw new Exception();
//
//        task {asyncMethod(name)}

        return author
    }

    def save(Map params) {

        def author = new Author()
        author.name = params.name

        params.books?.each {
            author.addToBooks(it)
        }

//        author.book = new Book(params.book)

        author.validate()
        if (author.hasErrors()){
            log.info("save book error: ${author.errors}")
            throw IllegalArgumentException(author.errors.toString())
        }

        author.save()

        /*if (params.bookTitle && params.name) {
            author.name = params.name
            author.books
            author = new Author(name: params.name, book: new Book(title: params.bookTitle))

            author.validate()
            if (author.hasErrors()){
                log.error("save book error: ${author.errors}")
                throw IllegalArgumentException(author.errors.toString())
            }

            author.save(failOnError: true)
        }*/

        return author
    }

    def delete (Map params) {
        def author = Author.findByName(params.name)
        author.delete()
    }

    def asyncMethod(String name) {
        List nameList = []
        Author.withSession {
            def author = Author.findByName(name)
            def books = author.books
            books.each {nameList << it.title}

        }
        println nameList
    }
}
