package com.yueyue.todolist.modules.main.db;

import java.util.List;

/**
 * author : yueyue on 2018/3/13 16:36
 * desc   :
 */

public interface INoteModel<T> {
    void initNote();


    List<T> loadPrivacyNoteList();

    List<T> loadRecycleBinNoteList();

    List<T> loadNormalNoteList();

    int loadTodayNormalCount();

    int loadTodayPrivacyCount();

    void addNote(T note);

    void deleteNote(T note);

    void deleteNotes();

    void deleteAllNoteRecycleIn();
}