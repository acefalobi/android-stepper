/*
 * Copyright 2021 Ayomide Falobi.
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
package com.aceinteract.android.stepper.presentation.samples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.aceinteract.android.stepper.R
import com.aceinteract.android.stepper.StepperNavListener
import kotlinx.android.synthetic.main.tab_stepper_without_navigation_components_activity.*
import ng.softcom.android.utils.ui.showToast

/**
 * Activity showing an sample usage of a tab stepper without navigation components.
 */
class TabStepperWithoutNavigationComponentsActivity : AppCompatActivity(), StepperNavListener {

    private val menuTitles = arrayListOf<String>()

    /**
     * Setup stepper and activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_stepper_without_navigation_components_activity)

        initializeStepper()
        initializeToolbar()
        initializeButtons()
    }

    private fun initializeStepper() {
        stepper.stepperNavListener = this@TabStepperWithoutNavigationComponentsActivity
    }

    private fun initializeToolbar() {
        for (i in 0 until stepper.menu.size()) {
            menuTitles.add(stepper.menu.getItem(i).title.toString())
        }

        toolbar.title = menuTitles[0]
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initializeButtons() {
        button_next.setOnClickListener {
            stepper.goToNextStep()
        }

        button_previous.setOnClickListener {
            stepper.goToPreviousStep()
        }
    }

    override fun onStepChanged(step: Int) {
        showToast("Step changed to: $step")
        setupToolbarTitle(step)
        setupPreviousButtonVisibility(step)
        setupNextButtonImage(step)
        setupViewVisibility(step)
    }

    private fun setupToolbarTitle(step: Int) {
        toolbar.title = menuTitles[step]
    }

    private fun setupPreviousButtonVisibility(step: Int) {
        button_previous.isVisible = step > 0
    }

    private fun setupNextButtonImage(step: Int) {
        if (step == 3) {
            button_next.setImageResource(R.drawable.ic_check)
        } else {
            button_next.setImageResource(R.drawable.ic_right)
        }
    }

    private fun setupViewVisibility(step: Int) {
        val isVisible = step % 2 == 0

        layout_size_change.isVisible = isVisible
        layout_color_change.isVisible = !isVisible
    }

    override fun onCompleted() {
        showToast("Stepper completed")
    }
}
