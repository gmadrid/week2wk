package com.scrawlsoft.week2wk

data class TaskModel(var uid: String?, var text: String?) {
    // Firestore wants an empty ctor.
    constructor() : this(null, null)
}
