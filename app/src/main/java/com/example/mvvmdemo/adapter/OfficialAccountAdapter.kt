package com.example.mvvmdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmdemo.data.OfficialAccount
import com.example.mvvmdemo.databinding.ItemOfficialAccountBinding

/**
 * 公众号适配器
 */
class OfficialAccountAdapter(
    private val onItemClick: (OfficialAccount) -> Unit
) : RecyclerView.Adapter<OfficialAccountAdapter.OfficialAccountViewHolder>() {

    private var officialAccounts: List<OfficialAccount> = emptyList()

    fun updateData(newAccounts: List<OfficialAccount>) {
        officialAccounts = newAccounts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfficialAccountViewHolder {
        val binding = ItemOfficialAccountBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OfficialAccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfficialAccountViewHolder, position: Int) {
        holder.bind(officialAccounts[position])
    }

    override fun getItemCount(): Int = officialAccounts.size

    inner class OfficialAccountViewHolder(
        private val binding: ItemOfficialAccountBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(account: OfficialAccount) {
            binding.apply {
                tvOfficialAccountName.text = account.name
                tvOfficialAccountDesc.text = account.desc.ifEmpty { "暂无描述" }
                tvArticleCount.text = "文章数量: ${account.articleList.size}"
                tvOfficialAccountId.text = "ID: ${account.id}"
                
                // 设置点击事件
                root.setOnClickListener {
                    onItemClick(account)
                }
            }
        }
    }
} 