package sa.zad.easyretrofitexample

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        response_post.setOnClickListener {
            startActivity(getActivityIntent(NeverObservableActivity::class.java, this))
        }

        observable_get.setOnClickListener {
            startActivity(getActivityIntent(ResultObservableActivity::class.java, this))
        }

        download.setOnClickListener {
            startActivity(getActivityIntent(DownloadObservableActivity::class.java, this))
        }

        upload.setOnClickListener {
            startActivity(getActivityIntent(UploadObservableActivity::class.java, this))
        }
    }
}
