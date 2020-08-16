/*
 * Copyright 2020 Ayomide Falobi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aceinteract.android.stepper.presentation.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aceinteract.android.stepper.databinding.ActivityItemBinding
import com.aceinteract.android.stepper.models.ActivityItem

/**
 * RecyclerView list adapter for activity items.
 */
class ActivityListAdapter :
    ListAdapter<ActivityItem<*>, ActivityListAdapter.ViewHolder>(ActivityDiffCallback()) {

    /**
     * Inflate view binding into the view holder
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        ActivityItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    /**
     * Bind the data to the view holder.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * Holds view for recycling.
     */
    inner class ViewHolder(private val binding: ActivityItemBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Setup item view.
         *
         * @param item
         */
        fun <T> bind(item: ActivityItem<T>) {
            binding.run {
                textName.text = item.name
                root.setOnClickListener {
                    root.context.startActivity(Intent(root.context, item.activityClass))
                }
            }
        }
    }

    /**
     * Callback for checking difference between two activity items.
     */
    class ActivityDiffCallback : DiffUtil.ItemCallback<ActivityItem<*>>() {

        /**
         * Check if the items are the same.
         */
        override fun areItemsTheSame(
            oldItem: ActivityItem<*>,
            newItem: ActivityItem<*>
        ): Boolean = oldItem == newItem

        /**
         * Check if items contain the same content.
         */
        override fun areContentsTheSame(
            oldItem: ActivityItem<*>,
            newItem: ActivityItem<*>
        ): Boolean = oldItem.activityClass.name == newItem.activityClass.name
    }
}
