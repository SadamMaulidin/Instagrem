package cookode.instagram_clone.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cookode.instagram_clone.SettingAccountActivity
import cookode.instagram_clone._clone.R
import kotlinx.android.synthetic.main.fragment_profile.view.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewprofile = inflater.inflate(R.layout.fragment_profile, container, false)

        viewprofile.btn_edit_account.setOnClickListener {
            startActivity(Intent(context, SettingAccountActivity::class.java))
        }
        return viewprofile
    }

}
