package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MemberDAO;
import model.MemberDTO;
import model.ReplyDTO;
import com.oreilly.servlet.*;
//import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

/**
 * Servlet implementation class MemberServlet
 */
@WebServlet({ "/Member/*" })
public class MemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MemberServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURL().toString();
		String viewPage = null;

		MemberDAO dao = new MemberDAO();
		PrintWriter out = response.getWriter();

		if (url.indexOf("list.do") != -1) {
			int pageStart;
			String option = request.getParameter("option");
			String searchText = request.getParameter("searchText");

			String pageStart1 = request.getParameter("pageStart");

			if (pageStart1 == null) {
				pageStart = 0;
			} else {
				pageStart = Integer.parseInt(request.getParameter("pageStart"));
			}

			int countFor = dao.getCountFor(option, searchText);

			List<MemberDTO> list = dao.MemberList(option, searchText, pageStart);
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("list", list);
			map.put("count", Integer.valueOf(list.size()));
			request.setAttribute("map", map);

			viewPage = "/view/list.jsp";
			RequestDispatcher goUrl = request.getRequestDispatcher(viewPage);
			goUrl.forward(request, response);
		} else if (url.indexOf("content.do") != -1) {
			int no = Integer.parseInt(request.getParameter("no"));
			request.setAttribute("no", Integer.valueOf(no));

			List<ReplyDTO> list2 = dao.ReplyList(no);
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("list2", list2);
			map.put("count", Integer.valueOf(list2.size()));
			System.out.println(map);
			request.setAttribute("map", map);

			viewPage = "/view/content.jsp";
			RequestDispatcher goUrl = request.getRequestDispatcher(viewPage);
			goUrl.forward(request, response);

			int noForCount = Integer.parseInt(request.getParameter("no"));
			int view = Integer.parseInt(request.getParameter("view"));

			dao.setCountUpdate(noForCount, view);

		} else if (url.indexOf("write.do") != -1) {

			viewPage = "/view/write.jsp";
			RequestDispatcher goUrl = request.getRequestDispatcher(viewPage);
			goUrl.forward(request, response);
		} else if (url.indexOf("insert") != -1) {

			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=utf-8");
			
/**************************파일 업로드*****************************/
			
			String savePath = "upload";
			int uploadFileSizeLimit = 5 * 1024 * 1024;
			String encType = "UTF-8";
			
			ServletContext context = getServletContext();
			String uploadFilePath = context.getRealPath(savePath);
			System.out.println("서버상의 실제 디렉토리");
			System.out.println(uploadFilePath);
			
			MultipartRequest multi = new MultipartRequest(request, uploadFilePath, uploadFileSizeLimit, encType, new DefaultFileRenamePolicy());
			String fileName = multi.getFilesystemName("file");
			
/*************************************************************************/
			String writer = multi.getParameter("inputWriter");
			if (writer.equals("")) {
				writer = "Anonymous";
			}

			String title = multi.getParameter("inputTitle");
			String contents = multi.getParameter("inputContents");
			Date date = new Date();
			SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
			String strdate = simpleDate.format(date);

			Calendar cal = Calendar.getInstance();

			int mondifyedMonth = cal.get(2) + 1;

			String inputDate = String.valueOf(cal.get(1)) + "-" + mondifyedMonth + "-" + cal.get(5);

			int view = 1;

			boolean result = false;
			result = dao.setInsert(writer, title, contents, view, inputDate, fileName);

			if (result) {
				out.println("<script>alert('등록 완료')</script>");
				viewPage = "list.do";
			} else {
				out.println("<script>alert('등록 실패')</script>");
				viewPage = "list.do";
			}

			out.println("<script>location.href='list.do'</script>");

		} else if (url.indexOf("delete.do") != -1) {

			int no = Integer.parseInt(request.getParameter("no"));
			System.out.println(no);
			boolean result = false;
			result = dao.setDelete(no);

			if (result) {
				out.println("<script>alert('삭제 성공')</script>");
				viewPage = "list.do";
			} else {
				out.println("<script>alert('삭제 실패')</script>");
				viewPage = "list.do";
			}

			out.println("<script>location.href='list.do'</script>");

		} else if (url.indexOf("update.do") != -1) {

			viewPage = "/view/update.jsp";
			RequestDispatcher goUrl = request.getRequestDispatcher(viewPage);
			goUrl.forward(request, response);

		} else if (url.indexOf("updateComplite.do") != -1) {

			int no = Integer.parseInt(request.getParameter("no"));
			String title = request.getParameter("changeTitle");
			String writer = request.getParameter("changeWriter");
			String contents = request.getParameter("changeContents");
			System.out.println(no);
			boolean result = false;
			result = dao.setUpdate(no, title, writer, contents);

			if (result) {
				out.println("<script>alert('수정 성공')</script>");
				viewPage = "list.do";
			} else {
				out.println("<script>alert('수정 실패')</script>");
				viewPage = "list.do";
			}

			out.println("<script>location.href='list.do'</script>");
			
		} else if (url.indexOf("download.do") != -1) {

			String file1 = request.getParameter("file");
			
			request.setCharacterEncoding("UTF-8");  
		    // 파일 업로드된 경로
		    String root = request.getSession().getServletContext().getRealPath("/");
		    String savePath = root + "upload";
		    // 서버에 실제 저장된 파일명
		    String filename = file1;    
		    // 실제 내보낼 파일명
		    String orgfilename = file1;      
		 
		    InputStream in = null;
		    OutputStream os = null;
		    File file = null;
		    boolean skip = false;
		    String client = ""; 
		 
		    try{ 
		        // 파일을 읽어 스트림에 담기
		        try{
		            file = new File(savePath, filename);
		            in = new FileInputStream(file);
		        }catch(FileNotFoundException fe){
		            skip = true;
		        }
		         
		        client = request.getHeader("User-Agent");
		        // 파일 다운로드 헤더 지정
		        response.reset() ;
		        response.setContentType("application/octet-stream");
		        response.setHeader("Content-Description", "JSP Generated Data"); 
		 
		        if(!skip){
		            // IE
		            if(client.indexOf("MSIE") != -1){
		                response.setHeader ("Content-Disposition", "attachment; filename="+new String(orgfilename.getBytes("KSC5601"),"ISO8859_1"));
		            }else{
		                // 한글 파일명 처리
		                orgfilename = new String(orgfilename.getBytes("utf-8"),"iso-8859-1");
		 
		                response.setHeader("Content-Disposition", "attachment; filename=\"" + orgfilename + "\"");
		                response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
		            } 
		             
		            response.setHeader ("Content-Length", ""+file.length() );
		       
		            os = response.getOutputStream();
		            byte b[] = new byte[(int)file.length()];
		            int leng = 0;
		             
		            while( (leng = in.read(b)) > 0 ){
		                os.write(b,0,leng);
		            }
		        }else{
		            out.println("<script>alert('파일을 찾을 수 없습니다');history.back();</script>");
		        }
		 
		        in.close();
		        os.close();
		 
		    }catch(Exception e){
		      e.printStackTrace();
		    }

		} else if (url.indexOf("replyInsert.do") != -1) {

			String replyWriter = request.getParameter("replyWriter");
			String replyContent = request.getParameter("replyContent");
			int no = Integer.parseInt(request.getParameter("no"));
			int password = Integer.parseInt(request.getParameter("password"));
			int passwordCheck = Integer.parseInt(request.getParameter("passwordCheck"));

			if (password == passwordCheck) {
				boolean result = dao.setReplyInsert(replyWriter, replyContent, no, password);

				if (result) {
					out.println("<script>alert('등록 성공')</script>");
					response.sendRedirect("content.do?no=" + no);
				} else {
					out.println("<script>alert('등록 실패')</script>");
					response.sendRedirect("content.do?no=" + no);
				}
			} else {
				out.println("<script>alert('비밀번호가 일치하지 않습니다.')</script>");
				out.println("<script>history.go(-1)</script>");
			}

		} else if (url.indexOf("replyUpdate.do") != -1) {

			int inputPassword, no = Integer.parseInt(request.getParameter("no"));
			int upNo = Integer.parseInt(request.getParameter("upNo"));
			String changedReplyWriter = request.getParameter("changedReplyWriter");
			String changedReplyContent = request.getParameter("changedReplyContent");

			int bringPassword = dao.getPassword(no);
			String inputPassword1 = request.getParameter("inputPassword");

			if (inputPassword1.equals("")) {
				inputPassword = 0;
			} else {
				inputPassword = Integer.parseInt(request.getParameter("inputPassword"));
			}
			if (inputPassword == 0) {
				out.println("<script>alert('비밀번호를 입력하세요!')</script>");
				out.println("<script>history.go(-1)</script>");
			} else if (bringPassword == inputPassword) {
				boolean result = dao.setUpdateReply(no, changedReplyWriter, changedReplyContent);
				if (result) {
					out.println("<script>alert('수정 성공')</script>");
					response.sendRedirect("content.do?no=" + upNo);
				} else {
					out.println("<script>alert('수정 실패')</script>");
					response.sendRedirect("content.do?no=" + upNo);
				}
			} else {
				out.println("<script>alert('비밀번호가 일치하지 않습니다.!')</script>");
				out.println("<script>history.go(-1)</script>");
			}

		} else if (url.indexOf("replyDelete.do") != -1) {

			int inputPassword, no = Integer.parseInt(request.getParameter("no"));
			int upNo = Integer.parseInt(request.getParameter("upNo"));
			int bringPassword = dao.getPassword(no);
			String inputPassword1 = request.getParameter("inputPassword");

			if (inputPassword1.equals("")) {
				inputPassword = 0;
			} else {
				inputPassword = Integer.parseInt(request.getParameter("inputPassword"));
			}
			if (inputPassword != bringPassword) {
				out.println("<script>alert('비밀번호가 일치하지 않습니다.!')</script>");
				out.println("<script>history.go(-1)</script>");
			} else {
				boolean result = dao.setReplyDelete(no);
				if (result) {
					response.sendRedirect("content.do?no=" + upNo);
				} else {
					response.sendRedirect("content.do?no=" + upNo);
				}
			}
		} else if (url.indexOf("answerWriteFront.do") != -1) {

			viewPage = "/view/answerWriteFront.jsp";
			RequestDispatcher goUrl = request.getRequestDispatcher(viewPage);
			goUrl.forward(request, response);

		} else if (url.indexOf("answerWriteBack") != -1) {

			int motherNo = Integer.parseInt(request.getParameter("motherNo"));
			String answerWriter = request.getParameter("answerWriter");
			String answerTitle = request.getParameter("answerTitle");
			String answerContent = request.getParameter("answerContents");

			Date date = new Date();
			SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
			String strdate = simpleDate.format(date);

			Calendar cal = Calendar.getInstance();

			int mondifyedMonth = cal.get(2) + 1;

			String inputDate = String.valueOf(cal.get(1)) + "-" + mondifyedMonth + "-" + cal.get(5);

			boolean result = dao.setAnswerInsert(answerWriter, answerTitle, answerContent, motherNo, inputDate);
			
			if (result) {
				out.println("<script>alert('등록 성공')</script>");
				viewPage = "list.do";
			} else {
				out.println("<script>alert('등록 실패')</script>");
				viewPage = "list.do";
			}
			out.println("<script>location.href='list.do'</script>");

		} else {

			System.out.println("Servlet else");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
