/*
 * Copyright (C) 2007 The Android Open Source Project
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

package vike.flash.cards;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.SeekBar;
import vike.flash.cards.R;

public class Preferences extends PreferenceActivity {

	private String[] WORD_NAME=new String[]{};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
	}
	
	public class EditTextProgressPref extends EditTextPreference{
		private Context mCxt = this.getContext();
		private EditTextPreference ETP = this;
		public EditTextProgressPref(Context context) {
			super(context);
		}

		@Override
		protected View onCreateDialogView() {
			System.out.println("a");
			SeekBar mProgress = new SeekBar(mCxt);
			System.out.println("b");
			mProgress.setMax(WORD_NAME.length);
			System.out.println("c");
			mProgress.setProgress(1);
			System.out.println("d");
			mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					
					ETP.setText(progress+"");
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar){}
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {}
			});
			System.out.println("e");
			System.out.println("f");
			return super.onCreateDialogView();
		}
	}
}
