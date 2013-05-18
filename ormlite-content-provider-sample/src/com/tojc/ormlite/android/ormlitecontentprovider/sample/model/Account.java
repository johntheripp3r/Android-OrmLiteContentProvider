package com.tojc.ormlite.android.ormlitecontentprovider.sample.model;

import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultSortOrder;

@DatabaseTable(tableName = "accounts")
@DefaultContentUri(authority = "com.tojc.ormlite.android.ormlitecontentprovider.sample", path = "accounts")
@DefaultContentMimeTypeVnd(name = "com.tojc.ormlite.android.ormlitecontentprovider.sample.provider", type = "accounts")
public class Account {

    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @DefaultSortOrder
    private int id;

    @DatabaseField
    private String name;

    public Account() {
        // ORMLite needs a no-arg constructor
    }

    public Account(String name) {
        this.id = 0;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
