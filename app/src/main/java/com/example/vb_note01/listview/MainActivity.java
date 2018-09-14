package com.example.vb_note01.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

        List<String> hiraganaList = new ArrayList<>();
        hiraganaList.add("あ");
        hiraganaList.add("い");
        hiraganaList.add("う");
        hiraganaList.add("え");
        hiraganaList.add("お");

        // リストビューをとってくる
        ListView listView = this.findViewById(R.id.list_view);

        // アダプター経由でListViewに　あいうえお　をいれる
        ListAdapter ListViewAdapter = new ArrayAdapter<String>(
                this.getApplicationContext(),
                android.R.layout.simple_list_item_1,
                hiraganaList
        );

        final TextView topText = this.findViewById(R.id.top_text);

        // アダプタをセット
        listView.setAdapter(ListViewAdapter);

        // CLICKイベントを設定
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListView listView = (ListView) adapterView;
//                String hiragana = (String) listView.getSelectedItem();
                String hiragana = (String) listView.getItemAtPosition(i);
                topText.setText(hiragana);

            }
        });
    }

//    inner class
    class ListVieData {
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
