package com.developerhaoz.rteammessagehelper.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.developerhaoz.rteammessagehelper.MainActivity;
import com.developerhaoz.rteammessagehelper.R;
import com.developerhaoz.rteammessagehelper.bean.ContactBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Haoz
 * @date 2017/11/16.
 */

public class SearchContactsActivity extends AppCompatActivity {

    private FloatingSearchView mFsvSearch;
    private static final String KEY_CONTACT = "key_contact";

    public static void startActivity(Context context, ArrayList<ContactBean> contactBeanList){
        Intent intent = new Intent(context, SearchContactsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_CONTACT, contactBeanList);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contacts);
//        Intent intent = getIntent();
//        final ArrayList<ContactBean> contactBeen = intent.getParcelableArrayListExtra(KEY_CONTACT);
        mFsvSearch = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mFsvSearch.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                mFsvSearch.swapSuggestions(getSearchContactList(newQuery));
            }
        });

        mFsvSearch.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                PersonInfoDialogFragment dialogFragment = PersonInfoDialogFragment.newInstance((ContactBean)searchSuggestion);
                dialogFragment.show(SearchContactsActivity.this.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());
            }

            @Override
            public void onSearchAction(String currentQuery) {

            }
        });

    }

    private List<ContactBean> getSearchContactList(String query){
        List<ContactBean> result = new ArrayList<>();
        for (ContactBean contactBean : MainActivity.mContactBeanList) {
            if (contactBean.getName().contains(query)){
                result.add(contactBean);
            }
        }
        return result;
    }
}






















