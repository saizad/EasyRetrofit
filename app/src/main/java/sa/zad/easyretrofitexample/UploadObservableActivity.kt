package sa.zad.easyretrofitexample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_upload_observable.*
import kotlinx.android.synthetic.main.progress_layout.*
import sa.zad.easyretrofit.Utils
import sa.zad.easyretrofit.observables.UploadObservable
import sa.zad.easyretrofitexample.Utils.readTextIntToMillis
import java.io.File


class UploadObservableActivity : BaseProgress() {
    private val requestCode : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_upload_observable)
        progress_fab.isEnabled = false

        var selectedFile = File("")

        selected_file_bg.setOnClickListener {
            requestStoragePermission(requestCode)
        }

        permissionResult(requestCode)
                .filter {
                    log(it.toString())
                    if (!it) {
                        toast("One of the permisssion is not granted")
                    }
                    return@filter it
                }
                .subscribe({ callIntent() }, { toast(it.toString()) }, { toast("Completed") })

        result(requestCode)
                .subscribeOn(Schedulers.io())
                .filter { Utils.isNotNull(it.data) }
                .map {
                    val file = File.createTempFile("temp_", "." + File(getFileName(it.data)).extension)
                    Utils.writeStreamToFile(contentResolver.openInputStream(it.data), file)
                    return@map file
                }.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ file ->
                    file_size.text = getString(R.string.mb, (file.length() / 1000f / 1000f).toString())
                    file_name.text = file.name
                    selectedFile = file
                    progress_fab.isEnabled = true
                }, {
                    error(it.message!!)
                })


        progress_fab.setOnClickListener {
            onRequest(progress_fab)
            service.upload("http://www.csm-testcenter.org/test", UploadObservable.part(selectedFile))
                    .applyThrottle(readTextIntToMillis(default_throttle))
                    .onProgressStart({
                        updateStatus(it)
                    }, readTextIntToMillis(min_processing_time), 50, readTextIntToMillis(start_update_throttle))
                    .progressUpdate({
                        updateProgress(it, progress_fab)
                    }, readTextIntToMillis(update_throttle))
                    .onProgressCompleted {
                        updateProgress(it, progress_fab)
                        showSuccess("Upload Completed!!")
                    }.exception {
                        displayError(it.message!!, progress_fab)
                    }.doFinally {
                        done(progress_fab)
                    }.subscribe()
        }
    }

    private fun callIntent() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, requestCode)
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }
}
