package com.android.volley.http;

import com.android.volley.Request;

/**
 * LoadControler for Request
 * 
 * @author steven pan
 * 
 */
public interface LoadControler {
	void cancel();
}

/**
 * Abstract LoaderControler that implements LoadControler
 * 
 * @author steven pan
 * 
 */
class AbsLoadControler implements LoadControler {
	
	protected Request<?> mRequest;

	public void bindRequest(Request<?> request) {
		this.mRequest = request;
	}

	@Override
	public void cancel() {
		if (this.mRequest != null) {
			this.mRequest.cancel();
		}
	}

	protected String getOriginUrl() {
		return this.mRequest.getOriginUrl();
	}
}
