/** This file is part of client-side of the CampusTrees Project. 
It is subject to the license terms in the LICENSE file found in the top-level directory of this distribution. No part of CampusTrees Project, including this file, may be copied, modified, propagated, or distributed except according to the terms contained in the LICENSE file.*/
package com.speedacm.treeview.models;

import java.util.ArrayList;
import java.util.Iterator;

import android.os.Parcel;
import android.os.Parcelable;

public class ScavHunt implements Parcelable {

	private String title;
	private int scav_id;
//	private ArrayList<ScavHuntSubItem> sub_items;

	
	public ScavHunt(String pTitle, int num){
		title = pTitle;
		scav_id = num;
	//	sub_items = p_sub_items;
	}
	
	public String toString(){
		return title;
	}

	public String getTitle(){
		return title;
	}
	
	public int getScavId(){
		return scav_id;
	}
	
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
    	out.writeString(title);
    	out.writeInt(scav_id);
    }
    /*
    public void writeToParcel(Parcel out, int flags) {
    	out.writeString(title);
    	out.writeInt(sub_items.size());
    	
    	for(Iterator<ScavHuntSubItem> i = sub_items.iterator(); i.hasNext();){
    		ScavHuntSubItem item = i.next();
    		out.writeString(item.getTitle());
    		out.writeString(item.getBody());
    	}
    }
*/
    public static final Parcelable.Creator<ScavHunt> CREATOR
            = new Parcelable.Creator<ScavHunt>() {
        public ScavHunt createFromParcel(Parcel in) {
            return new ScavHunt(in);
        }

        public ScavHunt[] newArray(int size) {
            return new ScavHunt[size];
        }
    };
    
    private ScavHunt(Parcel in) {
    	title = in.readString();
    	scav_id = in.readInt();
    }
}
