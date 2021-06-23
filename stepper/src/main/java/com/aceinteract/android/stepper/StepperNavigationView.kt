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
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.core.content.withStyledAttributes
import androidx.navigation.AnimBuilder
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.aceinteract.android.stepper.menus.base.StepperMenu
import com.aceinteract.android.stepper.menus.fleets.FleetsStepperMenu
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
class StepperNavigationView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val defaultIconSize = 20 * resources.displayMetrics.scaledDensity.toInt()
    private val defaultWidgetColor = context.getColorCompat(R.color.color_stepper_default)
    private val defaultTextAppearance = android.R.style.TextAppearance
    private val defaultTextColor = Color.BLACK
    private val defaultFleetDuration = 5000L
    private val defaultTextSize = 16 * resources.displayMetrics.density.toInt()
    private val defaultType = StepperType.TAB

    /**
     * The menu that displays the steps.
     */
    lateinit var menu: StepperMenu

    /**
     * How long fleets should last in [R.string.stepper_type_fleets].
     */
    var fleetDuration: Long
        set(value) {
            if (menu is FleetsStepperMenu) {
                (menu as FleetsStepperMenu).fleetDuration = value
                menu.updateUI()
            }
        }
        get() = if (menu is FleetsStepperMenu) {
            (menu as FleetsStepperMenu).fleetDuration
        } else {
            defaultFleetDuration
        }

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
        set(@ColorInt value) {
            menu.textColor = value
            menu.updateUI()
        }
        get() = menu.textColor

    /**
     * The style to use for labels.
     */
    var textAppearance: Int
        set(value) {
            menu.textAppearance = value
            menu.updateUI()
        }
        get() = menu.textAppearance

    /**
     * The size of the label (in pixels).
     */
    var textSize: Int
        set(value) {
            menu.textSizeInPX = value
            menu.updateUI()
        }
        get() = menu.textSizeInPX ?: defaultTextSize

    /**
     * The size of the icon (in pixels).
     */
    var iconSize: Int
        set(value) {
            menu.iconSizeInPX = value
            menu.updateUI()
        }
        get() = menu.iconSizeInPX

    private var onStepChanged: (Int) -> Unit = {
        if (it == menu.size()) {
            stepperNavListener?.onCompleted()
        } else {
            menu.currentStep = it
            menu.updateUI()
            stepperNavListener?.onStepChanged(currentStep)
        }
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
        context.withStyledAttributes(attrs, R.styleable.StepperNavigationView, 0) {
            val fleetDurationAttr = getInteger(
                R.styleable.StepperNavigationView_stepperFleetDuration,
                defaultFleetDuration.toInt()
            ).toLong()
            val widgetColorAttr =
                getColor(R.styleable.StepperNavigationView_stepperWidgetColor, defaultWidgetColor)
            val iconSizeAttr = getDimensionPixelSize(
                R.styleable.StepperNavigationView_stepperIconSize,
                defaultIconSize
            )
            val textAppearanceAttr = getResourceId(
                R.styleable.StepperNavigationView_stepperTextAppearance,
                defaultTextAppearance
            )
            val textColorAttr =
                getColor(R.styleable.StepperNavigationView_stepperTextColor, defaultTextColor)
            val textSizeAttr = if (hasValue(R.styleable.StepperNavigationView_stepperTextSize)) {
                getDimensionPixelSize(
                    R.styleable.StepperNavigationView_stepperTextSize,
                    defaultTextSize
                )
            } else null

            val type = if (hasValue(R.styleable.StepperNavigationView_stepperType)) {
                StepperType.values().find {
                    it.identifier == getInt(
                        R.styleable.StepperNavigationView_stepperType,
                        StepperType.TAB.identifier
                    )
                }
            } else defaultType

            menu = when (type) {
                StepperType.TAB -> {
                    TabStepperMenu(
                        context,
                        widgetColorAttr,
                        iconSizeAttr,
                        textAppearanceAttr,
                        textColorAttr,
                        textSizeAttr
                    )
                }
                StepperType.TAB_NUMBERED -> {
                    TabNumberedStepperMenu(
                        context,
                        widgetColorAttr,
                        iconSizeAttr,
                        textAppearanceAttr,
                        textColorAttr,
                        textSizeAttr
                    )
                }
                StepperType.PROGRESS -> {
                    ProgressStepperMenu(
                        context,
                        widgetColorAttr,
                        iconSizeAttr,
                        textAppearanceAttr,
                        textColorAttr,
                        textSizeAttr
                    )
                }
                StepperType.FLEETS -> {
                    FleetsStepperMenu(
                        context,
                        widgetColorAttr,
                        iconSizeAttr,
                        textAppearanceAttr,
                        textColorAttr,
                        textSizeAttr,
                        fleetDurationAttr
                    )
                }
                else -> throw IllegalArgumentException("Invalid stepper type provided")
            }.apply {
                onStepChangedListener = { onStepChanged.invoke(it) }
            }

            if (hasValue(R.styleable.StepperNavigationView_stepperItems)) {
                val menuId = getResourceId(R.styleable.StepperNavigationView_stepperItems, 0)
                MenuInflater(context).inflate(menuId, menu)
            } else throw IllegalArgumentException("items attribute is required")
        }

        menu.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(menu)
    }

    /**
     * Sets up the stepper to navigate steps using navigation components.
     *
     * @param navController navigation controller for navigating between destinations.
     */
    @JvmOverloads
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
     * Go to the step at the index.
     */
    fun goToStep(index:Int) {
        if(index!=menu.currentStep)
        onStepChanged.invoke(index)
    }

    /**
     * Go to the step after the current one
     */
    fun goToNextStep() {
        onStepChanged.invoke(menu.currentStep + 1)
    }

    /**
     * Enumeration of the different type of steppers available.
     *
     * @property identifier the unique byte int for the stepper type.
     */
    enum class StepperType(val identifier: Int) {

        /**
         * Simple tabbed stepper.
         */
        TAB(0x01),

        /**
         * Tabbed stepper with step numbers.
         */
        TAB_NUMBERED(0x02),

        /**
         * Simple progress bar stepper.
         */
        PROGRESS(0x03),

        /**
         * Story-style stepper (Twitter fleets).
         */
        FLEETS(0x04)
    }
}
