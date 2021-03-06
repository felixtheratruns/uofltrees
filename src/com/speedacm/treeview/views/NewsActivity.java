/** This file is part of client-side of the CampusTrees Project. 
It is subject to the license terms in the LICENSE file found in the top-level directory of this distribution. No part of CampusTrees Project, including this file, may be copied, modified, propagated, or distributed except according to the terms contained in the LICENSE file.*/
package com.speedacm.treeview.views;

import java.util.ArrayList;
import com.speedacm.treeview.R;
import com.speedacm.treeview.data.DSResultListener;
import com.speedacm.treeview.data.DataStore;
import com.speedacm.treeview.models.News;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class NewsActivity extends Activity implements DSResultListener<News[]>{
	
	
	private ArrayList<News> menuEntries = new ArrayList<News>();
	private static final String tag = "NewsActivity";
	private int mCurrentFetchID = DataStore.NO_REQUEST;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsbox);
		mCurrentFetchID = DataStore.getInstance().beginGetAllNews(this);

	}
	//test

	@Override
	public void onDSResultReceived(int requestID, News[] payload) {
		// TODO Auto-generated method stub
		
		if(payload != null){
			for(News p : payload){
				menuEntries.add(p);
			}
		}
		
	    NewsArrayAdapter adapter = new NewsArrayAdapter(
				getApplicationContext(), R.layout.news_row, menuEntries);
	    
	    
		ListView lv = (ListView) this.findViewById(R.id.newsList);
		
		// Set the ListView adapter
		lv.setAdapter(adapter);
	}
}
