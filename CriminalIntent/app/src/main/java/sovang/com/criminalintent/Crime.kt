package sovang.com.criminalintent
import java.util.*

class Crime {
    var id: UUID = UUID.randomUUID()
    var date: Date? = null
    var title: String = ""
    var solved: Boolean = false
    init {
        date = Date()
    }
}