package com.ss.restaloca.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.ss.restaloca.databinding.RowSpinnerItemBinding

class MapSpinnerAdapter(context: Context, mapBusinessType: List<MapBusinessType>) :
    ArrayAdapter<MapBusinessType>(context, 0, mapBusinessType) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    private fun initView(position: Int, parent: ViewGroup): View {
        val binding = RowSpinnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val type = getItem(position)!!
        binding.name.text = type.name
        binding.image.setImageResource(type.image)

        return binding.root
    }
}