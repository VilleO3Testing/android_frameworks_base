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
 * limitations under the License.
 */

package com.android.systemui.volume;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.systemui.R;

import java.util.Objects;

public class SegmentedButtons extends LinearLayout {

    private final Context mContext;
    private final LayoutInflater mInflater;

    private Callback mCallback;
    private Object mSelectedValue;

    public SegmentedButtons(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        setOrientation(HORIZONTAL);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public Object getSelectedValue() {
        return mSelectedValue;
    }

    public void setSelectedValue(Object value) {
        if (Objects.equals(value, mSelectedValue)) return;
        mSelectedValue = value;
        for (int i = 0; i < getChildCount(); i++) {
            final View c = getChildAt(i);
            final Object tag = c.getTag();
            c.setSelected(Objects.equals(mSelectedValue, tag));
        }
        fireOnSelected();
    }

    public void addButton(int labelResId, Object value) {
        final Button b = (Button) mInflater.inflate(R.layout.segmented_button, this, false);
        b.setText(labelResId);
        final LayoutParams lp = (LayoutParams) b.getLayoutParams();
        if (getChildCount() == 0) {
            lp.leftMargin = lp.rightMargin = 0; // first button has no margin
        }
        b.setLayoutParams(lp);
        addView(b);
        b.setTag(value);
        b.setOnClickListener(mClick);
    }

    private void fireOnSelected() {
        if (mCallback != null) {
            mCallback.onSelected(mSelectedValue);
        }
    }

    private final View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setSelectedValue(v.getTag());
        }
    };

    public interface Callback {
        void onSelected(Object value);
    }
}
