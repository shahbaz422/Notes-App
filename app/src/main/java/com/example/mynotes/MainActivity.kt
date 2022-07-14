package com.example.mynotes

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tickets.*
import kotlinx.android.synthetic.main.tickets.view.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var listnotes=ArrayList<notes>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this,"add your note",Toast.LENGTH_SHORT).show()


        //load from database.
        LoadQuery("%")


    }



    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }



    @SuppressLint("Range")
    fun LoadQuery(title:String){



        var DbManager=dbClass(this)
        val projection= arrayOf("ID","title","Description")
        val selectionArgs= arrayOf(title)
        val cursor=DbManager.Query(projection,"title like ?",selectionArgs,"title")
        listnotes.clear()
        if(cursor.moveToFirst()){
            do {
                val ID=cursor.getInt(cursor.getColumnIndex("ID"))
                val title=cursor.getString(cursor.getColumnIndex("title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"))
                listnotes.add(notes(ID,title,Description))
            }while (cursor.moveToNext())
        }
        var MynotesAdapter=mynotesAdapter(this,listnotes)
        tvNotes.adapter=MynotesAdapter

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)

        val sv=menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext,query,Toast.LENGTH_LONG).show()
                LoadQuery("%"+ query +"%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })


        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {


            when(item.itemId) {
                R.id.addMenu -> {
                    //go to add page
                    var intent = Intent(this, AddNotes::class.java)
                    startActivity(intent)
                }
            }

        return  super.onOptionsItemSelected(item)
    }



    inner class mynotesAdapter:BaseAdapter{
        var listnoteadapter=ArrayList<notes>()
        var context:Context?=null
        constructor(context:Context,listnoteadapter:ArrayList<notes>):super(){
            this.context=context
            this.listnoteadapter=listnoteadapter

        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


            //to connect the code and the view define something like view.
            var myView=layoutInflater.inflate(R.layout.tickets,null)
            //now get the element from original arraylist
            var mynote=listnoteadapter[position]
            myView.tvTitle.text=mynote.heading
            myView.tvDes.text=mynote.des
          //  myView.dateText.text=getDate()//TODO
            myView.bu1.setOnClickListener(View.OnClickListener {
                var DbManager=dbClass(this.context!!)
                val selectionArgs= arrayOf(mynote.noteId.toString())
                DbManager.Delete("ID=?",selectionArgs)
                LoadQuery("%")
                Toast.makeText(this.context,"note deleted !",Toast.LENGTH_SHORT).show()

            })
            myView.bu2.setOnClickListener(View.OnClickListener {
               GoToUpdate(mynote)
                Toast.makeText(this.context,"updating your note",Toast.LENGTH_SHORT).show()
            })

            return myView
        }

        override fun getCount(): Int {
            return listnoteadapter.size
        }

        override fun getItem(position: Int): Any {
            return listnoteadapter[position]
        }

        override fun getItemId(position: Int): Long {
           return position.toLong()
        }


    }

    fun GoToUpdate(note:notes){
        var intent = Intent(this, AddNotes::class.java)
        intent.putExtra("ID",note.noteId)
        intent.putExtra("heading",note.heading)
        intent.putExtra("Des",note.des)
        startActivity(intent)

    }
}