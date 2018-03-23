package com.yueyue.todolist.modules.main.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.RecyclerFragment;
import com.yueyue.todolist.common.Constants;
import com.yueyue.todolist.component.PreferencesManager;
import com.yueyue.todolist.component.RxBus;
import com.yueyue.todolist.event.MainTabsShowModeEvent;
import com.yueyue.todolist.event.MainTabsUpdateEvent;
import com.yueyue.todolist.modules.edit.ui.EditNoteActivity;
import com.yueyue.todolist.modules.main.adapter.NoteBottomSheetFolderAdapter;
import com.yueyue.todolist.modules.main.adapter.NoteListAdapter;
import com.yueyue.todolist.modules.main.db.NoteDbHelper;
import com.yueyue.todolist.modules.main.domain.NoteEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.yueyue.todolist.modules.main.ui.MainActivity.EDIT_NOTE_REQUEST_CODE;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainTabsFragment extends RecyclerFragment {

    private static final String TAG = MainTabsFragment.class.getSimpleName();


    private static final String ITEM_TYPE = "item_type";
    public static final int ITEM_NORMAL = 0x111;     //主页
    public static final int ITEM_PRIMARY = 0x222; //私有
    public static final int ITEM_RECYCLE = 0x333; //垃圾篓
    private int mItemCurrent = 0x111;     //主页


    private RecyclerView mRecyclerView;
    private NoteListAdapter mAdapter;

    private List<Disposable> mDisposableList = new ArrayList<>();


    public static MainTabsFragment newInstance(int itemType) {
        MainTabsFragment fragment = new MainTabsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEM_TYPE, itemType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initArguments();
        setupView();
        initFab();
    }

    @Override
    protected void initData() {
        super.initData();

        boolean isFirst = PreferencesManager.getInstance().getIsFirst();
        if (isFirst) {
            NoteDbHelper.getInstance().initNote();
            PreferencesManager.getInstance().saveIsFirst(false);
        }

        registerMainTabsUpdateEvent();
        registerMainTabsShowModeEvent();
        initAdapter();
        load();
    }

    private void setupView() {
        mRecyclerView = getRecyclerView();
    }

    private void initArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mItemCurrent = bundle.getInt(ITEM_TYPE, ITEM_NORMAL);
        }
    }

    private void registerMainTabsUpdateEvent() {
        Disposable disposable = RxBus.getDefault()
                .toObservable(MainTabsUpdateEvent.class)
                .doOnNext(event -> load())
                .subscribe();
        mDisposableList.add(disposable);
    }

    private void registerMainTabsShowModeEvent() {
        Disposable disposable = RxBus.getDefault()
                .toObservable(MainTabsShowModeEvent.class)
                .doOnNext(event -> changeShowMode(event.getMode()))
                .subscribe();
        mDisposableList.add(disposable);

    }

    private void changeShowMode(int mode) {
        RecyclerView.LayoutManager manager =
                mode == Constants.STYLE_LINEAR ?
                        new LinearLayoutManager(getContext()) :
                        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(manager);
        mAdapter.notifyDataSetChanged();
    }


    public void initAdapter() {
        int mode = PreferencesManager.getInstance().getNoteListShowMode(Constants.STYLE_LINEAR);
        RecyclerView.LayoutManager layoutManager;
        if (mode == Constants.STYLE_LINEAR) {
            layoutManager = new LinearLayoutManager(getContext());
        } else {
            layoutManager = new StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL);
        }

        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new NoteListAdapter();
        //开启动画(默认为渐显效果)
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //禁止默认第一次加载Item进入回调onLoadMoreRequested方法
        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        //recyclerView 为空的时候展示
        View recyclerEmptyView = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_main_tabs_empty, null, false);
        mAdapter.setEmptyView(recyclerEmptyView);

        mAdapter.notifyDataSetChanged();

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (mItemCurrent == ITEM_RECYCLE) {
                showNoteRecoverDialog(position);
            } else {
                toEditNoteForEdit(mAdapter.getData().get(position), position);
            }
        });

        mAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            NoteEntity noteEntity = (NoteEntity) adapter.getData().get(position);
            showSingleChioceDialog(noteEntity);
            return true;
        });
    }

    /**
     * 显示单选对话框
     */
    public void showSingleChioceDialog(NoteEntity noteEntity) {
        String[] items;

        if (mItemCurrent == ITEM_NORMAL) {
            items = getResources().getStringArray(R.array.normal_selections);
        } else if (mItemCurrent == ITEM_PRIMARY) {
            items = getResources().getStringArray(R.array.privacy_selections);
        } else {
            items = getResources().getStringArray(R.array.recycle_selections);
        }


        new AlertDialog.Builder(mActivity)
                .setTitle(getString(R.string.select_your_operation))
                .setIcon(R.mipmap.ic_launcher)
                .setSingleChoiceItems(items, 0, (dialog, which) -> {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    handleResultFromItems(items[which], noteEntity);
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .setCancelable(false)
                .show();
    }

    private void handleResultFromItems(String item, NoteEntity noteEntity) {
        switch (item) {
            case "设为私密":
                noteEntity.isPrivacy = 1;
                break;
            case "删除":
                if (mItemCurrent == ITEM_RECYCLE) {
                    NoteDbHelper.getInstance().deleteNote(noteEntity);
                } else {
                    noteEntity.inRecycleBin = 1;
                    NoteDbHelper.getInstance().addNote(noteEntity);
                }
                break;
            case "移动":
                if (mItemCurrent == ITEM_RECYCLE) {
                    noteEntity.inRecycleBin = 0;
                } else {
                    showMoveBottomSheet(noteEntity);
                }
                break;
            case "移除私密":
                noteEntity.isPrivacy = 0;
                break;
            case "恢复":
                noteEntity.inRecycleBin = 0;
                break;
            default:
                break;
        }

        if (!"删除".equals(item)) {
            NoteDbHelper.getInstance().addNote(noteEntity);
        }

        load();


    }

    public void showMoveBottomSheet(NoteEntity noteEntity) {
        //BottomSheet、BottomSheetDialog使用详解 - 简书 https://www.jianshu.com/p/0a7383e0ad0f
        final BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
//                    获取contentView
        ViewGroup contentView = (ViewGroup) ((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0);
        View root = LayoutInflater.from(mActivity).inflate(R.layout.bottom_sheet_folder, contentView, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_bottom_sheet_folder);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        dialog.setContentView(root);
        recyclerView.setAdapter(getBottomSheetRvAdapter(dialog, noteEntity));
        dialog.show();
    }

    private NoteBottomSheetFolderAdapter getBottomSheetRvAdapter(final BottomSheetDialog dialog,
                                                                 NoteEntity noteEntity) {
        final NoteBottomSheetFolderAdapter folderAdapter = new NoteBottomSheetFolderAdapter();
        ArrayList<String> list = new ArrayList<String>() {{
            add(getString(R.string.todo));
            add(getString(R.string.privacy));
            add(getString(R.string.recycle_bin));
        }};
        folderAdapter.setNewData(list);
        folderAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position == 0) {
                noteEntity.inRecycleBin = 0;
                noteEntity.isPrivacy = 0;
            } else if (position == 1) {
                noteEntity.inRecycleBin = 0;
                noteEntity.isPrivacy = 1;
            } else {
                noteEntity.inRecycleBin = 1;
            }
            NoteDbHelper.getInstance().addNote(noteEntity);
            load();
            if (dialog != null) {
                dialog.dismiss();
            }
        });
        return folderAdapter;
    }


    public void showNoteRecoverDialog(final int position) {
        new AlertDialog.Builder(mActivity)
                .setMessage(getString(R.string.unable_to_open_note_recover_to_previous_folder))
                .setNegativeButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.recovery), (dialog, which) -> {
                    NoteEntity note = mAdapter.getData().get(position);
                    note.inRecycleBin = 0;
                    NoteDbHelper.getInstance().addNote(note);
                    load();
                })
                .show();
    }


    public void toEditNoteForEdit(NoteEntity note, int position) {
        note.adapterPos = position;
        EditNoteActivity.launch(mActivity, note, EDIT_NOTE_REQUEST_CODE);
    }

    private void initFab() {
        if (mActivity instanceof MainActivity) {
            final FloatingActionButton fab = ((MainActivity) mActivity).mFab;
            if (fab == null) return;


            fab.setOnClickListener(v -> {
                EditNoteActivity.launch(mActivity, null, MainActivity.ADD_NOTE_REQUEST_CODE);
            });

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    //大于这个距离mTouchSlop才可以触发
                    if (dy > 0) {
                        setFabOut(fab); // 手指向上滑动
                    } else {
                        setFabIn(fab);// 手指向下滑动
                    }
                }
            });
        }
    }

    private void setFabOut(FloatingActionButton fab) {
        ObjectAnimator
                .ofFloat(fab, "translationY", SizeUtils.dp2px(80))
                .setDuration(200)
                .start();
    }

    private void setFabIn(FloatingActionButton fab) {
        ObjectAnimator
                .ofFloat(fab, "translationY", SizeUtils.dp2px(0))
                .setDuration(200)
                .start();
    }

    private void load() {
        //简单的来说, subscribeOn() 指定的是上游发送事件的线程, observeOn() 指定的是下游接收事件的线程.
        Disposable disposable = Observable.timer(0, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .doOnSubscribe(subscription -> changeRefresh(true))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(aLong -> loadDataFromDB())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> {
                    changeRefresh(false);//发生错误,这里也要取消
                    mAdapter.notifyDataSetChanged();
                })
                .subscribe(noteEntityList -> {
                    changeRefresh(false);
                    mAdapter.setNewData(noteEntityList);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(0);
                });
        mDisposableList.add(disposable);
    }

    private List<NoteEntity> loadDataFromDB() {
        List<NoteEntity> list = new ArrayList<>(1);
        switch (mItemCurrent) {
            case ITEM_NORMAL:
                list = NoteDbHelper.getInstance().loadNormalNoteList();
                break;
            case ITEM_PRIMARY:
                list = NoteDbHelper.getInstance().loadPrivacyNoteList();
                break;
            case ITEM_RECYCLE:
                list = NoteDbHelper.getInstance().loadRecycleBinNoteList();
                break;
            default:
                break;
        }

        return list;
    }

    @Override
    public void onRefresh() {
        load();
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