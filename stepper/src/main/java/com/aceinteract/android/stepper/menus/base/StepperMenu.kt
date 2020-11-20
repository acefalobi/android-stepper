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
package com.aceinteract.android.stepper.menus.base

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.aceinteract.android.stepper.StepperNavigationView

/**
 * Menu showing steps in the [StepperNavigationView].
 *
 * @property widgetColor the color to use for widgets like icons and progress bars.
 * @property iconSizeInPX the size of icons in pixels.
 * @property textAppearance the text style of the label.
 * @property textColor the color to use for labels.
 * @property textSizeInPX the size of the label in pixels.
 */
abstract class StepperMenu constructor(
    context: Context,
    @ColorInt open var widgetColor: Int,
    open var iconSizeInPX: Int,
    @StyleRes open var textAppearance: Int,
    @ColorInt open var textColor: Int,
    open var textSizeInPX: Int?
) : ConstraintLayout(context), Menu {

    init {
        id = View.generateViewId()
    }

    /**
     * 0-indexed step at with the menu is at.
     */
    abstract var currentStep: Int

    /**
     * List of menu items in the menu.
     */
    protected abstract val menuItems: List<StepperMenuItem>

    /**
     * Called when the menu step is changed.
     */
    var onStepChangedListener: (Int) -> Unit = {}

    /**
     * Change the current step to the menu item with [itemId].
     *
     * @param itemId the id to search for.
     *
     * @return whether or not an item was found.
     */
    fun selectMenuItem(itemId: Int): Boolean {
        val index = menuItems.indexOfFirst { it.itemId == itemId }
        if (index == -1) {
            return false
        } else currentStep = index
        updateUI()
        return true
    }

    /**
     * Update the UI of the layout with the state of the menu items.
     */
    abstract fun updateUI()

    /**
     * Do nothing.
     */
    override fun setGroupCheckable(
        group: Int,
        checkable: Boolean,
        exclusive: Boolean
    ) {
    }

    /**
     * Actions are unsupported.
     */
    override fun performIdentifierAction(id: Int, flags: Int): Boolean = false

    /**
     * Do nothing.
     */
    override fun setGroupEnabled(group: Int, enabled: Boolean) {}

    /**
     * Return the menu item at the given index.
     */
    override fun getItem(index: Int): MenuItem = menuItems[index]

    /**
     * Shortcuts are unsupported.
     */
    override fun performShortcut(
        keyCode: Int,
        event: KeyEvent?,
        flags: Int
    ): Boolean = false

    /**
     * Do nothing.
     */
    override fun setGroupVisible(group: Int, visible: Boolean) {}

    /**
     * Add a new menu item.
     */
    override fun add(title: CharSequence?): MenuItem = add(0, 0, 0, title)

    /**
     * Add a new menu item.
     */
    override fun add(titleRes: Int): MenuItem = add(0, 0, 0, titleRes)

    /**
     * Add a new menu item.
     */
    override fun add(
        groupId: Int,
        itemId: Int,
        order: Int,
        titleRes: Int
    ): MenuItem = add(groupId, itemId, order, context.getString(titleRes))

    /**
     * Shortcuts are unsupported.
     */
    override fun isShortcutKey(
        keyCode: Int,
        event: KeyEvent?
    ): Boolean = false

    /**
     * Do nothing.
     */
    override fun setQwertyMode(isQwerty: Boolean) {}

    /**
     * Whether there are any items.
     */
    override fun hasVisibleItems(): Boolean = size() > 0

    /**
     * Throw an exception saying sub menu is not supported.
     */
    override fun addSubMenu(title: CharSequence?): SubMenu = throw UnsupportedOperationException(
        "Sub Menu is not supported."
    )

    /**
     * Throw an exception saying sub menu is not supported.
     */
    override fun addSubMenu(titleRes: Int): SubMenu = throw UnsupportedOperationException(
        "Sub Menu is not supported."
    )

    /**
     * Throw an exception saying sub menu is not supported.
     */
    override fun addSubMenu(
        groupId: Int,
        itemId: Int,
        order: Int,
        title: CharSequence?
    ): SubMenu = throw UnsupportedOperationException(
        "Sub Menu is not supported."
    )

    /**
     * Throw an exception saying sub menu is not supported.
     */
    override fun addSubMenu(
        groupId: Int,
        itemId: Int,
        order: Int,
        titleRes: Int
    ): SubMenu = throw UnsupportedOperationException(
        "Sub Menu is not supported."
    )

    /**
     * Intent Options are unsupported.
     */
    override fun addIntentOptions(
        groupId: Int,
        itemId: Int,
        order: Int,
        caller: ComponentName?,
        specifics: Array<out Intent>?,
        intent: Intent?,
        flags: Int,
        outSpecificItems: Array<out MenuItem>?
    ): Int = 0

    /**
     * Find a menu item that has the given [id].
     */
    override fun findItem(id: Int): MenuItem? = menuItems.find { it.itemId == id }

    /**
     * The number of menu items in the menu.
     */
    override fun size(): Int = menuItems.size

    /**
     * Do Nothing.
     */
    override fun close() {}
}
