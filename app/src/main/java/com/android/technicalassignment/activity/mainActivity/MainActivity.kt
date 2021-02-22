package com.android.technicalassignment.activity.mainActivity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.technicalassignment.R
import com.android.technicalassignment.activity.mainActivity.model.PictureModelClass
import com.android.technicalassignment.api.APIService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var modelClassList: ArrayList<PictureModelClass?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        modelClassList = ArrayList()

        APIService.invoke().getData().enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val str: String? = response.body()?.string()
                    var jsonArray: JSONArray = JSONArray(str)
                    for (i in 0 until jsonArray.length()) {

                        var modelClass: PictureModelClass? = PictureModelClass(
                            (jsonArray[i] as JSONObject).get("id").toString(),
                            (jsonArray[i] as JSONObject).get("author").toString()
                        )

                        modelClassList.add(modelClass)
                        recyclerView.adapter = PictureRvAdapter(modelClassList, this@MainActivity)
                        pb.visibility = View.GONE
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

        })
    }


    class PictureRvAdapter(
        private var modelClassList: ArrayList<PictureModelClass?>,
        private var context: Context
    ) :
        RecyclerView.Adapter<PictureRvAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.picture_item, parent, false)
            )
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            Glide.with(context)
                .load("https://picsum.photos/300/300?image=" + modelClassList[position]?.id)
                .thumbnail()
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.imageView)
            holder.name.text = modelClassList[position]?.auther
        }

        override fun getItemCount(): Int = modelClassList.size

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var imageView: ImageView = itemView.findViewById(R.id.imageView)
            var name: TextView = itemView.findViewById(R.id.tvName)
        }

    }

}