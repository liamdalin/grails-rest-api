package com.dalin.mongo

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import groovy.transform.ToString
import groovy.util.logging.Slf4j

/**
 * Builder for a mongo aggregate query pipeline
 * <pre>
 * {@code
 * QueryPipeBuilder builder = new QueryPipeBuilder()
 *   builder.&#123;method&#125;
 * def queryPipeline = builder.build()
 *}
 * </pre>
 */
@ToString(includes = 'queryPipe', includeFields = true, includeNames = true, includePackage = false)
@Slf4j(value = "log")
class QueryPipeBuilder implements MongoBuilder {

    static final int DEFAULT_MAX = 100
    static final int DEFAULT_OFFSET = 0

    static def create() {
        return new QueryPipeBuilder()
    }
    /**
     * Enum to represent sort order; Ascending and Descending
     */
    enum Sort {
        ASC(1),
        DESC(-1)

        private int value

        Sort(int value) {
            this.value = value
        }

        int getValue() {
            return value
        }

        static Sort from(String sort) {
            if ( sort ) {
                return valueOf(sort.toUpperCase())
            }
            return ASC
        }
    }

    private List queryPipe

    /**
     * Creates the query pipeline builder
     */
    QueryPipeBuilder() {
        this.queryPipe = new BasicDBList()
    }

    /**
     * Utility method used to create a copy for a QueryPipeBuilder
     * @param builder
     * @return new copy of QueryPipeBuilder
     */
    static QueryPipeBuilder copy(QueryPipeBuilder builder) {
        def newBuilder = new QueryPipeBuilder()
        newBuilder.queryPipe = new ArrayList(builder.queryPipe)
        log.debug "Copy builder with pipeline $newBuilder.queryPipe"
        return newBuilder
    }

    /**
     * Utility method to create a QueryPipeBuilder based on the supplied List
     *
     * @param queryPipe as List
     * @return QueryPipeBuilder
     */
    static QueryPipeBuilder copy(List queryPipe) {
        def newBuilder = new QueryPipeBuilder()
        newBuilder.queryPipe = new ArrayList(queryPipe)
        log.debug "Copy builder with pipeline $newBuilder.queryPipe"
        return newBuilder
    }

    /**
     * Returns the operated on mongo aggregate query pipeline
     * @return Mongo Query Pipeline as List
     */
    List build() {
        def cleanPipe = []
        queryPipe.each {
            if (it instanceof MongoBuilder) {
                cleanPipe << it.build()

            } else {
                cleanPipe << it
            }
        }
        log.debug "Created Query Pipeline: $queryPipe"
        return cleanPipe
    }

    /**
     * Adds a $match to the pipeline.  Skips if the value is falsey
     * <pre>
     * {@code
     *   // &#123;$match:&#123;field:value&#125;&#125;
     *   builder.match(field,value)
     *}
     *  </pre>
     * @param field to match
     * @param value to match
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder match(String field, value) {
        if (value) {
            queryPipe << new BasicDBObject('$match', new BasicDBObject(field, value))
        }
        return this
    }

    /**
     * Adds a $match of the field to null
     *
     * <pre>
     * {@code
     *   // &#123;$match: &#123;field:null&#125;&#125;
     *   builder.matchNull(field)
     *}
     * </pre>
     * @param field to match to null
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder matchNull(String field) {
        queryPipe << new BasicDBObject('$match', new BasicDBObject(field, null))
        return this
    }

    /**
     * Adds a $match on a field to a collection of values
     * <pre>
     * {@code
     *   // &#123;$match: &#123;field: &#123;$in:['a','b','c']&#125;&#125;&#125;
     *   builder.matchInList(field,['a','b','c'])
     *}
     * </pre>
     * @param field to match
     * @param valueList to match to field
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder matchInList(String field, List valueList) {
        queryPipe << new BasicDBObject('$match', new BasicDBObject(field, new BasicDBObject('$in', valueList)))
        return this
    }

    /**
     * Adds a $match on a field to a list of values in a n $or list
     * <pre>
     * {@code
     *   // &#123;$match: $or[&#123;field:'a'},&#123;field:'b'}]}
     *   builder.matchOrList(field,['a','b'])
     * }
     * </pre>
     * @param field to match
     * @param values to match
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder matchOrList(String field, values) {
        def orList = []
        values.each { orList << new BasicDBObject(field, it) }
        DBObject orMatchList = new BasicDBList()
        orMatchList.addAll(orList)
        queryPipe << new BasicDBObject('$match', new BasicDBObject('$or', orMatchList))
        return this
    }

    /**
     * Adds an $unwind on a field
     * <pre>
     * {@code
     *   // &#123;$unwind: &#123;path: "$field", preserveNullAndEmptyArrays: true/false&#125;&#125;
     *   builder.unwind(field)
     *   builder.unwind(field,[outerJoin:true])
     *}
     * </pre>
     * @param field to unwind
     * @param fieldConfig [outerJoin:true/false] to preserve null and empty arrays
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder unwindField(String field, Map fieldConfig = [outerJoin: false]) {
        queryPipe << new BasicDBObject('$unwind',
                new BasicDBObject([path                      : '$' + field,
                                   preserveNullAndEmptyArrays: fieldConfig?.outerJoin ?: false]))
        return this
    }

    /**
     * Adds a $lookup
     * <pre>
     * {@code
     *   // &#123;$lookup:&#123;from:field, localField: field, foreignField: '_id', as: field&#125;&#125;
     *   builder.addBasicJoin(field)
     *}
     * </pre>
     * @param field to lookup
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder addBasicJoin(String field) {
        DBObject lookup = new BasicDBObject(['$lookup': [
                'from': field, 'localField': field, 'foreignField': '_id', 'as': field
        ]])
        queryPipe << lookup
        unwindField(field)
        return this
    }

    /**
     * Adds a defined $lookup
     * <pre>
     * {@code
     *   // &#123;$lookup:&#123;from:'from', localField: 'local', foreignField: 'foreign', as: 'as'&#125;&#125;
     *   builder.addJoin(null,[[from: 'from',localField: 'local', foreignField: 'foreign', as: 'as']])
     *}
     * </pre>
     * @param field to lookup
     * @param fieldConfig options [from: ,localField: , foreignField: , as: ]
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder addJoin(String field, Map fieldConfig = [outerJoin: false]) {
        DBObject lookup = new BasicDBObject(['$lookup': [
                'from'        : fieldConfig.from ?: field,
                'localField'  : fieldConfig.localField ?: field,
                'foreignField': fieldConfig.foreignField ?: '_id',
                'as'          : fieldConfig.as ?: field
        ]])
        queryPipe << lookup
        unwindField(fieldConfig.as ?: field, fieldConfig)
        return this
    }

    /**
     * Adds a defined $lookup
     * <pre>
     * {@code
     *   // &#123;$lookup:&#123;from:'from', localField: 'local', foreignField: 'foreign', as: 'as'&#125;&#125;
     *   builder.lookup(null,[[from: 'from',localField: 'local', foreignField: 'foreign', as: 'as']])
     *}
     * </pre>
     * @param field to lookup
     * @param fieldConfig options [from: ,localField: , foreignField: , as: ]
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder lookup(String field, Map fieldConfig) {
        DBObject lookup = new BasicDBObject(['$lookup': [
                'from'        : fieldConfig.from ?: field,
                'localField'  : fieldConfig.localField ?: field,
                'foreignField': fieldConfig.foreignField ?: '_id',
                'as'          : fieldConfig.as ?: field
        ]])
        queryPipe << lookup
        return this
    }

    /**
     * Adds sort order; defaults to ascending
     * <pre>
     * {@code
     *   // &#123;$sort:&#123;field:'asc'&#125;&#125;
     *   builder.addSortOrder(field)
     *   builder.addSortOrder(field,Sort.ASC)
     *}
     * </pre>
     * @param field to sort on
     * @param sortOrder Sort.ASC/Sort.DESC
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder addSortOrder(String field, Sort sortOrder = Sort.ASC) {
        if ( field ) {
            queryPipe << new BasicDBObject('$sort', new BasicDBObject(field, sortOrder.value))
        }
        return this
    }

    /**
     * Adds $skip and $offset
     * <pre>
     * {@code
     *   // &#123;$skip: offset&#125;, &#123;$limit: max&#125;
     *   builder.addPagination(max,offset)
     *}
     * </pre>
     * @param max number of results
     * @param offset of the result set
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder addPagination(Integer max = DEFAULT_MAX, Integer offset) {
        max = max ?: DEFAULT_MAX
        offset = offset ?: DEFAULT_OFFSET
        queryPipe += [new BasicDBObject('$skip', offset), new BasicDBObject('$limit', max)]
        return this
    }

    /**
     * Adds a count
     * <pre>
     * {@code
     *   // $&#123;count: 'count'&#125;
     *   builder.count()
     *}
     * </pre>
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder count() {
        queryPipe << new BasicDBObject('$count', 'count')
        return this
    }

    /**
     * Adds a $group
     * <pre>
     * {@code
     *   // &#123;$group:&#123;_id:$field,count:&#123;$sum:1&#125;&#125;&#125;
     *   builder.groupCount(field)
     *}
     * </pre>
     * @param field to group and sum on
     * @param value of the sum - defaults to 1
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder groupCount(String field, sum = 1, String docField = null) {
        docField = docField ?: field
        DBObject groupHost = new BasicDBObject('_id', new BasicDBObject(field, "\$$docField"))
        groupHost.append('count', new BasicDBObject('$sum', sum))
        queryPipe << new BasicDBObject('$group', groupHost)
        return this
    }

    /**
     * Allows for the adding of a custom DBObject
     * @param dbObject
     * @return QueryPipeBuilder
     */
    QueryPipeBuilder addCustom(DBObject dbObject) {
        queryPipe << dbObject
        return this
    }

    /**
     * Adds all of the DBObjects in the list to the pipeline
     * @param list of DBObjects
     */
    QueryPipeBuilder addAll(List list) {
        list.each {
            if ( it instanceof DBObject ) {
                queryPipe << it
            }
        }
        return this
    }

    /**
     * Adds a Match
     *
     * @see Match
     * @return Match
     */
    Match match() {
        def match = new Match(this)
        queryPipe << match
        return match
    }

    /**
     * Adds a Projection
     *
     * @see Projection
     * @return Projection
     */
    Projection projection() {
        def projection = new Projection(this)
        queryPipe << projection
        return projection
    }

    /**
     * Adds a Filter
     *
     * @see Filter
     * @return Filter
     */
    Filter filter(String field, String asField = null) {
        def filter = new Filter(this, field, this, asField)
        queryPipe << filter
        return filter
    }

    GeoNear geoNear() {
        def geoNear = new GeoNear(this)
        queryPipe = [geoNear] + queryPipe // geoNear MUST be first in pipeline
        return geoNear
    }

    /**
     * Indication whether the query pipeline has been populated or is empty
     * @return boolean
     */
    boolean isPopulated() {
        queryPipe ? true : false
    }

    QueryPipeBuilder leftShift(DBObject dbObject) {
        queryPipe << dbObject
        return this
    }

    QueryPipeBuilder leftShift(List<DBObject> dbObjects) {
        addAll(dbObjects)
    }

}
