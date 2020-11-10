package com.example.happyplaces

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.activity.AddHappyPlaceActivity
import com.example.happyplaces.activity.HappyDetailActivity
import com.example.happyplaces.adapter.HappyPlaceAdapter
import com.example.happyplaces.database.DataBaseHandler
import com.example.happyplaces.model.HappyPlaceModel
import com.example.happyplaces.util.SwipeToDeleteCallBack
import com.example.happyplaces.util.SwipeToEditCallBack
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object{
        var ADD_HAPPY_ACTIVITY_REQUEST_CODE = 1
        var HAPPY_PLACE_DETAIL = "extra_place_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_add.setOnClickListener {
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivityForResult(intent, ADD_HAPPY_ACTIVITY_REQUEST_CODE)
        }
        //function untuk get data dari data base
        getHappyPlaceFromLocalDB()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_HAPPY_ACTIVITY_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                getHappyPlaceFromLocalDB()
            }else{
                Log.e("Activity", "called or Back Pressed")
            }
        }
    }

    private fun getHappyPlaceFromLocalDB() {
        //variable supaya database nya bisa kita gunakan di MainActivity
        val dbHandler= DataBaseHandler(this)
        //digunakan untuk menjalankan aksi get yang berasal dari databasehandler
        val getHappyPlaceList : ArrayList<HappyPlaceModel> = dbHandler.getHappyPlaceList()

        //sebuah kondisi ketika data itu ada
        if (getHappyPlaceList.size > 0){
            rv_place.visibility = View.VISIBLE
            tvnoRecord.visibility = View.GONE
            setupHappyPlaceRV(getHappyPlaceList)
            //sebuah kondisi ketika data itu kosong
        }else{
            rv_place.visibility = View.GONE
            tvnoRecord.visibility = View.VISIBLE
        }
    }

    //fucntion ini di gunakan untuk create recycleview di dalam mainactivity
    private fun setupHappyPlaceRV(happyPlaceList: ArrayList<HappyPlaceModel>) {
        //untuk mendeteksi data ketika ada perubahan seperti ada data baru yang masuk ke dalam recycleview
        rv_place.layoutManager= LinearLayoutManager(this)
        //buat trigger ketika ada data baru
        rv_place.setHasFixedSize(true)

        //untuk menjalankan adapter kita di dalam mainactivity sehingga recycleview bisa menjalankan dengan seharusnya
        val adapter = HappyPlaceAdapter(this,happyPlaceList)
        rv_place.adapter = adapter


        adapter.setOnClickListener(object : HappyPlaceAdapter.OnClickListener{
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent ( this@MainActivity, HappyDetailActivity::class.java)
                intent.putExtra(HAPPY_PLACE_DETAIL, model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallBack(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_place.adapter as HappyPlaceAdapter
                adapter.notifyEditItem(this@MainActivity, viewHolder.adapterPosition, ADD_HAPPY_ACTIVITY_REQUEST_CODE)

            }

        }

        val editItemTouch = ItemTouchHelper(editSwipeHandler)
        editItemTouch.attachToRecyclerView(rv_place)

        val deleteSwipe = object : SwipeToDeleteCallBack(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_place.adapter as HappyPlaceAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHappyPlaceFromLocalDB()
            }

        }

        val deleteItem = ItemTouchHelper(deleteSwipe)
        deleteItem.attachToRecyclerView(rv_place)

    }


}