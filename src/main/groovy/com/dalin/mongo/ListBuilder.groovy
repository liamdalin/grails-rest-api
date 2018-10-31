
package com.dalin.mongo

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject


/**
 * Abstraction of BasicDBList used in mongo aggregations
 */
abstract class ListBuilder implements MongoBuilder{
    protected BasicDBList list
    protected QueryPipeBuilder builder

    /**
     * Creates the {@see BasicDBList} and associates the QueryPipeBuilder
     * @param builder
     */
    ListBuilder(QueryPipeBuilder builder) {
        this.builder = builder
        this.list = new BasicDBList()
    }

    /**
     * Add a field value to a ListBuilder type
     * @param field to match
     * @param value to match
     * @return extends ListBuilder
     */
    ListBuilder addOption(String field, String value) {
        list << new BasicDBObject(field,value)
        return this
    }

    /**
     * Adds field value match combinations to a ListBuilder type
     * @param options of field/value combinations
     * @return extends ListBuilder
     */
    ListBuilder addOptions(Map<String, Object> options) {
        options.each {k,v ->
            list << new BasicDBObject(k,v)
        }
        return this
    }

    /**
     * Returns focus of the builder to the {@see QueryPipeBuilder}
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder end() {
        return builder
    }
}
