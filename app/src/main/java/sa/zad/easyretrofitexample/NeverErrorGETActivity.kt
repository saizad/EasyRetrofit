package sa.zad.easyretrofitexample

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_never_error_observable.*

class NeverErrorGETActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_never_error_observable)
        request_user.setOnClickListener { _ ->
            service.users((0..12).shuffled().last())
                    .neverException {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                    .map { it.data }
                    .doOnNext {
                        name.text = """${it.first_name} ${it.last_name}"""
                        circularProgressbar.visibility = View.VISIBLE
                    }
                    .flatMap { service.avatar(it.avatar).progress {
                        Log.d("asdfasdf", "asdfasdf progress " + it.progress)
                        circularProgressbar.progress = it.progress.toInt()
                    } }
                    .subscribe {
                        circularProgressbar.visibility = View.GONE
                        avatar.setImageURI(Uri.fromFile(it.value))
                    }
        }
    }
}
