package com.here.android.example.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_your_request.*

class YourRequest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_request)

        var yourRequestlist=ArrayList<Rider>()

        yourrequest.adapter=YourRequestAdapter(yourRequestlist)
        yourrequest.layoutManager=LinearLayoutManager(this)
        var myRef=FirebaseDatabase.getInstance().getReference("Rides")
        myRef.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.d("Error","Something wentwrong")
            }

            override fun onDataChange(datasnapshot: DataSnapshot) {

                for (snapshot in datasnapshot.children)
                {
                    var ob=snapshot.getValue(Rider::class.java)
                   if(ob!=null) {


                       yourRequestlist.add(ob)

                       yourrequest.adapter!!.notifyDataSetChanged()
                   }
                }


            }
        })




    }
}
