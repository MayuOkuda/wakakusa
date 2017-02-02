<?php

require_once "test.php";
require_once "student.php";
require_once "course.php";
require_once "score.php";
require_once "news.php";

define('DSN','mysql:host=localhost;dbname=KUTPortal');
define('DB_USER','root');
define('DB_PASSWORD','root00');
error_reporting(E_ALL & ~E_NOTICE);

mb_language("uni");
mb_internal_encoding("utf-8"); //内部文字コードを変更
mb_http_input("auto");
mb_http_output("utf-8");


/* DBアクセスモジュール */
function connectDb() {
    //DBアクセス
    try {
        return new PDO(DSN, DB_USER, DB_PASSWORD);
    } catch (PDOException $e) {
        echo $e->getMessage();
        exit;
    }
};

/* JSON表示モジュール */
function json($num, $day, $time){

     //それぞれのデータを呼び出し
     student_json($num, $day, $time);
     course_json($num, $day, $time);
     score_json($num, $day, $time);
     test_json($num, $day, $time);
     news_json($num, $day, $time);

}
?>
