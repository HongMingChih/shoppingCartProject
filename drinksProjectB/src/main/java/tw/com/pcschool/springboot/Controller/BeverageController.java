package tw.com.pcschool.springboot.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import tw.com.pcschool.springboot.Mapper.BeverageMapper;
import tw.com.pcschool.springboot.Model.Beverage_Goods;
import tw.com.pcschool.springboot.Model.Beverage_Member;
import tw.com.pcschool.springboot.Model.SalesReport;

@RestController
@RequestMapping("beverage")
public class BeverageController {
	@Autowired // new 物件
	BeverageMapper bma;
	
	
	
	@PostMapping("FrontendServlet")
	public ModelAndView FrontendServlet(HttpSession session,HttpServletRequest request) {
		System.out.println("buyGoods...");
		
		ModelAndView modelAndView=null;
		String customerID= request.getParameter("customerID");
		Integer inputMoney=Integer.parseInt(request.getParameter("inputMoney"));
		
		List<Beverage_Member>  l=bma.queryUser1(customerID);
		List<Beverage_Goods>  bgl=bma.queryGroup("1");
		
		
		if (inputMoney<=0 || session.getAttribute("srcList")==null) {
			modelAndView=new ModelAndView("VendingMachine");
			Integer rows=bma.queryRowg();
			modelAndView.addObject("ROWS",rows);
			Beverage_Member[] bm=l.toArray(new Beverage_Member[l.size()]);
			Beverage_Goods[] bg=bgl.toArray(new Beverage_Goods[bgl.size()]);
			modelAndView.addObject("BM",bm[0]);
			modelAndView.addObject("BG",bg);
			 session.removeAttribute("APR") ;
			return modelAndView;
		}
		
		Integer allSessionPrice=(Integer) session.getAttribute("APR");
		if (inputMoney<allSessionPrice) {
			modelAndView=new ModelAndView("VendingMachine");
		
			modelAndView.addObject("messagePrice", "您投入金額不足!請投入");
			modelAndView.addObject("inputMoney",inputMoney);
		}else if (inputMoney>=allSessionPrice) {
			modelAndView=new ModelAndView("VendingMachine");
			List<SalesReport> sessionList=(List<SalesReport>) session.getAttribute("srcList");
			for (SalesReport salesReport : sessionList) {
				Integer gnq=Integer.parseInt(bma.queryGoodsNameQuantity(salesReport.getGoodsName()));
				int Sbq=salesReport.getBuyQuantity();
				if (gnq>= Sbq) {
					int change_amount=inputMoney-allSessionPrice;
					modelAndView.addObject("IPM",inputMoney);
					modelAndView.addObject("APRS", allSessionPrice);
					modelAndView.addObject("CHA",change_amount);
					modelAndView.addObject("srcSList", sessionList);
					session.removeAttribute("srcList") ;
					session.removeAttribute("APR") ;
					session.setAttribute("message", "消費明細!");
				}else {
					modelAndView.addObject("BEYOND","商品數量超出,庫存數量");
					modelAndView.addObject("inputMoney",inputMoney);
				}
			}
		}
		
		Integer rows=bma.queryRowg();
		modelAndView.addObject("ROWS",rows);
		Beverage_Member[] bm=l.toArray(new Beverage_Member[l.size()]);
		Beverage_Goods[] bg=bgl.toArray(new Beverage_Goods[bgl.size()]);
		modelAndView.addObject("BM",bm[0]);
		modelAndView.addObject("BG",bg);
	
		
		
		
		return modelAndView;
	}
	@RequestMapping("buyGoods")
	public ModelAndView buyGoods(HttpSession session,HttpServletRequest request,String customerID) {
		
		List<Beverage_Member>  l=bma.queryUser1(customerID);
		List<Beverage_Goods>  bgl=bma.queryGroup("1");
		
		Integer rows=bma.queryRowg();
	String	goodsName=request.getParameter("goodsName");
	Integer buyQuantity=Integer.parseInt(request.getParameter("buyQuantity"));
	Integer goodsBuyPrice=Integer.parseInt(request.getParameter("PRICE"));
	int buyAmount=buyQuantity*goodsBuyPrice;
	int allPrice=0;
	
//			System.out.println(goodsName);
//			System.out.println(goodsBuyPrice);
//			System.out.println(buyQuantity);
//			System.out.println(buyAmount);
	List<SalesReport> srcList = (List<SalesReport>)session.getAttribute("srcList");
	if (srcList == null) {
	    srcList = new ArrayList<>();
	}
			SalesReport src=new SalesReport(goodsName, goodsBuyPrice, buyQuantity, buyAmount);
			int index = srcList.indexOf(src);
			if(index != -1) {
			    SalesReport existingSrc = srcList.get(index);
			    existingSrc.setBuyQuantity(existingSrc.getBuyQuantity() + buyQuantity);
			    existingSrc.setBuyAmount(existingSrc.getBuyAmount() + buyAmount);
			} else {
			    srcList.add(src);
			}
			
			allPrice = (session.getAttribute("APR") != null) ? (int)session.getAttribute("APR") : 0;
			allPrice += buyAmount;
			
			
		ModelAndView modelAndView=null;
		modelAndView=new ModelAndView("VendingMachine");
		modelAndView.addObject("ROWS",rows);
		Beverage_Member[] bm=l.toArray(new Beverage_Member[l.size()]);
		Beverage_Goods[] bg=bgl.toArray(new Beverage_Goods[bgl.size()]);
		modelAndView.addObject("message", "請選取喜歡的商品!");
		modelAndView.addObject("BM",bm[0]);
		modelAndView.addObject("BG",bg);
		modelAndView.addObject("message", "您的。購物車!");
		modelAndView.addObject("SRC", src);
		modelAndView.addObject("APR", allPrice);
		session.setAttribute("srcList", srcList);
		session.setAttribute("APR", allPrice);
		session.setAttribute("message", "您的。購物車!");
		return modelAndView;
	}
	
	
	
	//商品補貨
	@PostMapping("updateGoods")
	public ModelAndView updateGoods(HttpServletRequest request) throws JsonProcessingException
	{
		System.out.println("updateGoods...");
		Integer GOODS_ID=Integer.parseInt(request.getParameter("goodsID"));
		List<Beverage_Goods>bgl=bma.queryBgId(GOODS_ID);
		for (Beverage_Goods bg : bgl) {
			Integer PRICE=Integer.parseInt(request.getParameter("goodsPrice"));
			Integer QUANTITY=bg.getQUANTITY();
			QUANTITY+=Integer.parseInt(request.getParameter("goodsQuantity"));
			String STATUS=request.getParameter("status");
			bg.setPRICE(PRICE);
			bg.setQUANTITY(QUANTITY);
			bg.setSTATUS(STATUS);
			bma.update(bg);
		}
		List<Beverage_Goods>  bglq=bma.queryAll();
		ModelAndView modelAndView=new ModelAndView("VM_Backend_GoodsReplenishment");
		
		 // 將物件轉換為 JSON 並傳到前端
//	    ObjectMapper objectMapper = new ObjectMapper();
//	    String json = objectMapper.writeValueAsString(bglq);
		Beverage_Goods[] bgg=bglq.toArray(new Beverage_Goods[bglq.size()]);
		modelAndView.addObject("message", "商品維護作業成功！");
	    modelAndView.addObject("BG",bgg);
//	    modelAndView.addObject("BG", json);
		
		return modelAndView;
	}
	
	@GetMapping("VM_Backend_GoodsReplenishment")
	public ModelAndView VM_Backend_GoodsReplenishment()
	{
		
		List<Beverage_Goods>  bgl=bma.queryAll();
		ModelAndView modelAndView=null;
		if (bgl.size()!=0) {
			
			modelAndView=new ModelAndView("VM_Backend_GoodsReplenishment");
			Beverage_Goods[] bg=bgl.toArray(new Beverage_Goods[bgl.size()]);
			
			modelAndView.addObject("BG",bg);
			
			
		}else {
			modelAndView=new ModelAndView("VM_Backend_ERROR");
		}
				
				
		return modelAndView;
	}
	//新增商品
	@PostMapping("createGoods")
	public ModelAndView CreateGoods(HttpServletRequest request,@RequestParam("goodsImage") MultipartFile file)  {
		  // 取得圖片檔案名稱
		System.out.println("addGoods...");
		String GOODS_NAME =request.getParameter("goodsName");
		Integer PRICE =Integer.parseInt(request.getParameter("goodsPrice"));
		Integer QUANTITY =Integer.parseInt(request.getParameter("goodsQuantity"));
		String IMAGE_NAME =file.getOriginalFilename();
		String STATUS =request.getParameter("status");
		
		// 定義圖片保存路徑
		Path path = Paths.get("src/main/resources/static/DrinksImage/" + IMAGE_NAME);
//		// 檢查圖片是否已經存在
		if (Files.exists(path)) {
		    // 圖片已經存在，提示用戶或者修改圖片名稱
		    ModelAndView modelAndView = new ModelAndView();
		    modelAndView.addObject("message", "圖片已存在!");
		    modelAndView.setViewName("VM_Backend_GoodsCreate");
		    return modelAndView;
		}
			try {
				Files.write(path, file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			 ModelAndView modelAndView = new ModelAndView();
			 modelAndView.addObject("message", "商品新增上架成功!");
			 modelAndView.setViewName("VM_Backend_GoodsCreate");
			 bma.createGoods(new Beverage_Goods(GOODS_NAME, PRICE,QUANTITY,IMAGE_NAME,STATUS));
		return modelAndView;
	}
	
	@GetMapping("RegisterAccount")
	public ModelAndView RegisterAccount()
	{
		return new ModelAndView("RegisterAccount");
	}
	
	
	@GetMapping("VM_Backend_GoodsCreate")
	public ModelAndView VM_Backend_GoodsCreate()
	{
		return new ModelAndView("VM_Backend_GoodsCreate");
	}
	
	@GetMapping("queryGoods")
	public ModelAndView queryGoods()
	{
		return new ModelAndView("VM_Backend_GoodsList");
	}
	
	@GetMapping("VendingMachine")
	public ModelAndView VendingMachine()
	{
			
			List<Beverage_Goods>  bgl=bma.queryGroup("1");
			ModelAndView modelAndView=null;
			modelAndView=new ModelAndView("VendingMachine");
			Beverage_Goods[] bg=bgl.toArray(new Beverage_Goods[bgl.size()]);
			
			modelAndView.addObject("BG",bg);
			
		
		return modelAndView;
	}
	//分頁查詢
		@RequestMapping("FrontendPage")
		public ModelAndView FrontendPage(HttpServletRequest request,Integer pageNo,String customerID)
		{
			ModelAndView modelAndView=null;
			List<Beverage_Member>  l=bma.queryUser1(customerID);
			List<Beverage_Goods>  bgl=bma.queryGroup(String.valueOf(pageNo));
			Integer rows=bma.queryRowg();
				Beverage_Member[] bm=l.toArray(new Beverage_Member[l.size()]);
				Beverage_Goods[] bg=bgl.toArray(new Beverage_Goods[bgl.size()]);
				
				modelAndView=new ModelAndView("VendingMachine");
				modelAndView.addObject("ROWS",rows);
				modelAndView.addObject("BG",bg);
				modelAndView.addObject("BM",bm[0]);
			
			return modelAndView;
		}
	
	@GetMapping("queryFrontend")
	public ModelAndView queryFrontend(HttpServletRequest request,HttpSession session,String searchKeyword)
	{
		System.out.println("queryGoods...");
		List<Beverage_Member>  l=bma.queryAllMember();
		List<Beverage_Goods>  bgl=bma.queryFrontend(searchKeyword);
		Integer rows=bma.queryRowg();
		System.out.println(searchKeyword);
		ModelAndView modelAndView=null;
			if (bgl.size()!=0) {
				
			modelAndView=new ModelAndView("VendingMachine");
			Beverage_Goods[] bg=bgl.toArray(new Beverage_Goods[bgl.size()]);
			modelAndView.addObject("BG",bg);
			Beverage_Member[] bm=l.toArray(new Beverage_Member[l.size()]);
			modelAndView.addObject("ROWS",rows);
			modelAndView.addObject("BM",bm[0]);
			session.setAttribute("BM",bm[0]);
		}else {
			modelAndView=new ModelAndView("VendingMachine");
		}
		return modelAndView;
	}
	
	@GetMapping("querySalesReport")
	public ModelAndView querySalesReport(String queryStartDate, String queryEndDate)
	{
		System.out.println("querySalesReport...");
		List<SalesReport>  qobd=bma.queryOrderBetweenDate(queryStartDate+" 00:00:00", queryEndDate+" 23:59:59");
		ModelAndView modelAndView=null;
			modelAndView=new ModelAndView("VM_Backend_GoodsSaleReport");
			SalesReport[] qob=qobd.toArray(new SalesReport[qobd.size()]);
			modelAndView.addObject("QOB",qob);
		return modelAndView;
	}
	
	@GetMapping("VM_Backend_GoodsSaleReport")
	public ModelAndView VM_Backend_GoodsSaleReport()
	{
		List<SalesReport>  qobd=bma.queryOrder();
		ModelAndView modelAndView=null;
			if (qobd.size()!=0) {
			modelAndView=new ModelAndView("VM_Backend_GoodsSaleReport");
			SalesReport[] qob=qobd.toArray(new SalesReport[qobd.size()]);
			modelAndView.addObject("QOB",qob);
		}else {
			modelAndView=new ModelAndView("VM_Backend_ERROR");
		}
		return modelAndView;
	}
	
	//商品查詢總數
	@GetMapping("VM_Backend_GoodsList")
	public ModelAndView VM_Backend_GoodsList()
	{
		System.out.println("queryGoods...");
		List<Beverage_Goods>  bgl=bma.queryAll();
		ModelAndView modelAndView=null;
		if (bgl.size()!=0) {
			
			modelAndView=new ModelAndView("VM_Backend_GoodsList");
			Beverage_Goods[] bg=bgl.toArray(new Beverage_Goods[bgl.size()]);
			
			modelAndView.addObject("BG",bg);
			
			
		}else {
			modelAndView=new ModelAndView("VM_Backend_GoodsList");
		}
				
				
		return modelAndView;
	}
	
	
	@PostMapping("register")
	public ModelAndView register(HttpServletRequest request) {
		String IDENTIFICATION_NO = request.getParameter("IDENTIFICATION_NO");
		
		List<Beverage_Member>  l=bma.queryUser1(IDENTIFICATION_NO);
		ModelAndView modelAndView=null;
		if (l.size()!=0) {
			
			modelAndView=new ModelAndView("VM_Backend_ERROR");
		}else {
			String CUSTOMER_NAME = request.getParameter("CUSTOMER_NAME");
			String PASSWORD = request.getParameter("PASSWORD");
			
			bma.addMember(new Beverage_Member(IDENTIFICATION_NO, PASSWORD, CUSTOMER_NAME));
			modelAndView=new ModelAndView("../static/index.html");
			modelAndView.addObject("IDENTIFICATION_NO", IDENTIFICATION_NO);
			modelAndView.addObject("PASSWORD", PASSWORD);
		}
				
				
		return modelAndView;
		
	}
	
	@GetMapping("logout")
	  public ModelAndView logout(HttpSession session) {
	    // 清除當前用戶的登入狀態和 session
	    session.invalidate();
	    ModelAndView  modelAndView=new ModelAndView("../static/index.html");
//	    return "redirect:/login";
	    
	    return modelAndView;
	  }

	
	//登入
	@PostMapping("loginController")
	public ModelAndView loginController(HttpServletRequest request,HttpSession session, String IDENTIFICATION_NO, String PASSWORD) {
		List<Beverage_Member>  l=bma.queryUser(IDENTIFICATION_NO, PASSWORD);
		List<Beverage_Goods>  bgl=bma.queryGroup("1");
		Integer rows=bma.queryRowg();
//		for (Beverage_Goods beverage_Goods : bgl) {
//			System.out.println(beverage_Goods.getGROUP_ID());
//		}
		ModelAndView modelAndView=null;
		if (l.size()!=0) {
			
			modelAndView=new ModelAndView("VendingMachine");
			Beverage_Member[] bm=l.toArray(new Beverage_Member[l.size()]);
			Beverage_Goods[] bg=bgl.toArray(new Beverage_Goods[bgl.size()]);
			modelAndView.addObject("message", "請選取喜歡的商品!");
			modelAndView.addObject("ROWS",rows);
			modelAndView.addObject("BM",bm[0]);
			modelAndView.addObject("BG",bg);
			session.setAttribute("BM",bm[0]);
			
		}else {
			modelAndView=new ModelAndView("VM_Backend_ERROR");
		}
				
				
		return modelAndView;
		
	}
	
}
