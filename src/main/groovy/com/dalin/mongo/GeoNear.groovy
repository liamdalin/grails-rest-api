package com.dalin.mongo

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.mongodb.client.model.geojson.Position
import groovy.transform.ToString

@ToString(includeFields = true, excludes = ['builder', 'metaClass', 'parent'], includePackage = false)
class GeoNear implements MongoBuilder {
    DBObject dbObject
    final QueryPipeBuilder builder

//    final Map<String, Object> options = [spherical    : false, limit: null, num: null, maxDistance: null,
//                                         query        : null, distanceMultiplier: null, near: null,
//                                         distanceField: 'distance', includeLocs: null, minDistance: 0]

    enum UnitsOfLength {
        METERS(1),
        MILES(1609.344),
        KILOMETERS(1000)

        BigDecimal multiplier

        UnitsOfLength(BigDecimal multiplier) {
            this.multiplier = multiplier
        }

    }

    GeoNear(QueryPipeBuilder builder) {
        this.builder = builder
    }

    /**
     * Finds all things within the defined number of miles from the coordinates provided
     * @param locationField - Field that contains the GeoJSON point
     * @param distance from coordinates
     * @param coordinates as mongo Position
     * @param spherical true/false, default true
     * @return QueryPipeBuilder
     *
     */
    QueryPipeBuilder distanceFromPosition(String locationField,
                                          double distance,
                                          Position coordinates,
                                          UnitsOfLength units,
                                          boolean spherical = true) {
        def geoLocation = new BasicDBList() + [coordinates.values[0], coordinates.values[1]]
        def meters = distance * units.multiplier
        dbObject = new BasicDBObject('$geoNear',
                new BasicDBObject([near         : new BasicDBObject([type: 'Point', coordinates: geoLocation]),
                                   distanceField: 'distance',
                                   maxDistance  : meters,
                                   includeLocs  : locationField,
                                   spherical    : spherical
                ]))
        return builder
    }

    @Override
    def build() {
        if (!dbObject || !dbObject.$geoNear) {
            throw new IllegalStateException('$geoNear must be defined')
        }

        return dbObject
    }
}
