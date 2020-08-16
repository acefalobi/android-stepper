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
package com.aceinteract.android.stepper.presentation.samples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.aceinteract.android.stepper.R
import com.aceinteract.android.stepper.StepperNavListener
import com.aceinteract.android.stepper.StepperNavigation
import com.aceinteract.android.stepper.models.StepperSettings
import com.aceinteract.android.stepper.presentation.steps.StepperViewModel
import kotlinx.android.synthetic.main.tab_stepper_activity.button_next
import kotlinx.android.synthetic.main.tab_stepper_activity.stepper
import kotlinx.android.synthetic.main.tab_stepper_activity.toolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ng.softcom.android.utils.ui.showToast

/**
 * Activity showing an sample usage of custom transition animations.
 */
@ExperimentalCoroutinesApi
class FadeAnimStepperActivity : AppCompatActivity(), StepperNavListener {

    private val viewModel: StepperViewModel by lazy { ViewModelProvider(this)[StepperViewModel::class.java] }

    /**
     * Setup stepper and activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_stepper_activity)

        stepper.initializeStepper()

        setSupportActionBar(toolbar)

        // Setup Action bar for title and up navigation.
        setupActionBarWithNavController(
            findNavController(R.id.frame_stepper)
        )

        button_next.setOnClickListener {
            stepper.goToNextStep()
        }

        collectStateFlow()
    }

    private fun StepperNavigation.initializeStepper() {
        viewModel.updateStepper(
            StepperSettings(
                widgetColor,
                textColor,
                textSize,
                iconSize
            )
        )

        stepperNavListener = this@FadeAnimStepperActivity
        setupWithNavController(findNavController(R.id.frame_stepper)) {
            enter = android.R.anim.fade_in
            exit = android.R.anim.fade_out
            popEnter = android.R.anim.fade_in
            popExit = android.R.anim.fade_out
        }
    }

    private fun collectStateFlow() {
        viewModel.stepperSettings.onEach {
            stepper.widgetColor = it.iconColor
            stepper.textColor = it.textColor
            stepper.textSize = it.textSize
            stepper.iconSize = it.iconSize
        }.launchIn(lifecycleScope)
    }

    override fun onStepChanged(step: Int) {
        showToast("Step changed to: ${step + 1}")
        if (step == 3) {
            button_next.setImageResource(R.drawable.ic_check)
        } else {
            button_next.setImageResource(R.drawable.ic_right)
        }
    }

    override fun onCompleted() {
        showToast("Stepper completed")
    }

    /**
     * Use navigation controller to navigate up.
     */
    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.frame_stepper).navigateUp()

    /**
     * Navigate up when the back button is pressed..
     */
    override fun onBackPressed() {
        if (stepper.currentStep == 0) {
            super.onBackPressed()
        } else {
            findNavController(R.id.frame_stepper).navigateUp()
        }
    }
}
