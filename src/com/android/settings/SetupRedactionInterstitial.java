/*
 * Copyright (C) 2014 The Android Open Source Project
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
 * limitations under the License
 */

package com.android.settings;

import com.android.settings.notification.RedactionInterstitial;
import com.android.setupwizardlib.SetupWizardLayout;
import com.android.setupwizardlib.view.NavigationBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Setup Wizard's version of RedactionInterstitial screen. It inherits the logic and basic structure
 * from RedactionInterstitial class, and should remain similar to that behaviorally. This class
 * should only overload base methods for minor theme and behavior differences specific to Setup
 * Wizard. Other changes should be done to RedactionInterstitial class instead and let this class
 * inherit those changes.
 */
public class SetupRedactionInterstitial extends RedactionInterstitial {

    public static Intent createStartIntent(Context ctx) {
        Intent startIntent = RedactionInterstitial.createStartIntent(ctx);
        startIntent.setClass(ctx, SetupRedactionInterstitial.class);
        startIntent.putExtra(EXTRA_PREFS_SHOW_BUTTON_BAR, false)
                .putExtra(EXTRA_SHOW_FRAGMENT_TITLE_RESID, -1);
        return startIntent;
    }

    @Override
    public Intent getIntent() {
        Intent modIntent = new Intent(super.getIntent());
        modIntent.putExtra(EXTRA_SHOW_FRAGMENT,
                SetupEncryptionInterstitialFragment.class.getName());
        return modIntent;
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return SetupEncryptionInterstitialFragment.class.getName().equals(fragmentName);
    }

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        resid = SetupWizardUtils.getTheme(getIntent());
        super.onApplyThemeResource(theme, resid, first);
    }

    public static class SetupEncryptionInterstitialFragment extends RedactionInterstitialFragment
            implements NavigationBar.NavigationBarListener {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final SetupWizardLayout layout = new SetupWizardLayout(inflater.getContext());
            layout.setIllustration(R.drawable.setup_illustration_lock_screen,
                    R.drawable.setup_illustration_horizontal_tile);
            layout.setBackgroundTile(R.drawable.setup_illustration_tile);
            layout.setHeaderText(R.string.notification_section_header);

            View content = super.onCreateView(inflater, layout, savedInstanceState);
            layout.addView(content);

            final NavigationBar navigationBar = layout.getNavigationBar();
            navigationBar.setNavigationBarListener(this);
            final Button backButton = navigationBar.getBackButton();
            backButton.setVisibility(View.GONE);
            SetupWizardUtils.setImmersiveMode(getActivity());
            return layout;
        }

        @Override
        public void onNavigateBack() {
            final Activity activity = getActivity();
            if (activity != null) {
                activity.onBackPressed();
            }
        }

        @Override
        public void onNavigateNext() {
            final SetupRedactionInterstitial activity = (SetupRedactionInterstitial) getActivity();
            if (activity != null) {
                activity.setResult(RESULT_OK, activity.getResultIntentData());
                finish();
            }
        }
    }
}
