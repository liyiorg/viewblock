package viewblock.jsptag;

import java.io.IOException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

public class TimeoutAsyncListener implements AsyncListener{
	
	private boolean timeout = false;

	public void onComplete(AsyncEvent arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void onError(AsyncEvent arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void onStartAsync(AsyncEvent arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void onTimeout(AsyncEvent arg0) throws IOException {
		timeout = true;
	}

	public boolean isTimeout() {
		return timeout;
	}

	
}
