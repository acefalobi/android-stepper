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

    private var animationStartTime = 0L
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
        stepper.setupWithoutNavController()
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
            if (stepper.currentStep == 0 && System.currentTimeMillis() - animationStartTime < 300) {
                return@setOnClickListener
            }
            animationStartTime = System.currentTimeMillis()
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
        when (step > 0) {
            true -> button_previous.show()
            false -> button_previous.hide()
        }
    }

    private fun setupNextButtonImage(step: Int) {
        when (step == 3) {
            true -> button_next.setImageResource(R.drawable.ic_check)
            false -> button_next.setImageResource(R.drawable.ic_right)
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