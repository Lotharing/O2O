/**
 * 点击更换验证码
 */
function changeVerifyCode(img) {
	img.src="../Kaptcha?"+Math.floor(Math.random()*100);
}
/**
 * 从URL参数中用正则表达式自动匹配
 * 获取shopId
 * name是调用时候传递过来的shopId
 */
function getQueryString(name) {
	var reg=new RegExp("(^|&)"+name+"=([^&]*)(&|$)");
	var r=window.location.search.substr(1).match(reg);
	if(r!=null){
		return decodeURIComponent(r[2]);
	}
	return "";
}