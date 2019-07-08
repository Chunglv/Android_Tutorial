package sovang.com.criminalintent

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_crime_pager.*
import kotlinx.android.synthetic.main.activity_crime_pager.view.*
import java.util.*

class CrimePagerActivity : AppCompatActivity() {
    var crimes: List<Crime> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)
        crimes = CrimeLab.getInstance(applicationContext).getCrimes()
        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID) as UUID
        crime_view_pager?.setAdapter(object: FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return crimes.size
            }

            override fun getItem(p0: Int): Fragment {
                val crime = crimes.get(p0)
                return CrimeFragment.newInstance(crime.id)
            }
        })
        for (i in 0 until crimes.size) {
            val crime = crimes.get(i)
            if (crime.id == crimeId) {
                crime_view_pager?.setCurrentItem(i)
                break
            }
        }
    }

    companion object {
        const val EXTRA_CRIME_ID = "crime_id"
        fun newIntent(context: Context, uuid: UUID): Intent {
            val intent = Intent(context, CrimePagerActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, uuid)
            return intent
        }
    }
}
