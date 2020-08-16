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
package com.aceinteract.android.stepper.models

import androidx.annotation.ColorInt
import com.aceinteract.android.stepper.StepperNavigationView

/**
 * Data class representing settings for a [StepperNavigationView] view
 *
 * @property iconColor the color of the icon in the view.
 * @property textColor the color of the text in the view.
 * @property textSize the size of the text in the view.
 * @property iconSize the size of the icon in the view.
 */
data class StepperSettings(
    @ColorInt var iconColor: Int = -1,
    @ColorInt var textColor: Int = -1,
    var textSize: Int = -1,
    var iconSize: Int = -1
)
