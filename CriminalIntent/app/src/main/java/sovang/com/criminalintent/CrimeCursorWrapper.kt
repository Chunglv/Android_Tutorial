package sovang.com.criminalintent

import android.database.Cursor
import android.database.CursorWrapper
import sovang.com.criminalintent.CrimeDatabaseScheme.*
import java.util.*

class CrimeCursorWrapper(private val cursor: Cursor): CursorWrapper(cursor) {
    fun getCrime(): Crime {
        val uuid = cursor.getString(getColumnIndex(CrimeTable.cols.uuid))
        val titlt = cursor.getString(getColumnIndex(CrimeTable.cols.title))
        val date = cursor.getLong(getColumnIndex(CrimeTable.cols.date))
        val isSolved = cursor.getInt(getColumnIndex(CrimeTable.cols.solved))

        val crime = Crime(UUID.fromString(uuid))
        crime.title = titlt
        crime.date = Date(date)
        crime.solved = isSolved != 0
        return crime
    }
}