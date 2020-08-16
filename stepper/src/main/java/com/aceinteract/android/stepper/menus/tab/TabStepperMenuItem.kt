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
package com.aceinteract.android.stepper.menus.tab

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.aceinteract.android.stepper.menus.base.StepperMenuItem

/**
 * Shared menu item for [TabStepperMenu] and [TabNumberedStepperMenu].
 *
 * @property context the context to get resources from.
 * @property iconView the view containing the item icon label.
 * @property labelView the view containing the item step label.
 * @property connectorView the view containing the item step connector.
 */
class TabStepperMenuItem(
    private val context: Context,
    id: Int,
    groupId: Int = 0,
    order: Int = 0,
    val iconView: FrameLayout,
    val labelView: TextView,
    val connectorView: View? = null
) : StepperMenuItem(id, groupId, order) {

    init {
        iconView.setOnClickListener {
            onClickListener.invoke(this)
        }
        labelView.setOnClickListener {
            onClickListener.invoke(this)
        }
    }

    /**
     * Change the label view text of the item to the [title].
     */
    override fun setTitle(title: CharSequence?): MenuItem {
        labelView.text = title
        return this
    }

    /**
     * Set the title of the Menu Item.
     */
    override fun setTitle(title: Int): MenuItem {
        setTitle(context.getString(title))
        return this
    }

    /**
     * Get the title from the label view.
     */
    override fun getTitle(): CharSequence = labelView.text

    /**
     * Get the text in the label view.
     */
    override fun getTitleCondensed(): CharSequence = labelView.text
}
