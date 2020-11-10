package com.example.happyplaces.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.MainActivity
import com.example.happyplaces.R
import com.example.happyplaces.activity.AddHappyPlaceActivity
import com.example.happyplaces.database.DataBaseHandler
import com.example.happyplaces.model.HappyPlaceModel
import kotlinx.android.synthetic.main.item_happy_place.view.*

class HappyPlaceAdapter (
    private val context: Context,
    private val list: ArrayList<HappyPlaceModel>

):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_happy_place,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]

        if (holder is MyViewHolder){
            holder.itemView.iv_images.setImageURI(Uri.parse(model.image))
            holder.itemView.tvTitle.text = model.title
            holder.itemView.tvDescription.text = model.description
            holder.itemView.setOnClickListener{
                if (onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    //a fucntion to edit the added happy place detail and pass the existing details through intent
    fun notifyEditItem(activity: MainActivity, position: Int, requestCode: Int){
        val intent = Intent(context, AddHappyPlaceActivity :: class.java)
        intent.putExtra(MainActivity.HAPPY_PLACE_DETAIL, list[position])
        //activity is started with requestcode
        activity.startActivityForResult(intent, requestCode)

        //notify any registered observes that the item  at position has changed
        notifyItemChanged(position)

    }

    fun removeAt(position: Int){
        val dbHandler = DataBaseHandler(context)
        val isDelete = dbHandler.deleteHappyPlace(list[position])
        if (isDelete > 0 ){
            list.removeAt(position)
            notifyItemRemoved(position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }



    private class MyViewHolder(view: View):RecyclerView.ViewHolder(view)

    interface OnClickListener{
        fun onClick (position: Int, model:HappyPlaceModel)
    }


}