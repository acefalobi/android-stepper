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
package com.aceinteract.android.stepper.presentation.steps

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aceinteract.android.stepper.databinding.ColorChangeFragmentBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Fragment for holding and controlling views for the first step.
 */
@ExperimentalCoroutinesApi
class Step1Fragment : Fragment() {

    private lateinit var viewBinding: ColorChangeFragmentBinding

    private val viewModel: StepperViewModel by lazy { ViewModelProvider(requireActivity())[StepperViewModel::class.java] }

    private val onSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(
            seekBar: SeekBar?,
            progress: Int,
            fromUser: Boolean
        ) {
            viewBinding.run {
                viewModel.updateStepper(
                    viewModel.stepperSettings.value.copy(
                        textColor = Color.rgb(
                            seekRed.progress,
                            seekGreen.progress,
                            seekBlue.progress
                        )
                    )
                )
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

    /**
     * Setup view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = ColorChangeFragmentBinding.inflate(inflater, container, false).apply {
            setupUI()
        }
        return viewBinding.root
    }

    private fun ColorChangeFragmentBinding.setupUI() {
        val textColor = viewModel.stepperSettings.value.textColor
        textColor.run {
            seekRed.progress = red
            seekGreen.progress = green
            seekBlue.progress = blue
        }
        seekRed.setOnSeekBarChangeListener(onSeekBarChangeListener)
        seekGreen.setOnSeekBarChangeListener(onSeekBarChangeListener)
        seekBlue.setOnSeekBarChangeListener(onSeekBarChangeListener)
    }
}
