package com.example.vb_note01.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

        List<ListViewData> hiraganaList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            hiraganaList.add(new ListViewData(""+i+"", ""+i+""));
        }
        for (int i = 101; i <= 130; i++) {
            hiraganaList.add(new ListViewData("dead", "dead"));
        }
        // リストビューをとってくる
        ListView listView = this.findViewById(R.id.list_view);

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

        final TextView topText = this.findViewById(R.id.top_text);

        // アダプタをセット
        listView.setAdapter(ListViewAdapter);

        // CLICKイベントを設定
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ListView listView = (ListView) adapterView;
//                ListViewData hiragana = (ListViewData) listView.getItemAtPosition(i);
//                topText.setText(hiragana.getLeftText());
//
//            }
//        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int firstItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == 0){
                    topText.setText(""+this.firstItem+"");
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
