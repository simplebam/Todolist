package com.yueyue.todolist.modules.main.db;

import java.util.List;

/**
 * author : yueyue on 2018/3/13 16:36
 * desc   :
 */

public interface INoteModel<T> {
    void initNote(int folderId);


    List<T> loadPrivacyNoteList();

    List<T> loadRecycleBinNoteList();

    List<T> loadNormalNoteList();

    void addNote(T note);

    void deleteNote(T note);

    void deleteNotes();
}