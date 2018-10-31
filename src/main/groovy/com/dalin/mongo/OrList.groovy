
package com.dalin.mongo

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import groovy.transform.ToString

/**
 * Represents an $or in mongo aggregate.
 * Exposes methods that can be used to build an $or
 *
 * @see {ListBuilder}
 */
@ToString(includeFields = true, excludes = ['builder', 'metaClass', 'parent'], includePackage = false)
class OrList extends ListBuilder {
    private DBObject dbObject

    OrList(QueryPipeBuilder builder) {
        super(builder)
        dbObject = new BasicDBObject('$or',list)
    }

    /**
     * Adds a $gt on field with a value
     * <pre>
     * {@code
     *   // ...&#123;$or:[&#123;field:&#123;$gt:value&#125;&#125;]&#125;
     *   ... .or().gt(field,value)
     * }
     * </pre>
     * @param field to perform gt on
     * @param value to be gt
     * @return OrList
     */
    OrList gt(String field, value) {
        list << new BasicDBObject(field, new BasicDBObject('$gt', value))
        return this
    }

    /**
     * Adds an equal on a field and value to an OrList
     * <pre>
     * {@code
     *   // ...&#123;$or:[&#123;field:value&#125;]&#125;
     *   ... .or().equal(field,value)
     * }
     * </pre>
     * @param field to equal
     * @param value to match
     * @return OrList
     */
    OrList equal(String field, value) {
        list << new BasicDBObject(field,new BasicDBObject('$eq',value))
        return this
    }

    /**
     * Adds equal check to field and all values to the OrList
     * <pre>
     * {@code
     *   // ...&#123;$or:[&#123;field:value&#125;,&#123;field2:value2&#125;]&#125;
     *   ... .or().inList(field,[value,value2]
     * }
     * </pre>
     * @param field to equal
     * @param values to equal
     * @return OrList
     */
    OrList inList(String field, values) {
        def orList = []
        values.each { orList << new BasicDBObject(field, it) }
        list.addAll(orList)
        return this
    }

    /**
     * Adds an $and to the $or list; exposes the AndList
     * <pre>
     * {@code
     *   // ...&#123;$or:[&#123;$and:[]&#125;]&#125;
     *   ... .or().and()
     * }
     * </pre>
     * @return AndList
     */
    AndList and() {
        def andList = new AndList(builder)
        list.add(andList)
        return andList
    }

    /**
     * Returns the built DBObject of the $or list
     * @return DBObject
     */
    DBObject build() {
        def cleanedList = new BasicDBList()
        list.each {
            if (it instanceof MongoBuilder ) {
                cleanedList << it.build()
            } else {
                cleanedList << it
            }
        }
        dbObject.$or = cleanedList
        return dbObject
    }
}
