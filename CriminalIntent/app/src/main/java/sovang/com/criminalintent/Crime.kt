package sovang.com.criminalintent
import java.util.*

class Crime() {
    var id: UUID
    var date: Date? = null
    var title: String = ""
    var suspect: String = ""
    var solved: Boolean = false
    var requiresPolice: Boolean = false
    init {
        id = UUID.randomUUID()
        date = Date()
    }

    constructor(uuid: UUID) : this() {
        id = uuid
        date = Date()
    }
}