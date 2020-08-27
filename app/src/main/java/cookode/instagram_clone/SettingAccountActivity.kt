package cookode.instagram_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import cookode.instagram_clone._clone.R
import kotlinx.android.synthetic.main.activity_setting_account.*

class SettingAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_account)

        logout_btn_setprofile.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@SettingAccountActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    }
}
