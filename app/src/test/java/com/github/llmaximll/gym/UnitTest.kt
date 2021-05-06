package com.github.llmaximll.gym

import com.github.llmaximll.gym.fragments.otherfragments.LogInFragment
import com.github.llmaximll.gym.fragments.otherfragments.SignUpFragment
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {
    @Test
    fun `Should return SignUpFragment()`() {
        assertTrue(SignUpFragment.newInstance() is SignUpFragment)
    }

    @Test
    fun `Should return LogInFragment()`() {
        assertTrue(LogInFragment.newInstance() is LogInFragment)
    }
}