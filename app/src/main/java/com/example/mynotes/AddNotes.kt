package com.example.mynotes

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*
import java.lang.Exception

class AddNotes : AppCompatActivity() {
    val dbTable="Notes"
    var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)



            try {
                var bundle: Bundle? =intent.extras
                id=bundle!!.getInt("ID",0)
                if(id!=0){
                    et1.setText(bundle.getString("heading").toString())
                    et2.setText(bundle.getString("Des").toString())
                }
            }catch (ex:Exception){}

    }




    fun buAdd(view: View){

        var DbManager=dbClass(this)

        var values=ContentValues()
        values.put("title",et1.text.toString())
        values.put("Description",et2.text.toString())

        if(id==0){
        val ID= DbManager.Insert(values)
        if(ID>0){
            Toast.makeText(this,"note added",Toast.LENGTH_SHORT).show()
            finish()
        }else{
            Toast.makeText(this,"can't add note !",Toast.LENGTH_SHORT).show()
        }
        }else{
            var selectionArgs= arrayOf(id.toString())
            val ID= DbManager.Update(values,"ID=?",selectionArgs)
            if(ID>0){
                Toast.makeText(this,"note added",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this,"can't add note !",Toast.LENGTH_SHORT).show()
            }
        }
    }
}