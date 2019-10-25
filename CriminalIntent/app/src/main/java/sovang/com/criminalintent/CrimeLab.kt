package sovang.com.criminalintent

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import sovang.com.criminalintent.CrimeDatabaseScheme.*
import java.util.*
import kotlin.collections.ArrayList

open class SingletonHolder<out T: Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}

class CrimeLab private constructor(context: Context) {
    private var database: SQLiteDatabase = CrimeBaseHelper(context).writableDatabase
    private var context: Context = context.applicationContext

    private fun getContentValues(crime: Crime): ContentValues {
        val values = ContentValues()
        values.put(CrimeTable.cols.uuid, crime.id.toString())
        values.put(CrimeTable.cols.title, crime.title)
        values.put(CrimeTable.cols.date, crime.date?.time)
        values.put(CrimeTable.cols.solved, crime.solved)
        return values
    }

    fun getCrimes(): List<Crime> {
        val crimeList = mutableListOf<Crime>()
        val cursorWrapper = queryCrime(null, null)
        cursorWrapper.use { cursorWrapper ->
            cursorWrapper.moveToFirst()
            while(!cursorWrapper.isAfterLast) {
                crimeList.add(cursorWrapper.getCrime())
                cursorWrapper.moveToNext()
            }
        }
        return crimeList
    }

    fun addCrime(crime: Crime) {
        val values = getContentValues(crime)
        database.insert(CrimeTable.name, null, values)
    }

    fun getCrime(uuid: UUID): Crime? {
        val cursor = queryCrime(CrimeTable.cols.uuid + "= ?", arrayOf(uuid.toString()))
        if (cursor.count == 0) {
            return null
        }
        cursor.use { cursor ->
            cursor.moveToFirst()
            return cursor.getCrime()
        }
    }
    fun updateCrime(crime: Crime) {
        val crimeId = crime.id
        val values = getContentValues(crime)
        database.update(CrimeTable.name, values, CrimeTable.cols.uuid + "= ?",
                arrayOf(crimeId.toString()))
    }

    private fun queryCrime(whereClause: String?, whereArg: Array<String>?): CrimeCursorWrapper {
        val cursor = database.query(CrimeTable.name,
                null, whereClause, whereArg, null, null, null)
        return CrimeCursorWrapper(cursor)
    }

    fun deleteCrime(crime: Crime) {
        val uuid = crime.id
        database.delete(CrimeTable.name, CrimeTable.cols.uuid + "= ?", arrayOf(uuid.toString()))
    }

    fun delete(uuid: String) {
        database.delete(CrimeTable.name, CrimeTable.cols.uuid + "= ?", arrayOf(uuid))
    }
    companion object: SingletonHolder<CrimeLab, Context>(::CrimeLab)
}