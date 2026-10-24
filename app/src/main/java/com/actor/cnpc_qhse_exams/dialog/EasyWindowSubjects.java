package com.actor.cnpc_qhse_exams.dialog;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.text.Editable;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.actor.cnpc_qhse_exams.R;
import com.actor.cnpc_qhse_exams.adapter.StudyAdapter;
import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.cnpc_qhse_exams.databinding.EasyWindowSubjectsBinding;
import com.actor.cnpc_qhse_exams.utils.SubjectSelectUtils;
import com.actor.myandroidframework.recyclerview.BaseItemDecoration;
import com.actor.myandroidframework.utils.BRVUtils;
import com.actor.myandroidframework.utils.ConfigUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.hjq.window.EasyWindow;
import com.hjq.window.OnWindowViewClickListener;
import com.hjq.window.OnWindowViewTouchListener;
import com.hjq.window.draggable.SpringBackWindowDraggableRule;

import java.util.List;

/**
 * description: 描述
 * company    :
 *
 * @author : ldf
 * date       : 2025/4/21 on 22
 * @version 1.0
 */
public class EasyWindowSubjects extends EasyWindow<EasyWindowSubjects> implements OnWindowViewClickListener<View> {

    private final long         version;
    private final boolean      isStudyAll;
    private final StudyAdapter mAdapter = new StudyAdapter();
    private final int          SIZE     = 10;
    private final EasyWindowSubjectsBinding viewBinding;
    private final int[] offsets = {-1, -1};

    /**
     * @param version 版本
     * @param isStudyAll 是否学习全部, if true, 会忽略 ↓ 章节列表
     * @param chapters 章节列表
     */
    public EasyWindowSubjects(long version, boolean isStudyAll, @Nullable List<String> chapters) {
        //会导致 ShapeEditText 点击没反应
        super(ConfigUtils.APPLICATION);
//        super(getContextThemeWrapper(ConfigUtils.APPLICATION));
        this.version = version;
        this.isStudyAll = isStudyAll;

        Context contextThemeWrapper = getContextThemeWrapper(ConfigUtils.APPLICATION);

//        setContentView(R.layout.easy_window_subjects);
        View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.easy_window_subjects, getRootLayout(), false);
        setContentView(view);
//        setWindowSize((int) (ScreenUtils.getScreenWidth() * 0.92f), SizeUtils.dp2px(350f));
        viewBinding = EasyWindowSubjectsBinding.bind(getContentView());
        viewBinding.bsChapters.setData(chapters);
        if (chapters == null || chapters.isEmpty()) {
            viewBinding.bsChapters.setVisibility(View.INVISIBLE);
        }
        // 设置成可拖拽的
//        setWindowDraggableRule()                    //随意移动
        setWindowDraggableRule(new SpringBackWindowDraggableRule());    //随意移动 & 自动靠边
//        setGravity(Gravity.END or Gravity.CENTER_VERTICAL)
        // 设置显示时长
//        setDuration(1000)
        // 设置动画样式
//        setWindowAnim(android.R.style.Animation_Translucent)
        setWindowAnim(android.R.style.Animation_Toast);
        // 设置外层是否能被触摸
//        setOutsideTouchable(true)
        // 设置窗口背景阴影强度
        //setBackgroundDimAmount(0.5f)
//        setImageDrawable(R.id.iv_pet, R.mipmap.ic_launcher)
//        setText(android.R.id.message, "点我消失")

        //设置输入法, 否则不能弹出输入法
//        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setOnClickListenerByView(R.id.iv_close, this);
        setOnClickListenerByView(R.id.iv_clear, this);
        setOnClickListenerByView(R.id.stv_search, this);

        setOnTouchListenerByView(R.id.set_search, new OnWindowViewTouchListener<View>() {
            @Override
            public boolean onTouch(@NonNull EasyWindow<?> easyWindow, @NonNull View view, @NonNull MotionEvent event) {
                offsets[0] = getWindowParams().x;
                offsets[1] = getWindowParams().y;
                return false;
            }
        });

        mAdapter.setShowAnswer(true);
        BRVUtils.setOnLoadMoreListener(mAdapter, () -> {
            getList(false);
        });
        viewBinding.recyclerView.addItemDecoration(new BaseItemDecoration(0f, SizeUtils.dp2px(5f)));
        viewBinding.recyclerView.setAdapter(mAdapter);

        //设置tag, 用于移除
//        tag = activity::class.java.name
//        setOnClickListener(R.id.iv_pet, this)
    }

    private void getList(boolean isRefresh) {
        Editable editable = viewBinding.setSearch.getText();
        String subject = null;
        if (editable != null) subject = editable.toString().trim();
        //章节
        int chapter = isStudyAll ? -2 : viewBinding.bsChapters.getSelectedItemPosition();
        int subType = viewBinding.bsTypes.getSelectedItemPosition();
        int page = BRVUtils.getPage(mAdapter, isRefresh, SIZE);
        List<SubjectDriver> subjectDrivers = SubjectSelectUtils.selectPage(version, subject, chapter, subType, page, SIZE);
        if (isRefresh) {
            mAdapter.setList(subjectDrivers);
        } else {
            mAdapter.addData(subjectDrivers);
        }
        BRVUtils.setLoadMoreStateBySize(mAdapter, subjectDrivers, SIZE);
    }

    @Override
    public void onClick(@NonNull EasyWindow<?> easyWindow, @NonNull View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                recycle();
                break;
            case R.id.iv_clear:
                viewBinding.setSearch.setText("");
                break;
            case R.id.stv_search:
                KeyboardUtils.hideSoftInput(view);
                viewBinding.setSearch.clearFocus();
                viewBinding.getRoot().setFocusableInTouchMode(true);
                viewBinding.getRoot().requestFocus();
                if (offsets[0] >= 0 && offsets[1] >= 0) {
                    setWindowLocation(offsets[0], offsets[1]);
                    offsets[0] = offsets[1] = -1;
                }
                getList(true);
                break;
            default:
                break;
        }
        // 点击这个 View 后消失
//        easyWindow?.cancel()
        // 跳转到某个Activity
//        Context context = view?.context
//        if (context != null && isLogin() && context !is QXActivity) {
//            easyWindow?.startActivity(QXActivity::class.java)
//        }
    }

    /**
     * 当 context 是Application 的时候, spinnerMode=dialog点击无反应, spinnerMode=dropdown点击报错
     * android.view.InflateException: Binary XML file line #19: Failed to resolve attribute at index 1: TypedValue{t=0x2/d=0x7f04015b a=-1}
     * Caused by: java.lang.UnsupportedOperationException: Failed to resolve attribute at index 1: TypedValue{t=0x2/d=0x7f04015b a=-1}
     *     at android.content.res.TypedArray.getLayoutDimension(TypedArray.java:816)
     * 乺: 将 Application 包装成 ContextThemeWrapper, 让 Spinner 能够在 SYSTEM_ALERT_WINDOW 全局悬浮窗里也能够下拉
     */
    @NonNull
    private  /*static*/ Context getContextThemeWrapper(Context context) {
        if (context instanceof ContextThemeWrapper) return context;

        // 1. 获取一个与 Display 关联的 Context
        DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        Display defaultDisplay = dm.getDisplay(Display.DEFAULT_DISPLAY);
        Context displayContext = context.createDisplayContext(defaultDisplay); // 关键步骤
        // 2. 从 displayContext 创建 WindowContext
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Context windowContext = displayContext.createWindowContext(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, null);
            // 3. 应用主题
            windowContext.setTheme(com.google.android.material.R.style.Theme_AppCompat_Light_NoActionBar);
            return windowContext;
        }

        // Android R 以下，直接用 displayContext 包装主题即可
        return new ContextThemeWrapper(displayContext, com.google.android.material.R.style.Theme_AppCompat_Light_NoActionBar);
    }
}