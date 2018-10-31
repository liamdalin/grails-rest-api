package com.dalin.security

import com.dalin.Book
import com.dalin.helper.UserHelper
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    SpringSecurityService springSecurityService

    String id
    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Date dateCreated
    Date lastUpdated

    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    User(String username, String password) {
        this()
        this.username = username
        this.password = password
    }

    static mapWith = 'mongo'

    static transients = ['springSecurityService']

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
    }

    static mapping = {
	    password column: '`password`'
        username index: true
    }

    /*static hasMany = [books: Book]*/

    /*def beforeInsert() {
        password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }*/

}
