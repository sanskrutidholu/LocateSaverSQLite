package com.example.locateonmap.user

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.locateonmap.R
import com.example.locateonmap.database.DBClass
import com.example.locateonmap.modelClass.UserHistoryModelClass

class UserLocationHistoryAdapter(context: Context, historyList: List<UserHistoryModelClass>) :
    RecyclerView.Adapter<UserLocationHistoryAdapter.MyViewHolder>() {

    private val ctx : Context = context
    private val historyList: ArrayList<UserHistoryModelClass> = historyList as ArrayList<UserHistoryModelClass>
    lateinit var dbClass : DBClass

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        dbClass = DBClass(ctx)
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val historyList : UserHistoryModelClass = historyList!![position]
        holder.address.text = historyList.address
        holder.date.text = historyList.date
        holder.time.text = historyList.time
        holder.lat.text = historyList.latitude.toString()
        holder.long.text = historyList.longitude.toString()

    }


    override fun getItemCount(): Int {
        return historyList!!.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var address : TextView = view.findViewById(R.id.address)
        var date : TextView = view.findViewById(R.id.date)
        var time : TextView = view.findViewById(R.id.time)
        val lat : TextView = view.findViewById(R.id.latitude)
        val long : TextView = view.findViewById(R.id.longitude)
        var button : ImageView = view.findViewById(R.id.delete)

        init {
            button.setOnClickListener(View.OnClickListener {
                val builder: AlertDialog.Builder = AlertDialog.Builder(ctx)
                builder.setTitle("Are you sure?")
                builder.setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        //dbClass = DBClass(ctx)
                        val position = adapterPosition
                        val item : UserHistoryModelClass = historyList.get(position)
                        dbClass.deleteUserLocation(item.userId)
                        removeItem(position)
                    })
                builder.setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialogInterface, i -> })
                val dialog: AlertDialog = builder.create()
                dialog.show()

            })
        }

    }

    private fun removeItem(position: Int) {
        historyList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, historyList.size)
    }


}

