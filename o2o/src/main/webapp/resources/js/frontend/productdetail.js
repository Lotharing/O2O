/**
 * 
 */

$(function(){
	//从url中获取productId信息
var productId=getQueryString('productId');
//设置商品信息的Url
var url='/o2o/frontend/listproductdetailpageinfo?productId='+productId;

$.getJSON(
		url,
		function(data){
			if(data.success){
				//获取商品
				var product=data.product;
				
				//给商品相关信息的HTML控件赋值
				
				//商品缩略图片
				$('#product-img').attr('src',product.imgAddr);
				//商品更新时间
				$('#product-time').text(
						new Date(product.lastEditTime).format("Y-m-d"));
				if(product.point!=null){
					$('#product-point').text('购买可得'+product.point+'积分');
					}
				
		        //商品名字
				$('#product-name').text(product.productName);
                //商品描述
				$('#product-desc').text(product.productDesc);
				// 商品价格展示逻辑，主要判断原价现价是否为空 ，所有都为空则不显示价格栏目
				if (product.normalPrice != undefined
						&& product.promotionPrice != undefined) {
					// 如果现价和原价都不为空则都展示，并且给原价加个删除符号
					$('#price').show();
					$('#normalPrice').html(
							'<del>' + '￥' + product.normalPrice + '</del>');
					$('#promotionPrice').text('￥' + product.promotionPrice);
				} else if (product.normalPrice != undefined
						&& product.promotionPrice == undefined) {
					// 如果原价不为空而现价为空则只展示原价
					$('#price').show();
					$('#promotionPrice').text('￥' + product.normalPrice);
				} else if (product.normalPrice == undefined
						&& product.promotionPrice != undefined) {
					// 如果现价不为空而原价为空则只展示现价
					$('#promotionPrice').text('￥' + product.promotionPrice);
				}
			
		
				//商品详情图列表虎丘并在前端展示
				var productImgList=product.productImgList;
				var imglisthtml='';
				productImgList.map(function(item,index){
					imglisthtml+='<div> <img src="' + item.imgAddr
					+ '" width="100%" /></div>';
				});
				//显示在前端页面控件中
				$('#imgList').html(imglisthtml);
			}
		});
		// 点击后打开右侧栏
		$('#me').click(function() {
			$.openPanel('#panel-right-demo');
		});
		$.init();
		
		
		
		
		
		$('#addcart').click(function(){
			var addcartUrl='/o2o/frontend/addcart';
			$.ajax({
				url : addcartUrl,
				type : 'POST',
				data : 'productId='+productId,
				success: function(data){
					//进行逻辑判断
					if(data.success){
						$.toast('成功添加购物车！');
					}else{
						$.toast('添加购物车失败！');
					}

				}
			});
		})
})


