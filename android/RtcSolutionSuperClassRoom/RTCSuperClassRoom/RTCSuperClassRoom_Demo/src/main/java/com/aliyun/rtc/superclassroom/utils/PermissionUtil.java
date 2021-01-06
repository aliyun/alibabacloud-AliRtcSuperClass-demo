package com.aliyun.rtc.superclassroom.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionUtil {

    public static final int PERMISSION_REQUEST_CODE = 10;
    private static final int REQUEST_CODE_SETTING = 11;

    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public interface PermissionGrantedListener {
        /**
         * 成功
         */
        void onPermissionGranted();

        /**
         * 失败
         */
        void onPermissionCancel();
    }

    /**
     *  权限申请
     * @param activity
     * @param permissions
     * @param requestCode
     * @param listener
     */
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode, PermissionGrantedListener listener) {
        List<String> notGrantedPermissions = getNotGrantedPermissions(activity, permissions);

        if (notGrantedPermissions == null || notGrantedPermissions.size() == 0) {
            //权限都申请过了
            if (listener != null) {
                listener.onPermissionGranted();
            }
            return;
        }

        //申请未拥有的权限
        for (String notGrantedPermission : notGrantedPermissions) {
            if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(permissions, requestCode);
            }
        }
    }

    /**
     * 获取权限集合中未申请成功的权限
     * @param activity
     * @param permissions
     * @return
     */
    private static List<String> getNotGrantedPermissions(Activity activity, String[] permissions) {
        List<String> notGrantedPermissions = new ArrayList<>();
        if (permissions == null || permissions.length == 0) {
            return null;
        }

        for (String permission : permissions) {
            boolean isGrented = checkSelfPermission(activity, permission);
            if (!isGrented) {
                notGrantedPermissions.add(permission);
            }
        }
        return notGrantedPermissions;
    }

    /**
     * 检测是否有该权限
     * @param activity 上下文
     * @param permission 权限
     * @return true 有权限  false 无权限
     */
    private static boolean checkSelfPermission(Activity activity, String permission) {
        if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkSelfPermission = activity.checkSelfPermission(permission);
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            // 如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选项，
            // 此方法将返回 false。如果设备规范禁止应用具有该权限，此方法也会返回 false。
            boolean showRequestPermissionRationale = activity.shouldShowRequestPermissionRationale(permission);
            //客户手动禁止该权限，并不再询问就不用在弹窗
            return showRequestPermissionRationale && checkSelfPermission == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public static void requestPermissionsResult(final Activity activity, final int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults, PermissionGrantedListener listener) {
        if (activity == null) {
            return;
        }

        Map<String, Integer> perms = new HashMap<>();

        ArrayList<String> notGranted = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            perms.put(permissions[i], grantResults[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(permissions[i]);
            }
        }

        if (notGranted.size() == 0) {
//            Toast.makeText(activity, "all permission success", Toast.LENGTH_SHORT).show();
            listener.onPermissionGranted();
        } else {
            openSettingActivity(activity, "those permission need granted!", notGranted.toArray(new String[notGranted.size()]), listener);
        }

    }

    private static void openSettingActivity(final Activity activity, String message, String[] permissions, final PermissionGrantedListener listener) {

//        showMessageOKCancel(activity, message,
//                permissions,
//                (dialog, which) -> {
//                    Intent intent = new Intent();
//                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
//                    intent.setData(uri);
//                    activity.startActivityForResult(intent, PermissionUtil.REQUEST_CODE_SETTING);
//                }, (dialog, which) -> listener.onPermissionCancel());
    }

    private static void showMessageOKCancel(final Activity context, String message, String[] permissions, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(context)
        .setTitle(message)
        .setItems(permissions, null)
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", cancelListener)
        .create()
        .show();

    }
}
