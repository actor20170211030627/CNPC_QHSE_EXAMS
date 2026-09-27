package com.actor.cnpc_qhse_exams.dialog;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.actor.cnpc_qhse_exams.databinding.DialogUpdateBinding;
import com.actor.cnpc_qhse_exams.global.Global;
import com.actor.myandroidframework.dialog.ViewBindingDialog;
import com.actor.myandroidframework.utils.toaster.ToasterUtils;
import com.actor.pgyer.bean.AppCheckBean;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.RomUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnDownloadListener;
import com.hjq.http.model.HttpMethod;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.io.File;
import java.util.List;

/**
 * description: 检查更新
 * company    :
 *
 * @author : ldf
 * date       : 2026/9/27 on 10
 * @version 1.0
 */
public class UpdateDialog extends ViewBindingDialog<DialogUpdateBinding> {

    private final AppCheckBean.AppCheckResponse.DataBean info;

    public UpdateDialog(@NonNull Context context, AppCheckBean.AppCheckResponse.DataBean data) {
        super(context);
        this.info = data;
        setCancelAble(false);
        //       一些垃圾平板竖屏后, 获取到的宽度实际是高度...
//        setWidthPercent(0.7467f, SizeUtils.dp2px(308f))
        setWidthPercent(0.88888f, SizeUtils.dp2px(308f));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.stvVersion.setText("v" + info.buildVersion);
//        HtmlCompat.fromHtml(info?.buildUpdateDescription ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
        viewBinding.tvContent.setText(info.buildUpdateDescription);
        //强制更新
        if (info.needForceUpdate) {
            viewBinding.stvConfirm2.setVisibility(View.VISIBLE);
        } else {
            viewBinding.stvConfirm2.setVisibility(View.INVISIBLE);
        }

        viewBinding.stvCancel.setOnClickListener(v -> {
            dismiss();
        });
        viewBinding.stvConfirm.setOnClickListener(v -> {
            if (hasApkDownload()) {
                checkInstallPermission();
            } else {
                download();
            }
        });
        viewBinding.stvConfirm2.setOnClickListener(v -> {
            viewBinding.stvConfirm.callOnClick();
        });

        //已下载
        if (hasApkDownload()) {
            viewBinding.stvConfirm2.setText("立即安装");
            viewBinding.stvConfirm2.setVisibility(View.VISIBLE);
        }
    }

    private void download() {
        String downloadURL = info.downloadURL;
        if (TextUtils.isEmpty(downloadURL)) {
            String buildShortcutUrl = info.buildShortcutUrl;
            if (TextUtils.isEmpty(buildShortcutUrl)) buildShortcutUrl = info.appURl;
            if (TextUtils.isEmpty(buildShortcutUrl)) buildShortcutUrl = Global.BUILD_SHORTCUT_URL;
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(buildShortcutUrl)));
            } catch (ActivityNotFoundException e) {
                ToasterUtils.errorFormat("没有找到浏览器打开链接: %s", buildShortcutUrl);
            }
            return;
        }
        viewBinding.progressBar.setVisibility(View.VISIBLE);
        viewBinding.progressBar.setProgress(0);
        //取消
        viewBinding.stvCancel.setVisibility(View.INVISIBLE);
        //立即更新
        viewBinding.stvConfirm.setVisibility(View.INVISIBLE);
        viewBinding.stvConfirm2.setVisibility(View.INVISIBLE);

        EasyHttp.download(this)
                .method(HttpMethod.GET)
                .file(getApkDownloadPath())
                .url(downloadURL)
                .listener(new OnDownloadListener() {
                    @Override
                    public void onDownloadProgressChange(@NonNull File file, int progress) {
                        viewBinding.progressBar.setProgress(progress);
                    }
                    @Override
                    public void onDownloadSuccess(@NonNull File file) {
                        viewBinding.stvConfirm2.setVisibility(View.VISIBLE);
                        viewBinding.stvConfirm2.setText("立即安装");
                    }
                    @Override
                    public void onDownloadFail(@NonNull File file, @NonNull Throwable throwable) {
                        viewBinding.stvConfirm2.setVisibility(View.VISIBLE);
                        viewBinding.stvConfirm2.setText("点击重新下载");
                    }
                }).start();
    }

    private void checkInstallPermission() {
        Activity activity = getActivity();
        if (activity == null) activity = ActivityUtils.getTopActivity();
        if (activity == null) return;
        if (XXPermissions.isGranted(activity, Permission.REQUEST_INSTALL_PACKAGES)
                //小米直接安装, 否则会有危险倒计时弹框, 并且跳转未知来源授权列表, 手动滑动并找到授权
                || RomUtils.isXiaomi()) {
            install();
        } else {
            //申请的时候, 小米有危险倒计时弹框
            XXPermissions.with(activity).permission(Permission.REQUEST_INSTALL_PACKAGES)
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                            if (allGranted) {
                                install();
                            } else {
                                tipGetPermission();
                            }
                        }
                        @Override
                        public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
//                            super.onDenied(permissions, doNotAskAgain);
                            ToasterUtils.warning("您拒绝了安装权限.");
                            if (!doNotAskAgain) tipGetPermission();
                        }
            });
        }
    }

    private void tipGetPermission() {
        Activity activity = getActivity();
        if (activity == null) activity = ActivityUtils.getTopActivity();
        if (activity == null) return;
        //跳转的时候, 小米有危险倒计时弹框
        XXPermissions.startPermissionActivity(activity, Permission.REQUEST_INSTALL_PACKAGES);
//        TipsDialog3(context = activity,
//            content = "当前操作需要获取安装权限！",
//            yesText = "同意",
//            yesClickListener = {
//                XXPermissions.startPermissionActivity(activity, Permission.REQUEST_INSTALL_PACKAGES)
//            }
//        ).show()
    }

    private void install() {
        AppUtils.installApp(getApkDownloadPath());
    }

    /**
     * 当前版本apk是否已下载
     * @return
     */
    private boolean hasApkDownload() {
        File file = new File(getApkDownloadPath());
        return file.exists() && file.isFile() && file.length() == info.buildFileSize;
    }

    /**
     * 获取下载apk path
     * @return /storage/emulated/0/Android/data/package/cache/7e124515555a8a2ac7cdbedda54a5527.apk
     */
    @NonNull
    private String getApkDownloadPath() {
        return PathUtils.getExternalAppCachePath() + File.separator + info.buildFileKey;
    }
}
