package com.hikko.scheduleapp

// Пара
class Activity : Comparable<Activity> {

    var startTime: String?
    var endTime: String?
    var name: String?
    var type: String?
    private var cabinet: String
    var id: Int

    constructor(name: String?, type: String?, start: String?, end: String?, id: Int) {
        this.name = name
        this.type = type
        this.id = id
        cabinet = ""
        startTime = start
        endTime = end
    }

    constructor(name: String?, type: String?, start: String?, end: String?) {
        this.name = name
        this.type = type
        id = -1
        cabinet = ""
        startTime = start
        endTime = end
    }

    constructor(name: String?, type: String?, cabinet: String, start: String?, end: String?) {
        this.name = name
        this.type = type
        this.cabinet = cabinet
        id = -1
        startTime = start
        endTime = end
    }

    // for sorting
    override fun compareTo(other: Activity): Int {
        var startTime = other.startTime

        if (startTime == null) {
            startTime = ""
        }

        return toString().compareTo(startTime)
    }
}