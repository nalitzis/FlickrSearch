package ado.com.flickrsearch.network;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

public class RequestExecutor implements Callable<Response> {

    private static final String TAG = "RequestExecutor";

    private final RequestListener mListener;
    private final Request mRequest;

    private static final int CONN_TIMEOUT_MSEC = 3000;
    private static final String HTTP_METHOD = "GET";

    RequestExecutor(final Request request, final RequestListener listener) {
        mRequest = request;
        mListener = listener;
    }


    @Override
    public Response call() throws Exception {
        try {
            Response response = downloadUrl();
            if(response != null) {
                mListener.onCompleted(mRequest.getUrl(), response);
            }  else {
                mListener.onError(mRequest.getUrl(), new Exception("response was empty"));
            }
            return response;
        } catch (IOException ioe) {
            Log.d(TAG, "IO exception: " + ioe.getMessage());
            mListener.onError(mRequest.getUrl(), ioe);
        }
        return null;
    }


    private Response downloadUrl() throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        Response response = null;
        try {
            connection = (HttpsURLConnection) mRequest.getUrl().openConnection();
            connection.setReadTimeout(CONN_TIMEOUT_MSEC);
            connection.setConnectTimeout(CONN_TIMEOUT_MSEC);
            connection.setRequestMethod(HTTP_METHOD);
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
                    response = new NetworkResponse(mRequest.getUrl(), contents, Response.Type.TEXT);
                } else if(mRequest.getType() == Request.ExpectedResultType.IMAGE) {
                    response = new NetworkResponse(mRequest.getUrl(), contents, Response.Type.IMAGE);
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
    }

}
