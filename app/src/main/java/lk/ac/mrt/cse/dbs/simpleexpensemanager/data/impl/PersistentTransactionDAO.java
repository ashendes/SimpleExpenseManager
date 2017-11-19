package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.SQLite_database.DBConnection;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.SQLite_database.DB_Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by ASUS on 11/19/2017.
 */

public class PersistentTransactionDAO implements TransactionDAO {

    DBConnection db;
    private static  final SimpleDateFormat dateFormat = new SimpleDateFormat("mm-dd-yyyy");
    Context context;
    String toast;

    public PersistentTransactionDAO(Context context){
        db= DBConnection.getConnection(context);
        this.context=context;
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues values = new ContentValues();
        values.put(DB_Constants.tr_COL1,accountNo);
        values.put(DB_Constants.tr_COL2, dateFormat.format(date));
        values.put(DB_Constants.tr_COL3,expenseType.toString());
        values.put(DB_Constants.tr_COL4,amount);
        long result = db.getDb().insert(DB_Constants.table_trans,null, values);
        if(result == -1){
            toast = "Transaction failed!";
        }
        else
            toast = "Transaction successful!";
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();
        Cursor cursor = db.getDb().rawQuery("SELECT * FROM " + DB_Constants.table_trans + ";",null);

        if(cursor.getCount()==0){
            toast = "Transactions unavailable";
            //Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
        }
        else {
            if(cursor.moveToFirst()) {
                do {
                    try {
                        String accountNo = cursor.getString(cursor.getColumnIndex(DB_Constants.tr_COL1));
                        Date date = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DB_Constants.tr_COL2)));
                        String expenseTypeString = cursor.getString(cursor.getColumnIndex(DB_Constants.tr_COL3));
                        double amount = cursor.getDouble(cursor.getColumnIndex(DB_Constants.tr_COL4));
                        ExpenseType expenseType;
                        if (expenseTypeString.equals(ExpenseType.EXPENSE.toString())){
                            expenseType = ExpenseType.EXPENSE;
                        }
                        else{
                            expenseType = ExpenseType.INCOME;
                        }

                        Transaction transaction = new Transaction(date,accountNo,expenseType,amount);
                        transactions.add(transaction);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }while (cursor.moveToNext()) ;
            }
        }

        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = getAllTransactionLogs();

        if (transactions.isEmpty()){
            return new ArrayList<Transaction>();
        }
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        } else {

        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }
}
