package cookode.instagram_clone.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import cookode.instagram_clone.Adapter.MyImagesAdapter
import cookode.instagram_clone.Adapter.User
import cookode.instagram_clone.Model.Post
import cookode.instagram_clone.SettingAccountActivity
import cookode.instagram_clone._clone.R
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser

    var postListGrid: MutableList<Post>? = null
    var myImagesAdapter: MyImagesAdapter? = null

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

    private fun checkFollowerAndFollowingStatus(){
        val followingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Following")
        }

        if (followingRef != null)
        {
            followingRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.child(profileId).exists())
                    {
                        view?.btn_edit_account?.text = "Following"
                    } else {
                        view?.btn_edit_account?.text = "Follow"
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }
    }

    private fun getFollowers()
    {
        val followersRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(profileId)
            .child("Followers")

        followersRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot)
            {
                if (p0.exists()){
                    view?.txt_totalFollowers?.text = p0.childrenCount.toString()
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun getFollowings()
    {
        val followersRef = FirebaseDatabase.getInstance().reference
            //sesuai yang berada di firebase
            .child("Follow").child(profileId)
            .child("Following")

        followersRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    view?.txt_totalFollowing?.text = p0.childrenCount.toString()
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun myPost(){

        val postRef = FirebaseDatabase.getInstance().reference.child("Posts")
        postRef.addValueEventListener(object :ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){

                    (postListGrid as ArrayList<Post>).clear()

                    for (snapshot in p0.children){
                        val post = snapshot.getValue(Post::class.java)
                        if (post?.publisher.equals(profileId))
                        {
                            (postListGrid as ArrayList<Post>).add(post!!)
                        }

//                        postListGrid?.reverse()
                        Collections.reverse(postListGrid)
                        myImagesAdapter!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun userInfo(){
        val usersRef = FirebaseDatabase.getInstance().getReference()
            .child("Users").child(profileId)

        usersRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    val user = p0.getValue<User>(User::class.java)

                    Picasso.get().load(user?.image).placeholder(R.drawable.profile)
                        .into(view?.profile_image_gbr_frag)
                    view?.profile_fragment_username?.text = user?.username
                    view?.txt_full_namaProfile?.text = user?.fullname
                    view?.txt_bio_profile?.text = user?.bio
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

}
