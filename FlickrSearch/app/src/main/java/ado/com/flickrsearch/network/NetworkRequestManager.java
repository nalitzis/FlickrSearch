package ado.com.flickrsearch.network;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class NetworkRequestManager implements RequestManager {

    private static final String TAG = "NetworkRequestManager";

    private final ExecutorService mService;

    //TODO
    //understand what is best to do here: separate search request from img requests??
    public NetworkRequestManager() {
        mService = Executors.newFixedThreadPool(3);
    }

    @Override
    public void add(Request request) {
        mService.execute(new RequestExecutor(request));
    }

    private class RequestExecutor implements Runnable {
        private Request mRequest;

        RequestExecutor(final Request request) {
            mRequest = request;
        }

        @Override
        public void run() {
            try {
                Response response = downloadUrl();
            } catch (IOException ioe) {
                Log.d(TAG, "IO exception: " + ioe.getMessage());
            }
        }

        private Response downloadUrl() throws IOException {
            //TODO
            InputStream stream = null;
            HttpsURLConnection connection = null;
            Response response = null;
            try {
                connection = (HttpsURLConnection) mRequest.getUrl().openConnection();
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                stream = connection.getInputStream();
                if (stream != null) {
                    if(mRequest.getType() == Request.ExpectedResultType.TEXT) {
                        //TODO create a response with String (to be parsed)
                        //result = readStream(stream);
                    } else if(mRequest.getType() == Request.ExpectedResultType.BLOB) {
                        //TODO create a response with InputStream
                    }

                }
            } finally {
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            //TODO return response
            return null;
        }

        /*public String readStream(InputStream stream)
                throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] rawBuffer = new char[maxReadSize];
            int readSize;
            StringBuffer buffer = new StringBuffer();
            while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
                if (readSize > maxReadSize) {
                    readSize = maxReadSize;
                }
                buffer.append(rawBuffer, 0, readSize);
                maxReadSize -= readSize;
            }
            return buffer.toString();
        }*/

    }

    @Override
    public void cancel(UUID requestId) {

    }
}
