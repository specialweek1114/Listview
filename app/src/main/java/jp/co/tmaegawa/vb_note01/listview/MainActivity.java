package jp.co.tmaegawa.vb_note01.listview;
// C:\Users\vb_note01\Documents\android_lesson\calc2.jks

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ゲーム終了後ポップアップの各ID取得
        final ConstraintLayout dummy_page1 = findViewById(R.id.dummy_page1);
        final ConstraintLayout dummy_page2 = findViewById(R.id.dummy_page2);
        dummy_page1.setVisibility(View.INVISIBLE);//最初は非表示
        dummy_page2.setVisibility(View.INVISIBLE);//最初は非表示
        final TextView resultScore = this.findViewById(R.id.result_score);
        final TextView result_message = this.findViewById(R.id.result_message);

        // SQLite接続用のオブジェクト生成
        final SQLiteOpenHelper helper = new MySQLiteOpenHelper(this);//contextはどのアクティビティ

        // スコアランキングリセット
//        SQLiteDatabase db3 = helper.getWritableDatabase();
//        try {
//            db3.delete("score", "1", null);
//        }finally {
//            db3.close();
//        }

        // リストビュー宣言
        List<String> PointList = new ArrayList<>();
        final List<ListViewData> ScoreList = new ArrayList<>();

        // ゲーム画面用リストビュー表示用
        for (int i = 1; i <= 100; i++) {
            PointList.add(""+i+"");
        }
        for (int i = 101; i <= 130; i++) {
            PointList.add("dead");
        }

        // リストビューをとってくる
        final ListView listView = this.findViewById(R.id.list_view);
        final ListView ScorelistView = this.findViewById(R.id.list_view_score);

        //リストビューを先頭に移動
        listView.setSelection(0);
        ScorelistView.setSelection(0);

        // ゲーム画面用リストアダプター
        ArrayAdapter<String> ListViewAdaper = new ArrayAdapter<>(
                this,
                R.layout.list_view_inner2,
                R.id.text1,
                PointList
        );

        // アダプター経由でスコア用のListViewに　スコアランキング　をいれる
        ListAdapter ScoreListViewAdapter = new ArrayAdapter<ListViewData>(
                this.getApplicationContext(),
                R.layout.list_view_inner,
                ScoreList
        ){
            @Override
            public View getView(
                    int position,
                    View convertView,
                    ViewGroup parent
            ){
                LayoutInflater mInflater = LayoutInflater.from(this.getContext());

                if (convertView == null){
                    convertView = mInflater.inflate(R.layout.list_view_inner, parent, false);
                }

                ListViewData rowData = getItem(position);

                TextView fit = convertView.findViewById(R.id.first_text);
                fit.setText(rowData.getFirstText());

                TextView st = convertView.findViewById(R.id.second_text);
                st.setText(rowData.getSecondText());

                TextView tt = convertView.findViewById(R.id.third_text);
                tt.setText(rowData.getThirdText());

                TextView fot = convertView.findViewById(R.id.fourth_text);
                fot.setText(rowData.getFourthText());

                return convertView;
            }
        };

        // アダプタをセット
        listView.setAdapter(ListViewAdaper);
        ScorelistView.setAdapter(ScoreListViewAdapter);

        // スクロール開始後の処理
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int firstItem;
            String message;

            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onScrollStateChanged(AbsListView view, final int scrollState) {
                if (scrollState == 0) {
                    ScoreList.clear();
                    // DB保存データ変数
                    ContentValues values = new ContentValues();

                    //点数、メッセージのDB保存と表示の準備
                    values.put("score",this.firstItem);
                    resultScore.setText(""+this.firstItem+"point");
                    if (this.firstItem <= 50) {
                        message = "チキン野郎！";
                    }else if( this.firstItem > 50 && this.firstItem <= 75 ) {
                        message = "なかなか ";
                    }else if( this.firstItem > 75 && this.firstItem <= 90 ) {
                        message = "勇気あり ";
                    }else if( this.firstItem > 90 && this.firstItem <= 99 ) {
                        message = "真の勇者 ";
                    }else if( this.firstItem == 100 ) {
                        message = "パーフェクト！";
                    }else if( this.firstItem > 100 ){
                        resultScore.setText("dead");
                        values.put("score",0);
                        message = "死にました";
                    }
                    result_message.setText(message);
                    values.put("message",message);

                    // プレイ日時取得
                    Calendar cTime = Calendar.getInstance();
                    cTime.add(Calendar.HOUR, 9);
                    Date time = cTime.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    values.put("date",sdf.format(time).toString());

                    // スコアのDB保存
                    SQLiteDatabase db = helper.getWritableDatabase();
                    try {
                        db.insert("score", null, values);
                    }finally {
                        db.close();
                    }

                    // スコアランキングの取得
                    ScoreList.add(new ListViewData("rank","score","message","date"));
                    SQLiteDatabase db2 = helper.getWritableDatabase();
                    try{
                        Cursor cursor = db2.rawQuery("SELECT score, message, date FROM score ORDER BY score DESC",null);
                        if(cursor.getCount() != 0) {
                            String rank;
                            for (cursor.moveToFirst(); !cursor.isLast() ;cursor.moveToNext()){
                                rank = ""+(cursor.getPosition()+1)+"";
                                ScoreList.add(new ListViewData(rank,cursor.getString(0),cursor.getString(1),cursor.getString(2)));
                            }
                        }
                    }finally {
                        db2.close();
                    }

                    //　スクロール1回したらスクロール禁止
                    listView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (scrollState != 0) {
                                return false;
                            } else {
                                /** タッチイベントをHandlingしたよ！　てことだとおもいます。*/
                                /** falseを返すと、スクロールするようになります。*/
                                return true;
                            }
                        }
                    });

                    // スコア表示をアクティブにする
                    dummy_page1.setVisibility(View.VISIBLE);
                    dummy_page2.setVisibility(View.VISIBLE);

                    //　restartボタンクリックでゲーム再スタート
                    Button restart = findViewById(R.id.restart_button);
                    restart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // ボタンを非アクティブにする
                            dummy_page1.setVisibility(View.INVISIBLE);
                            dummy_page2.setVisibility(View.INVISIBLE);

                            //リストビューを先頭に移動
                            listView.setSelection(0);
                            ScorelistView.setSelection(0);

                            // スクロール有効化
                            listView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    return false;
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstItemIndex, int visibleItem, int toatlItemCount) {
                this.firstItem = firstItemIndex;
            }
        });
    }

//    inner class
    class ListViewData {
        private final String firstText;
        private final String secondText;
        private final String thirdText;
        private final String fourthText;

    public String getFirstText() {
        return firstText;
    }

    public String getSecondText() {
        return secondText;
    }

    public String getThirdText() {
        return thirdText;
    }

    public String getFourthText() {
        return fourthText;
    }

    //        コンストラクタ
        public ListViewData( String firstText, String secondText, String thirdText, String fourthText){
            this.firstText = firstText;
            this.secondText = secondText;
            this.thirdText = thirdText;
            this.fourthText = fourthText;
        }
    }

    class MySQLiteOpenHelper extends SQLiteOpenHelper {

        private static final int DB_VERSION = 1;
        private static final String DB_NAME = "chicken_db";

        public MySQLiteOpenHelper(Context context){
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE score (\n"
                    + "  score integer not null,\n"
                    + "  message text not null,\n"
                    + "  date text not null\n"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            db.execSQL("ALTER TABLE hiragana ADD COLUMN price integer;");
        }
    }

}
