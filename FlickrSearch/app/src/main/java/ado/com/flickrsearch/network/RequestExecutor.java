package ado.com.flickrsearch.network;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

public class RequestExecutor implements Callable<Response> {

    private static final String TAG = "RequestExecutor";

    private final RequestManager.RequestListener mListener;
    private final Request mRequest;

    RequestExecutor(final Request request, final RequestManager.RequestListener listener) {
        mRequest = request;
        mListener = listener;
    }


    @Override
    public Response call() throws Exception {
        try {
            Log.d(TAG, "request url: " + mRequest.getUrl());

            //TODO maybe revert to Runnable
            Response response = downloadUrl();

            if(response != null) {
                //final String result = readStream(response.getContents());
                Log.d(TAG, "size:" + response.getContents().length);
                mListener.onCompleted(mRequest.getUrl(), response);
            }  else {
                mListener.onError(mRequest.getUrl());
            }


            return response;
        } catch (IOException ioe) {
            Log.d(TAG, "IO exception: " + ioe.getMessage());
        }
        return null;
    }


    private Response downloadUrl() throws IOException {
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
                final byte[] contents = readStream(stream);
                if(mRequest.getType() == Request.ExpectedResultType.TEXT) {
                    response = new NetworkResponse(contents, Response.Type.TEXT);
                } else if(mRequest.getType() == Request.ExpectedResultType.IMAGE) {
                    response = new NetworkResponse(contents, Response.Type.IMAGE);
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
        return response;
    }

    private byte[] readStream(final InputStream stream) throws IOException {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toByteArray();
        //return result.toString("UTF-8");
    }

}
