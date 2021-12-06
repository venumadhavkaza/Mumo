package com.venumadhav.mumo.network;

import com.google.gson.annotations.SerializedName;

public class PeekaLinkResponse {

	@SerializedName("image")
	private Image image;

	@SerializedName("redirected")
	private boolean redirected;

	@SerializedName("icon")
	private Icon icon;

	@SerializedName("description")
	private String description;

	@SerializedName("mimeType")
	private String mimeType;

	@SerializedName("title")
	private String title;

	@SerializedName("url")
	private String url;

	@SerializedName("nextUpdate")
	private String nextUpdate;

	@SerializedName("lastUpdated")
	private String lastUpdated;

	@SerializedName("domain")
	private String domain;

	@SerializedName("name")
	private String name;

	@SerializedName("trackersDetected")
	private boolean trackersDetected;

	@SerializedName("contentType")
	private String contentType;

	public Image getImage(){
		return image;
	}

	public boolean isRedirected(){
		return redirected;
	}

	public Icon getIcon(){
		return icon;
	}

	public String getDescription(){
		return description;
	}

	public String getMimeType(){
		return mimeType;
	}

	public String getTitle(){
		return title;
	}

	public String getUrl(){
		return url;
	}

	public String getNextUpdate(){
		return nextUpdate;
	}

	public String getLastUpdated(){
		return lastUpdated;
	}

	public String getDomain(){
		return domain;
	}

	public String getName(){
		return name;
	}

	public boolean isTrackersDetected(){
		return trackersDetected;
	}

	public String getContentType(){
		return contentType;
	}
}