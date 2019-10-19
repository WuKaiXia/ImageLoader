package com.wk.imageloader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.wk.imageloaderlibrary.ImageLoader
import com.wk.imageloaderlibrary.ImageLoaderConfig

/**
 *Time: 2019-10-19
 *Author: wukai
 *Description:
 */
class ImgAdapter(
    var context: Context,
    var mData: List<String>,
    var layoutId: Int = R.layout.item_img
    ) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(layoutId, null)
        var viewHolder: ViewHolder
        if (convertView == null) {
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        ImageLoader.with(context)
            .initConfig(ImageLoaderConfig.Builder().setError(R.drawable.ic_launcher_background).build())
            .displayImage(mData[position], viewHolder.imageView)

        return view
    }

    override fun getItem(position: Int): String {
        return if (mData.isNullOrEmpty()) "" else mData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return if (mData.isNullOrEmpty()) 0 else mData.size
    }


    inner class ViewHolder(view: View) {
        var imageView: ImageView = view.findViewById(R.id.ivImg)
    }
}