package cookode.instagram_clone

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import cookode.instagram_clone.Adapter.CommentAdapter
import cookode.instagram_clone.Model.Comment
import cookode.instagram_clone.Model.User
import cookode.instagram_clone._clone.R
import kotlinx.android.synthetic.main.activity_comment.*

class CommentActivity : AppCompatActivity() {

    private var postId = ""
    private var publisherId = ""
    private var firebaseUser: FirebaseUser? = null
    private var commentAdapter: CommentAdapter? = null
    private var commentList: MutableList<Comment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        postId = intent.getStringExtra("postId").toString()
        publisherId = intent.getStringExtra("publisherId").toString()
        firebaseUser = FirebaseAuth.getInstance().currentUser

        commentList = ArrayList()
        commentAdapter = CommentAdapter(this, commentList as ArrayList<Comment>)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        recyclerview_comments.layoutManager = linearLayoutManager
        recyclerview_comments.adapter = commentAdapter

        getPostImage()
        getPostComment()
        getUserInfo()
        txtpost_comments.setOnClickListener {
            when (add_comment.text.toString()){
                "" -> Toast.makeText(this, "Tidak boleh kosong", Toast.LENGTH_LONG).show()
                else -> postComment()
            }
        }
    }

    private fun getPostImage() {
        val refImage = FirebaseDatabase.getInstance().reference.child("Posts").child(postId).child("postimage")
        refImage.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val image = snapshot.value.toString()
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(post_image_comment)
                }
            }
        })
    }

    private fun getPostComment() {
        val refComment = FirebaseDatabase.getInstance().reference.child("Comments").child(postId)
        refComment.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val comments = i.getValue(Comment::class.java)
                        commentList?.add(comments!!)
                    }
                    commentAdapter?.notifyDataSetChanged()
                }
            }

        })
    }

    private fun getUserInfo() {
        val refImage = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        refImage.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val refImage = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(refImage?.image).placeholder(R.drawable.profile).into(profile_image_comment)
                }
            }
        })
    }

    private fun postComment() {
        val refComment = FirebaseDatabase.getInstance().reference.child("Comments").child(postId)
        val commentsMap = HashMap<String, Any>()
        commentsMap["comment"] = add_comment.text.toString()
        commentsMap["publisher"] = firebaseUser!!.uid
        refComment.push().setValue(commentsMap)
        add_comment.text.clear()
    }
}