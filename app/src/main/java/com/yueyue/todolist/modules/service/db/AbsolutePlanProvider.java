package com.yueyue.todolist.modules.service.db;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

/**
 * author : yueyue on 2018/3/3 13:03
 * desc   :
 */

public class AbsolutePlanProvider extends ContentProvider {
    public static final String TAG = AbsolutePlanProvider.class.getSimpleName();

    private AbsolutePlanDBHelper mOpenHelper = null;
    private ContentResolver resolver = null;

    private static final int MATCH_PLANTASK = 1;
    private static final int MATCH_PLANTASK_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AbsolutePlanContract.AUTHORITY, AbsolutePlanDBHelper.DB_TABLE_PLANTASK, MATCH_PLANTASK);
        uriMatcher.addURI(AbsolutePlanContract.AUTHORITY, AbsolutePlanDBHelper.DB_TABLE_PLANTASK + "/#", MATCH_PLANTASK_ID);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new AbsolutePlanDBHelper(getContext(), AbsolutePlanDBHelper.DB_NAME, null, AbsolutePlanDBHelper.DB_VERSION);
        resolver = getContext().getContentResolver();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        String limit = null;
        Cursor cursor = null;

        switch (uriMatcher.match(uri)) {
            case MATCH_PLANTASK:
                sqlBuilder.setTables(AbsolutePlanDBHelper.DB_TABLE_PLANTASK);
                break;
            case MATCH_PLANTASK_ID:
                long id = ContentUris.parseId(uri);
                sqlBuilder.setTables(AbsolutePlanDBHelper.DB_TABLE_PLANTASK);
                String newSelection = AbsolutePlanContract.PlanTask.TASK_ID + " = " + id;
                sqlBuilder.appendWhere(newSelection);
                break;
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder, limit);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MATCH_PLANTASK:
                return AbsolutePlanContract.PlanTask.CONTENT_PLANTASK_DIR_TYPE;
            case MATCH_PLANTASK_ID:
                return AbsolutePlanContract.PlanTask.CONTENT_PLANTASK_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String table = null;
        switch (uriMatcher.match(uri)) {
            case MATCH_PLANTASK:
                table = AbsolutePlanDBHelper.DB_TABLE_PLANTASK;
                break;
            case MATCH_PLANTASK_ID:
                throw new UnsupportedOperationException("Cannot insert into that URI: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        return insertValues(uri, table, values);
    }

    private Uri insertValues(Uri uri, String table, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long id = -1;
        try {
            id = db.insert(table, null, values);
        } catch (Exception ex) {
            throw new SQLiteException("Failed to insert row into: " + ex);
        }

        if (id > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, id);
            resolver.notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLiteException("Failed to insert row into " + uri);
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        ContentProviderResult[] results = null;
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            results = super.applyBatch(operations);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(TAG, "" + ex);
        } finally {
            db.endTransaction();
        }
        return results;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count = 0;
        long id = 0;
        String newSelection = null;

        switch (uriMatcher.match(uri)) {
            case MATCH_PLANTASK:
                count = db.delete(AbsolutePlanDBHelper.DB_TABLE_PLANTASK, selection, selectionArgs);
                break;
            case MATCH_PLANTASK_ID:
                id = ContentUris.parseId(uri);
                newSelection = AbsolutePlanContract.PlanTask.TASK_ID + "=" + id;
                if (!TextUtils.isEmpty(selection)) {
                    newSelection = newSelection + " and " + selection;
                }
                count = db.delete(AbsolutePlanDBHelper.DB_TABLE_PLANTASK, newSelection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
        resolver.notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count = 0;
        long id = 0;
        String newSelection = null;

        switch (uriMatcher.match(uri)) {
            case MATCH_PLANTASK:
                count = db.update(AbsolutePlanDBHelper.DB_TABLE_PLANTASK, values, selection, selectionArgs);
                break;
            case MATCH_PLANTASK_ID:
                id = ContentUris.parseId(uri);
                newSelection = AbsolutePlanContract.PlanTask.TASK_ID + " = " + id;
                if (selection != null && !selection.equals("")) {
                    newSelection = newSelection + " and " + selection;
                }
                count = db.update(AbsolutePlanDBHelper.DB_TABLE_PLANTASK, values, newSelection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Error Uri:" + uri);
        }
        resolver.notifyChange(uri, null);
        return count;
    }
}