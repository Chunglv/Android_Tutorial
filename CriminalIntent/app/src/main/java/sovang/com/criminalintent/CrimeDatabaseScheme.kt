package sovang.com.criminalintent

public class CrimeDatabaseScheme() {
    final class CrimeTable () {
        companion object {
            val name = "crime"
        }
        final class cols() {
            companion object {
                val uuid = "uuid"
                val title = "title"
                val date = "date"
                val solved = "solved"
            }
        }
    }
}