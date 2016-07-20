package leesc.chatchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import leesc.chatchat.data.Contact;
import leesc.chatchat.widget.IndexBar;

import static leesc.chatchat.utils.CommonUtils.sMultiSelectMode;

public class ContactsFragment extends Fragment implements OnClickListener {

    private Activity mActivity;
    private EditText mSearchEdit;
    private ListView mListView;
    private View mEmptyView;
    private IndexBar mIndexBar;
    private TextView mIndexText;

    private LinearLayout mBottomLayout;
    private Button mCancelButton;
    private Button mConfirmButton;

    private ContactsAdapter mAdapter;
    private ContactsConfirmListener mListener;
    private Button mAllSelected;

    public interface ContactsConfirmListener {
        public void onConfirm();

        public void onRefresh();
    }

    private ArrayList<Contact> sContactList = new ArrayList<Contact>();
    private int mPosition;

    public ContactsFragment() {

    }

    public ContactsFragment(ContactsConfirmListener listener) {
        this.mListener = listener;
    }

    public ContactsFragment(int position, ContactsConfirmListener listener) {
        this.mListener = listener;
        mPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        findViews(rootView);
        initViews();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private void findViews(View rootView) {
        mSearchEdit = (EditText) rootView.findViewById(R.id.contacts_search_edit);
        mListView = (ListView) rootView.findViewById(R.id.contacts_list);
        mEmptyView = rootView.findViewById(R.id.layout_list_empty);
        mIndexBar = (IndexBar) rootView.findViewById(R.id.contacts_indexbar);
        mIndexText = (TextView) rootView.findViewById(R.id.contacts_index_text);
        mBottomLayout = (LinearLayout) rootView.findViewById(R.id.contact_bottom_layout);
        mCancelButton = (Button) rootView.findViewById(R.id.contact_cancel_button);
        mConfirmButton = (Button) rootView.findViewById(R.id.contact_confirm_button);
        mAllSelected = (Button) rootView.findViewById(R.id.all_select);
    }

    private void initViews() {
        setHasOptionsMenu(true);
        // TODO :: contact list setting

        Contact contact1 = new Contact("tester1", "01011112222", "test@gmail.com", null);
        Contact contact2 = new Contact("tester2", "01033334444", "test@gmail.com", null);
        sContactList.add(contact1);
        sContactList.add(contact2);

        mAdapter = new ContactsAdapter(mActivity, sContactList, sMultiSelectMode);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyView);
        mIndexBar.setIndexView(mIndexText);
        mIndexBar.setPinnedHeaderListView(mListView);
        mIndexBar.setSectionIndexer(mAdapter.getSetionIndexer());

        mSearchEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!sMultiSelectMode) {
                    ArrayList<Contact> items = new ArrayList<Contact>();
                    Contact item = (Contact) mAdapter.getItem(position);// SplashActivity.sAddressList.get(position);
                    items.add(item);

                    // TODO :: contact list click event set
//
//                    Intent intent = new Intent(mActivity, ComposeActivity.class);
//                    intent.putExtra(ComposeActivity.SEND_FROM, ComposeActivity.SEND_FROM_CONTACTS);
//                    mActivity.startActivity(intent);
                } else {
                    ((CheckBox) view.findViewById(R.id.contact_item_select_checkbox)).performClick();
                }
            }
        });

        mCancelButton.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
        mAllSelected.setOnClickListener(this);
        setVisibleLayout();

    }

    private void redownloadContact() {
        getAddress();
    }

    private void getAddress() {
        // TODO :: get contact list
    }

    public void refresh() {
        setVisibleLayout();
        if (mAdapter != null) {
            mAdapter.setMultiSelectMode(sMultiSelectMode);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setVisibleLayout() {
        if (mBottomLayout != null) {
            if (!sMultiSelectMode) {
                mBottomLayout.setVisibility(View.GONE);
                mAllSelected.setVisibility(View.GONE);
            } else {
                mBottomLayout.setVisibility(View.VISIBLE);
                mAllSelected.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_cancel_button:
                sMultiSelectMode = false;
                setVisibleLayout();
                mAdapter.clearCheckedItems();
                mAdapter.setMultiSelectMode(sMultiSelectMode);
                mAdapter.notifyDataSetChanged();
                mListener.onRefresh();
                break;
            case R.id.contact_confirm_button:
                // ArrayList<GetAddressResponseList> items = getCheckedItems();

                sMultiSelectMode = false;
                // if (!mMultiSelectMode) {
                // mBottomLayout.setVisibility(View.GONE);
                // }

                // mAdapter.clearCheckedItems();
                // mAdapter.setMultiSelectMode(mMultiSelectMode);
                // mAdapter.notifyDataSetChanged();
                mListener.onConfirm();
                //
                // Intent intent = new Intent(mActivity, ComposeActivity.class);
                // intent.putExtra(ComposeActivity.SEND_FROM, ComposeActivity.SEND_FROM_CONTACTS);
                // mActivity.startActivity(intent);
                break;
            case R.id.all_select:
                if (sMultiSelectMode) {
                    boolean flag = mAdapter.setAllChecked();
                    if (flag) {
                        mAllSelected.setText("전체 해제");
                        mAllSelected.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
                    } else {
                        mAllSelected.setText("전체 선택");
                        mAllSelected.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right));
                    }

                }
                break;
        }

    }

    public ArrayList<Contact> getCheckedItems() {
        if (mAdapter != null) {
            return mAdapter.getCheckedItems();
        } else {
            return new ArrayList<Contact>();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.contacts_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_write_message:
//                if (!sMultiSelectMode) {
//                    sMultiSelectMode = true;
//                    setVisibleLayout();
//                    mAdapter.setMultiSelectMode(sMultiSelectMode);
//                    mAdapter.notifyDataSetChanged();
//                    mListener.onRefresh();
//                } else {
//                }
//                return true;

            case R.id.action_add_contact:
                // TODO :: 친구추가 기능 구현 필요
                Toast.makeText(mActivity, "친구추가 기능 구현 필요", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
