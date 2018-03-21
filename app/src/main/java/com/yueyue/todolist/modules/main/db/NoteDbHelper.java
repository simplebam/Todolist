package com.yueyue.todolist.modules.main.db;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;
import com.yueyue.todolist.R;
import com.yueyue.todolist.component.PLog;
import com.yueyue.todolist.modules.main.domain.NoteEntity;

import org.litepal.crud.DataSupport;
import org.litepal.exceptions.DataSupportException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * author : yueyue on 2018/3/8 09:30
 * desc   :
 */
public class NoteDbHelper implements INoteModel<NoteEntity> {
    private static final String TAG = NoteDbHelper.class.getSimpleName();


    private NoteDbHelper() {
    }

    private static final class SingletonHolder {
        private static final NoteDbHelper sInstance = new NoteDbHelper();
    }

    public static NoteDbHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    @Override
    public void initNote(int folderId) {
        long years = (long) 12 * 30 * 24 * 60 * 60 * 1000;
        long month = (long) 24 * 60 * 60 * 1000 * 30;
        long days = (long) 24 * 60 * 60 * 1000;
        long m = (long) 60 * 1000;

        long time = TimeUtils.getNowMills();
        createNoteEntity(folderId, time - 4 * m, R.string.database_content_three);
        createNoteEntity(folderId, time - 3 * m, R.string.database_content_four);
        createNoteEntity(folderId, time - 2 * m, R.string.database_content_five);
        createNoteEntity(folderId, time - m, R.string.database_content_one);
        createNoteEntity(folderId, time, R.string.database_content_two);
    }

    private void createNoteEntity(int folderId, long time, int resId) {
        NoteEntity note = new NoteEntity();
        note.createdTime = time;
        note.modifiedTime = time;
        note.noteContent = Utils.getApp().getResources().getString(resId);
        note.isPrivacy = 0;
        note.inRecycleBin = 0;
        note.noteId = UUID.randomUUID().toString();
        note.save();
    }

    /**
     * 分页查询
     *
     * @param offset 偏移量
     */
    public List<NoteEntity> pageLoadList(int offset, String... conditions)
            throws DataSupportException {
        return DataSupport.where(conditions)
                .limit(10).offset(offset).find(NoteEntity.class);
    }

    /**
     * 方便测试使用
     */
    public List<NoteEntity> loadAll() throws DataSupportException {
        return DataSupport.findAll(NoteEntity.class);
    }



    @Override
    public List<NoteEntity> loadPrivacyNoteList() {
        try {
            List<NoteEntity> data = DataSupport
                    .where("isPrivacy = ? and inRecycleBin = ?", "1", "0")
                    .order("createdTime desc").find(NoteEntity.class);
            return data;
        } catch (DataSupportException e) {
            PLog.e(TAG, "loadPrivacyNoteList: " + e.toString());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<NoteEntity> loadRecycleBinNoteList() {
        try {
            List<NoteEntity> data = DataSupport
                    .where("inRecycleBin = ?", "1")
                    .order("createdTime desc")
                    .find(NoteEntity.class);
            return data;
        } catch (DataSupportException e) {
            PLog.e(TAG, "loadRecycleBinNoteList: " + e.toString());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<NoteEntity> loadNormalNoteList() {
        try {
            List<NoteEntity> data = DataSupport
                    .where("isPrivacy = ? and inRecycleBin = ?", "0", "0")
                    .order("createdTime desc")
                    .find(NoteEntity.class);
            return data;
        } catch (Exception e) {
            PLog.e(TAG, "loadNormalNoteList: " + e.toString());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void addNote(NoteEntity note) {
        try {
            if (note != null) {
                note.saveOrUpdate("noteId=?", note.noteId);
            }
        } catch (Exception e) {
            PLog.e(TAG, "addNote: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteNote(NoteEntity note) {
        try {
            if (note != null) {
                DataSupport.deleteAll(NoteEntity.class, "noteId=?", note.noteId);
            }
        } catch (Exception e) {
            PLog.e(TAG, "deleteNote: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteNotes() {

    }

}
