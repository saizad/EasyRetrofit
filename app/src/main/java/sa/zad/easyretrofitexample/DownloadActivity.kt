package sa.zad.easyretrofitexample

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_download_observable.*
import sa.zad.easyretrofit.ProgressListener
import sa.zad.easyretrofit.Utils


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

        progress_fab.setOnClickListener {
            progress_fab.setIndeterminate(true)
            progress_fab.setShowProgressBackground(true)
            request_error.visibility = View.GONE
            service.download(url)
                    .onProgressStart({
                    }, Utils.toInteger(min_processing_time.text.toString(), 1).toLong() * 1000, 50)
                    .progressUpdate {
                        progress_fab.setIndeterminate(false)
                        updateProgress(it)
                    }.onProgressCompleted {
                        progress_fab.setIndeterminate(false)
                        updateProgress(it)
                    }.successResponse {
                        toast("Download Completed")
                    }.exception {
                        request_error.text = it.message
                        request_error.visibility = View.VISIBLE
                        progress_fab.hideProgress()
                    }.doFinally {
                        progress_fab.hideProgress()
                    }.subscribe()
        }
    }

    private fun updateProgress(progress: ProgressListener.Progress<*>){
        updateStatus(progress)
        progress_fab.setProgress(progress.progress.toInt(), false)
    }

    private fun updateStatus(progress: ProgressListener.Progress<*>){
        time_remaining.text = (progress.timeRemaining() / 1000).toString() + " Sec"
        size_remaining.text = ((progress.size - progress.written) / (1024 * 1000)).toString() + " Mb"
        elapsed_time.text = (progress.elapsedTime()/ 1000).toString() + " Sec"
    }
}
