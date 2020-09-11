package com.mp2.bhojanam.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mp2.bhojanam.R
import com.mp2.bhojanam.database.StudentEntity
import com.squareup.picasso.Picasso

class StudentsAdapter(val context: Context,val data:List<StudentEntity>,val onStudentSelected: OnStudentSelected): RecyclerView.Adapter<StudentsAdapter.StudentsViewHolder>() {

    class StudentsViewHolder(view:View):RecyclerView.ViewHolder(view){
        val txtName = view.findViewById<TextView>(R.id.txtSDName)
        val txtSchool = view.findViewById<TextView>(R.id.txtSDSchool)
        val txtClass = view.findViewById<TextView>(R.id.txtSDClass)
        val txtIncharge = view.findViewById<TextView>(R.id.txtSDIncharge)
        val llSD = view.findViewById<LinearLayout>(R.id.llSD)
        val imgSD = view.findViewById<ImageView>(R.id.imgSD)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_student,parent,false)

        return StudentsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref), Context.MODE_PRIVATE)

        Picasso.get().load(data[position].image).error(R.drawable.avatar).into(holder.imgSD)
        holder.txtName.text = data[position].name
        holder.txtClass.text = "Class : ${data[position].stdClass}"
        holder.txtSchool.text = "School : ${sharedPreferences.getString("school","")}"
        holder.txtIncharge.text = "In-charge : ${sharedPreferences.getString("name","")}"

        holder.llSD.setOnClickListener {
            onStudentSelected.onStudentSelected(data[position])
        }
    }

}

public interface OnStudentSelected{
    fun onStudentSelected(studentEntity: StudentEntity)
}