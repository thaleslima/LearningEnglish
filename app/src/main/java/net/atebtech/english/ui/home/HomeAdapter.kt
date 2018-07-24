package net.atebtech.english.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_home.view.*
import net.atebtech.english.R
import net.atebtech.english.model.Category
import java.util.ArrayList

class HomeAdapter(private val itemClickedListener: (Category) -> Unit)
    : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val mDataSet: MutableList<Category> = ArrayList()

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Category
            itemClickedListener(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_home, parent, false)
        return ViewHolder(view)
    }

    fun replaceData(dataSet: List<Category>) {
        setList(dataSet)
        notifyDataSetChanged()
    }

    private fun setList(dataSet: List<Category>) {
        mDataSet.clear()
        mDataSet.addAll(dataSet)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataSet[position]
        //holder.mIdView.text = item.id
        //holder.mContentView.text = item.content

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mDataSet.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val description: TextView = mView.home_description_view
        val descriptionAux: TextView = mView.home_description_aux_view

        override fun toString(): String {
            return super.toString() + " '" + description.text + "'"
        }
    }
}
