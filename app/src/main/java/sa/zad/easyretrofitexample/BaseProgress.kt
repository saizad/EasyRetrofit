package sa.zad.easyretrofitexample

import android.view.View
import com.github.clans.fab.FloatingActionButton
import kotlinx.android.synthetic.main.progress_layout.*
import sa.zad.easyretrofit.ProgressListener

abstract class BaseProgress : BaseActivity() {

    protected fun onRequest(fab: FloatingActionButton){
        fab.setIndeterminate(true)
        fab.setShowProgressBackground(true)
        request_error.visibility = View.GONE
    }

    protected fun updateStatus(progress: ProgressListener.Progress<*>) {
        time_remaining.text = getString(R.string.seconds, progress.estimatedTimeRemaining() / 1000)
        size_remaining.text = getString(R.string.mb, progress.sizeRemainingMB().toString())
        elapsed_time.text = getString(R.string.seconds, progress.elapsedTime() / 1000)
    }

    protected fun updateProgress(progress: ProgressListener.Progress<*>, fab: FloatingActionButton) {
        fab.setIndeterminate(false)
        updateStatus(progress)
        fab.setProgress( Math.ceil(progress.progress.toDouble()).toInt(), false)
    }

    protected fun error(string: String) {
        request_error.text = string
        request_error.visibility = View.VISIBLE
    }
}