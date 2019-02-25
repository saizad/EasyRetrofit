package sa.zad.easyretrofitexample

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.layout_easy_observable.*
import sa.zad.easyretrofitexample.Utils.hideSoftKeyboard
import sa.zad.easyretrofitexample.model.RegisterBody
import sa.zad.easyretrofitexample.model.RegisterError


class EasyObservableActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_easy_observable)

        register.setOnClickListener { _ ->

            resetResponseView()

            val registerBody = RegisterBody(email.text.toString(), password.text.toString())
            hideSoftKeyboard(this)

            service.register(registerBody)
                    .apiException({
                        showError(it.error)
                    }, RegisterError::class.java)
                    .neverException { showError(it.message) }
                    .doOnNext {
                        showSuccess("Registered Successfully!!")
                    }.doFinally {
                        postRequest()
                    }.subscribe()
        }

        api_error_test.setOnClickListener { _ ->
            resetResponseView()
            observable_menu.close(true)
            service.register(registerBody("random@email.com"))
                    .apiException({
                        showSuccess("Api Error Tested Successfully!!")
                        showError(it.error)
                    }, RegisterError::class.java)
                    .neverException {
                        showError("Api Error Test Failed \n \n" + it.message)
                    }.doFinally {
                        postRequest()
                    }.subscribe()
        }


        api_success_test.setOnClickListener { _ ->
            resetResponseView()
            observable_menu.close(true)
            service.register(registerBody("random@email.com", "rand_pass"))
                    .apiException({
                        request_error.text = "Success test failed \n \n" + it.error
                        request_error.visibility = View.VISIBLE
                    }, RegisterError::class.java)
                    .neverException {
                        showError("Success test failed \n \n" + it.message)
                    }.doOnNext {
                        showSuccess("Success Test Passed!!")
                    }
                    .doFinally {
                        postRequest()
                    }.subscribe()
        }

        random_error_test.setOnClickListener { _ ->
            resetResponseView()
            observable_menu.close(true)
            service.register(registerBody())
                    .neverException {
                        showSuccess("Random Error Tested Successfully!!")
                        showError(it.message)
                    }
                    .doOnComplete {
                        progressBar.visibility = View.GONE
                    }.subscribe()
        }
    }

    private fun showSuccess(successMessage: String) {
        group.visibility = View.VISIBLE
        success_text.text = successMessage
    }

    private fun showError(error: String?) {
        request_error.text = error
        request_error.visibility = View.VISIBLE
    }

    private fun resetResponseView() {
        progressBar.visibility = View.VISIBLE
        group.visibility = View.GONE
        request_error.visibility = View.GONE
        observable_menu.isEnabled = false
        register.isEnabled = false
    }

    private fun postRequest(){
        progressBar.visibility = View.GONE
        observable_menu.isEnabled = true
        register.isEnabled = true
    }

    private fun registerBody(emailStr: String = "", passwordStr: String = ""): RegisterBody {
        email.setText(emailStr)
        password.setText(passwordStr)
        return RegisterBody(emailStr, passwordStr)
    }
}
