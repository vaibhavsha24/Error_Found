package com.here.android.example.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_get_ride.*
import kotlinx.android.synthetic.main.activity_your_request.*

class GetRide : AppCompatActivity() {

   private lateinit var riderl:ArrayList<Rider>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_ride)

        riderl= ArrayList()

        riderlist.adapter=RiderAdapter(riderl)

        riderlist.layoutManager=LinearLayoutManager(this)

var dest:String=""
  var start=""
        if(intent.extras!=null)
        {
            dest=intent.getStringExtra("dest").toLowerCase()
            start=intent.getStringExtra("start").toLowerCase()
        }


        var myRef= FirebaseDatabase.getInstance().getReference("Rides")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("Error","Something wentwrong")
            }

            override fun onDataChange(datasnapshot: DataSnapshot) {

                for (snapshot in datasnapshot.children)
                {
                    var ob=snapshot.getValue(Rider::class.java)
                    if(ob!=null) {
                        if(ob.destination.equals(dest)&&ob.starting.equals((start))) {
                            riderl.add(ob)
                            riderlist.adapter!!.notifyDataSetChanged()
                        }
                    }

                }


            }
        })

    }
}
