package com.utl;

public class ServletPath {
	public static final String SUCCESS = "1";
	public static final String FAILURE = "0";
	public static final String MANAGERGETOWNER = "http://" + Url.url + "/"
			+ "HouseService02/OwnerServlet?operate=getOwnerByHouseId";
	public static final String MANAGERGETNOTICE = "http://" + Url.url
			+ "/HouseService02/ManagerServlet?operate=getAllNotices";;
	public static final String MANAGERSENDNOTICE = "http://" + Url.url
			+ "/HouseService02/ManagerServlet?operate=sendNotice";
	public static final String GETALLHOUSE = "http://" + Url.url
			+ "/HouseService02/ManagerServlet?operate=getHousesInfo";
	public static final String MANAGERADDHOUSE = "http://" + Url.url
			+ "/HouseService02/AccountServlet";
	public static final String MANAGERADDSELLER = "http://" + Url.url
			+ "/HouseService02/AccountServlet?operate=buildSellerAccount";
	public static final String MANAGERGETSELLER = "http://" + Url.url
			+ "/HouseService02/ManagerServlet?operate=getSellers";
	public static final String MANGERIFNOUPLOAD = "http://" + Url.url
			+ "/HouseService02/AccountServlet?operate=updateManagerInfo";
	public static final String MANAGERINFOTOGET = "http://" + Url.url
			+ "/HouseService02/AccountServlet?operate=getManagerInfo";

	public static final String MANAGERUPLOADOWNERANDHOUSE = "http://" + Url.url
			+ "/HouseService02/AccountServlet?operate=uploadOwnerAndHouseInfo";
	public static final String MANAGERPICTUREFORMUPLOAD = "http://" + Url.url
			+ "/HouseService02/AccountServlet" + "?operate=updateManagerInfo";
	public static final String MANGERDELETENOTICEBYID = "http://" + Url.url
			+ "/HouseService02/ManagerServlet" + "?operate=deleteNoticeById";
	public static final String MANAGERUPDATEINFO = "http://" + Url.url
			+ "/HouseService02/AccountServlet?operate=updateManagerInfo";
	public static final String SELLERMODIFYINFO = "http://" + Url.url
			+ "/HouseService02/AccountServlet" + "?operate=updateSellerInfo";
	public static final String SELLERGETINFO = "http://" + Url.url
			+ "/HouseService02/AccountServlet" + "?operate=getSellerInfo";
	public static final String SELLERGETSUCCESSBARGAIN = "http://" + Url.url
			+ "/HouseService02/SellerServlet" + "?operate=getDealProject";
	public static final String GETHOUSEINFOBYID = "http://" + Url.url
			+ "/HouseService02/AccountServlet" + "?operate=getHouseByHouseId";
	public static final String GETOWNERINFOBYID = "http://" + Url.url
			+ "/HouseService02/AccountServlet"
			+ "?operate=getOwnerInfoByHouseId";

	public static final String SELLERINSERTBUYER = "http://" + Url.url
			+ "/HouseService02/SellerServlet?operate=UploadBuyerInfo";

	public static final String SELLERGETALLBUYER = "http://"
			+ Url.url
			+ "/HouseService02/SellerServlet?operate=getAllBuyer&selleraccount=";

	public static final String SELLERGETBUYERINFO = "http://" + Url.url
			+ "/HouseService02/SellerServlet?operate=getBuyerRecord&buyerId=";

	public static final String SELLERDELETEBUYER = "http://" + Url.url
			+ "/HouseService02/SellerServlet?operate=deleteBuyer&buyerId=";
	
	public static final String SELLERUPDATEBUYERHOUSEID="Http://"+Url.url+"/HouseService02/SellerServlet?operate=updateBuyerHouseInfo";
	
	
	public static final String SELLERDEALHOUSE="http://"+Url.url+"/HouseService02/SellerServlet?operate=dealHouseSuccess";

}
