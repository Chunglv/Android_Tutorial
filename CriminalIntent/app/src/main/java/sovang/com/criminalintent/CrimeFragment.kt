package sovang.com.criminalintent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_crime.*
import java.util.*

class CrimeFragment: Fragment() {
    private var crime: Crime? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.intent?.run {
            val uuid = getSerializableExtra(CrimeActivity.uuidKey) as UUID
            crime = CrimeLab.getInstance(activity!!.applicationContext).getCrime(uuid)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_crime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeTitle?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                crime?.let {
                    it.title = p0.toString()
                }
            }

        })
        crime?.let {
            crimeTitle?.text = Editable.Factory.getInstance().newEditable(it.title)
            crimeSolved?.isChecked = it.solved
        }
        crime?.let {
            crimeDate?.text = it.date.toString()
            Toast.makeText(context, crimeDate?.text, Toast.LENGTH_SHORT).show()
        }

        crimeSolved?.setOnCheckedChangeListener({
            _, b ->
            crime?.let {
                it.solved = b
            }
        })
    }
}