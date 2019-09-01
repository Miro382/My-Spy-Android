package com.myspy.myspyandroid.functions;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by Miroslav Murin on 17.01.2017.
 */

public final class ContactName {

    private ContactName(){}

    /**
     * Get from number contact name
     * @param context Context
     * @param phoneNumber Phone number
     * @return String name of the contact
     */
    public static String GetContactName(Context context, String phoneNumber) {
        try {
            ContentResolver cr = context.getContentResolver();
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
            if (cursor == null) {
                return null;
            }
            String contactName = null;
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            }

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

            if (contactName != null && !contactName.isEmpty() && !contactName.equals("null"))
                return contactName;
            else
                return phoneNumber;
        }catch (Exception ex)
        {
            Log.w("Error",""+ex);
            return phoneNumber;
        }
    }

}
