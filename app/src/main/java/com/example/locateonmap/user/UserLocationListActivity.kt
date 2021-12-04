package com.example.locateonmap.user

import android.app.AlertDialog
import android.content.DialogInterface
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locateonmap.R
import com.example.locateonmap.database.DBClass
import com.example.locateonmap.modelClass.UserHistoryModelClass
import java.util.*


class UserLocationListActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var dbClass: DBClass? = null
    private val list : MutableList<UserHistoryModelClass> = ArrayList<UserHistoryModelClass>()
    private var adapter: UserLocationHistoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_location_history)

        dbClass = DBClass(this)
        recyclerView = findViewById(R.id.idRVHistory)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.setHasFixedSize(true)
        loadData()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        deleteAllData()
        return true
    }


   // @SuppressLint("Range")
    private fun loadData() {
       val cursor: Cursor? = dbClass!!.readAllUserLocationHistory()
       if (cursor!!.count == 0) {
            Toast.makeText(this, "No Data to show", Toast.LENGTH_SHORT).show()
        } else {
            while (cursor.moveToNext()) {
                list.add(
                    UserHistoryModelClass(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
                )
                )
            }
        }
        cursor.close()
        adapter = UserLocationHistoryAdapter(this, list)
        recyclerView!!.adapter = adapter
    }

    private fun deleteAllData() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure, to delete all data ?")
        builder.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialogInterface, i ->
                dbClass!!.deleteAllUserLocationHistory()
                recreate()

            })
        builder.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialogInterface, i -> })
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }



}