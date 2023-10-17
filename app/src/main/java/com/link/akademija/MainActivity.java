package com.link.akademija;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private EditText editTextIme;
    private EditText editTextPrezime;
    private EditText editTextGodinaUpisa;
    private EditText editTextBrojPoena;

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        editTextIme = findViewById(R.id.editTextIme);
        editTextPrezime = findViewById(R.id.editTextPrezime);
        editTextGodinaUpisa = findViewById(R.id.editTextGodinaUpisa);
        editTextBrojPoena = findViewById(R.id.editTextBrojPoena);

        tableLayout = findViewById(R.id.tableLayout);

        Button buttonUnesi = findViewById(R.id.buttonUnesi);
        buttonUnesi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unesiPolaznika();
            }
        });

        Button buttonPrikazi = findViewById(R.id.buttonPrikazi);
        buttonPrikazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolaznike();
            }
        });

        Button buttonObrisi = findViewById(R.id.buttonObrisi);
        buttonObrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obrisiTabelu();
            }
        });
    }

    private void unesiPolaznika() {
        String ime = editTextIme.getText().toString();
        String prezime = editTextPrezime.getText().toString();
        int godinaUpisa = Integer.parseInt(editTextGodinaUpisa.getText().toString());
        int brojPoena = Integer.parseInt(editTextBrojPoena.getText().toString());

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IME, ime);
        values.put(DatabaseHelper.COLUMN_PREZIME, prezime);
        values.put(DatabaseHelper.COLUMN_GODINA_UPISA, godinaUpisa);
        values.put(DatabaseHelper.COLUMN_BROJ_POENA, brojPoena);

        long result = database.insert(DatabaseHelper.TABLE_POLAZNIK, null, values);

        if (result != -1) {
            Toast.makeText(this, "Polaznik uspesno unet.", Toast.LENGTH_SHORT).show();
            editTextIme.setText("");
            editTextPrezime.setText("");
            editTextGodinaUpisa.setText("");
            editTextBrojPoena.setText("");
        } else {
            Toast.makeText(this, "Greska prilikom unosa polaznika.", Toast.LENGTH_SHORT).show();
        }
    }

    private void prikaziPolaznike() {
        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_POLAZNIK
                + " WHERE " + DatabaseHelper.COLUMN_BROJ_POENA + " > (SELECT MAX("
                + DatabaseHelper.COLUMN_BROJ_POENA + ") * 0.8 FROM "
                + DatabaseHelper.TABLE_POLAZNIK + ") LIMIT 5;";
        Cursor cursor = database.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
            int id = cursor.getInt(idIndex);

            int imeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IME);
            String ime = cursor.getString(imeIndex);

            int prezimeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PREZIME);
            String prezime = cursor.getString(prezimeIndex);

            int godinaUpisaIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_GODINA_UPISA);
            int godinaUpisa = cursor.getInt(godinaUpisaIndex);

            int brojPoenaIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BROJ_POENA);
            int brojPoena = cursor.getInt(brojPoenaIndex);


            TableRow tableRow = new TableRow(this);

            TextView textViewId = new TextView(this);
            textViewId.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            textViewId.setPadding(8, 8, 8, 8);
            textViewId.setText(String.valueOf(id));
            tableRow.addView(textViewId);

            TextView textViewIme = new TextView(this);
            textViewIme.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            textViewIme.setPadding(8, 8, 8, 8);
            textViewIme.setText(ime);
            tableRow.addView(textViewIme);

            TextView textViewPrezime = new TextView(this);
            textViewPrezime.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            textViewPrezime.setPadding(8, 8, 8, 8);
            textViewPrezime.setText(prezime);
            tableRow.addView(textViewPrezime);

            TextView textViewGodinaUpisa = new TextView(this);
            textViewGodinaUpisa.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            textViewGodinaUpisa.setPadding(8, 8, 8, 8);
            textViewGodinaUpisa.setText(String.valueOf(godinaUpisa));
            tableRow.addView(textViewGodinaUpisa);

            TextView textViewBrojPoena = new TextView(this);
            textViewBrojPoena.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            textViewBrojPoena.setPadding(8, 8, 8, 8);
            textViewBrojPoena.setText(String.valueOf(brojPoena));
            tableRow.addView(textViewBrojPoena);

            tableLayout.addView(tableRow);
        }

        cursor.close();
    }

    private void obrisiTabelu() {
        database.execSQL("DELETE FROM " + DatabaseHelper.TABLE_POLAZNIK + ";");
        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
        Toast.makeText(this, "Tabela Polaznik je uspesno obrisana.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
        databaseHelper.close();
    }
}