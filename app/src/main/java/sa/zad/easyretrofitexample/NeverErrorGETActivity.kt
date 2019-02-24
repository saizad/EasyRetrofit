package sa.zad.easyretrofitexample

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import kotlinx.android.synthetic.main.layout_never_error_observable.*
import java.io.File

class NeverErrorGETActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_never_error_observable)
        request_user.setOnClickListener { _ ->
           fetchUser((1..15).shuffled().last())
        }
        error_check.setOnClickListener {
            fetchUser(13)
        }
    }

    @SuppressLint("CheckResult")
    private fun fetchUser(userId: Int){

        request_error.visibility = View.GONE
        loading_bg.visibility = View.VISIBLE
        request_user.isEnabled = false
        unknown_progress.visibility = View.VISIBLE

        val users = if(userId > 12)
            service.notFound()
        else
            service.users(userId)

        users.
                neverException {
                    request_error.visibility = View.VISIBLE
                    request_error.text = it.message
                    name.text = "User Not Found"
                    avatar.setImageDrawable(ResourcesCompat.getDrawable(resources,
                            R.drawable.ic_person_black_64dp, null))
                }
                .flatMap { it ->
                    val user = it.data
                    service.avatar(user.avatar).map {
                        Pair<File, String>(it.value!!,
                                getString(R.string.name, user.first_name, user.last_name))
                    }
                }
                .doOnComplete {
                    request_user.isEnabled = true
                    loading_bg.visibility = View.GONE
                    unknown_progress.visibility = View.GONE
                }
                .subscribe {
                    name.text = it.second
                    avatar.setImageURI(Uri.fromFile(it.first))
                }
    }
}
