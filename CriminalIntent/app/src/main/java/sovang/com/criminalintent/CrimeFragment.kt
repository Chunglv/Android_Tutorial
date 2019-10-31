package sovang.com.criminalintent

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_crime.*
import android.text.format.DateFormat
import java.util.*

class CrimeFragment: Fragment() {
    companion object {
        const val ARG_CRIME_ID = "crime_id"
        const val requestDate = 0
        const val requestContact = 1
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
            requestContact -> {
                data?.apply {
                    val contactURI = getData()
                    val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

                    val c = activity!!.contentResolver.query(contactURI, queryFields, null, null, null)
                    c.use { cursor ->
                        if (cursor.count == 0) {
                            return
                        }
                        cursor.moveToFirst()
                        val suspect = cursor.getString(0)
                        crime?.apply {
                            this.suspect = suspect
                        }
                        crimeSuspect?.apply {
                            text = suspect
                        }
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

    private fun getCrimeReport(): String {
        var solvedString: String
        crime?.apply {
            solvedString = if (solved) {
                getString(R.string.crimeReportSolved)
            } else {
                getString(R.string.crimeReportUnsolved)
            }
            val stringFormat = "EEE, MMM dd"
            val dateString = DateFormat.format(stringFormat, date)

            var suspect = suspect
            suspect = if (suspect == null) {
                getString(R.string.crimeReportNoSuspect)
            } else {
                getString(R.string.crimeReportSuspect, suspect)
            }
             return getString(R.string.crimeReport, title, dateString, solvedString, suspect)
        }
        return ""
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

        crimeReport?.setOnClickListener {
            var intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport())
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crimeReportSubject))
            intent = Intent.createChooser(intent, getString(R.string.sendReport))
            startActivity(intent)
        }
        val pickContact = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        crimeSuspect?.setOnClickListener {
            startActivityForResult(pickContact, requestContact)
        }
        crime?.apply {
            if (!suspect.isNullOrEmpty()) {
                crimeSuspect?.text = suspect
            }
        }

        val packageManager = activity!!.packageManager

        if (packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY) == null) {
            crimeSuspect?.isEnabled = false
        }

    }
}