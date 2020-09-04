package cookode.instagram_clone.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainer
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import cookode.instagram_clone.Fragments.ProfileFragment
import cookode.instagram_clone._clone.R
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(
    private var mContext: Context,
    private val mUser: List<User>,
    private var isFragment: Boolean = false
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //memanggil layout user_item_layout
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user = mUser[position]
        holder.userNametxtView.text = user.getUsername()
        holder.fullNametxtView.text = user.getFullname()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(holder.userProfileImage)

        cekFollowingStatus(user.getUID(), holder.followButton)

        holder.itemView.setOnClickListener {
            val pref = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            pref.putString("profileID", user.getUID())
            pref.apply()
            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,ProfileFragment()).commit()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //mengelkan widget yg ada di layout user_item_layout
        var userNametxtView: TextView = itemView.findViewById(R.id.user_name_search)
        var fullNametxtView: TextView = itemView.findViewById(R.id.user_fullname_search)
        var userProfileImage: CircleImageView = itemView.findViewById(R.id.user_profile_image_search)
        var followButton: TextView = itemView.findViewById(R.id.follow_btnsearch)
    }

    private fun cekFollowingStatus(uid: String, followButton: TextView) {

    }
}