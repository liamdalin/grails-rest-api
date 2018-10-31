import grails.util.Environment
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

import static grails.util.Environment.TEST

def logFolderPath = './logs'
def logFileName = 'dalin-api'
def appenderList = ['STDOUT', 'ROLLING']

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

def logPattern = '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
        '%clr(%5p) ' + // Log level
        '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
        '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
        '%m%n%wex' // Message


// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern = logPattern
    }
}

appender('ROLLING', RollingFileAppender) {
    file = "${logFolderPath}/${logFileName}.log"
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern = logPattern // "%d %level %thread %mdc %logger - %m%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        FileNamePattern = "${logFolderPath}/${logFileName}-%d{yyyy-MM-dd}.zip"
        maxHistory = 30   // keep 30 days (1 month) log history
        cleanHistoryOnStart = true
    }
}

if (Environment.isDevelopmentMode()) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${logFolderPath}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = logPattern
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
}
root(ERROR, ['STDOUT'])

if (Environment.isDevelopmentMode() || Environment.currentEnvironment == TEST) {
//    logger("org.springframework.security", DEBUG, appenderList, false)
//    logger("grails.plugin.springsecurity", DEBUG, appenderList, false)
    logger("org.pac4j", DEBUG, appenderList, false)
    logger('com.dalin', DEBUG, appenderList, false)
    logger('dalin.api', DEBUG, appenderList, false)

    // hibernate SQL and bind variable tracking
    logger('org.hibernate.SQL', DEBUG, appenderList, false)    // show sql statements
    logger('org.hibernate.type.descriptor.sql.BasicBinder', TRACE, appenderList, false)
} else {
    logger('com.dalin', INFO, appenderList, false)
    logger('dalin.api', INFO, appenderList, false)

    // hibernate SQL and bind variable tracking
    logger('org.hibernate.SQL', INFO, appenderList, false)    // show sql statements
    logger('org.hibernate.type.descriptor.sql.BasicBinder', INFO, appenderList, false)
}
