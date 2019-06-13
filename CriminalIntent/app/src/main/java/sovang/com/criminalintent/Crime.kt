package sovang.com.criminalintent
import java.util.*

class Crime {
    private var id: UUID? = null
    var date: Date? = null
    var title: String = ""
    var solved: Boolean = false
    init {
        id = UUID.randomUUID()
        date = Date()
    }
}