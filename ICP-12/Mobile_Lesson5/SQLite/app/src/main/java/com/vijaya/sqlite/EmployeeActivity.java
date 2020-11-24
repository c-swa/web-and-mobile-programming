package com.vijaya.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vijaya.sqlite.databinding.ActivityEmployeeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EmployeeActivity extends AppCompatActivity {

    private ActivityEmployeeBinding binding;
    private static final String TAG = "EmployeeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        String[] queryCols = new String[]{"_id", SampleDBContract.Employer.COLUMN_NAME};
        String[] adapterCols = new String[]{SampleDBContract.Employer.COLUMN_NAME};
        int[] adapterRowViews = new int[]{android.R.id.text1};

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();
        Cursor cursor = database.query(
                SampleDBContract.Employer.TABLE_NAME,     // The table to query
                queryCols,                                // The columns to return
                null,                             // The columns for the WHERE clause
                null,                          // The values for the WHERE clause
                null,                             // don't group the rows
                null,                              // don't filter by row groups
                null                              // don't sort
        );

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_spinner_item, cursor, adapterCols, adapterRowViews, 0);
        cursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.employerSpinner.setAdapter(cursorAdapter);

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
            }
        });

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFromDB();
            }
        });

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFromDB();
            }
        });

        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDB();
            }
        });
    }

    private void updateDB() {
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();

        String updateFirstName = binding.firstnameEditText.getText().toString();
        String updateLastName = binding.lastnameEditText.getText().toString();
        String updateJobDesc = binding.jobDescEditText.getText().toString();

        int employerSelection = ((Cursor)binding.employerSpinner.getSelectedItem()).getInt(0);

        long dob;
        long doe;

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(binding.dobEditText.getText().toString()));
            dob = calendar.getTimeInMillis();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(binding.employedEditText.getText().toString()));
            doe = calendar.getTimeInMillis();
        } catch (Exception e){
            Log.e(TAG,"Error thrown:",e);
            Toast.makeText(this, "Date is in the wrong format",Toast.LENGTH_LONG).show();
            return;
        }

        values.put(SampleDBContract.Employee.COLUMN_EMPLOYER_ID,employerSelection);
        values.put(SampleDBContract.Employee.COLUMN_JOB_DESCRIPTION,updateJobDesc);
        values.put(SampleDBContract.Employee.COLUMN_DATE_OF_BIRTH,dob);
        values.put(SampleDBContract.Employee.COLUMN_EMPLOYED_DATE,doe);

        String updateClause = SampleDBContract.Employee.COLUMN_LASTNAME + " LIKE ? AND " +
                SampleDBContract.Employee.COLUMN_FIRSTNAME + " LIKE ?";
        String[] updateArgs = {
                "%" + updateLastName + "%",
                "%" + updateFirstName + "%"
        };

        database.update(SampleDBContract.Employee.TABLE_NAME,values,updateClause,updateArgs);
        Toast.makeText(this,"Updated employee(s) information with updated data",Toast.LENGTH_LONG);
        readFromDB();

        // Clear inputs
        binding.lastnameEditText.setText("");
        binding.firstnameEditText.setText("");
        binding.jobDescEditText.setText("");
        binding.dobEditText.setText("");
        binding.employedEditText.setText("");
    }

    private void deleteFromDB() {
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

        String deleteFirstName = binding.firstnameEditText.getText().toString();
        String deleteLastName = binding.lastnameEditText.getText().toString();

        String deleteClause = SampleDBContract.Employee.COLUMN_FIRSTNAME +
                " LIKE ? AND " + SampleDBContract.Employee.COLUMN_LASTNAME +
                " LIKE ?";
        String[] deleteArgs = {
                "%" + deleteFirstName + "%",
                "%" + deleteLastName + "%"
        };

        database.delete(SampleDBContract.Employee.TABLE_NAME,deleteClause,deleteArgs);
        binding.firstnameEditText.setText("");
        binding.lastnameEditText.setText("");

        Toast.makeText(this, "Deleted item(s) from database", Toast.LENGTH_LONG).show();
        readFromDB();
        // Clear inputs
        binding.lastnameEditText.setText("");
        binding.firstnameEditText.setText("");
        binding.jobDescEditText.setText("");
        binding.dobEditText.setText("");
        binding.employedEditText.setText("");
    }

    private void saveToDB() {
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SampleDBContract.Employee.COLUMN_FIRSTNAME, binding.firstnameEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_LASTNAME, binding.lastnameEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_JOB_DESCRIPTION, binding.jobDescEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_EMPLOYER_ID,
                ((Cursor) binding.employerSpinner.getSelectedItem()).getInt(0));

        Log.d("getINT", ((Cursor) binding.employerSpinner.getSelectedItem()).getInt(0) + "");
        Log.d("getColumnName", ((Cursor) binding.employerSpinner.getSelectedItem()).getColumnName(0));

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(
                    binding.dobEditText.getText().toString()));
            long date = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employee.COLUMN_DATE_OF_BIRTH, date);

            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(
                    binding.employedEditText.getText().toString()));
            date = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employee.COLUMN_EMPLOYED_DATE, date);
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
            Toast.makeText(this, "Date is in the wrong format", Toast.LENGTH_LONG).show();
            return;
        }
        long newRowId = database.insert(SampleDBContract.Employee.TABLE_NAME, null, values);

        Toast.makeText(this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
        // Clear inputs
        binding.lastnameEditText.setText("");
        binding.firstnameEditText.setText("");
        binding.jobDescEditText.setText("");
        binding.dobEditText.setText("");
        binding.employedEditText.setText("");
    }

    private void readFromDB() {
        String firstName = binding.firstnameEditText.getText().toString();
        String lastName = binding.lastnameEditText.getText().toString();

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

        String[] selectionArgs = {"%" + firstName + "%", "%" + lastName + "%"};

        Cursor cursor = database.rawQuery(SampleDBContract.SELECT_EMPLOYEE_WITH_EMPLOYER, selectionArgs);
        binding.recycleView.setAdapter(new SampleJoinRecyclerViewCursorAdapter(this, cursor));
        // Clear inputs
        binding.lastnameEditText.setText("");
        binding.firstnameEditText.setText("");
        binding.jobDescEditText.setText("");
        binding.dobEditText.setText("");
        binding.employedEditText.setText("");
    }
}