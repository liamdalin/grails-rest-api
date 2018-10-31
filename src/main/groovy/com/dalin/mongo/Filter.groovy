
package com.dalin.mongo

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import groovy.transform.ToString


/**
 * Adds a $filter to query pipeline
 */
@ToString(includeFields = true, excludes = ['builder', 'metaClass', 'parent'], includePackage = false)
class Filter implements MongoBuilder {
    private DBObject dbObject
    private QueryPipeBuilder builder
    private MongoBuilder parent

    Filter(QueryPipeBuilder builder, String field, MongoBuilder parent, String asName = "${field}_") {
        dbObject = new BasicDBObject('$filter', new BasicDBObject([input: "\$$field", as: "$asName"]))
        this.builder = builder
        this.parent = parent
    }

    Condition cond() {
        def condition = new Condition(builder,parent)
        dbObject.$filter.cond = condition
        return condition
    }


    @Override
    def build() {
        if (!dbObject.$filter.cond) {
            throw new IllegalStateException('Filter requires an array to operate on with a condition')
        }

        if (dbObject.$filter.cond instanceof MongoBuilder) {
            def cond = dbObject.$filter.remove('cond')
            dbObject.$filter << cond.build()
        }

        dbObject
    }

}
