package sovang.com.criminalintent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import kotlinx.android.synthetic.main.fragment_crime_list.*
import kotlinx.android.synthetic.main.fragment_crime_list.view.*
import kotlinx.android.synthetic.main.item_crime_list.view.*

class CrimeListFragment: Fragment() {

    private var isShowTitle = false

    private val subtitleKey = "subtitle"

    private class CrimeViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        private var crime: Crime? = null
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            crime?.apply {
                val intent = CrimePagerActivity.newIntent(itemView.context, id)
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
            crime?.apply {
                val intent = CrimePagerActivity.newIntent(itemView.context, id)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(crime: Crime) {
            this.crime = crime
            itemView.crimeTitle.text = crime.title
            itemView.crimeDate.text = crime.date.toString()
        }
    }

    private class CrimeAdapter(var crimeList: List<Crime>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

        fun setCrimes(crimes: List<Crime>) {
            this.crimeList = crimes
        }

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        view.crimeRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext ?: context)
            adapter = CrimeAdapter(CrimeLab.getInstance(activity?.applicationContext ?: context).getCrimes())
        }

        savedInstanceState?.let {
            isShowTitle = savedInstanceState.getBoolean(subtitleKey)
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.crime_list_fragment, menu)
        val menuItem = menu?.findItem(R.id.title)
        if (isShowTitle) {
            menuItem?.setTitle(R.string.hideSubtitle)
        }
        else {
            menuItem?.setTitle(R.string.showSubtitle)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.addCrime -> {
                val crime = Crime()
                val crimeLab = CrimeLab.getInstance(activity!!.applicationContext)
                crimeLab.addCrime(crime)
                val intent = CrimePagerActivity.newIntent(activity!!.applicationContext, crime.id)
                startActivity(intent)
                return true
            }
            R.id.title -> {
                isShowTitle = !isShowTitle
                (activity as AppCompatActivity)!!.invalidateOptionsMenu()
                updateSubtitle()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateSubtitle() {
        val crimeLab = CrimeLab.getInstance(activity!!.applicationContext)
        val size = crimeLab.getCrimes().size
        var title = getString(R.string.subtitleFormat, size)
        if (!isShowTitle) {
            title = ""
        }
        (activity as AppCompatActivity)!!.supportActionBar!!.title = title
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(subtitleKey, isShowTitle)
    }

    private fun updateUI() {
        val adapter = crimeRecyclerView.adapter as CrimeAdapter
        val crimes = CrimeLab.getInstance(context!!.applicationContext).getCrimes()
        adapter?.apply {
            setCrimes(crimes)
            notifyDataSetChanged()
        }
        updateSubtitle()
    }
}