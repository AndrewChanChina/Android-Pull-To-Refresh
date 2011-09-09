package com.dmobile.pulltorefresh;

import java.util.ArrayList;

import com.dmobile.pulltorefresh.PullRefreshContainerView.OnChangeStateListener;
import com.dmobile.pulltorefresh.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UsageDemoActivity extends Activity {
	
	private PullRefreshContainerView mContainerView;
	private TextView mRefreshHeader;
	private ListView mList;
	
	private ArrayList<String> mStrings = new ArrayList<String>();
	private ArrayAdapter<String> mAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mRefreshHeader = new TextView(this);
        mRefreshHeader.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mRefreshHeader.setGravity(Gravity.CENTER);
        mRefreshHeader.setText("Pull to refresh...");

        mContainerView = (PullRefreshContainerView) findViewById(R.id.container);
        mContainerView.setRefreshHeader(mRefreshHeader);
        
        mContainerView.setOnChangeStateListener(new OnChangeStateListener() {
    		@Override
    		public void onChangeState(PullRefreshContainerView container, int state) {
    			switch(state) {
    			case PullRefreshContainerView.STATE_IDLE:
    			case PullRefreshContainerView.STATE_PULL:
    				mRefreshHeader.setText("Pull to refresh...");
    				break;
    			case PullRefreshContainerView.STATE_RELEASE:
    				mRefreshHeader.setText("Release to refresh...");
    				break;
    			case PullRefreshContainerView.STATE_LOADING:
    				mRefreshHeader.setText("Loading...");
    				
    				new Thread(new Runnable() {
						@Override
						public void run() {
							UsageDemoActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									try {
										Thread.sleep(3000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									
									addStrings(1);
									mContainerView.completeRefresh();
								}
		    				});
						}
    					
    				}).start();
    				break;
    			}
    		}
        });
        
        mList = mContainerView.getList();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,mStrings);
        mList.setAdapter(mAdapter);
        
        addStrings(3);
    }
    
    private void addStrings(int count) {
    	int curSize = mStrings.size();
    	for(int i = 0; i < count; ++i) {
    		mStrings.add("String " + (curSize + i));
    	}
    	
    	mAdapter.notifyDataSetChanged();
    }
}