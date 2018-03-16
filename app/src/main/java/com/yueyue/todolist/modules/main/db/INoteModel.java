package com.yueyue.todolist.modules.main.db;

import com.yueyue.todolist.common.listener.LoadDataCallBack;

/**
 * author : yueyue on 2018/3/13 16:36
 * desc   :
 */

public interface INoteModel<T> {
    void initNote(int folderId);

    void loadAllNoteList(LoadDataCallBack<T> callBack);

    void loadPrivacyNoteList(LoadDataCallBack<T> callBack);

    void loadRecycleBinNoteList(LoadDataCallBack<T> callBack);

    void loadNormalNoteList(int folderId, LoadDataCallBack<T> callBack);

    void addNote(T note);

    void deleteNote(T note);

    void deleteNotes();
}