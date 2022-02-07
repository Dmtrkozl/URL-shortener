# URL-shortener
URL shortener RESTful web service



A simple application providing RESTful API for creating your short links.
You can manage generated links:
- set link expired time
- delete 
- view various statistics (link click count, unique ip list, last click date, created/expired time and date, url, hash)
#### Also you can use UI and watch links statistic:

![web_example](https://user-images.githubusercontent.com/68345551/152799924-3ec63558-404f-428b-8a21-834c8d038982.png)

## API description:
All endpoints require basic authentication (HTTP Basic)\
As a login and password, you must use: 
- Login: admin
- Password: admin

### 1. Create a short link
**POST** http://localhost:8080/api/link

Request body in JSON format
#### Request example:
```bash
curl --location --request POST 'http://localhost:8080/api/link' \
--header 'Authorization: Basic YWRtaW46YWRtaW4=' \
--header 'Content-Type: application/json' \
--data-raw '{
"url": "https://mail.ru",
"expire": "300"
}'
```
#### Purpose of fields:
- url - shortened link address. Link must contain Http/Https
- expire (optional) - sets the lifetime of the link in seconds, after which it is deleted.
#### Return value example:
```bash
{
"hash": "c0vui2",
"url": "https://mail.ru",
"statistic": {
"id": 1,
"createdDateTime": "2022-01-28T16:57:48.52793",
"expiredDateTime": "2022-01-28T17:02:48.52793",
"clickCount": 0,
"uniqueClickCount": 0,
"lastClickDateTime": null,
"uniqueIpList": []
}
}
```
#### Field values:
- hash - unique link identifier used for further redirection.
- url - shortened link address
- statistic - statistics array, contains:
  - createdDateTime - date and time of creation;
  - expiredDateTime - date and time when "life" ends;
  - clickCount - number of clicks;
  - uniqueClickCount - number of unique clicks;
  - lastClickDateTime - date and time of the last click;
  - uniqueIpList - an array of unique ip addresses that followed the link.

### 2. Redirect by link
**GET** http://localhost:8080/api/{hash}
#### Request example:
```bash
curl --location --request GET 'http://localhost:8080/api/c0vui2' \
--header 'Authorization: Basic YWRtaW46YWRtaW4='
```
#### Return value:
Redirect to the specified URL in the link

### 3. View information about the created link
**GET** http://localhost:8080/api/link/{hash}

#### Request example:
```bash
curl --location --request GET 'http://localhost:8080/api/link/4rvb4s' \
--header 'Authorization: Basic YWRtaW46YWRtaW4='
```
#### Return value:
```bash
{
"hash": "4rvb4s",
"url": "https://google.com",
"statistic": {
"id": 2,
"createdDateTime": "2022-01-28T18:43:40.182224",
"expiredDateTime": null,
"clickCount": 0,
"uniqueClickCount": 0,
"lastClickDateTime": null,
"uniqueIpList": []
}
}
```

### 4. View information about all links created by the user
**GET** http://localhost:8080/api/link
#### Request example:
```bash
curl --location --request GET 'http://localhost:8080/api/link/' \
--header 'Authorization: Basic YWRtaW46YWRtaW4='
Return value:
[
{
"hash": "4rvb4s",
"url": "https://google.com",
"statistic": {
"id": 2,
"createdDateTime": "2022-01-28T18:43:40.182224",
"expiredDateTime": null,
"clickCount": 0,
"uniqueClickCount": 0,
"lastClickDateTime": null,
"uniqueIpList": []
}
},
{
"hash": "akccga",
"url": "https://stackoverflow.com",
"statistic": {
"id": 11,
"createdDateTime": "2022-01-28T19:10:53.210301",
"expiredDateTime": "2022-01-28T19:44:13.210301",
"clickCount": 5,
"uniqueClickCount": 2,
"lastClickDateTime": "2022-01-28T19:15:33.588135",
"uniqueIpList": [
"192.168.1.9",
"127.0.0.1"
]
}
}
]
```

### 5. Removing a link
**DELETE** http://localhost:8080/api/link/{hash}
#### Request example:
```bash
curl --location --request DELETE 'http://localhost:8080/api/link/1kju2h' \
--header 'Authorization: Basic YWRtaW46YWRtaW4='
#### Return value:
HTTP STATUS 200 on successful removal
```
### 6. Changing the link (editing the expiration date)
**PUT** http://localhost:8080/api/link/{hash}

Request body in JSON format
#### Request example:
```bash
curl --location --request PUT 'http://localhost:8080/api/link/4rvb4s' \
--header 'Authorization: Basic YWRtaW46YWRtaW4=' \
--header 'Content-Type: application/json' \
--data-raw '{
"expire": "200"
}'
```
#### Purpose of fields:
expire - sets the lifetime of the link in seconds, after which it is deleted. With a value of 0, the lifetime is unlimited.

#### Return value:
HTTP STATUS 200 on successful data change.

## Project build:
mvn package in root directory

## Additional Information:
To monitor statistics, you can use the graphical interface available at: http://localhost:8080. It also allows you to add and remove links.
