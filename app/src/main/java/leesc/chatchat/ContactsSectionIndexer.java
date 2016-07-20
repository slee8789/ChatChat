/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package leesc.chatchat;

import android.widget.SectionIndexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import leesc.chatchat.data.Contact;
import leesc.chatchat.utils.CharUtils;


/**
 * A section indexer that is configured with precomputed section titles and their respective counts.
 */
public class ContactsSectionIndexer implements SectionIndexer {

    private final ArrayList<String> mSections;
    private final ArrayList<Integer> mPositions;
    private final int mCount;
    ArrayList<Contact> mAlContactItems;

    /**
     * Constructor.
     * 
     * @param sections
     *            a non-null array
     * @param counts
     *            a non-null array of the same size as <code>sections</code>
     */
    public ContactsSectionIndexer(ArrayList<Contact> contactItems) {
        if (contactItems == null) {
            throw new NullPointerException();
        }
        mAlContactItems = contactItems;

        this.mSections = new ArrayList<String>();
        this.mPositions = new ArrayList<Integer>();

        int position = 0;
        for (Contact contactItem : mAlContactItems) {
            if (contactItem == null)
                continue;
            String displayName = contactItem.getName();
            if (displayName == null)
                continue;
            String initialChar = CharUtils.getInitialChar(displayName);

            if (mSections.size() == 0
                || (mSections.size() > 0 && !mSections.get(mSections.size() - 1).equals(initialChar))) {
                this.mPositions.add(position);
                this.mSections.add(initialChar);
            }
            position++;
        }

        if (mSections.size() != mPositions.size()) {
            throw new IllegalArgumentException("The sections and counts arrays must have the same length");
        }
        mCount = position;
    }

    public Object[] getSections() {
        return mSections.toArray();
    }

    public int getPositionForSection(int section) {
        if (section == '#') {
            return getPositionForNumberSection(section);
        } else if (section > 127) {
            return getPositionForHangulSection(section);
        } else if ((section >= 'A' && section <= 'Z') || (section >= 'a' && section <= 'z')) {
            return getPositionForAlphabetSection(section);
        } else if (section >= '0' && section <= '9') {
            return getPositionForNumberSection(section);
        } else {
            return -1;
        }
    }

    private int getPositionForHangulSection(int section) {
        int count = mSections.size();
        for (int i = 0; i < count; i++) {
            int key = mSections.get(i).charAt(0);
            if (key > 127 && key == section) {
                return mPositions.get(i);
            }
        }
        return -1;
    }

    private int getPositionForAlphabetSection(int section) {
        section = String.valueOf((char) section).toUpperCase(Locale.getDefault()).charAt(0);
        int count = mSections.size();
        for (int i = 0; i < count; i++) {
            int key = mSections.get(i).toUpperCase(Locale.getDefault()).charAt(0);
            if (((key >= 'A' && key <= 'Z') || (key >= 'a' && key <= 'z')) && key >= section) {
                return mPositions.get(i);
            }
        }
        return getPositionForFirstNumberSection();
    }

    private int getPositionForNumberSection(int section) {
        int count = mSections.size();
        for (int i = 0; i < count; i++) {
            int key = mSections.get(i).charAt(0);
            if ((key >= '0' && key <= '9') && key >= section) {
                return mPositions.get(i);
            }
        }
        return getPositionForFirstExtraSection();
    }

    private int getPositionForFirstNumberSection() {
        int count = mSections.size();
        for (int i = 0; i < count; i++) {
            int key = mSections.get(i).charAt(0);
            if (key >= '0' && key <= '9') {
                return mPositions.get(i);
            }
        }
        return getPositionForFirstExtraSection();
    }

    private int getPositionForFirstExtraSection() {
        int count = mSections.size();
        for (int i = 0; i < count; i++) {
            int key = mSections.get(i).charAt(0);
            if (!(key > 127 || (key >= 'A' && key <= 'Z') || (key >= 'a' && key <= 'z') || key >= '0' && key <= '9')) {
                return mPositions.get(i);
            }
        }
        return -1;
    }

    public int getPositionForSection2(int section) {
        if (section < 0 || section >= mSections.size()) {
            return -1;
        }
        return mPositions.get(section);
    }

    public int getSectionForPosition(int position) {
        if (position < 0 || position >= mCount) {
            return -1;
        }
        int index = Arrays.binarySearch(mPositions.toArray(), position);
        return index >= 0 ? index : -index - 2;
    }
}
