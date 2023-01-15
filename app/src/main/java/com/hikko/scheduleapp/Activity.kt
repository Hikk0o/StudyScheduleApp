package com.hikko.scheduleapp

// Пара
class Activity : Cloneable {

    var startTime: String = ""
    var endTime: String = ""
    var name: String = ""
    var type: String = ""
    var cabinet: String = ""
    var id: Int = -1

    public override fun clone(): Activity {
        return super.clone() as Activity
    }

    override fun toString(): String {
        return "{id: $id, type: \"$type\", name: \"$name\", " +
                "cabinet: \"$cabinet\", startTime: \"$startTime\", endTime: \"$endTime\"}"
    }
}