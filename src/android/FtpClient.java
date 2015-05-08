package se.mwthinker.ftpclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class FtpClient extends CordovaPlugin {
    public static final String TAG = FtpClient.class.getSimpleName();
    
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.v(TAG,"Init plugin");
    }
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.v(TAG, "Plugin performs action:" + action);
        if (action == null || action == "") {
        	return false;
        } else {
        	try {
        		String filename = args.getString(0);
        		URL url = new URL(args.getString(1));
        		
        		if (action.equals("downloadAsciiString")) {        			
        			cordova.getThreadPool().execute(new DownloadAsAsciiString(url, callbackContext));
                } else if (action.equals("downloadAsciifile")) {
                	cordova.getThreadPool().execute(new DownloadAsFile(filename, true, url, callbackContext));
                } else if (action.equals("downloadBinaryFile")) {
                	cordova.getThreadPool().execute(new DownloadAsFile(filename, false, url, callbackContext));
                } else if (action.equals("upload")) {
                	cordova.getThreadPool().execute(new DownloadAsAsciiString(url, callbackContext));
                } else {
                    // Action does not match!
                    return false;
                }
        	} catch (MalformedURLException e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.MALFORMED_URL_EXCEPTION));
                Log.v(TAG, "MALFORMED_URL_EXCEPTION" + e.getMessage());
    		} catch (JSONException e) {
        		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
                Log.v(TAG, "JSON_EXCEPTION" + e.getMessage());
        	}
        	return true;
        }
    }
    
    abstract class FtpHelper implements Runnable {
		protected URL url;
		protected CallbackContext callbackContext;
	    
        public FtpHelper(URL url, CallbackContext callbackContext) {
        	this.url = url;
        	this.callbackContext = callbackContext;
        }

    	protected void close(FTPClient ftp) throws IOException {
    		ftp.logout();
    		ftp.disconnect();
    	}
        
    	protected FTPClient connectToServer() throws IOException {
    	    FTPClient ftp = new FTPClient();
    	    ftp.connect(url.getHost(), extractPort());
    	    String password = "anonymous";
    	    String username = "anonymous";
    	    
    	    String userInfo = url.getUserInfo();
    	    if (userInfo != null) {
                String[] array = userInfo.split(":");
                if (array.length != 2) {
                    throw new IOException("Userinfo incorrect in the ftp URL");
                }
    	    	username = array[0];
    	    	password = array[1];
    	    }
    	    ftp.login(username, password);

    		ftp.enterLocalPassiveMode();
    		return ftp;
    	}
    	
    	protected int extractPort() {
    		int port = url.getPort();
    		if (port == -1) {
    			return url.getDefaultPort();
    		}
    		return port;
    	}
    }
    
    class DownloadAsAsciiString extends FtpHelper {
        public DownloadAsAsciiString(URL url, CallbackContext callbackContext) {
            super(url, callbackContext);
        }
		
        @Override
		public void run() {
        	try {
				callbackContext.success(downloadAsString());
			} catch (IOException e) {
				callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.IO_EXCEPTION));
                Log.v(TAG, "IO_EXCEPTION" + e.getMessage());
			}
		}

    	private String downloadAsString() throws IOException {
    		FTPClient ftp = connectToServer();
    		ftp.setFileType(FTP.ASCII_FILE_TYPE);
    				
    		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    		ftp.retrieveFile(url.getFile(), byteOut);
    		String text = byteOut.toString();
    		
    		byteOut.flush();
    		byteOut.close();;
    		close(ftp);
    		return text;
    	}
    }
    
    class DownloadAsFile extends FtpHelper {
    	private boolean ascii;
    	private String filename;
    	
        public DownloadAsFile(String filename, boolean ascii, URL url, CallbackContext callbackContext) {
            super(url, callbackContext);
            this.ascii = ascii;
            this.filename = filename;
        }
        
        @Override
		public void run() {
        	try {
        		download();
            	callbackContext.success();
			} catch (IOException e) {
				callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.IO_EXCEPTION));
                Log.v(TAG, "IO_EXCEPTION" + e.getMessage());
			}
		}

    	private void download() throws IOException {
    		FTPClient ftp = connectToServer();
    		if (ascii) {
    			ftp.setFileType(FTP.ASCII_FILE_TYPE);
    		} else {
    			ftp.setFileType(FTP.BINARY_FILE_TYPE);
    		}
    		BufferedOutputStream buffOut = new BufferedOutputStream(new FileOutputStream(filename));
    		ftp.retrieveFile(url.getFile(), buffOut);
    		buffOut.close();;
    		close(ftp);
    	}
    }
    
    class UploadFile extends FtpHelper {
    	private String filename;
    	
        public UploadFile(String filename, boolean ascii, URL url, CallbackContext callbackContext) {
            super(url, callbackContext);
            this.filename = filename;
        }
        
        @Override
		public void run() {
        	try {
        		upload();
            	callbackContext.success();
			} catch (IOException e) {
				callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.IO_EXCEPTION));
                Log.v(TAG, "IO_EXCEPTION" + e.getMessage());
			}
		}

        private void upload() throws IOException {
    		FTPClient ftp = connectToServer();
    		BufferedInputStream buffIn = new BufferedInputStream(new FileInputStream(filename));
    		ftp.storeFile(url.getFile(), buffIn);
    		buffIn.close();
    		close(ftp);
    	}
    }
}
