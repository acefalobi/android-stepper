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
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import com.aceinteract.android.stepper.R
import com.aceinteract.android.stepper.menus.base.StepperMenu
import com.aceinteract.android.stepper.menus.base.StepperMenuItem
import kotlin.math.max

/**
 * Menu showing steps in numbered tab mode.
 */
class TabNumberedStepperMenu(
    context: Context,
    override var widgetColor: Int,
    override var iconSizeInPX: Int,
    override var textAppearance: Int,
    override var textColor: Int,
    override var textSizeInPX: Int?
) : StepperMenu(context, widgetColor, iconSizeInPX, textAppearance, textColor, textSizeInPX) {

    override var currentStep: Int = 0

    override val menuItems: List<StepperMenuItem> get() = _menuItems

    private val _menuItems: ArrayList<TabStepperMenuItem> = arrayListOf()

    override fun updateUI() {
        _menuItems.forEachIndexed { index, item ->
            val labelView = item.labelView.apply {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                    setTextAppearance(context, textAppearance)
                else
                    setTextAppearance(textAppearance)
                setTextColor(textColor)
                textSizeInPX?.let { setTextSize(TypedValue.COMPLEX_UNIT_PX, it.toFloat()) }
            }
            val iconView = item.iconView.apply {
                findViewById<TextView>(R.id.text_inner).run {
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, iconSizeInPX / 2F)
                    text = (index + 1).toString()
                }
                layoutParams = LayoutParams(iconSizeInPX, iconSizeInPX).apply {
                    startToStart = labelView.id
                    endToEnd = labelView.id
                    topToTop = this@TabNumberedStepperMenu.id
                }
            }
            val connectorView = item.connectorView?.apply {
                setBackgroundColor(widgetColor)
            }

            when {
                index == currentStep -> {
                    iconView.findViewById<AppCompatImageView>(R.id.icon_outer).setImageDrawable(ColorDrawable(widgetColor))
                    iconView.findViewById<AppCompatImageView>(R.id.icon_inner).isVisible = false
                    iconView.findViewById<TextView>(R.id.text_inner).run {
                        isVisible = true
                        setTextColor(Color.WHITE)
                    }
                    labelView.alpha = 1F
                    connectorView?.alpha = 1F
                }
                index < currentStep -> {
                    iconView.findViewById<AppCompatImageView>(R.id.icon_outer).setImageDrawable(ColorDrawable(widgetColor))
                    iconView.findViewById<AppCompatImageView>(R.id.icon_inner).isVisible = true
                    iconView.findViewById<TextView>(R.id.text_inner).isVisible = false
                    labelView.alpha = 1F
                    connectorView?.alpha = 1F
                }
                index > currentStep -> {
                    iconView.findViewById<AppCompatImageView>(R.id.icon_outer).setImageDrawable(ColorDrawable(Color.WHITE))
                    iconView.findViewById<AppCompatImageView>(R.id.icon_inner).isVisible = false
                    iconView.findViewById<TextView>(R.id.text_inner).run {
                        isVisible = true
                        setTextColor(widgetColor)
                    }
                    labelView.alpha = 0.45F
                    connectorView?.alpha = 0.25F
                }
            }
        }
    }

    private fun addItemView(
        groupId: Int,
        itemId: Int,
        order: Int,
        title: CharSequence?
    ): TabStepperMenuItem {
        val iconView = LayoutInflater.from(context).inflate(
            R.layout.stepper_tab_item_number_icon,
            this,
            false
        ) as FrameLayout
        val labelView = LayoutInflater.from(context).inflate(
            R.layout.stepper_tab_item_label,
            this,
            false
        ) as TextView
        var connectorView: View? = null

        iconView.id = View.generateViewId()
        labelView.id = View.generateViewId()

        addView(iconView)
        addView(labelView)

        (labelView.layoutParams as LayoutParams).run {
            endToEnd = id
            topToBottom = iconView.id
        }

        if (_menuItems.isNotEmpty()) {
            val lastIconView = _menuItems.last().iconView
            val lastLabelView = _menuItems.last().labelView

            (lastLabelView.layoutParams as LayoutParams).run {
                endToStart = labelView.id
                endToEnd = -1
            }

            (labelView.layoutParams as LayoutParams).startToEnd = lastLabelView.id

            connectorView = View(context).apply {
                id = View.generateViewId()
                layoutParams = LayoutParams(0, 3 * resources.displayMetrics.density.toInt())
            }

            addView(connectorView)

            (connectorView.layoutParams as LayoutParams).run {
                topToTop = iconView.id
                bottomToBottom = iconView.id
                startToEnd = lastIconView.id
                endToStart = iconView.id
            }
        } else {
            (labelView.layoutParams as LayoutParams).startToStart = id
        }

        iconView.layoutParams = LayoutParams(iconSizeInPX, iconSizeInPX).apply {
            startToStart = labelView.id
            endToEnd = labelView.id
            topToTop = id
        }

        val maxWidth = max(
            _menuItems.maxByOrNull { it.labelView.width }?.labelView?.width ?: 0,
            labelView.width
        )

        _menuItems.forEach {
            it.labelView.layoutParams.width = maxWidth
        }

        labelView.run {
            text = title
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                setTextAppearance(context, textAppearance)
            else
                setTextAppearance(textAppearance)
            setTextColor(textColor)
            textSizeInPX?.let { setTextSize(TypedValue.COMPLEX_UNIT_PX, it.toFloat()) }
            layoutParams.width = maxWidth
        }

        return TabStepperMenuItem(
            context,
            itemId,
            groupId,
            order,
            iconView,
            labelView,
            connectorView
        )
    }

    /**
     * Remove all menu items.
     */
    override fun clear() {
        _menuItems.clear()
        updateUI()
    }

    /**
     * Remove menu item with item id [id].
     */
    override fun removeItem(id: Int) {
        _menuItems.removeAll { it.itemId == id }
        updateUI()
    }

    /**
     * Remove menu items associated with [groupId].
     */
    override fun removeGroup(groupId: Int) {
        _menuItems.removeAll { groupId == it.groupId }
        updateUI()
    }

    /**
     * Add a new menu item.
     */
    override fun add(
        groupId: Int,
        itemId: Int,
        order: Int,
        title: CharSequence?
    ): MenuItem = addItemView(groupId, itemId, order, title).apply {
        _menuItems.add(this)
        _menuItems.sortBy { it.order }
        updateUI()
    }
}
