package sovang.com.criminalintent

import android.content.Context
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
    private var crimeList: MutableList<Crime> = arrayListOf()

    init {
    }

    fun addCrime(crime: Crime) {
        crimeList.add(crime)
    }
    fun getCrimes(): List<Crime> {
        return crimeList
    }

    fun getCrime(uuid: UUID): Crime? {
        for (crime: Crime in crimeList) {
            if (crime.id == uuid) {
                return crime
            }
        }
        return null
    }
    companion object: SingletonHolder<CrimeLab, Context>(::CrimeLab)
}