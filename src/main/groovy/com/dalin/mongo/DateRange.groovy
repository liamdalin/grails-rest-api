
package com.dalin.mongo

/**
 * Date Specific implementation of the {@see Range} interface
 */
class DateRange extends Range<Date> {
    /**
     * Creates a DateRange.  For use in Range aggregates
     * @param upper Date
     * @param lower Date
     */
    DateRange(Date upper, Date lower) {
        super(upper, lower)
    }
}
