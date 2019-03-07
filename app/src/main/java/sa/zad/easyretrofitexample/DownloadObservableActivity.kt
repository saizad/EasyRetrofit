package sa.zad.easyretrofitexample

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_download_observable.*
import kotlinx.android.synthetic.main.progress_layout.*
import sa.zad.easyretrofit.CachePolicy


class DownloadObservableActivity : BaseProgress() {
    private val requestCode : Int = 1

    var url: String? = null
    var cachePolicy = CachePolicy.SERVER_ONLY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_download_observable)
        val downloadSizeList = ArrayList<String>()
        downloadSizeList.add("16MB")
        downloadSizeList.add("32MB")
        downloadSizeList.add("64MB")
        downloadSizeList.add("128MB")
        downloadSizeList.add("256MB")
        downloadSizeList.add("512MB")
        downloadSizeList.add("1024MB")
        downloadSizeList.add("2048MB")

        val downloadSizeListMap: HashMap<String, String> = HashMap()
        downloadSizeList.forEach {
            downloadSizeListMap[it] = getString(R.string.download_url, it)
        }
        download_list.adapter = dataAdapter(downloadSizeList)

        val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                var urls = Sample.getInstance().sampleEasyRetrofit().client().cache()?.urls()
                url = downloadSizeListMap[(view as TextView).text]
                checkBox.isChecked = false
                while (urls!!.hasNext()) {
                    if (url == urls.next()) {
                        checkBox.isChecked = true
                        break
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        download_list.onItemSelectedListener = onItemSelectedListener

        val cacheOptionsListMap: HashMap<String, Int> = HashMap()
        cacheOptionsListMap["Server only"] = CachePolicy.SERVER_ONLY
        cacheOptionsListMap["Local only"] = CachePolicy.LOCAL_ONLY
        cacheOptionsListMap["Local if available"] = CachePolicy.LOCAL_IF_AVAILABLE
        cacheOptionsListMap["Local if fresh"] = CachePolicy.LOCAL_IF_FRESH
        cache_options.adapter = dataAdapter(cacheOptionsListMap.keys.toList())


        cache_options.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cachePolicy = cacheOptionsListMap.values.toList()[position]
            }

        }

        progress_fab.setOnClickListener {
            requestStoragePermission(requestCode)
        }

        permissionResult(requestCode)
                .filter {
                    if (!it) {
                        toast("One of the permisssion is not granted")
                    }
                    return@filter it
                }
                .subscribe({ download() }, { toast(it.toString()) }, { toast("Completed") })

    }

    private fun dataAdapter(list: List<*>): ArrayAdapter<Any?> {
        val dataAdapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, list)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return dataAdapter
    }

    private fun download() {
        onRequest(progress_fab)
        service.cacheDownload(url, cachePolicy)
                .applyThrottle(Utils.readTextIntToMillis(default_throttle))
                .onProgressStart({ updateStatus(it) },
                        Utils.readTextIntToMillis(min_processing_time), 100,
                        Utils.readTextIntToMillis(start_update_throttle))
                .progressUpdate({
                    updateProgress(it, progress_fab)
                }, Utils.readTextIntToMillis(update_throttle))
                .onProgressCompleted {
                    updateProgress(it, progress_fab)
                    showSuccess("Download Completed!!")
                }.exception {
                    displayError(it.message!!, progress_fab)
                }
                .doFinally {
                    done(progress_fab)
                }.subscribe {
                    log(it.name)
                }
    }
}
