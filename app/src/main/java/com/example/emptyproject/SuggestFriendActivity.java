package com.example.emptyproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SuggestFriendActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int READ_CONTACTS_REQUEST_CODE = 100;
    private static final int CONTACT_LOADER = 1;
    private boolean isASC = true;

    RecyclerView rvSuggestFriends;
    UserAdapter suggestAdapter;

    List<String> devicePhoneContacts = new ArrayList<>();
    ArrayList<User> suggestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_friend);

        rvSuggestFriends = findViewById(R.id.lvSuggestFriends);
        rvSuggestFriends.setLayoutManager(new GridLayoutManager(this, 2));


        suggestAdapter = new UserAdapter(this, suggestList, 4, (targetUser, mode, isPrimaryAction) -> {
            LoginActivity.currentUser.getSentRequests().add(targetUser);
            targetUser.getReceivedRequests().add(LoginActivity.currentUser);

            suggestList.remove(targetUser);
            suggestAdapter.notifyDataSetChanged();

            Toast.makeText(this, "Đã gửi lời mời kết bạn!", Toast.LENGTH_SHORT).show();
        });
        rvSuggestFriends.setAdapter(suggestAdapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_REQUEST_CODE);
        } else {
            LoaderManager.getInstance(this).restartLoader(CONTACT_LOADER, null, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CONTACTS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoaderManager.getInstance(this).restartLoader(CONTACT_LOADER, null, this);
            } else {
                Toast.makeText(this, "Bị từ chối quyền đọc danh bạ!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == CONTACT_LOADER) {
            String[] SELECTED_FIELDS = new String[] {
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            };
            return new CursorLoader(this, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    SELECTED_FIELDS, null, null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " " + (isASC ? "ASC" : "DESC"));
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == CONTACT_LOADER) {
            devicePhoneContacts.clear();
            if (data != null) {
                while (!data.isClosed() && data.moveToNext()) {
                    String phone = data.getString(1);
                    if (phone != null) {
                        devicePhoneContacts.add(phone.replaceAll("[\\s\\-]", ""));
                    }
                }
                data.close();
            }
            filterSuggestFriends();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }

    private void filterSuggestFriends() {
        suggestList.clear();

        ArrayList<User> tempServerUsers = new ArrayList<>();
        tempServerUsers.add(new User("User1", "test1@gmail.com", "123", "0911111111"));
        tempServerUsers.add(new User("User2", "test2@gmail.com", "123", "0922222222"));

        for (User u : tempServerUsers) {
            if (LoginActivity.currentUser != null && !u.getEmail().equals(LoginActivity.currentUser.getEmail())) {

                boolean isPhoneInContact = false;
                for (String contactPhone : devicePhoneContacts) {
                    if (contactPhone.equals(u.getPhone())) {
                        isPhoneInContact = true;
                        break;
                    }
                }

                if (isPhoneInContact
                        && !LoginActivity.currentUser.getFriendList().contains(u)
                        && !LoginActivity.currentUser.getSentRequests().contains(u)
                        && !LoginActivity.currentUser.getReceivedRequests().contains(u)) {

                    suggestList.add(u);
                }
            }
        }
        suggestAdapter.notifyDataSetChanged();
    }
}