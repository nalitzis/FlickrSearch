package ado.com.flickrsearch.network;

public class NetworkResponse implements Response {
    private final byte[] mContents;
    private final Type mType;

    NetworkResponse(final byte[] contents, final Type type) {
        mContents = contents;
        mType = type;
    }

    @Override
    public byte[] getContents() {
        return mContents;
    }

    @Override
    public Type getType() {
        return mType;
    }
}
