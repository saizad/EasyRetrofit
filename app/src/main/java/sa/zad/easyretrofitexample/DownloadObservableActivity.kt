package sa.zad.easyretrofitexample

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_download_observable.*
import kotlinx.android.synthetic.main.progress_layout.*
import sa.zad.easyretrofit.CachePolicy
import sa.zad.easyretrofitexample.Utils.readTextIntToMillis


class DownloadObservableActivity : BaseProgress() {
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

        progress_fab.setOnClickListener {
            onRequest(progress_fab)
            service.cacheDownload(url, CachePolicy.LOCAL_IF_AVAILABLE)
                    .applyThrottle(readTextIntToMillis(default_throttle))
                    .onProgressStart({ updateStatus(it) },
                            readTextIntToMillis(min_processing_time), 100,
                            readTextIntToMillis(start_update_throttle))
                    .progressUpdate({
                        updateProgress(it, progress_fab)
                    }, readTextIntToMillis(update_throttle))
                    .onProgressCompleted {
                        updateProgress(it, progress_fab)
                    }.successResponse {
                        toast("Download Completed")
                    }.exception {
                        error(it.message!!)
                        progress_fab.hideProgress()
                        toast(it.message)
                    }.doFinally {
                        progress_fab.hideProgress()
                    }.subscribe()
        }
    }
}
