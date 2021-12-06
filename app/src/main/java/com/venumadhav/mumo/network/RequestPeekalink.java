package com.venumadhav.mumo.network;

import com.google.gson.annotations.SerializedName;

public class RequestPeekalink{

	@SerializedName("link")
	private String link;

	public void setLink(String link){
		this.link = link;
	}

	public String getLink(){
		return link;
	}
}