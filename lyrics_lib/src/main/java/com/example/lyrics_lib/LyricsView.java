package com.example.lyrics_lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by admin on 7/17/2018.
 */

public class LyricsView extends ScrollView{
    private int originalColor = Color.parseColor("#000000");
    private int changeColor = Color.parseColor("#2E2EFE");
    private float textSize = 36;
    private int rowScroll = 0;
    private ArrayList<Row> listRow = new ArrayList<>();
    private int rowPass = -1;
    private LinearLayout layout = new LinearLayout(getContext());
    private int count = 1;

    public LyricsView(Context context) {
        this(context,null);
    }

    public LyricsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LyricsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttribute();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LyricsView);
        originalColor = typedArray.getColor(R.styleable.LyricsView_original_color, originalColor);
        changeColor = typedArray.getColor(R.styleable.LyricsView_change_color, changeColor);
        textSize = typedArray.getDimensionPixelSize(R.styleable.LyricsView_text_size, (int) textSize);
        rowScroll = typedArray.getInt(R.styleable.LyricsView_row_scroll, rowScroll);
        typedArray.recycle();
    }

    public void loadFile(File file) {
        readFile(file);
        initLyrics();
    }

    public void loadFile(URL url) {
        new ReadFile().execute(url);
    }

    //chạy lyrics kiểu karaoke tại thời điểm timeCurrent
    public void runKaraoke(int timeCurrent) {
        int rowCurrent = getRowCurrent(timeCurrent);
        if (rowCurrent == listRow.size() - 1 && rowCurrent > rowPass) {
            int time = timeCurrent - listRow.get(rowCurrent).getTime();
            handleKaraoke(rowCurrent, time);
        }
        else if (rowCurrent > -1 && rowCurrent > rowPass) {
            int time = listRow.get(rowCurrent + 1).getTime() - listRow.get(rowCurrent).getTime();
            handleKaraoke(rowCurrent, time);
        }
    }

    //chạy lyrics kiểu hightlight tại thời điểm timeCurrent
    public void runHightLight(int timeCurrent) {
        int rowCurrent = getRowCurrent(timeCurrent);
        if (rowCurrent > rowPass) {
            handleHightLight(rowCurrent);
        }
        if (rowPass == listRow.size() - 1) {
            rowPass = -1;
        }
    }

    //update lyrics kiểu karaoke cho đến thời điểm timeCurrent
    public void updateKaraoke(int timeCurrent) {
        int rowCurrent = getRowCurrent(timeCurrent);
        rowPass = rowCurrent - 1;
        //bôi màu những dòng đã qua
        for (int i = 0 ; i <= rowPass ; i++) {
            GradienTextView gtv = (GradienTextView) layout.getChildAt(i);
            gtv.start(Orientation.LEFT_TO_RIGHT, 0);
        }
        //reset những dòng chưa tới
        for (int i = rowCurrent ; i < listRow.size() ; i++) {
            if (i > -1) {
                GradienTextView gtv = (GradienTextView) layout.getChildAt(i);
                gtv.reset();
            }
        }
    }

    //update lyrics kiểu hightlight cho đến thời điểm timeCurrent
    public void updateHightLight(int timeCurrent) {
        int rowCurrent = getRowCurrent(timeCurrent);
        rowPass = rowCurrent - 1;
        //reset tất cả các dòng
        for (int i = 0 ; i < listRow.size() ; i++) {
            GradienTextView gtv = (GradienTextView) layout.getChildAt(i);
            gtv.reset();
        }
    }

    //update OriginalColor
    public void setOriginalColor(String color) {
        originalColor = Color.parseColor(color);
        initLyrics();
    }

    //update ChangeColor
    public void setChangeColor(String color) {
        changeColor = Color.parseColor(color);
        initLyrics();
    }

    //update TextSize
    public void setTextSize(float size) {
        textSize = size;
        initLyrics();
    }

    //update row mà Scroll sẽ cuộn tới
    public void setRowScroll(int row) {
        rowScroll = row;
    }

    private int getRowCurrent(int timeCurrent) {
        int rowCurrent = -1;
        for (int i = 0; i < listRow.size() - 1; i++) {
            if (timeCurrent < listRow.get(i + 1).getTime() && timeCurrent > listRow.get(i).getTime()) {
                rowCurrent = i;
                break;
            }
            else if (timeCurrent > listRow.get(i + 1).getTime()) {
                rowCurrent = i + 1;
            }
        }
        return rowCurrent;
    }

    private void initLyrics() {
        rowPass = -1;
        layout.removeAllViews();
        for (int i = 0; i < listRow.size() ; i++) {
            GradienTextView gtv = new GradienTextView(getContext());
            gtv.setText(listRow.get(i).getLyrics());
            gtv.setPadding(0,8,0,8);
            gtv.setOriginalColor(originalColor);
            gtv.setChangeColor(changeColor);
            gtv.setTextSize(textSize);
            gtv.init();

            layout.addView(gtv);
        }
    }

    private int convertTime(String time) {
        time = time.replace(".",":");
        String[] t = time.split(":");
        return Integer.parseInt(t[0])*60*1000 + Integer.parseInt(t[1])*1000 + Integer.parseInt(t[2]);
    }

    private void readFile(File file) {
        listRow.clear();
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            String line = "";
            while ((line = reader.readLine()) != null) {
                int begin = line.indexOf("[");
                int end = line.indexOf("]");
                String time = line.substring(begin + 1,end);
                String lyrics = line.substring(end + 1);

                Row row = new Row();
                row.setTime(convertTime(time));
                row.setLyrics(lyrics);
                listRow.add(row);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setAttribute() {
        layout.setOrientation(LinearLayout.VERTICAL);
        this.addView(layout);
    }

    private class ReadFile extends AsyncTask<URL,Void,Void> {

        @Override
        protected Void doInBackground(URL... urls) {
            listRow.clear();
            try {
                InputStreamReader isr = new InputStreamReader(urls[0].openStream());
                BufferedReader reader = new BufferedReader(isr);
                String line = "";
                while ((line = reader.readLine()) != null) {
                    int begin = line.indexOf("[");
                    int end = line.indexOf("]");
                    String time = line.substring(begin + 1,end);
                    String lyrics = line.substring(end + 1);

                    Row row = new Row();
                    row.setTime(convertTime(time));
                    row.setLyrics(lyrics);
                    listRow.add(row);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            initLyrics();
        }
    }

    private void handleKaraoke(int rowCurrent, int time) {
        GradienTextView gtv = (GradienTextView) layout.getChildAt(rowCurrent);
        gtv.start(Orientation.LEFT_TO_RIGHT, time);

        //scroll lyrics
        if (rowCurrent <= rowScroll) {
            int y = layout.getChildAt(0).getTop();
            this.smoothScrollTo(0, y);
        }
        else if (rowCurrent < listRow.size() - rowScroll) {
            int y = layout.getChildAt(rowCurrent - rowScroll).getTop();
            this.smoothScrollTo(0,y);
        }

        //update lyrics đã chạy
        rowPass = rowCurrent;
    }

    private void handleHightLight(int rowCurrent) {
        //hightlight lyrics
        GradienTextView gtv = (GradienTextView) layout.getChildAt(rowCurrent);
        gtv.start(Orientation.LEFT_TO_RIGHT, 0);

        String content = gtv.getText().toString();
        if (content.equals("") || content.equals(null)) {
            //đếm số dòng có nội dung rỗng hoặc null
            count ++;
        }
        else if (rowCurrent > 0) {
            //reset dòng lyrics
            GradienTextView gtvPre = (GradienTextView) layout.getChildAt(rowCurrent - count);
            count = 1;
            gtvPre.reset();
        }

        //scroll lyrics
        if (rowCurrent <= rowScroll) {
            int y = layout.getChildAt(0).getTop();
            this.smoothScrollTo(0, y);
        }
        else if (rowCurrent < listRow.size()) {
            int y = layout.getChildAt(rowCurrent - rowScroll).getTop();
            this.smoothScrollTo(0,y);
        }

        //update lyrics đã chạy
        rowPass = rowCurrent;
    }
}
