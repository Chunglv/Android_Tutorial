package sovang.com.criminalintent

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import java.util.*

class CrimeActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID) as UUID
        return CrimeFragment.newInstance(crimeId)
    }

    companion object {
        const val EXTRA_CRIME_ID = "crime_id"
        fun newIntent(context: Context, uuid: UUID): Intent {
            val intent = Intent(context, CrimeActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, uuid)
            return intent
        }
    }

}
