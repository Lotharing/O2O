/**
 * 从后台获取展示界面
 * $(document).ready(function(){})简写$(function() {});
 */
$(function() {
	// 从URL里获取shopId参数的值
	var shopId = getQueryString('shopId');
	//看shopId是否为空，为空时候就是注册店铺的，不为空就是获取店铺可以进行修改的，以为两者用的同一页面
	var isEdit = shopId ? true : false;
	// 用于店铺注册时候的店铺类别以及区域列表的初始化的URL
	var initUrl ='/o2o/shopadmin/getshopinitinfo';
	// 注册店铺的URL
	var registerShopUrl = '/o2o/shopadmin/registershop';
	// 编辑店铺前需要获取店铺信息，这里为获取当前店铺信息的URL
	var shopInfoUrl = "/o2o/shopadmin/getshopbyid?shopId=" + shopId;
	// 编辑店铺调用的Url
	var editShopUrl = "/o2o/shopadmin/modifyshop";
	
	//判断是编辑操作还是注册操作
	if (!isEdit) {
		//取得初始化区域，类别下拉列表信息
		getShopInitInfo();
	}else{
		getShopInfo(shopId);;
	}

	//根据shopId获取相应的shop信息并放在前端页面中
	function getShopInfo(shopId) {
		$.getJSON(shopInfoUrl,function(data){
			if (data.success) {
				var shop = data.shop;
				$('#shop-name').val(shop.shopName);
				$('#shop-addr').val(shop.shopAddr);
				$('#shop-phone').val(shop.phone);
				$('#shop-desc').val(shop.shopDesc);
				// 给店铺类别选定原先的店铺类别值
				var shopCategory = '<option data-id="'
						+ shop.shopCategory.shopCategoryId + '" selected>'
						+ shop.shopCategory.shopCategoryName + '</option>';
				
				var tempAreaHtml = '';
				
				// 初始化区域列表
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
				});
				//指定店铺类别已选项
				$('#shop-category').html(shopCategory);
				// 不允许选择店铺类别
				$('#shop-category').attr('disabled', 'disabled');
				//所有的区域信心
				$('#area').html(tempAreaHtml);
				// 给店铺选定原先的所属的区域
				$("#area option[data-id='" + shop.area.areaId + "']").attr(
						"selected", "selected");
			}
		});
	}
	
	// 取得所有二级店铺类别以及区域信息，并分别赋值进类别列表以及区域列表下拉框
	function getShopInitInfo(){
		$.getJSON(initUrl,function(data){
			if(data.success){
				var tempHtml = '';
				var tempAreaHtml = '';
				data.shopCategoryList.map(function(item,index){
					tempHtml += '<option data-id="' + item.shopCategoryId
					+ '">' + item.shopCategoryName + '</option>';
				});
				data.areaList.map(function(item,index){
					tempAreaHtml+='<option data-id="'+item.areaId+'">'+item.areaName+'</option>'
				});
				$('#shop-category').html(tempHtml);
				$('#area').html(tempAreaHtml);
			}
		});
	}
	/**
	 * 表单的提交
	 */
	$('#submit').click(function () {
		var shop = {};
		if (isEdit) {
			shop.shopId=shopId;
		}
		// 获取表单里的数据并填充进对应的店铺属性中
		shop.shopName = $('#shop-name').val();
		shop.shopAddr = $('#shop-addr').val();
		shop.phone = $('#shop-phone').val();
		shop.shopDesc = $('#shop-desc').val();
		// 选择选定好的店铺类别
		shop.shopCategory = {
			shopCategoryId : $('#shop-category').find('option').not(function() {
				return !this.selected;
			}).data('id')
		};
		// 选择选定好的区域信息
		shop.area = {
			areaId : $('#area').find('option').not(function() {
				return !this.selected;
			}).data('id')
		};
		// 获取上传的图片文件流
		var shopImg = $('#shop-img')[0].files[0];
		// 生成表单对象，用于接收参数并传递给后台
		var formData = new FormData();
		// 添加图片流进表单对象里
		formData.append('shopImg', shopImg);
		// 将shop json对象转成字符流保存至表单对象key为shopStr的的键值对里
		formData.append('shopStr', JSON.stringify(shop));
		var verifyCodeActual = $('#j_captcha').val;
		if(!verifyCodeActual){
			$.toast('请输入验证码');
			return;
		}
		//提交后提供CodeUtil从request获取并进行验证
		formData.append('verifyCodeActual',verifyCodeActual);
		// 将数据提交至后台处理相关操作
		$.ajax({
			url : (isEdit ? editShopUrl : registerShopUrl),
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
				} else {
					//负责展示ShopManagementController的提交错误时modelMap中存储的错误信息
					$.toast('提交失败！' + data.errMsg);
				}
				//每次提交不论成功失败都更换验证码
				$('#captcha').click();
			}
		});
	});
	
});