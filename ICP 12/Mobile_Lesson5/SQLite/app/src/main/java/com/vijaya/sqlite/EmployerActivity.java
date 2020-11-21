package com.vijaya.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vijaya.sqlite.databinding.ActivityEmployerBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EmployerActivity extends AppCompatActivity {

    private static final String TAG = "EmployerActivity";
    private ActivityEmployerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employer);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
            }
        });

        // Added Delete Button
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFromDB();
            }
        });

        // Added Update Button
        // Updates the name, description, and founded date of the company.
        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDB();
            }
        });

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFromDB();
            }
        });
    }

    // Updates the data in the Database
    private void updateDB(){
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();
        ContentValues value = new ContentValues();

        String updateName = binding.nameEditText.getText().toString();
        String updateDesc = binding.descEditText.getText().toString();
        long updateDate;

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyy")).parse(
                    binding.foundedEditText.getText().toString()));
            long date = calendar.getTimeInMillis();
            updateDate = date;
        } catch (Exception e){
            Log.e(TAG,"Error", e);
            Toast.makeText(this,"Date is in the wrong format", Toast.LENGTH_LONG).show();
            return;
        }

        value.put(SampleDBContract.Employer.COLUMN_NAME, updateName);
        value.put(SampleDBContract.Employer.COLUMN_DESCRIPTION, updateDesc);
        value.put(SampleDBContract.Employer.COLUMN_FOUNDED_DATE, updateDate);

        String[] updateArgs = {
                "%" + updateName + "%"
        };
        String updateClause = SampleDBContract.Employer.COLUMN_NAME + " LIKE ?";

        database.update(SampleDBContract.Employer.TABLE_NAME,value,updateClause,updateArgs);
        Toast.makeText(this,"Updated database with modified arguments",Toast.LENGTH_LONG).show();

        readFromDB();
    }

    // Deletes the displayed elements within the database.
    private void deleteFromDB(){
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

        String deleteName = binding.nameEditText.getText().toString();
        String deleteDesc = binding.descEditText.getText().toString();

        String[] deleteArgs = {
                "%" + deleteName + "%",
                "%" + deleteDesc + "%"
        };
        String deleteClause = SampleDBContract.Employer.COLUMN_NAME + " LIKE ? AND " +
                SampleDBContract.Employer.COLUMN_DESCRIPTION + " LIKE ?";

        database.delete(SampleDBContract.Employer.TABLE_NAME,deleteClause,deleteArgs);

        binding.nameEditText.setText("");
        binding.descEditText.setText("");
        binding.foundedEditText.setText(""); // In case the user put in the date, even though we didn't need it, don't want it filled
        Toast.makeText(this,"Deleted entries from database",Toast.LENGTH_LONG).show();
        readFromDB();
    }

    private void saveToDB() {
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SampleDBContract.Employer.COLUMN_NAME, binding.nameEditText.getText().toString());
        values.put(SampleDBContract.Employer.COLUMN_DESCRIPTION, binding.descEditText.getText().toString());

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(
                    binding.foundedEditText.getText().toString()));
            long date = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employer.COLUMN_FOUNDED_DATE, date);
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
            Toast.makeText(this, "Date is in the wrong format", Toast.LENGTH_LONG).show();
            return;
        }
        long newRowId = database.insert(SampleDBContract.Employer.TABLE_NAME, null, values);

        // Clear text from the inputs
        binding.nameEditText.setText("");
        binding.descEditText.setText("");
        binding.foundedEditText.setText("");

        Toast.makeText(this, "The new Row id is " + newRowId, Toast.LENGTH_LONG).show();
    }

    private void readFromDB() {
        String name = binding.nameEditText.getText().toString();
        String desc = binding.descEditText.getText().toString();
        long date = 0;

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(
                    binding.foundedEditText.getText().toString()));
            date = calendar.getTimeInMillis();
        } catch (Exception e) {
        }

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

        String[] projection = {
                SampleDBContract.Employer._ID,
                SampleDBContract.Employer.COLUMN_NAME,
                SampleDBContract.Employer.COLUMN_DESCRIPTION,
                SampleDBContract.Employer.COLUMN_FOUNDED_DATE
        };

        String selection =
                SampleDBContract.Employer.COLUMN_NAME + " like ? and " +
                        SampleDBContract.Employer.COLUMN_FOUNDED_DATE + " > ? and " +
                        SampleDBContract.Employer.COLUMN_DESCRIPTION + " like ?";

        String[] selectionArgs = {"%" + name + "%", date + "", "%" + desc + "%"};

        Cursor cursor = database.query(
                SampleDBContract.Employer.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                             // don't group the rows
                null,                              // don't filter by row groups
                null                              // don't sort
        );

        binding.recycleView.setAdapter(new SampleRecyclerViewCursorAdapter(this, cursor));
        // Even though the database is displayed, I want to make sure the user knows that the database
        // has been reloaded.
        Toast.makeText(this,"Displaying database entries",Toast.LENGTH_SHORT).show();
    }
}