package org.aplas.mylibrary.models;

import android.content.Context;

import org.aplas.mylibrary.viewmodels.Book;
import org.aplas.mylibrary.viewmodels.BookList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class BookData {
    private Context context;
    private ArrayList<String> fileList;

    private final String FILE_EXT = ".data";
    private String DATA_LOCATION;

    public BookData(Context c) {
        context=c;
        DATA_LOCATION = context.getFilesDir().getAbsolutePath()+File.separator;
        loadFileList();
    }

    public Book getBookData(String fileName) {
        String fname = (fileName.isEmpty())?fileList.get(0):fileName;
        try {
            final File file = new File(DATA_LOCATION+fname);
            if (file.exists()) {
                BufferedReader reader;
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                reader = new BufferedReader(isr);
                String line, title="", type="", year="";
                while((line = reader.readLine()) != null){
                    if (line.startsWith("Title:")) {
                        title = line.split(":")[1];
                    } else if (line.startsWith("Type:")) {
                        type = line.split(":")[1];
                    } else if (line.startsWith("Year:")) {
                        year = line.split(":")[1];
                    }
                }
                //fis.close();
                //isr.close();
                try {
                    reader.wait();
                } catch (Exception ignored) {

                }
                reader.close();
                int number = fileList.indexOf(fileName)+1;
                return new Book(number,title.trim(),type.trim(),year.trim());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BookList getBookList() {
        return new BookList(fileList);
    }

    public boolean saveBookData(String title, String type, String year) {
        String fname = getFileName(title);
        try {
            FileOutputStream fos = context.openFileOutput(fname, Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fos);
            outputWriter.write("Title: " +title+ "\nType: "
                    +type+ "\nYear: " +year);
            outputWriter.close();
            fos.close();
            loadFileList();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBookData(String filename) {
        if (context.deleteFile(filename)) {
            loadFileList();
            return true;
        } else {
            return false;
        }
    }

    public String getFileName(String title) {
        return title.replace(" ","_")+FILE_EXT;
    }

    private void loadFileList() {
        fileList = new ArrayList<>();
        File directory = new File(DATA_LOCATION);
        File[] files = directory.listFiles();
        if (files != null){
            for (int x = 0; x < files.length; x++) {
                String fname = files[x].getName();
                if (fname.endsWith(FILE_EXT)) {
                    fileList.add(fname);
                }
            }
        }
    }
}
