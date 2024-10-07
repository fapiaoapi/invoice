<?php

function generateRandomString($length) {
    $characters = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $index = rand(0, strlen($characters) - 1);
        $randomString .= $characters[$index];
    }
    return $randomString;
}
$appKey = "test";
$secret = "ZfFNRhzMbdpGQsYArc9dSZZAVEJ3A7";
$requestId = rand(1,10000);
$timeStamp = time();
$randomString = generateRandomString(20);
$requestString = generateRandomString(20);

$string = "RandomString=".$randomString."&RequestId=".$requestId."&TimeStamp=".$timeStamp."&appKey=test&secret=".$secret;

$signString = strtoupper(md5($string));

echo $string.PHP_EOL;
echo $signString.PHP_EOL;


$url = 'https://api.fa-piao.com/v3/merchant/login';
$data = ['id' => 101];
$headers = [
    'AppKey: '.$appKey,
    'RequestId: '.$requestId,
    'TimeStamp: '.$timeStamp,
    'RandomString: '.$randomString,
    'RequestString: '.$requestString,
    'Sign: '.$signString
];

$curl = curl_init();
curl_setopt($curl, CURLOPT_URL, $url);
curl_setopt($curl, CURLOPT_HEADER, 1);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($curl, CURLOPT_POST, 1);
curl_setopt($curl, CURLOPT_POSTFIELDS, http_build_query($data));

curl_setopt($curl, CURLOPT_HEADER, 0);
curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);

$result = curl_exec($curl);
curl_close($curl);
echo $result.PHP_EOL;