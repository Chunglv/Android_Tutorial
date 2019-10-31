package sovang.com.criminalintent

 class CrimeDatabaseScheme() {
     class CrimeTable {
        companion object {
            const val name = "crime"
        }
        class Cols {
            companion object {
                const val uuid = "uuid"
                const val title = "title"
                const val date = "date"
                const val solved = "solved"
                const val suspect = "suspect"
            }
        }
    }
}