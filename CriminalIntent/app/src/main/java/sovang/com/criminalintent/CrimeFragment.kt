package sovang.com.criminalintent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_crime.*
import java.util.*

class CrimeFragment: Fragment() {
    companion object {
        const val ARG_CRIME_ID = "crime_id"
        const val requestDate = 0
        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeId)
            val fragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private val dialogDate = "dialogDate"
    private var crime: Crime? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId =  arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crime = CrimeLab.getInstance(activity!!.applicationContext).getCrime(crimeId)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.crime_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.deleteCrime -> {
                crime?.let {
                    CrimeLab.getInstance(context!!.applicationContext).delete(it.id.toString())
                    activity!!.finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode){
            requestDate -> {
                data?.apply {
                    val date = getSerializableExtra(DatePickerFragment.extraDate) as Date
                    crime?.let {
                        it.date = date
                        updateDate(it)
                    }
                }
            }
        }
    }

    override fun onPause() {
        crime?.let {
            CrimeLab.getInstance(context!!.applicationContext).updateCrime(it)
        }
        super.onPause()
    }

    private fun updateDate(crime: Crime) {
        crimeDate?.text = crime.date.toString()
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
        crime?.apply {
            updateDate(this)
            crimeDate?.setOnClickListener {
                val fragmentManager = fragmentManager
                val dialog = DatePickerFragment.newInstance(date)
                dialog.setTargetFragment(this@CrimeFragment, requestDate)
                dialog.show(fragmentManager, dialogDate)
            }
        }

        crimeSolved?.setOnCheckedChangeListener {
            _, b ->
            crime?.let {
                it.solved = b
            }
        }
    }
}