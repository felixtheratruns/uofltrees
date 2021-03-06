/** This file is part of client-side of the CampusTrees Project. 
It is subject to the license terms in the LICENSE file found in the top-level directory of this distribution. No part of CampusTrees Project, including this file, may be copied, modified, propagated, or distributed except according to the terms contained in the LICENSE file.*/
package com.speedacm.treeview.models;

import com.google.android.maps.GeoPoint;

public class Building {
	
	private GeoPoint mLocation;
	
	public Building(GeoPoint location)
	{
		mLocation = location;
	}
	
	public GeoPoint getLocation() { return mLocation; }
}
