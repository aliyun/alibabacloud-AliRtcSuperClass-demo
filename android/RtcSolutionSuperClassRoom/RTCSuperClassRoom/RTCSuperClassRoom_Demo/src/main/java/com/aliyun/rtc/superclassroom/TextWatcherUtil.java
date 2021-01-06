package com.aliyun.rtc.superclassroom;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.aliyun.rtc.superclassroom.listener.UserInputListener;

public class TextWatcherUtil {

    private UserInputListener mUserInputListener;

    public void setUserInputListener(UserInputListener mUserInputListener) {
        this.mUserInputListener = mUserInputListener;
    }

    public void addClassCodeTextWatch(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s == null || s.length() == 0) {
//                    return;
//                }
//                StringBuilder sb = new StringBuilder();
//                for (int i = 0; i < s.length(); i++) {
//                    if (i % 4 != 3  && s.charAt(i) == ' ') {
//                        continue;
//                    } else {
//                        sb.append(s.charAt(i));
//                        if (sb.length() % 4 == 0 && sb.charAt(sb.length() - 1) != ' ') {
//                            sb.insert(sb.length() - 1, ' ');
//                        }
//                    }
//                }
//
//                if (!sb.toString().equals(s.toString())) {
//                    int index = start + 1;
//                    if (sb.charAt(start) == ' ') {
//                        if (before == 0) {
//                            index++;
//                        } else {
//                            index--;
//                        }
//                    } else {
//                        if (before == 1) {
//                            index--;
//                        }
//                    }
//                    editText.removeTextChangedListener(this);
//                    if (sb.length() >= 11) {
//                        editText.setText(sb.subSequence(0,11));
//                        editText.setSelection(11);
//                    } else {
//                        editText.setText(sb.toString());
//                        editText.setSelection(index);
//                    }
//                    editText.addTextChangedListener(this);
//                }

                    editText.removeTextChangedListener(this);
                    if (s.length() >= 5) {
                        editText.setText(s.subSequence(0,5));
                        editText.setSelection(5);
                    }
                    editText.addTextChangedListener(this);

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if(s.length() >= 7){
//                    if(mUserInputListener != null){
//                        mUserInputListener.getClassCode(true);
//                    }
//                } else {
//                    if(mUserInputListener != null){
//                        mUserInputListener.getClassCode(false);
//                    }
//                }

                if(s.length() >= 5){
                    if(mUserInputListener != null){
                        mUserInputListener.getClassCode(true);
                    }
                } else {
                    if(mUserInputListener != null){
                        mUserInputListener.getClassCode(false);
                    }
                }

            }
        });
    }



    public void addNameTextWatch(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    if(mUserInputListener != null){
                        mUserInputListener.getUserName(true);
                    }
                } else {
                    if(mUserInputListener != null){
                        mUserInputListener.getUserName(false);
                    }
                }

            }
        });
    }

}
