package com.example.bill.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bill.R
import com.example.bill.databinding.RecordItemBinding
import com.example.bill.db.Record

class RecordAdapter(private val recordList: List<Record>) :
    RecyclerView.Adapter<RecordAdapter.ViewHolder>() {
    inner class ViewHolder(binding: RecordItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val icon: ImageView = binding.icon
        val money: TextView = binding.money
        val time: TextView = binding.time
        val purpose: TextView = binding.purpose

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = recordList[position]
        if (record.isPay==0)
            holder.icon.setImageResource(R.mipmap.ic_pay)
        else
            holder.icon.setImageResource(R.mipmap.ic_income)

        //holder.icon.setImageResource(R.mipmap.photo)
        holder.money.text = "金额是：${record.money}"
        holder.time.text = "${record.time}"
        holder.purpose.text = "${record.purpose}"
    }

    override fun getItemCount() = recordList.size
}