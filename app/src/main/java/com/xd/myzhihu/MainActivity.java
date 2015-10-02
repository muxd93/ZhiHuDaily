package com.xd.myzhihu;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<ZhiHuItem> mZhiHuItemList;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ListView listView = (ListView) findViewById(R.id.list_view);
            listView.setAdapter(new MyAdapter());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getZhihuDaily();


    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mZhiHuItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = null;
            ZhiHuItem zhiHuItem = mZhiHuItemList.get(position);
            ViewHolder mHolder;
            if (convertView == null) {
                v = View.inflate(MainActivity.this, R.layout.item_layout, null);

                mHolder = new ViewHolder();
                mHolder.itemTitle = (TextView) v.findViewById(R.id.item_title);
                mHolder.itemComment = (TextView) v.findViewById(R.id.item_comment);
                mHolder.itemContent = (TextView) v.findViewById(R.id.item_content);
                mHolder.mSmartImageView = (SmartImageView) v.findViewById(R.id.item_head);
                v.setTag(mHolder);
            } else {
                v = convertView;
                mHolder = (ViewHolder) v.getTag();
            }
            Log.d("X", "Title" + zhiHuItem.getTitle());
            Log.d("X", "" + mHolder.toString());
            mHolder.itemTitle.setText(zhiHuItem.getTitle());
            mHolder.itemContent.setText(Html.fromHtml(zhiHuItem.getContent()));
            mHolder.itemComment.setText(zhiHuItem.getComment());
            mHolder.mSmartImageView.setImageUrl("http://imgsrc.baidu.com/forum/wh%3D1024%2C768/sign=1fbbea65253fb80e0c8469d606e31b02/9c058d8ba61ea8d3e34d9250930a304e241f5872.jpg");

            return v;
        }

        class ViewHolder {
            TextView itemContent;
            TextView itemComment;
            TextView itemTitle;
            com.loopj.android.image.SmartImageView mSmartImageView;

        }
    }


    public void getZhihuDaily() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                String path = "http://zhihurss.miantiao.me/dailyrss";
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();
                    Log.d("X", "创建连接");
                    if (connection.getResponseCode() == 200) {
                        Log.d("X", "创建连接成功");
                        InputStream inputStream = connection.getInputStream();
                        Log.d("X", "开始解析");
                        xmlParse(inputStream);
                        Log.d("X", "解析成功");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();
    }

    public void xmlParse(InputStream inputStream) {
        XmlPullParser xp = Xml.newPullParser();

        try {

            xp.setInput(inputStream, "utf-8");
            int type = xp.getEventType();
            ZhiHuItem zhiHuItem = null;

            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if ("channel".equals(xp.getName())) {
                            mZhiHuItemList = new ArrayList<>();
                            Log.d("X", "成功创建列表");
                        } else if ("item".equals(xp.getName())) {
                            zhiHuItem = new ZhiHuItem();
                            Log.d("X", "成功创建Item");
                        } else if ("title".equals(xp.getName())) {
                            String title = xp.nextText();
                            if (zhiHuItem != null) {
                                zhiHuItem.setTitle(title);
                            }
                            Log.d("X", "成功设置Title" + title);

                        } else if ("description".equals(xp.getName())) {
                            String content = xp.nextText();

                            if (zhiHuItem != null) {
                                zhiHuItem.setContent(content);
                            }
                            Log.d("X", "成功添加Content" + content);

                        } else if ("pubDate".equals(xp.getName())) {
                            String pubDate = xp.nextText();

                            if (zhiHuItem != null) {
                                zhiHuItem.setComment(pubDate);
                            }
                            Log.d("X", "成功添加pubDate" + pubDate);

                        } else if ("guid".equals(xp.getName())) {
                            String guid = xp.nextText();

                            if (zhiHuItem != null) {
                                zhiHuItem.setImgSource(guid);
                            }
                            Log.d("X", "成功添加Guid" + guid);

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equals(xp.getName())) {
                            mZhiHuItemList.add(zhiHuItem);
                            Log.d("X", "成功添加列表" + mZhiHuItemList.size());

                        }
                        break;
                }
                type = xp.next();
            }

            mHandler.sendEmptyMessage(0);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
