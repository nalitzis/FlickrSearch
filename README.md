# FlickrSearch

## Architecture
It is based on MVP pattern. The view requests new images to be fetched, and the presenter asks the network layer (via ServiceApi) to fetch data (which will lead to a call to Flickr endpoint or to a URL of an image).
I tried to create a design which might be easily adapted to other image providers.

## Tradeoffs
Unit tests are not exhaustive. The FlickrRequestManager for instance would need some unit tests but unfortunately I didn't have time to implement those.


