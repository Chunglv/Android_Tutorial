package sovang.com.criminalintent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

abstract class SingleFragmentActivity: AppCompatActivity() {
    abstract fun createFragment(): Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment == null) {
            val fragment = createFragment()
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment)
                    .commit()
        }
    }
}