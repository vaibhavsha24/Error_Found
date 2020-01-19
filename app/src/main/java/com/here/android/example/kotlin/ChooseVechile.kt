package com.here.android.example.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_choose_vechile.*

class ChooseVechile : AppCompatActivity() {


    var choose="car"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_vechile)

        var dest=""
        var start=""

        if(intent.extras!=null)
        {
            dest=intent.getStringExtra("dest").toLowerCase()
            start=intent.getStringExtra("start").toLowerCase()
        }
        car.setOnClickListener {
            choose="car"
            car.backgroundTintList=ContextCompat.getColorStateList(this, R.color.choose)
            bike.backgroundTintList=ContextCompat.getColorStateList(this, R.color.black)

            image.setImageResource(R.drawable.car1)

            carnoplate.hint="Car No."
            textView2.visibility= View.VISIBLE
            noseats.visibility=View.VISIBLE

            bikehelmet.visibility=View.GONE

        }

        bike.setOnClickListener {
            choose="bike"
            car.backgroundTintList=ContextCompat.getColorStateList(this, R.color.black)
            bike.backgroundTintList=ContextCompat.getColorStateList(this, R.color.choose)


            carnoplate.hint="Bike No."
            textView2.visibility= View.GONE
           noseats.visibility=View.GONE
            image.setImageResource(R.drawable.bike)
            bikehelmet.visibility=View.VISIBLE


        }

        updatevechile.setOnClickListener {

           var db= FirebaseDatabase.getInstance()

            var data=Rider()
            data.name="Vaibhav"

            if(choose=="car")
            {
                data.veichle=true

                data.totalseats=noseats.text.toString()


            }
            else
            {
                data.totalseats=1.toString()
            }

            data.Car_No=carnoplate.text.toString()
            data.destination=dest
            data.starting=start
            data.price=price.text.toString()
            data.Car_No="Rj14dc2233"


            startActivity(Intent(this,YourRequest::class.java))
            var ref=db.getReference("Rides").push()
                ref.setValue(data)


        }
        bikehelmet.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener
        {group,checkedId->



        })


    }



}
