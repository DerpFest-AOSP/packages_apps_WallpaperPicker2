/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.wallpaper.picker.preview.ui.binder

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import com.android.wallpaper.picker.preview.ui.fragment.smallpreview.DualPreviewViewPager
import com.android.wallpaper.picker.preview.ui.fragment.smallpreview.adapters.DualPreviewPagerAdapter
import com.android.wallpaper.util.DisplayUtils
import kotlinx.coroutines.CoroutineScope

/**
 * This binder binds the data and view models for the dual preview collection on the small preview
 * screen.
 */
object DualPreviewSelectorBinder {

    fun bind(
        tabsViewPager: ViewPager,
        dualPreviewView: DualPreviewViewPager,
        homeScreenPreviewViewModel: DualPreviewPagerAdapter.DualPreviewPagerViewModel,
        lockScreenPreviewViewModel: DualPreviewPagerAdapter.DualPreviewPagerViewModel,
        applicationContext: Context,
        isSingleDisplayOrUnfoldedHorizontalHinge: Boolean,
        viewLifecycleOwner: LifecycleOwner,
        isRtl: Boolean,
        mainScope: CoroutineScope,
        displayUtils: DisplayUtils
    ) {
        // set up tabs view pager
        TabPagerBinder.bind(tabsViewPager)

        // synchronize both view and tabs pager
        synchronizePreviewAndTabsPager(tabsViewPager, dualPreviewView)

        DualPreviewPagerBinder.bind(
            dualPreviewView,
            homeScreenPreviewViewModel,
            lockScreenPreviewViewModel,
            applicationContext,
            isSingleDisplayOrUnfoldedHorizontalHinge,
            viewLifecycleOwner,
            isRtl,
            mainScope,
            displayUtils
        )
    }

    private fun synchronizePreviewAndTabsPager(
        tabsViewPager: ViewPager,
        previewsViewPager: ViewPager,
    ) {
        val onPageChangeListenerTabs =
            object : ViewPager.OnPageChangeListener {
                override fun onPageSelected(position: Int) {
                    previewsViewPager.setCurrentItem(position, true)
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {}

                override fun onPageScrollStateChanged(state: Int) {}
            }

        tabsViewPager.addOnPageChangeListener(onPageChangeListenerTabs)

        val onPageChangeListenerPreviews =
            object : ViewPager.OnPageChangeListener {
                override fun onPageSelected(position: Int) {
                    tabsViewPager.setCurrentItem(position, true)
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {}

                override fun onPageScrollStateChanged(state: Int) {}
            }

        previewsViewPager.addOnPageChangeListener(onPageChangeListenerPreviews)
    }
}
