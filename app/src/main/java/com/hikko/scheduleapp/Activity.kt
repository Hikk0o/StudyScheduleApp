package com.hikko.scheduleapp

// Пара
class Activity : Comparable<Activity>, Cloneable {

    var startTime: String? = ""
    var endTime: String? = ""
    var name: String? = ""
    var type: String? = ""
    var cabinet: String? = ""
    var id: Int = -1


    // for sorting
    override fun compareTo(other: Activity): Int {
        var startTime = other.startTime

        if (startTime == null) {
            startTime = ""
        }

        return toString().compareTo(startTime)
    }

    public override fun clone(): Activity {
        return super.clone() as Activity
    }

    override fun toString(): String {
        return "{id: ${this.id}, type: \"${this.type}\", name: \"${this.name}\", " +
                "cabinet: \"${this.cabinet}\", startTime: \"${this.startTime}\", endTime: \"${this.endTime}\"}"
    }
}