package com.aceinteract.android.stepper.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

/**
 * Find and return the nav controller from a fragment.
 *
 * @param id the id of the fragment to fetch the nav controller from.
 *
 * @return the found nav controller or throw an error if it can't be found
 */
fun AppCompatActivity.findNavControllerFromFragment(id: Int): NavController {
    val fragment = supportFragmentManager.findFragmentById(id)
    check(fragment is NavHostFragment) { ("Activity $this does not have a NavHostFragment") }
    return fragment.navController
}