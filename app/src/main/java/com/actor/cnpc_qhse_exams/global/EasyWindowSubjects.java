package com.actor.cnpc_qhse_exams.global;

import android.text.Editable;
import android.view.View;
import android.view.WindowManager;

import com.actor.cnpc_qhse_exams.R;
import com.actor.cnpc_qhse_exams.adapter.SearchAdapter;
import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.cnpc_qhse_exams.databinding.EasyWindowSubjectsBinding;
import com.actor.cnpc_qhse_exams.utils.SubjectSelectUtils;
import com.actor.myandroidframework.utils.ConfigUtils;
import com.actor.myandroidframework.widget.BaseItemDecoration;
import com.blankj.utilcode.util.SizeUtils;
import com.hjq.window.EasyWindow;
import com.hjq.window.draggable.SpringBackDraggable;

import java.util.List;

/**
 * description: 描述
 * company    :
 *
 * @author : ldf
 * date       : 2025/4/21 on 22
 * @version 1.0
 */
public class EasyWindowSubjects extends EasyWindow<EasyWindowSubjects> implements EasyWindow.OnClickListener<View> {


    private final SearchAdapter mAdapter = new SearchAdapter();


    public EasyWindowSubjects() {
        super(ConfigUtils.APPLICATION);
        setContentView(R.layout.easy_window_subjects);
        EasyWindowSubjectsBinding viewBinding = EasyWindowSubjectsBinding.bind(getContentView());
        // 设置成可拖拽的
//        setDraggable()                    //随意移动
        setDraggable(new SpringBackDraggable());    //随意移动 & 自动靠边
//        setGravity(Gravity.END or Gravity.CENTER_VERTICAL)
        // 设置显示时长
//        setDuration(1000)
        // 设置动画样式
//        setAnimStyle(android.R.style.Animation_Translucent)
        setAnimStyle(android.R.style.Animation_Toast);
        // 设置外层是否能被触摸
//        setOutsideTouchable(true)
        // 设置窗口背景阴影强度
        //setBackgroundDimAmount(0.5f)
//        setImageDrawable(R.id.iv_pet, R.mipmap.ic_launcher)
//        setText(android.R.id.message, "点我消失")

        //设置输入法, 否则不能弹出输入法
        setWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        viewBinding.ivClose.setOnClickListener(v -> {
            recycle();
        });

        viewBinding.stvSearch.setOnClickListener(v -> {
            Editable editable = viewBinding.setSearch.getText();
            String subject = null;
            if (editable != null) subject = editable.toString().trim();
            //章节
            int chapter = viewBinding.bsChapters.getSelectedItemPosition();
            int subType = viewBinding.bsTypes.getSelectedItemPosition();

            List<SubjectDriver> subjectDrivers = SubjectSelectUtils.select(subject, chapter, subType);
            mAdapter.setList(subjectDrivers);
        });

        viewBinding.recyclerView.addItemDecoration(new BaseItemDecoration(0f, SizeUtils.dp2px(5f)));
        viewBinding.recyclerView.setAdapter(mAdapter);

        //设置tag, 用于移除
//        tag = activity::class.java.name
//        setOnClickListener(R.id.iv_pet, this)
    }

    @Override
    public void onClick(EasyWindow<?> easyWindow, View view) {
        // 点击这个 View 后消失
//        easyWindow?.cancel()
        // 跳转到某个Activity
//        Context context = view?.context
//        if (context != null && isLogin() && context !is QXActivity) {
//            easyWindow?.startActivity(QXActivity::class.java)
//        }
    }
}