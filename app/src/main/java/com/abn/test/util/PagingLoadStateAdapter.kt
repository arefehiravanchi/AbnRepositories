package com.abn.test.util

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abn.test.R

class PagingLoadStateAdapter :
    LoadStateAdapter<PagingLoadStateAdapter.NetworkStateItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        NetworkStateItemViewHolder(parent)

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    class NetworkStateItemViewHolder(
        parent: ViewGroup,
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_network_state, parent, false)
    ) {
        private val progressBar = itemView.findViewById<ProgressBar>(R.id.loading_pb)
        private val errorText = itemView.findViewById<TextView>(R.id.error_txt)
        fun bind(loadState: LoadState) {
            progressBar.isVisible = loadState is LoadState.Loading
            errorText.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
            errorText.text = (loadState as? LoadState.Error)?.error?.message
        }
    }
}
