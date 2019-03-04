package sa.zad.easyretrofitexample

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import kotlinx.android.synthetic.main.layout_result_observable.*
import java.io.File

class ResultObservableActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_result_observable)
        request_user.setOnClickListener {
            fetchUser((1..15).shuffled().last())
        }
        error_check.setOnClickListener {
            fetchUser(13)
        }


    }

    @SuppressLint("CheckResult")
    private fun fetchUser(userId: Int) {

        request_error.visibility = View.GONE
        loading_bg.visibility = View.VISIBLE
        request_user.isEnabled = false
        error_check.isEnabled = false
        unknown_progress.visibility = View.VISIBLE

        val users = if (userId > 12)
            service.notFound()
        else
            service.users(userId)

        users
                .neverError { failed("Never Error: " + it.message) }
                .failedResult { failed("Failed Result: $it") }
                .failedResponse { failed("Failed Response: " + it?.message!!) }
                .flatMap {
                    val user = it.response()?.body()?.data
                    log(user?.avatar)
                    service.download(user?.avatar).map {
                        Pair<File, String>(it,
                                getString(R.string.name, user?.first_name, user?.last_name))
                    }
                }
                .doFinally {
                    error_check.isEnabled = true
                    request_user.isEnabled = true
                    loading_bg.visibility = View.GONE
                    unknown_progress.visibility = View.GONE
                }
                .subscribe {
                    name.text = it.second
                    avatar.setImageURI(Uri.fromFile(it.first))
                }
    }

    private fun failed(error: String, nameText: String = "User Not Found") {
        request_error.visibility = View.VISIBLE
        request_error.text = error
        name.text = nameText
        avatar.setImageDrawable(ResourcesCompat.getDrawable(resources,
                R.drawable.ic_person_black_64dp, null))
    }
}
