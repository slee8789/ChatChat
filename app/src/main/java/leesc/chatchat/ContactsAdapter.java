package leesc.chatchat;

import static leesc.chatchat.utils.CommonUtils.sCheckedItems;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.mogua.localization.KoreanTextMatch;
import com.mogua.localization.KoreanTextMatcher;

import java.util.ArrayList;

import leesc.chatchat.data.Contact;
import leesc.chatchat.db.MessageDB;
import leesc.chatchat.utils.CharUtils;
import leesc.chatchat.utils.DummyContact;
import leesc.chatchat.widget.HighlightTextView;

public class ContactsAdapter extends BaseAdapter implements SectionIndexer, Filterable {

    private ArrayList<Contact> mContactItems;
    private ArrayList<Contact> mOriginalContactItems;
    private Context mContext;
    private LayoutInflater mInflater;
    private ContactsSectionIndexer mIndexer;
    private ContactsFilter mFilter;
    private boolean mMultiSelectMode;
    private boolean mShowIndex;
    private String mSearchString;
    private boolean mAllSelected;

    public ContactsAdapter(Context context, ArrayList<Contact> contactItems, boolean multiSelectMode) {
        super();
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mContactItems = contactItems;
        mIndexer = new ContactsSectionIndexer(contactItems);
        mFilter = new ContactsFilter();
        mMultiSelectMode = multiSelectMode;
        mShowIndex = true;
        clearCheckedItems();
    }

    public ContactsAdapter(Context context, ArrayList<Contact> contactItems, boolean multiSelectMode,
                           boolean showIndex) {
        super();
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mContactItems = contactItems;
        mIndexer = new ContactsSectionIndexer(contactItems);
        mFilter = new ContactsFilter();
        mMultiSelectMode = multiSelectMode;
        mShowIndex = showIndex;
        clearCheckedItems();
    }

    @Override
    public int getCount() {
        return mContactItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setMultiSelectMode(boolean multiSelectMode) {
        mMultiSelectMode = multiSelectMode;
        if (!multiSelectMode) {
            removeCheckedItems();
        }
    }

    public boolean setAllChecked() {
        mAllSelected = !mAllSelected;
        if (mAllSelected) {
            settingFlag(true);
        } else {
            settingFlag(false);
        }
        notifyDataSetChanged();
        return mAllSelected;
    }

    private void settingFlag(boolean flag) {
        for (int i = 0; i < mContactItems.size(); i++) {
            sCheckedItems.put(mContactItems.get(i).getEmail(), flag);
        }
    }

    public void removeCheckedItems() {
        if (sCheckedItems != null) {
            sCheckedItems.clear();
        }
    }

    public ArrayList<Contact> getCheckedItems() {
        ArrayList<Contact> contactItems = new ArrayList<Contact>();
        if (sCheckedItems != null) {
            for (int i = 0; i < mContactItems.size(); i++) {
                if (sCheckedItems.get(mContactItems.get(i).getEmail())) {
                    contactItems.add(mContactItems.get(i));
                }
            }
        }
        return contactItems;
    }

    public void setContactItems(ArrayList<Contact> contactItems) {
        mContactItems = contactItems;
        updateIndexer(contactItems);
        clearCheckedItems();
        notifyDataSetChanged();
    }

    public void clearCheckedItems() {
        // if (mMultiSelectMode) {
        // // if (sCheckedItems != null && sCheckedItems.size() > 0) {
        // // sCheckedItems.clear();
        // // }
        // if (mContactItems != null) {
        // for (int i = 0; i < mContactItems.size(); i++) {
        // sCheckedItems.put(mContactItems.get(i).getPhoneNumber(), false);
        // }
        // }
        // }
        if (mContactItems != null) {
            for (int i = 0; i < mContactItems.size(); i++) {
                if (sCheckedItems.get(mContactItems.get(i).getEmail()) == null) {
                    sCheckedItems.put(mContactItems.get(i).getEmail(), false);
                }
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_contact_item, parent, false);
            holder = new ViewHolder();
            holder.initialCharLayout = (LinearLayout) convertView
                    .findViewById(R.id.contact_item_initial_chosung_layout);
            holder.initialCharText = (TextView) convertView.findViewById(R.id.contact_item_initial_chosung_text);
            holder.selectCheck = (CheckBox) convertView.findViewById(R.id.contact_item_select_checkbox);
            holder.profileImage = (ImageView) convertView.findViewById(R.id.contact_item_profile_image);
            holder.nameText = (HighlightTextView) convertView.findViewById(R.id.contact_item_name_text);
            holder.StartChat =(Button) convertView.findViewById(R.id.contact_item_chat_btn);
            holder.StartChat.setTag(position);

            holder.nameText
                    .setHighlightTextColor(mContext.getResources().getColor(R.color.contact_searched_text_color));
            holder.emailText = (HighlightTextView) convertView.findViewById(R.id.contact_item_email_text);
            holder.emailText.setHighlightTextColor(mContext.getResources().getColor(
                    R.color.contact_searched_text_color));
            convertView.setTag(R.id.tag_contact_holder, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.tag_contact_holder);
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.push_down_in);
        holder.nameText.setAnimation(animation);
        String name = mContactItems.get(position).getName();
        final String email = mContactItems.get(position).getEmail();
        convertView.setTag(R.id.tag_contact_item, mContactItems.get(position));





        if (mMultiSelectMode) {
            holder.selectCheck.setVisibility(View.VISIBLE);

            holder.selectCheck.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = holder.selectCheck.isChecked();
                    sCheckedItems.put(email, checked);
                }
            });
            if (sCheckedItems.get(email) == null) {
                sCheckedItems.put(email, mAllSelected);
            }
            holder.selectCheck.setChecked(sCheckedItems.get(email));
        } else {
            holder.selectCheck.setVisibility(View.GONE);
        }

        String currentInitialChar = CharUtils.getInitialChar(mContactItems.get(position).getName());

        if (mShowIndex) {
            if (position == 0) {
                holder.initialCharText.setText(currentInitialChar);
                holder.initialCharLayout.setVisibility(View.VISIBLE);
            } else {
                String preInitialChar = CharUtils.getInitialChar(mContactItems.get(position - 1).getName());

                if (preInitialChar.equals(currentInitialChar)) {
                    holder.initialCharText.setText("");
                    holder.initialCharLayout.setVisibility(View.GONE);
                } else {
                    holder.initialCharText.setText(currentInitialChar);
                    holder.initialCharLayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            holder.initialCharText.setText("");
            holder.initialCharLayout.setVisibility(View.GONE);
        }

        holder.nameText.setText(name);
        holder.nameText.setHighlightText(getMatchedText(name, mSearchString));
        holder.emailText.setText(email);
        holder.emailText.setHighlightText(getMatchedText(email, mSearchString));
        holder.profileImage.setImageResource(DummyContact.getAvatar(mContactItems.get(position).getEmail()));


        // start 버튼 눌렀을 때

        holder.StartChat.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt((v.getTag().toString()));

                    final long threadId = MessageDB.getInstance().getThreadId(mContext, mContactItems.get(position).getEmail());
                    final String Name = mContactItems.get(position).getName();

                    Intent intent = new Intent(v.getContext(), MessageActivity.class)
                            .putExtra(MessageActivity.THREAD_ID, threadId);
                    intent.putExtra(MessageActivity.RECEIVER_NAME, Name);

                    // Log.d("name_contacts", Name+" "+ threadId);

                    mContext.startActivity(intent);
                }
            }
        );


        return convertView;
    }

    private String getMatchedText(String fullString, String matchString) {
        String result = "";
        if (TextUtils.isEmpty(matchString)) {
            return result;
        }
        KoreanTextMatch match = KoreanTextMatcher.match(fullString, matchString);
        if (match.getSuccess()) {
            result = match.getValue();
        }
        return result;
    }

    class ViewHolder {
        CheckBox selectCheck;
        LinearLayout initialCharLayout;
        TextView initialCharText;
        HighlightTextView nameText;
        HighlightTextView emailText;
        ImageView profileImage;
        Button StartChat;
    }

    private void updateIndexer(ArrayList<Contact> contactItems) {
        mIndexer = new ContactsSectionIndexer(contactItems);
    }

    public SectionIndexer getSetionIndexer() {
        return mIndexer;
    }

    @Override
    public int getPositionForSection(int section) {
        if (mIndexer == null) {
            return -1;
        }
        return mIndexer.getPositionForSection2(section);
    }

    @Override
    public int getSectionForPosition(int position) {
        if (mIndexer == null) {
            return -1;
        }
        return mIndexer.getSectionForPosition(position);
    }

    @Override
    public Object[] getSections() {
        if (mIndexer == null) {
            return new String[] { " " };
        } else {
            return mIndexer.getSections();
        }
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    class ContactsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (mOriginalContactItems == null) {
                mOriginalContactItems = mContactItems;
            }
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                mSearchString = "";
                results.count = mOriginalContactItems.size();
                results.values = mOriginalContactItems;
            } else {
                mSearchString = constraint.toString();
                final ArrayList<Contact> filtered = new ArrayList<Contact>();
                final String constraintString = constraint.toString();
                for (Contact item : mOriginalContactItems) {
                    if (KoreanTextMatcher.isMatch(item.getName(), constraintString)) {
                        filtered.add(item);
                    } else if (deleteHyphen(item.getEmail()).contains(deleteHyphen(constraintString))) {
                        filtered.add(item);
                    }
                }
                results.count = filtered.size();
                results.values = filtered;
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mContactItems = (ArrayList<Contact>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

        String extractNumber(String s) {
            final StringBuilder builder = new StringBuilder();
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    builder.append(c);
                } else if (Character.isLetter(c)) {
                    return null;
                }
            }
            return builder.toString();
        }

        String deleteHyphen(String s) {
            String result = s;
            if (s.contains("-")) {
                result = s.replaceAll("-", "");
            }
            return result;
        }
    }
}
