package com.android.volley.http;

/**
 * LoadListener special for ByteArrayLoadControler
 * 
 * @author steven-pan
 * 
 */
public interface LoadListener {
	
	void onStart();

	void onSuccess(byte[] data, String url, int actionId);

	void onError(String errorMsg, String url, int actionId);
}
