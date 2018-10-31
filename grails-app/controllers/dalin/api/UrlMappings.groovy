package dalin.api

class UrlMappings {

    static mappings = {
        delete "/$controller/delete"(action:"delete")
        get "/$controller"(action:"index")
        get "/$controller/$id(.$format)?"(action:"show")
        post "/$controller"(action:"save")
        post "/$controller/save"(action:"save")
        put "/$controller/$id(.$format)?"(action:"update")
        patch "/$controller/$id(.$format)?"(action:"patch")

        post "/$controller/seclogin"(action:"seclogin")

        //swagger default url mapping
        "/apidoc/$action?/$id?"(controller: "apiDoc", action: "getDocuments")

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
