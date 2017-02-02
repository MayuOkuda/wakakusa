

<?php

require_once "json.php";

/*Cookie設定モジュール
　ユーザにCookieを設定する
*/

function setup_auto_login($user_name)
{
	// Cookie生成
	$auto_login_key = sha1( uniqid() . mt_rand( 1,999999999 ) . '_auto_login' );
	$cookieExpire = time() + 3600*24*7;  // 7日間
	$cookiePath = '/';

	// DBに挿入
	$dbh = connectDb();
	$sql = "INSERT INTO wp_auto_login (user_name, auto_login_key) VALUES (:name, :login_key)";
	$stmt = $dbh->prepare($sql);
	$params = array(':name' => ("$user_name"), ':login_key' => ("$auto_login_key"));
	$stmt->execute($params);

	// Cookie設定
	setcookie( "auto_login", $auto_login_key, $cookieExpire, $cookiePath );

}

/*Cookie削除モジュール
　ユーザが保存している古いCookieを削除する
*/

function delete_auto_login($name, $auto_login_key = '')
{
	// DBから削除
        $dbh = connectDb();
	$sql = "DELETE FROM wp_auto_login WHERE user_name = :name";
	$stmt = $dbh->prepare($sql);
	$params = array(':name' => ("$name"));
	$stmt->execute($params);

	// Cookie削除
	$cookieExpire = time() - 1800;
	$cookiePath = '/';
	setcookie( "auto_login", $auto_login_key, $cookieExpire, $cookiePath );

}


?>
