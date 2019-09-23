package com.study.service.management.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


import com.github.pagehelper.PageHelper;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.study.mapper.management.BannerMapper;
import com.study.service.management.BannerService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;
import com.mongodb.BasicDBObject;

@Service("bannerService")
public class BannerServiceImpl implements BannerService {

	private static Logger logger = LoggerFactory.getLogger(BannerServiceImpl.class);

	@Autowired
	private BannerMapper bannerMapper;
	
	@Autowired
    private MongoTemplate mongoTemplate;

	/** 声明全局变量返回容器 */
	private List<Map<String, Object>> res_list;
	/** 声明全局变量返回容器 */
	private Map<String, Object> res_map;

	@Override
	public List<Map<String, Object>> findBannerView(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		res_list = bannerMapper.findBannerView(param);
		return res_list;
	}

	@Override
	public Map<String, Object> findBannerEntity(Integer id) {
		res_map = bannerMapper.findBannerEntity(id);
		return res_map;
	}

	@Override
	public void insertBannerEntity(Map<String, Object> param) {
		bannerMapper.insertBannerEntity(param);
	}

	@Override
	public void deleteBannerEntity(int id) {
		bannerMapper.deleteBannerEntity(id);
	}

	@Override
	public void updateBannerEntity(Map<String, Object> param) {
		bannerMapper.updateBannerEntity(param);
	}

	@Override
	public void enableBannerEntity(Map<String, Object> param) {
		bannerMapper.enableBannerEntity(param);
	}

	@Override
	public List<Map<String, Object>> findYubanWithDrawsDetail(
			Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		res_list = bannerMapper.findYubanWithDrawsDetail(param);
		logger.info("获取到的集合数量--"+res_list.size());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Map<String, Object> m:res_list){
			//logger.info("获取到的东西-"+m);
			m.put("TARGETAMOUNT",convert_targetAmount(m.get("PAYMENT_CURRENCIES")+"",m.get("AMOUNT")+""));
			m.put("SOURCECURRENCY","CNH");
			m.put("CREATE_TIME",sdf.format((Date)m.get("CREATE_TIME")));
		}
		return res_list;
	}
	
	public String convert_targetAmount(String countryCode,String amount){
		String re_string="";
		//首先根据 国家代码 去获取 汇率
		 DBCollection collection = mongoTemplate.getCollection("yuban_tx_huilv");
         BasicDBObject searchCond = new BasicDBObject();
         searchCond.put("country_code",countryCode);

         DBObject cursor = collection.findOne(searchCond);
         if(cursor!=null && cursor.containsKey("country_code")){
        	 //有汇率的话 就去计算汇率吧
        	 double huilv=Double.valueOf(cursor.get("bankConversionPri")+"")/100;
        	 re_string=Double.valueOf(amount)/huilv+1+"";
         }
         return re_string;
	}

	@Override
	public void updateTxStatus(String id, String status) {
		//首先根据状态去判断 走不同的逻辑
		if(status.equals("2")){
			//如果失败了的话  要把钱给用户加回去
			Map<String,Object> user_map=bannerMapper.getTxAmount(Integer.valueOf(id));
			double fee=bannerMapper.getUserAccount(user_map.get("USER_NAME")+"");
			user_map.put("BRICK_AMOUNT", Double.valueOf(user_map.get("AMOUNT")+""));
			bannerMapper.updateUserAccount(user_map);
			Map<String,Object> detail_map=new HashMap<String,Object>();
			detail_map.put("USER_NAME",user_map.get("USER_NAME"));
			detail_map.put("AMOUNT",user_map.get("BRICK_AMOUNT"));
			detail_map.put("OPT_TYPE1",1);
			detail_map.put("OPT_TYPE2",7);
			detail_map.put("OPT_TYPE3",169);
			detail_map.put("BEFORE_AMOUNT",fee);
			detail_map.put("AFTER_AMOUNT",fee+Double.valueOf(user_map.get("BRICK_AMOUNT")+""));
			detail_map.put("REMARK","语伴提现失败退还金砖");
			detail_map.put("ORDER_ID",id);
			bannerMapper.insertUserDetail(detail_map);
		}
		//然后需要改一下订单表的状态
		Map<String,Object> order_map=new HashMap<String, Object>();
		order_map.put("id", Long.valueOf(id));
		order_map.put("status", Integer.valueOf(status));
		bannerMapper.updateTxOrderStatus(order_map);
	}

	@Override
	public Map<String, Object> exportWithDrawsDetail(Map<String, Object> param) {
		Map<String, Object> map_res=new HashMap<String, Object>();
		res_list = bannerMapper.findYubanWithDrawsDetail(param);
		if(res_list!=null && res_list.size()>0){
			//excel工作薄对象
			HSSFWorkbook hwb = new HSSFWorkbook();
			
			//excel sheet对象
			HSSFSheet sheet = hwb.createSheet("提现表格");
			
			//excel行对象
			HSSFCell cell;
			
			//excel列对象
			HSSFRow row;
			
			//excel样式
			HSSFCellStyle style = hwb.createCellStyle();//创建一个单元的样式
			style.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);//背景色的设定
			style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//前景色的设定
			
			//填充模式
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//设置水平居中
			style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);//上下居中
			
			//设置上下左右边框样式
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			
			//设置字体
			HSSFFont font = hwb.createFont();
			font.setFontName("黑体");
			font.setFontHeightInPoints((short)22);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			
			//设置表头
			String [] head_row = {"countryCode", "payee", "sourceCurrency", "sourceAmount", "targetCurrency", "targetAmount", "sourceOrTarget", "merchantReference", "paymentReference", "accountNumber", "iban", "swift", "bankName", "bankCode/BSB code", "accountType","B_AddressLine1","B_City","B_State","B_CountryCode","B_Postcode"};
			
			//在第一行创建表头
			row = sheet.createRow(0);
			
			//遍历每一列
			for(int i = 0; i < head_row.length; i++) {
				
				//创建行对象
				cell = row.createCell((short)i);
				
				//设置编码
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				
				//设置内容
				cell.setCellValue(head_row[i]);
				
				//设置样式
				cell.setCellStyle(style);
				
			}
			
			//列游标(第一行index:0用于创建表头,所以内容从第二行index:1开始)
			int row_num = 1;
			
			//遍历数据
			for (Map<String, Object> map_page : res_list) {
				
				//待用数据赋值
				//String send_status = map_page.get("send_status");//发送状态
				
				//创建行对象
				row = sheet.createRow(row_num);
				
				//创建第1列(揭晓时间)
				cell = row.createCell((short)0);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("COUNTRYCODE")==null?"":map_page.get("COUNTRYCODE")+"");
				cell.setCellStyle(style);
				
				//创建第2列(期次)
				cell = row.createCell((short)1);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("TRUE_NAME")==null?"":map_page.get("TRUE_NAME")+"");
				cell.setCellStyle(style);
				
				//创建第3列(商品编号)
				cell = row.createCell((short)2);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("CNH");
				cell.setCellStyle(style);
				
				//创建第4列(商品名称)
				cell = row.createCell((short)3);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("AMOUNT")==null?"":map_page.get("AMOUNT")+"");
				cell.setCellStyle(style);
						
				//创建第5列(中奖者)
				cell = row.createCell((short)4);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("PAYMENT_CURRENCIES")==null?"":map_page.get("PAYMENT_CURRENCIES")+"");
				cell.setCellStyle(style);
				
				//创建第6列(手机号码)
				cell = row.createCell((short)5);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(convert_targetAmount(map_page.get("PAYMENT_CURRENCIES")+"",map_page.get("AMOUNT")+""));
				cell.setCellStyle(style);
				
				//创建第7列(真实姓名)
				cell = row.createCell((short)6);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("SOURCEORTARGET")==null?"":map_page.get("SOURCEORTARGET")+"");
				cell.setCellStyle(style);
				
				//创建第8列(收货地址)
				cell = row.createCell((short)7);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("");
				cell.setCellStyle(style);
				
				//创建第9列(物流公司)
				cell = row.createCell((short)8);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("");
				cell.setCellStyle(style);
				
				//创建第10列(物流编号)
				cell = row.createCell((short)9);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("BANK_NO")==null?"":map_page.get("BANK_NO")+"");
				cell.setCellStyle(style);
				
				//创建第11列(是否发货)
				cell = row.createCell((short)10);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("OGT_BANK_NO")==null?"":map_page.get("OGT_BANK_NO")+"");
				cell.setCellStyle(style);
				
				//创建第12列(卡号)
				cell = row.createCell((short)11);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("INTERNATIONL_BANK_NO")==null?"":map_page.get("INTERNATIONL_BANK_NO")+"");
				cell.setCellStyle(style);
				
				//创建第13列(卡密码)
				cell = row.createCell((short)12);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("BANK_NAME")==null?"":map_page.get("BANK_NAME")+"");
				cell.setCellStyle(style);
				
				//创建第14列(商品原价)
				cell = row.createCell((short)13);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("BANK_DAIMA")==null?"":map_page.get("BANK_DAIMA")+"");
				cell.setCellStyle(style);
				
				//创建第15列(商品售价)
				cell = row.createCell((short)14);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("");
				cell.setCellStyle(style);
				
				//创建第16列()
				cell = row.createCell((short)15);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("ADDRESS")==null?"":map_page.get("ADDRESS")+"");
				cell.setCellStyle(style);
				
				//创建第17列()
				cell = row.createCell((short)16);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("CITY")==null?"":map_page.get("CITY")+"");
				cell.setCellStyle(style);
				
				//创建第18列()
				cell = row.createCell((short)17);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("AREA")==null?"":map_page.get("AREA")+"");
				cell.setCellStyle(style);
				
				//创建第19列()
				cell = row.createCell((short)18);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("COUNTRYCODE")==null?"":map_page.get("COUNTRYCODE")+"");
				cell.setCellStyle(style);
				
				//创建第20列()
				cell = row.createCell((short)19);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(map_page.get("POST_NO")==null?"":map_page.get("POST_NO")+"");
				cell.setCellStyle(style);
				//游标自加
				row_num++;
				
			}
			
			//返回值赋值
			map_res.put("content", hwb);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
			map_res.put("file_name", "yuban-abroadTixian-" + sdf.format(new Date()) + ".xls");
		}
		return map_res;
	}

}
