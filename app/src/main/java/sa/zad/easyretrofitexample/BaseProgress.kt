package sa.zad.easyretrofitexample

import android.os.Handler
import android.os.Looper
import android.view.View
import com.github.clans.fab.FloatingActionButton
import kotlinx.android.synthetic.main.progress_layout.*
import kotlinx.android.synthetic.main.request_status_layout.*
import sa.zad.easyretrofit.ProgressListener

abstract class BaseProgress : BaseActivity() {

    protected fun onRequest(fab: FloatingActionButton){
        fab.isEnabled = false
        fab.setIndeterminate(true)
        fab.setShowProgressBackground(true)
        request_error.visibility = View.GONE
        group.visibility = View.GONE
    }

    protected fun updateStatus(progress: ProgressListener.Progress<*>) {
        time_remaining.text = getString(R.string.seconds, progress.estimatedTimeRemaining() / 1000)
        size_remaining.text = getString(R.string.mb, progress.sizeRemainingMB().toString())
        elapsed_time.text = getString(R.string.seconds, progress.elapsedTime() / 1000)
    }

    protected fun showSuccess(successMessage: String) {
        group.visibility = View.VISIBLE
        success_text.text = successMessage
    }

    protected fun updateProgress(progress: ProgressListener.Progress<*>, fab: FloatingActionButton) {
        fab.setIndeterminate(false)
        updateStatus(progress)
        fab.setProgress( Math.ceil(progress.progress.toDouble()).toInt(), false)
    }

    protected fun displayError(string: String, fab: FloatingActionButton) {
        fab.setIndeterminate(false)
        request_error.text = string
        request_error.visibility = View.VISIBLE
    }

    protected fun done(fab: FloatingActionButton){
        fab.isEnabled = true
        Handler(Looper.getMainLooper()).postDelayed({
            fab.hideProgress()
        }, 1000)
    }
}