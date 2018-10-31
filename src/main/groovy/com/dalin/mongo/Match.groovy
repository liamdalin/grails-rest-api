
package com.dalin.mongo

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import groovy.transform.ToString

/**
 * Collection of $match aggregate operations
 */
@ToString(includeFields = true, excludes = ['builder', 'metaClass', 'parent'], includePackage = false)
class Match implements MongoBuilder {
    private DBObject dbObject
    private QueryPipeBuilder builder

    Match(QueryPipeBuilder builder) {
        this.builder = builder
    }

    /**
     * Creates a $match of field to value
     * <pre>
     * {@code
     *   // &#123;$match:&#123;field:value&#125;&#125;
     *   builder.match().equal(field,value)
     *}
     * </pre>
     * @param field to match
     * @param value value to match
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder equal(String field, value) {
        dbObject = new BasicDBObject('$match', new BasicDBObject(field, value))
        return builder
    }

    /**
     * Creates a $match on 'null'
     * <pre>
     * {@code
     *   //&#123;$match:&#123;field:&#123;$exists:false&#125;&#125;&#125;
     *   builder.match().doesNotExist(field)
     *}
     * </pre>
     * @param field to not match on
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder doesNotExist(String field) {
        dbObject = new BasicDBObject('$match', new BasicDBObject(field, new BasicDBObject('$exists', false)))
        return builder
    }

    /**
     * Creates a $match on 'null'
     * <pre>
     * {@code
     *   //&#123;$match:&#123;field:&#123;$exists:true&#125;&#125;&#125;
     *   builder.match().exists(field)
     *}
     * </pre>
     * @param field to not match on
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder exists(String field) {
        dbObject = new BasicDBObject('$match', new BasicDBObject(field, new BasicDBObject('$exists', true)))
        return builder
    }

    /**
     * Creates a $match on a field to a list of values using an $in operator
     * <pre>
     * {@code
     *   // &#123;$match:&#123;field:&#123;$in:['a','b']&#125;&#125;&#125;
     *   builder.match().inList(field,['a','b'])
     *}
     * </pre>
     * @param field to match
     * @param values to match
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder inList(String field, values) {
        dbObject = new BasicDBObject('$match', new BasicDBObject(field, new BasicDBObject('$in', values)))
        return builder
    }

    /**
     * Creates a $match to between a range exclusive
     * <pre>
     * {@code
     *   //&#123;$match:&#123;$and:[&#123;field:&#123;$gt:10&#125;&#125;,&#123;field:&#123;$lt:100&#125;&#125;]&#125;&#125;
     *   builder.match().betweenRange(field,new IntegerRange(100,10)
     *}
     * </pre>
     * @param field to match between
     * @param range to match between
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder betweenRange(String field, Range range) {
        DBObject andList = new BasicDBList()
        andList.addAll([new BasicDBObject(field, new BasicDBObject('$gt', range.lower)),
                        new BasicDBObject(field, new BasicDBObject('$lt', range.upper))])
        dbObject = new BasicDBObject('$match', new BasicDBObject('$and', andList))
        return builder
    }

    /**
     * Creates a $match between a range inclusive of the lower bound
     *  <pre>
     * {@code
     *   //&#123;$match:&#123;$and:[&#123;field:&#123;$gte:10&#125;&#125;,&#123;field:&#123;$lt:100&#125;&#125;]&#125;&#125;
     *   builder.match().betweenRangeLowerInclusive(field,new IntegerRange(100,10)
     *}
     * </pre>
     * @param field to match between
     * @param range to match
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder betweenRangeLowerInclusive(String field, Range range) {
        DBObject andList = new BasicDBList()
        andList.addAll([new BasicDBObject(field, new BasicDBObject('$gte', range.lower)),
                        new BasicDBObject(field, new BasicDBObject('$lt', range.upper))])
        dbObject = new BasicDBObject('$match', new BasicDBObject('$and', andList))
        return builder
    }

    /**
     * Creates a $match between a range of the upper bound inclusive
     *  <pre>
     * {@code
     *   //&#123;$match:&#123;$and:[&#123;field:&#123;$gt:10&#125;&#125;,&#123;field:&#123;$lte:100&#125;&#125;]&#125;&#125;
     *   builder.match().betweenRangeUpperInclusive(field,new IntegerRange(100,10)
     *}
     *  </pre>
     * @param field
     * @param range
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder betweenRangeUpperInclusive(String field, Range range) {
        DBObject andList = new BasicDBList()
        andList.addAll([new BasicDBObject(field, new BasicDBObject('$gt', range.lower)),
                        new BasicDBObject(field, new BasicDBObject('$lte', range.upper))])
        dbObject = new BasicDBObject('$match', new BasicDBObject('$and', andList))
        return builder
    }

    /**
     * Creates a $match between a range inclusive of the upper and lower bounds
     * <pre>
     * {@code
     *   //&#123;$match:&#123;$and:[&#123;field:&#123;$gte:10&#125;&#125;,&#123;field:&#123;$lte:100&#125;&#125;]&#125;&#125;
     *   builder.match().betweenRangeInclusive(field,new IntegerRange(100,10)
     *}
     *  </pre>
     * @param field to match between
     * @param range to match between
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder betweenRangeInclusive(String field, Range range) {
        DBObject andList = new BasicDBList()
        andList.addAll([new BasicDBObject(field, new BasicDBObject('$gte', range.lower)),
                        new BasicDBObject(field, new BasicDBObject('$lte', range.upper))])
        dbObject = new BasicDBObject('$match', new BasicDBObject('$and', andList))
        return builder
    }

    /**
     * Adds a $or to a $match; exposes the {@see OrList}
     * <pre>
     * {@code
     *   // &#123;$match&#123;$or[]&#125;&#125;
     *   builder.match().or()
     *}
     * </pre>
     * @return OrList
     */
    OrList or() {
        def orList = new OrList(builder)
        dbObject = new BasicDBObject('$match', orList)
        return orList
    }

    /**
     * Adds an $and to a $match; exposes the AndList 
     * <pre>
     * {@code
     *   // &#123;$match: &#123;$and:[]&#125;&#125;
     *   builder.match().and()
     *}
     * </pre>
     * @see AndList
     * @return AndList
     */
    AndList and() {
        def andList = new AndList(builder)
        dbObject = new BasicDBObject('$match', andList)
        return andList
    }

    /**
     * Creates a $match on field $elemMatch of object as map
     * <pre>
     * {@code
     *   // &#123;$match:&#123;field:&#123;$elemMatch:{a:'a_value',b:'b_value'}&#125;&#125;&#125;
     *   builder.match().elemMatch(field,{a:'a_value',b:'b_value'})
     *}
     * </pre>
     * @param field to elemMatch on
     * @param element as Map of object
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder elemMatch(String field, Map element) {
        dbObject = new BasicDBObject('$match', new BasicDBObject(field, new BasicDBObject('$elemMatch', element)))
        return builder
    }

    /**
     * Creates a $match of field to value
     * <pre>
     * {@code
     *   // &#123;$match:&#123;field:&#123;$ne:value&#125;&#125;&#125;
     *   builder.match().notEqual(field,value)
     *}
     * </pre>
     * @param field to match
     * @param value value to match
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder notEqual(String field, value) {
        if (value) {
            dbObject = new BasicDBObject('$match', new BasicDBObject(field, new BasicDBObject('$ne', value)))
        }
        return builder
    }

    /**
     * Creates a $match of field to value
     * <pre>
     * {@code
     *   // &#123;$match:&#123;field:&#123;$ne:null&#125;&#125;&#125;
     *   builder.match().notEqualNull(field)
     *}
     * </pre>
     * @param field to match
     * @param value value to match
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder notEqualNull(String field) {
        dbObject = new BasicDBObject('$match', new BasicDBObject(field, new BasicDBObject('$ne', null)))
        return builder
    }

    /**
     * Creates a $gt of field to value
     * <pre>
     * {@code
     *   // &#123;$gt:&#123;field:value&#125;&#125;
     *   builder.match().greaterThan(field,value)
     *}
     * </pre>
     * @param field - to match greater than value
     * @param value - integer value
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder greaterThan(String field, int value) {
        dbObject = new BasicDBObject('$match', new BasicDBObject(field, new BasicDBObject('$gt', value)))
        return builder
    }

    /**
     * Creates a $match not in, or $nin
     * <pre>
     * // {$match:{field:{$nin:[value1,value2,...]]}}* builder.match().nin(field,[value1,value2,...]
     * </pre>
     * @param field
     * @param excludeList
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder nin(String field, List excludeList) {
        def ninList = new BasicDBList()
        ninList.addAll(excludeList?:[])
        dbObject = new BasicDBObject('$match', new BasicDBObject(field, new BasicDBObject('$nin', ninList)))
        return builder
    }

    /**
     * Builds the match object; builds and {@link MongoBuilder} types in the $match
     * @return DBObject
     */
    DBObject build() {
        if (!dbObject) {
            throw new IllegalStateException('Match is not defined')
        }
        if (dbObject.$match instanceof MongoBuilder) {
            dbObject.$match = dbObject.$match.build()
        }
        return dbObject
    }
}
