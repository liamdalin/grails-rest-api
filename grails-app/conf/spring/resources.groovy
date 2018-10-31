import com.dalin.BookService
import com.dalin.CustomTokenStoreService
import grails.plugin.springsecurity.rest.RestAuthenticationProvider

// Place your Spring DSL code here
beans = {
    bookService(BookService) {
        intValue = 1
        stringValue = 'qwe'
        service = countryService
    }

    customTokenStoreService(CustomTokenStoreService)

    restAuthenticationProvider(RestAuthenticationProvider) {
        tokenStorageService = customTokenStoreService
        useJwt = false
    }
}
