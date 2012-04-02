package com.speedacm.treeview.data;

import java.util.ArrayList;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.android.maps.GeoPoint;
import com.speedacm.treeview.models.Species;
import com.speedacm.treeview.models.Species.NativeType;
import com.speedacm.treeview.models.Tree;
import com.speedacm.treeview.models.Zone;

public class DataParser
{
	private ObjectMapper mMapper;
	
	public DataParser()
	{
		mMapper = new ObjectMapper();
	}
	
	private JsonNode mapNode(String json)
	{
		// at this point, we're "all or nothing" with regards to the JSON stuff,
		// so if any error occurs during the parsing process, just return null
		// hence, all Exception types regardless of subclass are caught here
		
		try
		{
			JsonParser jp = mMapper.getJsonFactory().createJsonParser(json);
			return mMapper.readTree(jp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private ArrayList<GeoPoint> getPointsFromNode(JsonNode arrayNode)
	{
		if(arrayNode == null) return null;
		
		ArrayList<GeoPoint> points = new ArrayList<GeoPoint>(arrayNode.size());
		Iterator<JsonNode> pointIter = arrayNode.getElements();
		
		while(pointIter.hasNext())
		{
			JsonNode pointNode = pointIter.next();
			double latitude = pointNode.path("lat").asDouble(Double.NaN);
			double longitude = pointNode.path("long").asDouble(Double.NaN);
			
			// this will trigger if these properties don't exist
			if(latitude == Double.NaN || longitude == Double.NaN ) return null;
			
			// convert lat/long to latE6/longE6 and return a point
			points.add(new GeoPoint((int)(latitude * 1E6),(int)(longitude * 1E6)));
		}
		
		return points;
	}
	
	public Zone[] parseAllZonesResponse(String json)
	{
		JsonNode rootNode = mapNode(json);
		if(rootNode == null) return null;
		
		ArrayList<Zone> zones = new ArrayList<Zone>(rootNode.size());
		
		Iterator<JsonNode> zoneIter = rootNode.getElements();
		while(zoneIter.hasNext())
		{
			JsonNode zoneNode = zoneIter.next();
			
			try
			{
				int zoneID = zoneNode.path("id").asInt(-1);
				if(zoneID == -1) return null;
				
				ArrayList<GeoPoint> points = getPointsFromNode(zoneNode.path("points"));
				
				zones.add(new Zone(zoneID, points));
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		
		return zones.toArray(new Zone[zones.size()]);
	}

	public void parseZoneTreesResponse(String json, Zone target)
	{
		JsonNode rootNode = mapNode(json);
		if(rootNode == null) return;
		
		ArrayList<Tree> trees = new ArrayList<Tree>(rootNode.size());
		
		Iterator<JsonNode> treeIter = rootNode.getElements();
		while(treeIter.hasNext())
		{
			JsonNode treeNode = treeIter.next();
			
			int id = treeNode.path("id").asInt(-1);
			double lat = treeNode.path("lat").asDouble(Double.NaN);
			double lng = treeNode.path("long").asDouble(Double.NaN);
			int sid = treeNode.path("sid").asInt(-1);
			double dbh = treeNode.path("dbh").asDouble(Double.NaN);
			double height = treeNode.path("height").asDouble(Double.NaN);
			
			
			if(id == -1 || lat == Double.NaN || lng == Double.NaN || sid == -1 ||
			   dbh == Double.NaN || height == Double.NaN)
			{
				continue;
			}
			
			int latE6 = (int)(lat * 1E6);
			int lngE6 = (int)(lng * 1E6);
			
			Tree t = new Tree(id, sid, new GeoPoint(latE6, lngE6), (float)dbh, (float)height);
			trees.add(t);
		}
		
		target.setTrees(trees);
	}

	public Species[] parseSpeciesResponse(String json)
	{
		JsonNode rootNode = mapNode(json);
		if(rootNode == null) return null;
		
		ArrayList<Species> specs = new ArrayList<Species>(rootNode.size());
		
		Iterator<JsonNode> specIter = rootNode.getElements();
		while(specIter.hasNext())
		{
			JsonNode specNode = specIter.next();
			
			int sid = specNode.path("sid").asInt(-1);
			String cname = specNode.path("commonname").asText();
			boolean nativeUS = specNode.path("american").asBoolean();
			boolean nativeKY = specNode.path("ky").asBoolean();
			//boolean nativeNo = specNode.path("nonnative").asBoolean(); // we don't need this
			//String comments = specNode.path("comments").asText();
			
			if(sid == -1) continue;
			
			NativeType nat;
			if(nativeKY)
				nat = NativeType.KY;
			else if(nativeUS)
				nat = NativeType.US;
			else
				nat = NativeType.None;
			
			// TODO: extra fields from JSON data
			specs.add(new Species(sid, cname, false, false, nat));
		}
		
		return specs.toArray(new Species[specs.size()]);
	}
}