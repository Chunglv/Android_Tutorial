package sovang.com.criminalintent

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import java.util.*

class CrimeActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return CrimeFragment()
    }

    companion object {
        const val uuidKey = "uuid_key"
        fun newIntent(context: Context, uuid: UUID): Intent {
            val intent = Intent(context, CrimeActivity::class.java)
            intent.putExtra(uuidKey, uuid)
            return intent
        }
    }

}
