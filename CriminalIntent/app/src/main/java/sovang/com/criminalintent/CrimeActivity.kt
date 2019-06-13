package sovang.com.criminalintent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class CrimeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime)
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment == null) {
            val fragment = CrimeFragment()
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment)
                    .commit()
         }
    }
}
