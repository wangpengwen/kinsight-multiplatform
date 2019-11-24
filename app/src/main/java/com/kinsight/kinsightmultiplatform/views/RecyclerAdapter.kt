package com.kinsight.kinsightmultiplatform.views

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kinsight.kinsightmultiplatform.R
import com.kinsight.kinsightmultiplatform.extensions.inflate
import com.kinsight.kinsightmultiplatform.models.IdeaModel
import com.kinsight.kinsightmultiplatform.resources.Strings
import kotlinx.android.synthetic.main.idea_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class RecyclerAdapter (private val ideas: List<IdeaModel>, val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerAdapter.IdeaHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
           IdeaHolder {
        val inflatedView = parent.inflate(R.layout.idea_item, false)
        return IdeaHolder(inflatedView)
    }

    override fun getItemCount(): Int = ideas.size

    override fun onBindViewHolder(holder: IdeaHolder, position: Int) {
        val itemIdea = ideas[position]
        holder.bindIdea(itemIdea, itemClickListener)
    }

    inner class IdeaHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var idea: IdeaModel? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClicked(idea!!, view)
            Log.d("Ideas RecyclerView", "Idea Clicked")
        }

        fun bindIdea(idea: IdeaModel, clickListener: OnItemClickListener) {
            val df = DecimalFormat("#0.00")
            df.roundingMode = RoundingMode.CEILING
            val alpha = df.format(idea.alpha)
            val psi = df.format(idea.benchMarkPerformance)
            this.idea = idea
            if (idea.alpha > 4){
                view.ideaImage2.setImageResource(R.drawable.ic_fish_green)
            }
            if (idea.alpha > 3 && idea.alpha < 4){
                view.ideaImage2.setImageResource(R.drawable.ic_fish_yellow)
            }
            if (idea.alpha < 1){
                view.ideaImage2.setImageResource(R.drawable.ic_fish_pale_yellow)
            }
            view.nameText.text = idea.securityTicker

            view.ideaAlpha.text =  alpha
            view.ideaTargetPrice.text = idea.securityName

            view.ideaCreatedBy.text ="By: ${idea.createdBy}"
            view.ideaPsi.text = Strings.psi + ": " + psi
            itemView.setOnClickListener {
                clickListener.onItemClicked(idea, view)
            }
        }

    }
}

interface OnItemClickListener{
    fun onItemClicked(idea: IdeaModel, view: View)
}