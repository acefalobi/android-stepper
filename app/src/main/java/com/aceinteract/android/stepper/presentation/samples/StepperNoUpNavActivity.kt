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
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.aceinteract.android.stepper.R
import com.aceinteract.android.stepper.StepperNavListener
import com.aceinteract.android.stepper.StepperNavigationView
import com.aceinteract.android.stepper.models.StepperSettings
import com.aceinteract.android.stepper.presentation.steps.StepperViewModel
import kotlinx.android.synthetic.main.stepper_no_up_nav_activity.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ng.softcom.android.utils.ui.findNavControllerFromFragmentContainer
import ng.softcom.android.utils.ui.showToast

/**
 * Activity showing an sample usage of a stepper without up navigation.
 */
@ExperimentalCoroutinesApi
class StepperNoUpNavActivity : AppCompatActivity(), StepperNavListener {

    private val viewModel: StepperViewModel by lazy { ViewModelProvider(this)[StepperViewModel::class.java] }

    /**
     * Setup stepper and activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stepper_no_up_nav_activity)

        stepper.initializeStepper()

        setSupportActionBar(toolbar)

        // Setup Action bar for title with top-level destinations.
        setupActionBarWithNavController(
            findNavControllerFromFragmentContainer(R.id.frame_stepper),
            AppBarConfiguration.Builder(
                R.id.step_1_dest,
                R.id.step_2_dest,
                R.id.step_3_dest,
                R.id.step_4_dest
            ).build()
        )

        button_previous.setOnClickListener {
            stepper.goToPreviousStep()
        }

        button_next.setOnClickListener {
            stepper.goToNextStep()
        }

        collectStateFlow()
    }

    private fun StepperNavigationView.initializeStepper() {
        viewModel.updateStepper(
            StepperSettings(
                widgetColor,
                textColor,
                textSize,
                iconSize
            )
        )

        stepperNavListener = this@StepperNoUpNavActivity
        setupWithNavController(findNavControllerFromFragmentContainer(R.id.frame_stepper))
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
        showToast("Step changed to: $step")

        button_previous.isVisible = step != 0

        if (step == 3) {
            button_next.setImageResource(R.drawable.ic_check)
        } else {
            button_next.setImageResource(R.drawable.ic_right)
        }
    }

    override fun onCompleted() {
        showToast("Stepper completed")
    }

}
