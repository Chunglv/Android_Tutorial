package sovang.com.criminalintent

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import sovang.com.criminalintent.CrimeDatabaseScheme.*

class CrimeBaseHelper(var context: Context, val dbName: String = "crimeBase.db", val version: Int = 1) : SQLiteOpenHelper(context, dbName, null, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.let{
            it.execSQL("create table " + CrimeTable.name + "("
            + " _id integer primary key autoincrement, "
            + CrimeTable.Cols.uuid + ", "
            + CrimeTable.Cols.title + ", "
            + CrimeTable.Cols.date + ", "
            + CrimeTable.Cols.solved + ", "
            + CrimeTable.Cols.suspect + ")"
            )
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}