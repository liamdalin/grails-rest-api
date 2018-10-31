import io.swagger.models.Scheme

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.dalin.security.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.dalin.security.UserRole'
grails.plugin.springsecurity.authority.className = 'com.dalin.security.Role'

grails.plugin.springsecurity.rest.token.storage.useGorm = true
grails.plugin.springsecurity.rest.token.storage.gorm.tokenDomainClassName = 'com.dalin.security.AuthenticationToken'

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']],
	[pattern: '/api/**',         access: ['permitAll']],
	[pattern: '/**',             access: ['ROLE_USER', 'ROLE_ADMIN']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/',               filters: 'none'],
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/error/**',       filters: 'none'],
	// security endpoint for user login
	[pattern: '/security/**',    filters: 'none'],

	[pattern: '/health/**',      filters: 'none'],
	[pattern: '/info/**',        filters: 'none'],

	//swagger
	[pattern: '/apidoc/**',      filters: 'none'],
	[pattern: '/webjars/swagger-ui/**',      filters: 'none'],

	//Stateless chain
	[
			pattern: '/**',
			filters: 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'
	],

	//Traditional, stateful chain
	[
			pattern: '/stateful/**',
			filters: 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter'
	]

]

swagger {
	info {
		description = "Move your app forward with the Swagger API Documentation"
		version = "ttn-swagger-1.0.0"
		title = "Swagger API"
		termsOfServices = "http://swagger.io/"
		contact {
			name = "Contact Us"
			url = "http://swagger.io"
			email = "contact@gmail.com"
		}
		license {
			name = "licence under http://www.tothenew.com/"
			url = "http://www.tothenew.com/"
		}
	}
	schemes = [Scheme.HTTP]
	consumes = ["application/json"]
}
