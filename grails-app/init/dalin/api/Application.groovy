package dalin.api

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.boot.actuate.health.DataSourceHealthIndicator
import org.springframework.boot.actuate.health.DiskSpaceHealthIndicatorProperties

class Application extends GrailsAutoConfiguration {

    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }

    @Override
    Closure doWithSpring() {
        { ->
            // ** spring boot actuator health check indicators ** //
//            databaseHealthCheck(DataSourceHealthIndicator, dataSource)

            diskSpaceHealthIndicatorProperties(DiskSpaceHealthIndicatorProperties) {
                threshold = 250 * 1024 * 1024
            }
        }
    }
}