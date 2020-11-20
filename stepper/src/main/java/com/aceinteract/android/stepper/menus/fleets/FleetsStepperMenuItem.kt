package com.aceinteract.android.stepper.menus.fleets

import android.animation.ValueAnimator
import android.view.MenuItem
import android.widget.FrameLayout
import com.aceinteract.android.stepper.menus.base.StepperMenuItem

/**
 * Menu item for [FleetsStepperMenu].
 *
 * @property view the view containing the progress.
 * @property progressAnimator a value animator for updating the progress.
 */
class FleetsStepperMenuItem(
    id: Int,
    groupId: Int = 0,
    order: Int = 0,
    val view: FrameLayout,
    var progressAnimator: ValueAnimator
) : StepperMenuItem(id, groupId, order) {

    /**
     * Do nothing since views don't have titles.
     */
    override fun setTitle(title: CharSequence?): MenuItem = this

    /**
     * Do nothing since views don't have titles.
     */
    override fun setTitle(title: Int): MenuItem = this

    /**
     * Returns an empty string since views don't have titles.
     */
    override fun getTitle(): CharSequence = ""

    /**
     * Do nothing since views don't have titles.
     */
    override fun getTitleCondensed(): CharSequence = ""
}
