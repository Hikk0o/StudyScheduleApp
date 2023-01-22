package com.hikko.scheduleapp.pageEditActivities.adapters

import android.os.Bundle
import com.hikko.scheduleapp.PageActivity

class WidgetIntent : PageActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("onCreate widget intent")
    }
}