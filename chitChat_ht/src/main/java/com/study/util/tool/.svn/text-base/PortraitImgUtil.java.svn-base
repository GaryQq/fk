package com.study.util.tool;

/**
 * 根据头像图片地址拼接前缀
 * 
 * @author Administrator
 *
 */
public class PortraitImgUtil {

	public static String getPortraitImgUtil(String url, String type) {
		String img = "";
		if (url.contains("http:")) {
			return url;
		} else {
			if (url.equals("") || url.contains("/face/temp/middlehead.jpg")) {
				img = "http://download.fengkuangtiyu.cn/cp365/face/temp/fktyDftHead.png";
			} else {
				if (StringUtil.nullZero(type) == 1) {
					String sub = url.subSequence(0, 1) + "";
					if (!"/".equals(sub)) {
						url = "/" + url;
					}
					img = "http://file.fengkuang.cn" + url;
				} else {
					img = "http://timg.cmwb.com/" + url;
				}

			}
		}

		return img;

	}

	public static void main(String[] args) {
		String str = getPortraitImgUtil("http://file.caipiao365.com/lotteryCs/009/00915300092683583436.png", "1");
		System.out.println(str);
	}
}
