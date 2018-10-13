package com.example.vb_note01.listview;
// C:\Users\vb_note01\Documents\android_lesson\calc2.jks

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConstraintLayout dummy_page1 = findViewById(R.id.dummy_page1);
        final ConstraintLayout dummy_page2 = findViewById(R.id.dummy_page2);
        dummy_page1.setVisibility(View.INVISIBLE);
        dummy_page2.setVisibility(View.INVISIBLE);

        final TextView resultScore = this.findViewById(R.id.result_score);
        final TextView result_message = this.findViewById(R.id.result_message);

        List<ListViewData> hiraganaList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            hiraganaList.add(new ListViewData(""+i+"", ""+i+""));
        }
        for (int i = 101; i <= 130; i++) {
            hiraganaList.add(new ListViewData("dead", "dead"));
        }
        // リストビューをとってくる
        final ListView listView = this.findViewById(R.id.list_view);

        // アダプター経由でListViewに　あいうえお　をいれる
        ListAdapter ListViewAdapter = new ArrayAdapter<ListViewData>(
                this.getApplicationContext(),
                R.layout.list_view_inner,
                hiraganaList
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

                TextView lt = convertView.findViewById(R.id.left_text);
                lt.setText(rowData.getLeftText());

                TextView rt = convertView.findViewById(R.id.right_text);
                rt.setText(rowData.getRightText());

                return convertView;
            }
        };

        // アダプタをセット
        listView.setAdapter(ListViewAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int firstItem;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onScrollStateChanged(AbsListView view, final int scrollState) {
                if (scrollState == 0) {
                    resultScore.setText(""+this.firstItem+"point");
                    if (this.firstItem <= 50) {
                        result_message.setText("チキン野郎！");
                    }else if( this.firstItem > 50 && this.firstItem <= 75 ) {
                        result_message.setText("なかなか ");
                    }else if( this.firstItem > 75 && this.firstItem <= 90 ) {
                        result_message.setText("勇気あり ");
                    }else if( this.firstItem > 90 && this.firstItem <= 99 ) {
                        result_message.setText("真の勇者 ");
                    }else if( this.firstItem == 100 ) {
                        result_message.setText("パーフェクト！");
                    }else if( this.firstItem > 100 ){
                        result_message.setText("死にました");
                    }


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

                    // ボタンをアクティブにする
                    dummy_page1.setVisibility(View.VISIBLE);
                    dummy_page2.setVisibility(View.VISIBLE);

                    //　ボタンクリックでゲーム再スタート
                    Button restart = findViewById(R.id.restart_button);
                    restart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // ボタンを非アクティブにする
                            dummy_page1.setVisibility(View.INVISIBLE);
                            dummy_page2.setVisibility(View.INVISIBLE);

                            //リストビューを先頭に移動
                            listView.setSelection(0);

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
        private final String leftText;
        private final String rightText;

    public String getLeftText() {
        return leftText;
    }

    public String getRightText() {
        return rightText;
    }

    //        コンストラクタ
        public ListViewData( String leftText, String rightText){
            this.leftText = leftText;
            this.rightText = rightText;
        }
    }
}
