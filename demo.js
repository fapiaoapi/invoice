<script src="https://cdn.bootcdn.net/ajax/libs/blueimp-md5/2.19.0/js/md5.js"></script>
<script src="https://cdn.jsdelivr.net/npm/fetch-polyfill@0.3.5/dist/fetch.min.js"></script>

let oHead = document.getElementsByTagName('HEAD').item(0);
let scriptMd5= document.createElement("script");
scriptMd5.type = "text/javascript";
scriptMd5.src="https://cdn.bootcdn.net/ajax/libs/blueimp-md5/2.19.0/js/md5.js";
oHead.appendChild(scriptMd5);

let scriptFetch = document.createElement("script");
scriptFetch.type = "text/javascript";
scriptFetch.src = "https://cdn.jsdelivr.net/npm/fetch-polyfill@0.3.5/dist/fetch.min.js";
oHead.appendChild(scriptFetch);

function generateRandomNum(min, max) {
    return Math.floor(Math.random() * (max - min)) + min;
}

function generateRandomString(length) {
    let x = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    let tmp = "";
    for (let i = 0; i < length; i++) {
        tmp += x.charAt(Math.ceil(Math.random() * 100000000) % x.length);
    }
    return tmp;
}


let appKey = "test";
let secret = "ZfFNRhzMbdpGQsYArc9dSZZAVEJ3A7";
let requestId = generateRandomNum(1, 10000);
let timeStamp = new Date().getTime().toString().substr(0, 10);
let randomString = generateRandomString(20);
let requestString = generateRandomString(20);

let string = "RandomString="+randomString+"&RequestId="+requestId+"&TimeStamp="+timeStamp+"&appKey=test&secret="+secret;
let signString = md5(string).toUpperCase();
console.log(string);
console.log(signString);

let url = "https://api.fa-piao.com/v3/merchant/login";
let data = {"id": 109,"name": "tom"};

fetch(url, {
    method: 'POST',
    body: JSON.stringify(data), // data can be `string` or {object}!
    headers: new Headers({
        'Content-Type': 'application/json',
        'AppKey': appKey,
        'RequestId': requestId,
        'TimeStamp': timeStamp,
        'RandomString': randomString,
        'RequestString': requestString,
        'Sign': signString
    })
}).then(res => res.json())
    .catch(error => console.error('Error:', error))
    .then(response => console.log('Success:', response));