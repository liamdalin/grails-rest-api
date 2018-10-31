package com.dalin

import grails.events.annotation.Subscriber
import grails.gorm.transactions.Transactional
import org.hibernate.SessionFactory

class CountryService {

    SessionFactory sessionFactory

    @Subscriber('testSession')
    @Transactional
    def testSession(Author author) {

        author.name = 'kk5'

        log.debug "sessionFactory.statistics ${sessionFactory.statistics.toString()}"

        // Get the current Hibernate session.
//        def session = sessionFactory.currentSession
//        log.debug "CountryService session ${session.toString()}"
    }
}
