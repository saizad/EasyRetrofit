package sa.zad.easyretrofitexample

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_download_observable.*


class DownloadActivity : BaseActivity() {
     var url: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_download_observable)

        val list = ArrayList<String>()
        list.add("16MB")
        list.add("32MB")
        list.add("64MB")
        list.add("128MB")
        list.add("256MB")
        list.add("512MB")
        list.add("1024MB")
        list.add("2048MB")

        val hashMap: HashMap<String, String> = HashMap()
        list.forEach {
            hashMap[it] = getString(R.string.download_url, it)
        }

        val dataAdapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, list)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        download_list.adapter = dataAdapter

        val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                url = hashMap[(view as TextView).text]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        download_list.onItemSelectedListener = onItemSelectedListener

        download_button.setOnClickListener {

            indeterminate.visibility = View.VISIBLE

            service.download(url)
                    .onProgressStart {
                        log(it.totalDuration().toString())
                        if(it.totalDuration() < 5000){
                            download_progress.visibility = View.GONE
                            return@onProgressStart
                        }
                        download_progress.visibility = View.VISIBLE
                        indeterminate.visibility = View.GONE
                    }
                    .progress {
                        download_progress.progress = it.progress.toInt()
                    }.neverException {
                        toast(it.message)
                    }.doFinally {
                        indeterminate.visibility = View.GONE
                        download_progress.visibility = View.GONE
                        download_progress.progress = 0
                    }.subscribe()
        }
    }
}
