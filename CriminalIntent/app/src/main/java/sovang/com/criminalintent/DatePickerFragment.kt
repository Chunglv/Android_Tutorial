package sovang.com.criminalintent

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_date.*
import java.util.*

class DatePickerFragment: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        return inflater.inflate(R.layout.dialog_date, container, false)
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = arguments?.getSerializable(argDate) as Date?
        date?.let {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            dialogDate?.init(year, month, day, null)
        }
        okButton?.setOnClickListener {
            dialogDate?.apply {
                val year = year
                val month = month
                val day = getDayOfMonth()
                val date = GregorianCalendar(year, month, day).time
                sendResult(Activity.RESULT_OK, date)
            }
            dialog.dismiss()
1
        }

    }

    private fun sendResult(resultCode: Int, date: Date){
        targetFragment?.apply {
            val intent = Intent()
            intent.putExtra(extraDate, date)
            onActivityResult(CrimeFragment.requestDate, resultCode,  intent)
        }
    }

    companion object {
        const val extraDate = "CriminalIntent.date"
        fun newInstance(date: Date?): DatePickerFragment {
            val arguments = Bundle()
            arguments.putSerializable(argDate, date)
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.arguments = arguments
            return datePickerFragment
        }

        const val argDate = "argDate"
    }
}