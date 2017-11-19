package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.SQLite_database.DBConnection;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.SQLite_database.DB_Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by ASUS on 11/19/2017.
 */

public class PersistentAccountDAO implements AccountDAO {

    DBConnection db;
    String toast;
    Context context;

    public PersistentAccountDAO(Context context){
        db = DBConnection.getConnection(context);
        this.context=context;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNoList = new ArrayList<>();
        Cursor cursor = db.getDb().rawQuery("SELECT "+ DB_Constants.ac_COL1 +" FROM " + DB_Constants.table_acc + ";",null);

        if(cursor.getCount()==0){
            toast = "No accounts available!";
            //Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
        }
        else {
            if (cursor.moveToFirst()) {
                do {
                    String acc_no = cursor.getString(cursor.getColumnIndex(DB_Constants.ac_COL1));
                    accountNoList.add(acc_no);
                } while (cursor.moveToNext());
            }
        }
        return accountNoList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountsList = new ArrayList<>();

        Cursor cursor = db.getDb().rawQuery("SELECT * FROM " + DB_Constants.table_acc + ";",null);

        if(cursor.getCount()==0){
            toast = "No accounts available";
            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
            return null;
        }
        else {
            if (cursor.moveToFirst()) {
                do {
                    String accountNo = cursor.getString(cursor.getColumnIndex(DB_Constants.ac_COL1));
                    String branchName = cursor.getString(cursor.getColumnIndex(DB_Constants.ac_COL2));
                    String acc_holders_name = cursor.getString(cursor.getColumnIndex(DB_Constants.ac_COL3));
                    double balance = cursor.getDouble(cursor.getColumnIndex(DB_Constants.ac_COL4));
                    Account account = new Account(accountNo,branchName,acc_holders_name,balance);
                    accountsList.add(account);
                } while (cursor.moveToNext());
            }
        }
        return accountsList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account = null;
        Cursor cursor = db.getDb().rawQuery("SELECT * FROM " + DB_Constants.table_acc + " WHERE " + DB_Constants.ac_COL1 +" = "+accountNo+" LIMIT 1;",null);

        if(cursor.getCount()==0){
            toast = "Invalid account number!";
            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
            return null;
        }
        else {
            if (cursor.moveToFirst()) {
                String branchName = cursor.getString(cursor.getColumnIndex(DB_Constants.ac_COL2));
                String acc_holders_name = cursor.getString(cursor.getColumnIndex(DB_Constants.ac_COL3));
                double balance = cursor.getDouble(cursor.getColumnIndex(DB_Constants.ac_COL4));

                account = new Account(accountNo,branchName,acc_holders_name,balance);
            }
        }
        return account;
    }

    @Override
    public void addAccount(Account account) {
        ContentValues values = new ContentValues();

        values.put(DB_Constants.ac_COL1,account.getAccountNo());
        values.put(DB_Constants.ac_COL2,account.getBankName());
        values.put(DB_Constants.ac_COL3,account.getAccountHolderName());
        values.put(DB_Constants.ac_COL4,account.getBalance());

        long state = db.getDb().insert(DB_Constants.table_acc,null,values);
        if(state == -1){
            toast = "Account not created!";
        }
        else
            toast = "Account created!";
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        int state = db.getDb().delete(DB_Constants.table_acc,DB_Constants.ac_COL1+" = "+accountNo + ";",null );

        if(state == 0){
            toast = "Unable to delete account!";
            throw new InvalidAccountException("Account invalid");
        }
        else
            toast = "Account deleted";
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        if (account == null) {
            throw new InvalidAccountException("Invalid account");
        }
        else {
            switch (expenseType) {
                case INCOME:
                    account.setBalance(account.getBalance() + amount);
                    break;
                case EXPENSE:
                    account.setBalance(account.getBalance() - amount);
                    break;
            }

            ContentValues values = new ContentValues();
            values.put(DB_Constants.ac_COL4, account.getBalance());

            int state = db.getDb().update(DB_Constants.table_acc, values, DB_Constants.ac_COL1 + " = " + account.getAccountNo() + ";", null);

            if (state == 0) {
                toast = "Balance update failed!";
            } else {
                toast = "Balance updated!";
            }
            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
        }
    }
}
