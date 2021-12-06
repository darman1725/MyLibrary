package org.aplas.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.aplas.mylibrary.databinding.ActivityShowDataBinding;
import org.aplas.mylibrary.models.BookData;
import org.aplas.mylibrary.models.UserData;
import org.aplas.mylibrary.viewmodels.Book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


public class ShowDataActivity extends AppCompatActivity {

    UserData userdata;
    BookData bookdata;
    int bookindex = 0;
    ActivityShowDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_data);

        userdata = new UserData(getApplicationContext());
        bookdata = new BookData(getApplicationContext());

        String filename = getIntent().getStringExtra("filename");

        binding.setUser(userdata.getUserData());
        if (filename == null || filename.equals("")) {
            binding.setBooklist(bookdata.getBookList());
            if (bookdata.getBookList().getSize() > 0){
                String title = bookdata.getBookList().getList().get(0);
                Book book = bookdata.getBookData(title);
                binding.setBook(book);
            } else {
                binding.setBooklist(bookdata.getBookList());
                Book book = new Book();
                binding.setBook(book);
            }
        } else {
            binding.setBooklist(bookdata.getBookList());
            binding.setBook(bookdata.getBookData(filename));
            bookindex = bookdata.getBookList().getList().indexOf(filename);
            binding.spBook.setSelection(bookindex);
        }

        binding.spBook.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String filename = binding.spBook.getSelectedItem().toString();
                binding.setBook(bookdata.getBookData(filename));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                return;
            }

        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdata.clearUserData();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        binding.btnEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InputDataActivity.class);
                intent.putExtra("filename", binding.spBook.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        binding.btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InputDataActivity.class);
                startActivity(intent);
            }
        });

        binding.btnDelData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.spBook.getCount()>0) {
                    String filename = binding.spBook.getSelectedItem().toString().trim();
                    if (bookdata.deleteBookData(filename)) {
                        binding.setBooklist(bookdata.getBookList());
                        Toast.makeText(getApplicationContext(), "Deleting is Success!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Deleting is Failed!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "There is no Book Data!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}