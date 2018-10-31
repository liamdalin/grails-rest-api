package com.dalin


import grails.rest.*
import grails.converters.*
import io.swagger.annotations.*

import static org.springframework.http.HttpStatus.*


@Api(value = "/author", tags = ["author"], description = "Author Api's")
class AuthorController implements ControllerSupport {
	static responseFormats = ['json', 'xml']

    def authorService

    def index() {
        Author author = authorService.get(params)
        respond(author)
    }

    @ApiOperation(
            value = "Save author",
            nickname = "/",
            produces = "application/json",
            consumes = "application/json",
            httpMethod = "POST",
            response = com.dalin.Author
    )
    @ApiResponses([
            @ApiResponse(code = 401,
                    message = "Unauthorized. Invalid Bearer Token"),

            @ApiResponse(code = 422,
                    message = "Unable to save project, please see errors"),

            @ApiResponse(code = 200,
                    message = "Success")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(name = "Authorization",
                    paramType = "header",
                    required = true,
                    defaultValue = "Bearer ",
                    value = "Bearer Token",
                    dataType = "string"),

            @ApiImplicitParam(name = 'body',
                    value = '''
                    {
                        "name": "dalin",
                        "books": [
                            {
                                title: "bookTitle1"
                            },
                            {
                                title: "bookTitle2"
                            }
                        ]
                    }
                    ''',
                    paramType = 'body',
                    required = true,
                    dataTypeClass = com.dalin.Author
            ),
    ])
    def save() {
        def params = request.JSON
        respond authorService.save(params), [status: CREATED]
    }

    def delete() {
        def params = request.JSON
        authorService.delete(params)
        respond( [status: OK])
    }
}
