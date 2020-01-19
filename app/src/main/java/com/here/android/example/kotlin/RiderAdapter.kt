package com.here.android.example.kotlin


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_get_ride.view.*
import kotlinx.android.synthetic.main.rider.view.*


class RiderAdapter(val imagelist: ArrayList<Rider>) : RecyclerView.Adapter<RiderAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rider, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(imagelist[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return imagelist.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(rider: Rider) {


            itemView.ridername.text=rider.name
            itemView.fromd.text=rider.starting
            itemView.tod.text=rider.destination
            itemView.price.text="₹ "+rider.price+" per/km"
            itemView.carnumber.text=rider.Car_No
            if(rider.veichle)
            {
                itemView.vimage.setImageResource(R.drawable.car)


            }
            itemView.bookride.setOnClickListener {

                Toast.makeText(itemView.context,"Ride Booked Sucessfully",Toast.LENGTH_LONG).show()

            }
        }
    }
    


}