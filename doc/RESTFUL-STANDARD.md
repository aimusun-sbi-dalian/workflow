# RESTful Api Design standard
## 1 HTTP protocol definition
- POST (create resource)
- GET (retrieve resource)
- PUT (update resource)
- DELETE (delete resource)

## 2 URL unified format
- URL only use nouns (Prohibit the use of verbs, and defining specific behaviors through the HTTP method);
- URL is first the object (`/object`), then the unique identifier of the object, e.g `GET /task/123`;
- URL names are all lowercase;
- URL use `-` to connect multiple words, for example: `http://api.domain.com/school/class/reading-corner`;
- Use camel case for parameter names;
- `/` indicates a hierarchical relationship;
- URL must have a clear version number;
- To request all data (without pagination) please use `/all` on the URL

## 3 HTTP status code
The server response uniformly uses 200

## 4 Unified request/response format
JSON (`Accept: application/json; Content-Type: application/json`)

## 5 API document management


