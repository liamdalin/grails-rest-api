
package com.dalin.mongo


abstract class Range<T> {
    protected T upper
    protected T lower

    Range(T upper, T lower) {
        this.upper = upper
        this.lower = lower
    }
}
