package control;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MemberDAO;
import model.MemberDTO;
import model.ReplyDTO;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

			String writer = request.getParameter("inputWriter");
			if (writer.equals("")) {
				writer = "Anonymous";
			}

			String title = request.getParameter("inputTitle");
			String contents = request.getParameter("inputContents");
			Date date = new Date();
			SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
			String strdate = simpleDate.format(date);

			Calendar cal = Calendar.getInstance();

			int mondifyedMonth = cal.get(2) + 1;

			String inputDate = String.valueOf(cal.get(1)) + "-" + mondifyedMonth + "-" + cal.get(5);

			int view = 1;

			boolean result = false;
			result = dao.setInsert(writer, title, contents, view, inputDate);

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

			System.out.print("다운로드.두 로 옴");
			String filePath = request.getRealPath("/download");
			File file = new File(String.valueOf(filePath) + "/" + request.getParameter("file"));

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
