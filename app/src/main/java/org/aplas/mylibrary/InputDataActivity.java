package org.aplas.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.aplas.mylibrary.databinding.ActivityInputDataBinding;
import org.aplas.mylibrary.models.BookData;
import org.aplas.mylibrary.models.UserData;
import org.aplas.mylibrary.viewmodels.Book;

import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class InputDataActivity extends AppCompatActivity {
    UserData userdata;
    BookData bookdata;
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInputDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_input_data);

        userdata = new UserData(getApplicationContext());
        bookdata = new BookData(getApplicationContext());

        binding.setUser(userdata.getUserData());

        if (getIntent().getExtras() != null) {
            filename = getIntent().getStringExtra("filename");
            bookdata = new BookData(getApplicationContext());
            Book book = bookdata.getBookData(filename);
            binding.setBook(book);

            String[] types = getResources().getStringArray(R.array.book_types);
            int idx = Arrays.asList(types).indexOf(book.getBookType());
            binding.spType.setSelection(idx);
        }

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdata.clearUserData();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        binding.btnSaveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookTitle = binding.editTitle.getText().toString().trim();
                String bookType = binding.spType.getSelectedItem().toString();
                String bookYear = binding.editYear.getText().toString().trim();

                if (!bookTitle.isEmpty() && !bookYear.isEmpty())  {
                    //Save
                    if (bookdata.saveBookData(bookTitle,bookType,bookYear)) {
                        Intent intent = new Intent(getApplicationContext(), ShowDataActivity.class);
                        intent.putExtra("filename", bookdata.getFileName(bookTitle));
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Storing data is failed!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Title, type, and year must be filled!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowDataActivity.class);
                intent.putExtra("filename", "");
                startActivity(intent);
            }
        });
    }

}