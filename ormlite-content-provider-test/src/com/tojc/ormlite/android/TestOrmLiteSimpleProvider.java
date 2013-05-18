package com.tojc.ormlite.android;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tojc.ormlite.android.test.model.Account;
import com.tojc.ormlite.android.test.provider.AccountContract;
import com.tojc.ormlite.android.test.provider.SampleHelper;

@MediumTest
public class TestOrmLiteSimpleProvider extends InstrumentationTestCase {

    private static final String TEST_NAME_1 = "Yamada Tarou";
    private static final String TEST_NAME_2 = "Stephane Nicolas";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getHelper().resetAllTables();
    }

    public void testOnInsert() {
        // given
        ContentValues values = new ContentValues();
        values.clear();
        values.put(AccountContract.NAME, TEST_NAME_1);

        // when
        getInstrumentation().getTargetContext().getContentResolver().insert(AccountContract.CONTENT_URI, values);

        // then
        RuntimeExceptionDao<Account, Integer> simpleDao = getHelper().getRuntimeExceptionDao(Account.class);
        List<Account> accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());
        assertEquals(TEST_NAME_1, accountList.get(0).getName());
    }

    public void testOnDelete() {
        // given
        Account account = new Account(TEST_NAME_2);
        RuntimeExceptionDao<Account, Integer> simpleDao = getHelper().getRuntimeExceptionDao(Account.class);
        simpleDao.create(account);
        List<Account> accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());

        // when
        getInstrumentation().getTargetContext().getContentResolver().delete(AccountContract.CONTENT_URI, BaseColumns._ID + " = " + account.getId(), null);

        // then
        accountList = simpleDao.queryForAll();
        assertEquals(0, accountList.size());
    }

    public void testOnUpdate() {
        // given
        Account account = new Account(TEST_NAME_1);
        RuntimeExceptionDao<Account, Integer> simpleDao = getHelper().getRuntimeExceptionDao(Account.class);
        simpleDao.create(account);
        List<Account> accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());

        ContentValues values = new ContentValues();
        values.clear();
        values.put(AccountContract.NAME, TEST_NAME_2);

        // when
        getInstrumentation().getTargetContext().getContentResolver().update(AccountContract.CONTENT_URI, values, BaseColumns._ID + " = " + account.getId(), null);

        // then
        accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());
        assertEquals(TEST_NAME_2, accountList.get(0).getName());
    }

    public void testOnQuery() {
        // given
        Account account1 = new Account(TEST_NAME_1);
        Account account2 = new Account(TEST_NAME_2);
        RuntimeExceptionDao<Account, Integer> simpleDao = getHelper().getRuntimeExceptionDao(Account.class);
        simpleDao.create(account1);
        simpleDao.create(account2);
        List<Account> accountList = simpleDao.queryForAll();
        assertEquals(2, accountList.size());

        // when
        Cursor cursor = getInstrumentation().getTargetContext().getContentResolver().query(AccountContract.CONTENT_URI, new String[] {BaseColumns._ID, AccountContract.NAME}, null, null, null);

        // then
        accountList = new ArrayList<Account>();
        while (cursor.moveToNext()) {
            Account account = new Account(cursor.getString(1));
            accountList.add(account);
        }
        cursor.close();
        assertEquals(2, accountList.size());
        assertEquals(TEST_NAME_1, accountList.get(0).getName());
        assertEquals(TEST_NAME_2, accountList.get(1).getName());
    }

    public void testOnQueryWithOrder() {
        // given
        Account account1 = new Account(TEST_NAME_1);
        Account account2 = new Account(TEST_NAME_2);
        RuntimeExceptionDao<Account, Integer> simpleDao = getHelper().getRuntimeExceptionDao(Account.class);
        simpleDao.create(account1);
        simpleDao.create(account2);
        List<Account> accountList = simpleDao.queryForAll();
        assertEquals(2, accountList.size());

        // when
        String order = BaseColumns._ID + " DESC";
        Cursor cursor = getInstrumentation().getTargetContext().getContentResolver().query(AccountContract.CONTENT_URI, new String[] {BaseColumns._ID, AccountContract.NAME}, null, null, order);

        // then
        accountList = new ArrayList<Account>();
        while (cursor.moveToNext()) {
            Account account = new Account(cursor.getString(1));
            accountList.add(account);
        }
        cursor.close();
        assertEquals(2, accountList.size());
        assertEquals(TEST_NAME_2, accountList.get(0).getName());
        assertEquals(TEST_NAME_1, accountList.get(1).getName());
    }

    private SampleHelper getHelper() {
        return new SampleHelper(getInstrumentation().getTargetContext());
    }
}
