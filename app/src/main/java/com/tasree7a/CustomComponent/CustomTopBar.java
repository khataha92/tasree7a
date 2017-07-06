package com.tasree7a.CustomComponent;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.R;
import com.tasree7a.interfaces.OnSearchBarStateChange;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

/**
 * Created by mac on 5/21/17.
 */

public class CustomTopBar extends RelativeLayout implements View.OnClickListener {

    private EditText searchText;

    ImageView closeSearch;

    ImageView clearText;

    ImageView openMenu;

    ImageView search;

    ImageView filter;

    TextView title;

    View separator;

    LinearLayout searchHistoryContainer;

    OnSearchBarStateChange onSearchBarStateChange;

    private OnClickListener onFirstIconClickListener;

    private TextWatcher onSearchTextChanged;

    public CustomTopBar(Context context) {

        super(context);

    }

    public CustomTopBar(Context context, AttributeSet attrs) {

        super(context, attrs);

        init(attrs);

    }

    public CustomTopBar(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        init(attrs);

    }

    private void init(AttributeSet attrs){

        LayoutInflater.from(getContext()).inflate(R.layout.view_custom_top_bar,this);

        searchText = (EditText) findViewById(R.id.search_text);

        closeSearch = (ImageView) findViewById(R.id.close_search);

        clearText = (ImageView) findViewById(R.id.clear_search);

        openMenu = (ImageView) findViewById(R.id.open_menu);

        search = (ImageView) findViewById(R.id.search);

        filter = (ImageView) findViewById(R.id.filter);

        title = (TextView) findViewById(R.id.title);

        separator = findViewById(R.id.separator);

        clearText.setOnClickListener(this);

        closeSearch.setOnClickListener(this);

        openMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(onFirstIconClickListener != null){

                    onFirstIconClickListener.onClick(v);
                }
            }
        });

        filter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager.showFilterFragment();

            }
        });

        searchHistoryContainer = (LinearLayout) findViewById(R.id.search_history_container);

        search.setOnClickListener(this);

    }

    public void showNormalMode(){

        searchText.setVisibility(GONE);

        closeSearch.setVisibility(GONE);

        clearText.setVisibility(GONE);

        separator.setVisibility(GONE);

        searchHistoryContainer.setVisibility(GONE);

        title.setVisibility(VISIBLE);

        openMenu.setVisibility(VISIBLE);

        filter.setVisibility(VISIBLE);

        search.setVisibility(VISIBLE);

        if(onSearchBarStateChange != null){

            onSearchBarStateChange.onSearchClose();

        }

        UIUtils.forceHideKeyboard();

    }

    public void showSearchMode(){

        searchText.setVisibility(VISIBLE);

        closeSearch.setVisibility(VISIBLE);

        clearText.setVisibility(VISIBLE);

        searchHistoryContainer.setVisibility(VISIBLE);

        title.setVisibility(GONE);

        openMenu.setVisibility(GONE);

        filter.setVisibility(GONE);

        search.setVisibility(GONE);

        if(UserDefaultUtil.getSearchHistory().size() > 0) {

            separator.setVisibility(VISIBLE);

        }

        UIUtils.showSoftKeyboard(searchText);

        if(onSearchBarStateChange != null){

            onSearchBarStateChange.onSearchOpen();
        }

    }

    public void setTitle(String title) {

        this.title.setText(title);

    }

    public String getTitle() {

        return title.getText().toString().trim();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.close_search:

                showNormalMode();

                break;

            case R.id.clear_search:

                searchText.setText("");

                break;

            case R.id.search:

                showSearchMode();

                break;
        }
    }

    public void setOnSearchTextChanged(TextWatcher onSearchTextChanged) {
        this.onSearchTextChanged = onSearchTextChanged;

        searchText.addTextChangedListener(onSearchTextChanged);
    }

    public void setOnSearchBarStateChange(OnSearchBarStateChange onSearchBarStateChange) {
        this.onSearchBarStateChange = onSearchBarStateChange;
    }

    public void setOnFirstIconClickListener(OnClickListener onFirstIconClickListener) {
        this.onFirstIconClickListener = onFirstIconClickListener;
    }
}
