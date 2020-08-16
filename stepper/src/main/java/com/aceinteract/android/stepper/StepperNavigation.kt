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
package com.aceinteract.android.stepper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MenuInflater
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.core.content.withStyledAttributes
import androidx.core.view.updatePadding
import androidx.navigation.AnimBuilder
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.aceinteract.android.stepper.menus.base.StepperMenu
import com.aceinteract.android.stepper.menus.progress.ProgressStepperMenu
import com.aceinteract.android.stepper.menus.tab.TabNumberedStepperMenu
import com.aceinteract.android.stepper.menus.tab.TabStepperMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import ng.softcom.android.utils.ui.getColorCompat

/**
 * Stepper Navigation for creating a wizard-like step-through user interface that uses a
 * [NavController] for navigation and a menu for displaying the steps similar to the implementation
 * done in the [BottomNavigationView].
 *
 * @param context the context to initialize the view with.
 * @param attrs the set of XML attributes to initialize the view with.
 */
class StepperNavigation(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val defaultIconSize = 16 * resources.displayMetrics.scaledDensity.toInt()
    private val defaultWidgetColor = context.getColorCompat(R.color.color_stepper_default)
    private val defaultTextColor = Color.BLACK
    private val defaultTextSize = 18 * resources.displayMetrics.density.toInt()
    private val defaultType = context.getString(R.string.stepper_type_tab)

    /**
     * The menu that displays the steps.
     */
    private lateinit var menu: StepperMenu

    /**
     * The color to use for widgets.
     */
    var widgetColor: Int
        set(value) {
            menu.widgetColor = value
            menu.updateUI()
        }
        get() = menu.widgetColor

    /**
     * The color to use for labels.
     */
    var textColor: Int
        set(value) {
            menu.textColor = value
            menu.updateUI()
        }
        get() = menu.textColor

    /**
     * The size of the label (in pixels).
     */
    var textSize: Int
        set(value) {
            menu.textSizeInPX = value
            menu.updateUI()
        }
        get() = menu.textSizeInPX

    /**
     * The size of the icon (in pixels).
     */
    var iconSize: Int
        set(value) {
            menu.iconSizeInPX = value
            menu.updateUI()
        }
        get() = menu.iconSizeInPX

    private var onStepChanged: ((Int) -> Unit) = {
        stepperNavListener?.onStepChanged(it)
        if (it > menu.size() - 1) stepperNavListener?.onCompleted()
    }

    /**
     * The listener checking for updates to the navigation.
     */
    var stepperNavListener: StepperNavListener? = null

    /**
     * The 0-indexed current step that shadows the current step from [menu].
     */
    val currentStep get() = menu.currentStep

    init {
        context.withStyledAttributes(attrs, R.styleable.StepperNavigation, 0) {
            val widgetColorAttr = if (hasValue(R.styleable.StepperNavigation_widgetColor)) {
                getColor(R.styleable.StepperNavigation_widgetColor, defaultWidgetColor)
            } else defaultWidgetColor

            val iconSizeAttr = if (hasValue(R.styleable.StepperNavigation_textSize)) {
                getDimensionPixelSize(R.styleable.StepperNavigation_iconSize, defaultIconSize)
            } else defaultIconSize

            val textColorAttr = if (hasValue(R.styleable.StepperNavigation_textColor)) {
                getColor(R.styleable.StepperNavigation_textColor, defaultTextColor)
            } else defaultTextColor

            val textSizeAttr = if (hasValue(R.styleable.StepperNavigation_textSize)) {
                getDimensionPixelSize(R.styleable.StepperNavigation_textSize, defaultTextSize)
            } else defaultTextSize

            val type = if (hasValue(R.styleable.StepperNavigation_type)) {
                getString(R.styleable.StepperNavigation_type)
            } else defaultType

            menu = when (type) {
                context.getString(R.string.stepper_type_tab) -> {
                    updatePadding(
                        16 * resources.displayMetrics.density.toInt(),
                        20 * resources.displayMetrics.density.toInt(),
                        16 * resources.displayMetrics.density.toInt(),
                        20 * resources.displayMetrics.density.toInt()
                    )
                    TabStepperMenu(
                        context,
                        widgetColorAttr,
                        iconSizeAttr,
                        textColorAttr,
                        textSizeAttr
                    )
                }
                context.getString(R.string.stepper_type_tab_numbered) -> {
                    updatePadding(
                        16 * resources.displayMetrics.density.toInt(),
                        20 * resources.displayMetrics.density.toInt(),
                        16 * resources.displayMetrics.density.toInt(),
                        20 * resources.displayMetrics.density.toInt()
                    )
                    TabNumberedStepperMenu(
                        context,
                        widgetColorAttr,
                        iconSizeAttr,
                        textColorAttr,
                        textSizeAttr
                    )
                }
                context.getString(R.string.stepper_type_progress) -> {
                    updatePadding(
                        40 * resources.displayMetrics.density.toInt(),
                        40 * resources.displayMetrics.density.toInt(),
                        40 * resources.displayMetrics.density.toInt(),
                        40 * resources.displayMetrics.density.toInt()
                    )
                    ProgressStepperMenu(
                        context,
                        widgetColorAttr,
                        iconSizeAttr,
                        textColorAttr,
                        textSizeAttr
                    )
                }
                else -> throw IllegalArgumentException("Invalid stepper type provided")
            }

            if (hasValue(R.styleable.StepperNavigation_items)) {
                val menuId = getResourceId(R.styleable.StepperNavigation_items, 0)
                MenuInflater(context).inflate(menuId, menu)
            }
        }

        menu.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(menu)
    }

    /**
     * Sets up the stepper to navigate steps using navigation components.
     *
     * @param navController navigation controller for navigating between destinations.
     */
    fun setupWithNavController(
        navController: NavController,
        navAnimBuilder: AnimBuilder.() -> Unit = {
            enter = R.anim.anim_slide_left_enter
            exit = R.anim.anim_slide_left_exit
            popEnter = R.anim.anim_slide_right_enter
            popExit = R.anim.anim_slide_right_exit
        }
    ) {
        onStepChanged = {
            menu.currentStep = it
            when {
                it < 1 -> {
                    navController.navigateWithAnimation(menu.getItem(0).itemId, navAnimBuilder)
                }
                it < menu.size() -> {
                    navController.navigateWithAnimation(
                        menu.getItem(currentStep).itemId,
                        navAnimBuilder
                    )
                }
                currentStep > menu.size() - 1 -> {
                    menu.currentStep = menu.size() - 1
                    stepperNavListener?.onCompleted()
                }
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            menu.selectMenuItem(destination.id)
            stepperNavListener?.onStepChanged(currentStep)
        }
    }

    private fun NavController.navigateWithAnimation(
        @IdRes resId: Int,
        navAnimBuilder: AnimBuilder.() -> Unit
    ) {
        val navOptions = navOptions {
            anim(navAnimBuilder)
        }
        navigate(resId, null, navOptions)
    }

    /**
     * Go to the step before the current one.
     */
    fun goToPreviousStep() {
        onStepChanged.invoke(menu.currentStep - 1)
    }

    /**
     * Go to the step after the current one
     */
    fun goToNextStep() {
        onStepChanged.invoke(menu.currentStep + 1)
    }
}
