
package com.dalin.mongo

import com.mongodb.BasicDBList

/**
 * Abstraction of BasicDBList used in mongo aggregations
 */
abstract class CondListBuilder implements MongoBuilder {

    protected BasicDBList list
    protected QueryPipeBuilder builder
    protected MongoBuilder parent

    /**
     * Creates the {@see BasicDBList} and associates the QueryPipeBuilder
     * @param builder
     */
    CondListBuilder(QueryPipeBuilder builder, MongoBuilder parent) {
        this.builder = builder
        this.parent = parent
        this.list = new BasicDBList()
    }


    /**
     * Returns focus of the builder to the {@see QueryPipeBuilder}
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder end() {
        return builder
    }

    /**
     * Returns focus of the builder to the parent {@see MongoBuilder}
     * @return MongoBuilder
     */
    MongoBuilder close() {
        return parent
    }
}
