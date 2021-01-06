package com.aliyun.svideo.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.svideo.common.R;

/**
 * 自定义的类似AlertDialog弹窗，可以传入dialog图标和message
 */
public class AlivcCustomAlertDialog extends Dialog {
    private TextView tvDialogTitle;
    private TextView tvDialogMessage;
    private TextView tvCancel;
    private TextView tvConfirm;
    private OnDialogClickListener mDialogClickListener;

    public AlivcCustomAlertDialog(@NonNull Context context) {
        this(context, R.style.TipDialog);
    }

    public AlivcCustomAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AlivcCustomAlertDialog(@NonNull Context context, boolean cancelable,
                                     @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initView() {
        tvDialogTitle = (TextView) findViewById(R.id.tv_dialog_title);
        tvDialogMessage = (TextView)findViewById(R.id.tv_dialog_message);
        tvCancel = (TextView)findViewById(R.id.tv_cancel);
        tvConfirm = (TextView)findViewById(R.id.tv_confirm);



        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogClickListener != null) {
                    mDialogClickListener.onConfirm();
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogClickListener != null) {
                    mDialogClickListener.onCancel();
                }
            }
        });
    }

    /**
     * 确认、取消监听
     */
    public interface OnDialogClickListener {
        void onConfirm();

        void onCancel();
    }

    public static class Builder {
        private Context mContext;
        private String message;
        private String title;
        private String confirm;
        private String cancel;
        private OnDialogClickListener dialogClickListener;
        private CustomDialogType customDialogType = CustomDialogType.Alert;
        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public AlivcCustomAlertDialog create() {
            AlivcCustomAlertDialog dialog = new AlivcCustomAlertDialog(mContext);
            dialog.setContentView(R.layout.alivc_common_dialog_alert_custom);
            dialog.initView();
            message = TextUtils.isEmpty(message) ? mContext.getResources().getString(R.string.alivc_common_note) : message;
            confirm = TextUtils.isEmpty(confirm) ? mContext.getResources().getString(R.string.alivc_common_confirm) : confirm;
            cancel = TextUtils.isEmpty(cancel) ? mContext.getResources().getString(R.string.alivc_common_cancel) : cancel;
            dialog.tvDialogMessage.setText(message);
            dialog.tvCancel.setText(cancel);
            dialog.tvDialogTitle.setText(title);
            dialog.tvConfirm.setText(confirm);
            dialog.mDialogClickListener = dialogClickListener;
            dialog.setDialogType(customDialogType);
            ViewGroup contentWrap = (ViewGroup)dialog.findViewById(R.id.contentWrap);

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)contentWrap.getLayoutParams();
            params.width = mContext.getResources().getDimensionPixelSize(R.dimen.alivc_common_alert_dialog_w);
            contentWrap.setLayoutParams(params);
            return dialog;
        }

        /**
         * 设置需要显示icon的id
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置dialog显示的内容
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置选中回调监听
         *
         * @param confirm       确认按钮文字提示
         * @param cancel        取消按钮文字提示
         * @param clickListener
         * @return
         */
        public Builder setDialogClickListener(String confirm, String cancel, OnDialogClickListener clickListener) {
            if (!TextUtils.isEmpty(confirm)) {
                this.confirm = confirm;
            }
            if (!TextUtils.isEmpty(cancel)) {
                this.cancel = cancel;
            }
            this.dialogClickListener = clickListener;
            return this;
        }

        public Builder setCustomDialogType(CustomDialogType customDialogType) {
            this.customDialogType = customDialogType;
            return this;
        }
    }

    private void setDialogType(CustomDialogType customDialogType) {
        if (customDialogType == CustomDialogType.Confirm) {
            tvCancel.setVisibility(View.GONE);
        } else {
            tvCancel.setVisibility(View.VISIBLE);
        }
    }

    /**
     * dialog 种类
     */
    public enum CustomDialogType {
        /**
         * 包含确认和取消
         */
        Alert,

        /**
         * 只有确认按钮
         */
        Confirm

    }
}
