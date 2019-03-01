package sa.zad.easyretrofitexample

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        response_post.setOnClickListener {
            startActivity(getActivityIntent(EasyObservableActivity::class.java, this))
        }

        observable_get.setOnClickListener {
            startActivity(getActivityIntent(ResultObservableActivity::class.java, this))
        }

        download.setOnClickListener {
            startActivity(getActivityIntent(DownloadActivity::class.java, this))
        }

        upload.setOnClickListener {
            startActivity(getActivityIntent(UploadActivity::class.java, this))
        }
    }
}
