package com.dalin.security

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='authority')
@ToString(includes='authority', includeNames=true, includePackage=false)
class Role implements Serializable {

	private static final long serialVersionUID = 1

	static final String ROLE_ADMIN = 'ROLE_ADMIN'
	static final String ROLE_USER = 'ROLE_USER'

	String id
	String authority

	Role(String authority) {
		this()
		this.authority = authority
	}

	static mapWith = 'mongo'

	static constraints = {
		authority nullable: false, blank: false, unique: true
	}

	static mapping = {
		cache true
	}
}
