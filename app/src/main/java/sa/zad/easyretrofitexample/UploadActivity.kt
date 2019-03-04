package sa.zad.easyretrofitexample

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.support.v4.app.ActivityCompat
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_upload_observable.*
import sa.zad.easyretrofit.ProgressListener
import sa.zad.easyretrofit.Utils
import sa.zad.easyretrofit.observables.UploadObservable
import java.io.File


class UploadActivity : BaseActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_upload_observable)
        progress_fab.hide(false)

        var selectedFile = File("")

        selected_file_bg.setOnClickListener {
            val perms = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this, perms, 1)
        }

        permissionResult(1)
                .filter {
                    if (!it) {
                        toast("One of the permisssion is not granted")
                    }
                    return@filter it
                }
                .subscribe({ callIntent() }, { toast(it.toString()) }, { toast("Completed") })

        result(1)
                .subscribeOn(Schedulers.io())
                .filter { Utils.isNotNull(it.data) }
                .map {
                    val file = File.createTempFile("temp_", "." + File(getFileName(it.data)).extension)
                    Utils.writeStreamToFile(contentResolver.openInputStream(it.data), file)
                    return@map file
                }.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ file ->
                    file_size.text = (file.length() / 1000f / 1000f).toString() + " Mb"
                    file_name.text = file.name
                    selectedFile = file
                    progress_fab.show(true)
                }, {
                    error(it.message!!)
                })


        progress_fab.setOnClickListener {
            progress_fab.setIndeterminate(true)
            progress_fab.setShowProgressBackground(true)
            request_error.visibility = View.GONE
            service.upload("http://www.csm-testcenter.org/test", UploadObservable.part(selectedFile))
                    .onProgressStart({
                    }, Utils.toInteger(min_processing_time.text.toString(), 1).toLong() * 1000, 50)
                    .progressUpdate {
                        updateStatus(it)
                        progress_fab.setIndeterminate(false)
                        progress_fab.setProgress(it.progress.toInt(), false)
                    }.onProgressCompleted {
                        updateStatus(it)
                    }.failedResponse {
                        toast("Failed Response received " + it.code())
                    }.successResponse {
                        toast("Success Response received")
                    }.exception {
                        error(it.message!!)
                    }.doFinally {
                        progress_fab.hideProgress()
                    }.subscribe()
        }
    }

    private fun callIntent() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, 1)
    }

    private fun updateStatus(progress: ProgressListener.Progress<*>) {
        time_remaining.text = (progress.timeRemaining() / 1000).toString() + " Sec"
        size_remaining.text = ((progress.size - progress.written) / (1024 * 1000)).toString() + " Mb"
        elapsed_time.text = (progress.elapsedTime() / 1000).toString() + " Sec"
    }

    private fun error(string: String) {
        request_error.text = string
        request_error.visibility = View.VISIBLE
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
