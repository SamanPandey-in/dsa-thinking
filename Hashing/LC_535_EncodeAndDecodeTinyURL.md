# 535. Encode and Decode TinyURL

## Description: 

TinyURL is a URL shortening service where you enter a URL such as https://leetcode.com/problems/design-tinyurl and it returns a short URL such as http://tinyurl.com/4e9iAk. Design a class to encode a URL and decode a tiny URL.

There is no restriction on how your encode/decode algorithm should work. You just need to ensure that a URL can be encoded to a tiny URL and the tiny URL can be decoded to the original URL.

Implement the Solution class:

Solution() Initializes the object of the system.
String encode(String longUrl) Returns a tiny URL for the given longUrl.
String decode(String shortUrl) Returns the original long URL for the given shortUrl. It is guaranteed that the given shortUrl was encoded by the same object.

## Step-by-step plan:

1. Use a List:

    - The simplest way to create a URL shortener is to assign each long URL a unique integer ID based on its position in a list. 
    - The short URL is just the base URL plus this index. 
    - When decoding, we extract the index from the short URL and retrieve the original URL from the list. 
    - This approach is straightforward but the short URL length grows with the number of stored URLs.

    ```java
    public class Codec {
        private List<String> urls;

        public Codec() {
            urls = new ArrayList<>();
        }

        // Encodes a URL to a shortened URL.
        public String encode(String longUrl) {
            urls.add(longUrl);
            return "http://tinyurl.com/" + (urls.size() - 1);
        }

        // Decodes a shortened URL to its original URL.
        public String decode(String shortUrl) {
            int idx = Integer.parseInt(shortUrl.substring(shortUrl.lastIndexOf('/') + 1));
            return urls.get(idx);
        }
    }
    ```

    - Time Complexity: O(n) - Same for all cases

    - Space Complexity: O(n*m) (Where n is the number of longUrls, m is the average length of the URLs)

2. Use a HashMap:

    - Instead of using a list with implicit indices, we use a hash map with an explicit incrementing ID. 
    - This offers more flexibility since we can use the ID as a key and don't rely on list indexing. Each new URL gets assigned the current ID, which then increments. 
    - The hash map allows constant-time lookup when decoding.

    - Maintain a hash map url_map and an integer id starting at 0.
    - encode(longUrl):
        - Store url_map[id] = longUrl.
        - Generate the short URL using the current id.
        - Increment id and return the short URL.
    - decode(shortUrl):
        - Parse the ID from the short URL.
        - Return url_map[id].

    ```java
    public class Codec {
        private HashMap<Integer, String> urlMap;
        private int id;

        public Codec() {
            urlMap = new HashMap<>();
            id = 0;
        }

        public String encode(String longUrl) {
            urlMap.put(id, longUrl);
            return "http://tinyurl.com/" + id++;
        }

        public String decode(String shortUrl) {
            int urlId = Integer.parseInt(shortUrl.substring(shortUrl.lastIndexOf('/') + 1));
            return urlMap.get(urlId);
        }
    }
    ```

    - Time Complexity: O(n) - Same for all cases

    - Space Complexity: O(n*m) (Where n is the number of longUrls, m is the average length of the URLs)