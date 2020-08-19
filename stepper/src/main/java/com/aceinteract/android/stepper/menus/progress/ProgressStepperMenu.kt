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
package com.aceinteract.android.stepper.menus.progress

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.aceinteract.android.stepper.R
import com.aceinteract.android.stepper.menus.base.StepperMenu
import com.aceinteract.android.stepper.menus.base.StepperMenuItem

/**
 * Menu showing steps progress in a progress bar.
 */
class ProgressStepperMenu(
    context: Context,
    override var widgetColor: Int,
    override var iconSizeInPX: Int,
    override var textAppearance: Int,
    override var textColor: Int,
    override var textSizeInPX: Int?
) : StepperMenu(context, widgetColor, iconSizeInPX, textAppearance, textColor, textSizeInPX) {

    private var progressAnimator: ValueAnimator = ValueAnimator.ofFloat()

    private val progressBar = LayoutInflater.from(context)
        .inflate(R.layout.stepper_progress, this, false) as FrameLayout

    init {
        progressBar.id = View.generateViewId()

        (progressBar.layoutParams as LayoutParams).run {
            topToTop = id
            bottomToBottom = id
        }

        addView(progressBar)
    }

    override var currentStep: Int = 0

    override val menuItems: List<StepperMenuItem> get() = _menuItems

    private val _menuItems: ArrayList<ProgressStepperMenuItem> = arrayListOf()

    override fun updateUI() {
        val percentCompleted = ((currentStep + 1F) / _menuItems.size) * 100
        progressBar.findViewById<ProgressBar>(R.id.progress_stepper).run {
            progressTintList = ColorStateList.valueOf(widgetColor)
            progressAnimator.cancel()
            progressAnimator =
                ValueAnimator.ofFloat(progress.toFloat(), percentCompleted).setDuration(200)
            progressAnimator.run {
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    progress = (it.animatedValue as Float).toInt()
                }
                start()
            }
        }
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
    ): MenuItem = ProgressStepperMenuItem(itemId).apply {
        _menuItems.add(this)
        _menuItems.sortBy { it.order }
        updateUI()
    }
}
