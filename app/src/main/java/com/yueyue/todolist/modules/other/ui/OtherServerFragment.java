package com.yueyue.todolist.modules.other.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.Utils;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseFragment;
import com.yueyue.todolist.component.PreferencesManager;
import com.yueyue.todolist.component.RxBus;
import com.yueyue.todolist.event.OtherShowChangeEvent;
import com.yueyue.todolist.modules.other.adapter.OtherServerAdapter;
import com.yueyue.todolist.modules.other.domain.ItemInfo;
import com.yueyue.todolist.modules.weather.ui.WeatherActivity;
import com.yueyue.todolist.widget.LineRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 *
 */
public class OtherServerFragment extends BaseFragment implements OtherServerAdapter.OnOtherServerItemClickListener {

    @BindView(R.id.rv_other_server)
    LineRecyclerView mRvOtherServer;

    @BindView(R.id.fab)
    FloatingActionButton mFab;
    private List<ItemInfo> itemInfos = new ArrayList<>();
    private List<Class> activities = new ArrayList<>();
    private OtherServerAdapter otherServerAdapter;
    private boolean isGrid;

    private List<Disposable> mDisposableList = new ArrayList<>();

    public static OtherServerFragment newInstance() {
        return new OtherServerFragment();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_other_server;
    }

    @Override
    protected void initViews() {
        initRecyclerView();
        registerOtherShowChangeEvent();
    }


    private void registerOtherShowChangeEvent() {
        Disposable disposable = RxBus.getDefault().toObservable(OtherShowChangeEvent.class)
                .subscribe(otherShowChangeEvent -> {
                    isGrid = PreferencesManager.getInstance().getOtherServerFragmentIsGrid(false);
                    initRecyclerView();
                });
        mDisposableList.add(disposable);

    }

    private void initRecyclerView() {
        otherServerAdapter = new OtherServerAdapter(getContext(), itemInfos, isGrid);
        otherServerAdapter.setOnOtherServerItemClickListener(this);
        if (isGrid) {
            mRvOtherServer.setDrawLine(true);
            mRvOtherServer.setLayoutManager(new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false));
        } else {
            mRvOtherServer.setDrawLine(false);
            mRvOtherServer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
        mRvOtherServer.setAdapter(otherServerAdapter);
    }


    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(getContext(), getActivity(position)));
    }

    private Class getActivity(int position) {
        return activities.get(position) == null ? WeatherActivity.class : activities.get(position);
    }


    @Override
    protected void initData() {
        Resources res = Utils.getApp().getResources();
        List<String> operIds = Arrays.asList(res.getStringArray(R.array.other_oper_id));
        List<String> operNames = Arrays.asList(res.getStringArray(R.array.other_oper_name));
        TypedArray typedArray = getResources().obtainTypedArray(R.array.other_oper_icon);
        for (int i = 0; i < operIds.size(); i++) {
            itemInfos.add(new ItemInfo(operIds.get(i),
                    typedArray.getResourceId(i, R.drawable.ic_weather), operNames.get(i)));
        }
        activities.add(WeatherActivity.class);
//        activities.add(PhoneAttrActivity.class);
//        activities.add(ZipCodeActivity.class);
//        activities.add(RecipeActivity.class);
//        activities.add(BaseStationLocationActivity.class);
//        activities.add(IdCardInquiriesActivity.class);
//        activities.add(TheQuestionBankActivity.class);
//        activities.add(TrainTicketInquiriesActivity.class);
//        activities.add(FootBallFiveLeagueActivity.class);
//        activities.add(ZGSolutionDreamActivity.class);
        /**
         * 是否是网格显示
         */
        isGrid = PreferencesManager.getInstance().getOtherServerFragmentIsGrid(false);
    }

    @OnClick(R.id.fab)
    void changeShowMode() {
        boolean isGrid = PreferencesManager.getInstance().getOtherServerFragmentIsGrid(false);
        PreferencesManager.getInstance().saveOtherServerFragmentIsGrid(!isGrid);
        RxBus.getDefault().post(new OtherShowChangeEvent());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //释放资源
        for (Disposable disposable : mDisposableList) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }
}
