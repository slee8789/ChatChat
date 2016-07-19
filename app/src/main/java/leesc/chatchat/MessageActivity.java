package leesc.chatchat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import leesc.chatchat.db.RecipientIdCache;

public class MessageActivity extends AppCompatActivity implements OnClickListener {

    public static final String THREAD_ID = "thread_id";

    private DrawerLayout mDrawerLayout;
    private ReceiverDrawerAdapter mAdapter;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private MessageFragment mMessageFragment;
    private int mIndex = 0;

    private long mThreadId;
    private String[] mNamesArray;
    private String[] mNumbersArray;

    private static boolean isDrawerOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mThreadId = getIntent().getLongExtra(MessageActivity.THREAD_ID, -1);
        if (mThreadId == -1) {
            finish();
        }
        findViews();
        initViews(savedInstanceState);
        initDrawer();

        if (savedInstanceState == null) {
            addFragment();
        }
    }

    private void findViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.messages_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.messages_drawer);
        mMessageFragment = new MessageFragment();

        RecipientIdCache.Entry entry = RecipientIdCache.getAddress(Long.toString(mThreadId));
        if (entry != null) {
            try {
                mNamesArray = entry.names.replace(" ", "").split("\\,");
                mNumbersArray = entry.numbers.replace(" ", "").split("\\,");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    private String getTextFromRadioButton(int id) {
        String msg = "";
        RadioButton radioBtn = (RadioButton) findViewById(id);
        msg = radioBtn.getTag().toString();
        radioBtn.setChecked(false);
        return msg;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if (mRecipients != null && mRecipients.size() == 1 && PhoneUtils.isPhone(mRecipients.get(0).getNumber())) {
        // return true;
        // } else {
        // return false;
        // }
        return true;
    }

    @Override
    public void onBackPressed() {

        if (isDrawerOpened) {
            mDrawerLayout.closeDrawers();
            isDrawerOpened = false;
            return;
        }
        super.onBackPressed();
    }

    private void initViews(Bundle savedInstanceState) {
        setActionBarTitle();
        ArrayList<ReceiverData> data = new ArrayList<ReceiverData>();

        if (mNamesArray != null) {
            for (int i = 0; i < mNamesArray.length; i++) {
                ReceiverData item = new ReceiverData(mNamesArray[i], mNumbersArray[i]);
                data.add(item);
            }
        }

        mAdapter = new ReceiverDrawerAdapter(getApplicationContext(), R.layout.drawer_receiver_list_item, data);
        mDrawerList.setAdapter(mAdapter);

        invalidateOptionsMenu();
    }

    private void initDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                isDrawerOpened = false;
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                isDrawerOpened = true;
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void addFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        try {
            transaction.add(R.id.messages_frame, mMessageFragment);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;

        case R.id.action_call:
            callToRecipient();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void callToRecipient() {
        String phoneNumber = "";
        RecipientIdCache.Entry entry = RecipientIdCache.getAddress(Long.toString(mThreadId));
        if (entry != null && entry.numbers != null) {
            phoneNumber = entry.numbers;
        }
        phoneNumber = phoneNumber.replaceAll("-", "");
//        boolean isCalled = PhoneUtils.startPhoneCall(this, phoneNumber);
//        if (!isCalled) {
//            Toast.makeText(getApplicationContext(), "통화를 할 수 없습니다.", Toast.LENGTH_SHORT).show();
//        }
    }

    private void setActionBarTitle() {
        // TODO :: ActionBar Title set
    }

    class ReceiverData {
        public String name;
        public String number;

        public ReceiverData(String name, String number) {
            this.name = name;
            this.number = number;
        }
    }

    class ReceiverDrawerAdapter extends BaseAdapter {
        ArrayList<ReceiverData> mData = new ArrayList<ReceiverData>();
        private int mLayout;
        private Context mContext;
        private LayoutInflater mInflater;

        public ReceiverDrawerAdapter(Context context, int layout, ArrayList<ReceiverData> data) {
            mData = data;
            mContext = context;
            mLayout = layout;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(mLayout, parent, false);
                holder = new ViewHolder();
                holder.nameText = (TextView) convertView.findViewById(R.id.item_name);
                holder.numberText = (TextView) convertView.findViewById(R.id.item_number);
                holder.smsBtn = (Button) convertView.findViewById(R.id.sms_btn);
                holder.callBtn = (Button) convertView.findViewById(R.id.call_btn);
                convertView.setTag(R.id.tag_receiver_item, holder);
            } else {
                holder = (ViewHolder) convertView.getTag(R.id.tag_receiver_item);
            }
            holder.nameText.setText(mData.get(position).name);
            holder.numberText.setText(mData.get(position).number);
            holder.callBtn.setTag(mData.get(position).number);
            holder.smsBtn.setTag(mData.get(position).number);
            holder.callBtn.setOnClickListener(MessageActivity.this);
            holder.smsBtn.setOnClickListener(MessageActivity.this);
            return convertView;
        }

        class ViewHolder {
            TextView nameText;
            TextView numberText;
            Button smsBtn;
            Button callBtn;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // TODO :: click 기능 추가
        }
    }
}
