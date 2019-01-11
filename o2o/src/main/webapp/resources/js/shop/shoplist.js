/**
 * 
 */
$(function() {
	getlist();
	//请求获得shopList
	function getlist(e) {
		$.ajax({
			url : "/o2o/shopadmin/getshoplist",
			type : "get",
			dataType : "json",
			success : function(data) {
				if (data.success) {
					handleList(data.shopList);
					handleUser(data.user);
				}
			}
		});
	}
	//user信息写进前台页面
	function handleUser(data) {
		$('#user-name').text(data.name);
	}
	//shoplist信息写进前台页面
	function handleList(data) {
		var html = '';
		data.map(function(item, index) {
			html += '<div class="row row-shop"><div class="col-40">'
					+ item.shopName + '</div><div class="col-40">'
					+ shopStatus(item.enableStatus)
					+ '</div><div class="col-20">'
					+ goShop(item.enableStatus, item.shopId) + '</div></div>';

		});
		//两个信息分别放入DIV
		$('.shop-wrap').html(html);
	}
	//判断店铺状态
	function shopStatus(status) {
		if (status == 0) {
			return '审核中';
		} else if (status == -1) {
			return '店铺非法';
		} else if (status == 1) {
			return '审核通过';
		}
	}
	//从店铺列表根据id指向商铺管理页
	function goShop(status, id) {
		if (status == 1) {
			return '<a href="/o2o/shopadmin/shopmanagement?shopId=' + id
					+ '">进入</a>';
		} else {
			return '';
		}
	}
});