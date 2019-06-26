package sovang.com.criminalintent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_crime_list.view.*
import kotlinx.android.synthetic.main.item_crime_list.view.*

class CrimeListFragment: Fragment() {

    private class CrimeViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        private var crime: Crime? = null
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            crime?.run {
                val intent = CrimeActivity.newIntent(itemView.context, id)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(crime: Crime) {
            this.crime = crime
            itemView.crimeTitle.text = crime.title
            itemView.crimeDate.text = crime.date.toString()
        }
    }

    private class CrimeDangerousViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        private var crime: Crime? = null
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            Toast.makeText(itemView.context, crime?.title + " Danger Clicked!!", Toast.LENGTH_SHORT).show()
        }

        fun bind(crime: Crime) {
            this.crime = crime
            itemView.crimeTitle.text = crime.title
            itemView.crimeDate.text = crime.date.toString()
        }
    }

    private class CrimeAdapter(val crimeList: List<Crime>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        val normalCrimeType = 0
        val dangerCrimeType = 1

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            return if (p1 == normalCrimeType) {
                val view = LayoutInflater.from(p0.context).inflate(R.layout.item_crime_list, p0, false)
                CrimeViewHolder(view)
            } else {
                val view = LayoutInflater.from(p0.context).inflate(R.layout.item_crime_dangerous_list, p0, false)
                CrimeDangerousViewHolder(view)
            }

        }

        override fun getItemCount(): Int {
            return crimeList.size
        }

        override fun getItemViewType(position: Int): Int {
            val crime = crimeList[position]
            val requiresPolice = crime.requiresPolice
            return if (requiresPolice) {
                dangerCrimeType
            } else {
                normalCrimeType
            }
        }

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            val crime = crimeList[p1]
            if (p0 is CrimeDangerousViewHolder) {
                p0.bind(crime)
            }
            else if (p0 is CrimeViewHolder) {
                p0.bind(crime)
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        view.crimeRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext ?: context)
            adapter = CrimeAdapter(CrimeLab.getInstance(activity?.applicationContext ?: context).getCrimes())
        }
        return view
    }


}