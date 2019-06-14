package sovang.com.criminalintent
import java.util.*

class Crime {
    var id: UUID = UUID.randomUUID()
    var date: Date? = null
    var title: String = ""
    var solved: Boolean = false
    var requiresPolice: Boolean = false
    init {
        date = Date()
    }
}