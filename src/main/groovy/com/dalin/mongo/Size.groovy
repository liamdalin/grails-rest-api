package com.dalin.mongo

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import groovy.transform.ToString

@ToString(includeFields = true, excludes = ['builder', 'metaClass', 'parent'])
class Size implements MongoBuilder {

    private DBObject dbObject
    private QueryPipeBuilder builder
    private MongoBuilder parent

    Size(QueryPipeBuilder builder, MongoBuilder parent) {
        this.builder = builder
        this.parent = parent
    }

    /**
     *
     * @param arrayField to filter
     * @param options
     * @return
     */
    Filter filter(String field, String asName = null) {
        def filter = new Filter(builder, field, parent, asName)
        dbObject = new BasicDBObject('$size', filter)
        return filter
    }

    MongoBuilder size(String field) {
        dbObject = new BasicDBObject('$size', "\$$field")
        return parent
    }

    /**
     * Builds the Size object; builds and {@link MongoBuilder} types in the $size
     * @return DBObject
     */
    @Override
    DBObject build() {
        if (!dbObject || !dbObject.$size ) {
            throw new IllegalStateException('Size is not defined')
        }
        dbObject.each { k,v ->
            if ( v instanceof MongoBuilder ) {
                dbObject[k] = v.build()
            }
        }
        return dbObject
    }
}
