package lk.ac.mrt.cse.dbs.simpleexpensemanager.SQLite_database;

/**
 * Created by ASUS on 11/19/2017.
 */

public class DB_Constants {

    public static final String DB_name = "150109N.db";

    public static final String table_acc = "ACCOUNTS";
    public static final String ac_COL1 = "acc_no";
    public static final String ac_COL2 = "branch_name";
    public static final String ac_COL3 = "acc_holders_name";
    public static final String ac_COL4 = "balance";

    public static final String table_trans  = "TRANSACTIONS";
    public static final String tr_COL1 = "acc_no";
    public static final String tr_COL2 = "date";
    public static final String tr_COL3 = "expense_type";
    public static final String tr_COL4 = "amount";
    public static final String tr_COL5 = "transaction_id";


}
