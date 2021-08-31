package model;

import config.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.MemberDTO;
import model.ReplyDTO;

public class MemberDAO {

	
	Connection conn;
	PreparedStatement pstmt;
	PreparedStatement pstmt2;
	PreparedStatement pstmt3;
	PreparedStatement pstmt4;
	PreparedStatement pstmt5;
	ResultSet rs;
	ResultSet rs2;
	ResultSet rs3;
	ResultSet rs4;
	ResultSet rs5;

	public List<MemberDTO> MemberList(String option, String searchText, int pageStart) {
		List<MemberDTO> list = new ArrayList<MemberDTO>();

		conn = null;
		pstmt = null;
		rs = null;
		String query = "";
		String query2 = "update user set listNum = ?";
		
		int data = 0;
		if (searchText == null) {
			query = "select * from user order by listNum desc, depth asc limit ?, 7";
			data = 0;
		} else if (option.equals("title")) {
			query = "select * from user where title like ? order by no desc limit ?, 7";
			data = 1;
		} else if (option.equals("writer")) {
			query = "select * from user where writer like ? order by no desc limit ?, 7";
			data = 2;
		}

		try {
			conn = Database.getConnection();
			if (data == 0) {
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, pageStart);
			} else {
				pstmt = conn.prepareStatement(query);

				pstmt.setString(1, "%" + searchText + "%");
				pstmt.setInt(2, pageStart);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int no = rs.getInt("no");
				String writer = rs.getString("writer");
				String title = rs.getString("title");
				String contents = rs.getString("contents");
				String date = rs.getString("date");
				int view = rs.getInt("view");
				int listNum = rs.getInt("listNum");
				int depth = rs.getInt("depth");

				MemberDTO dto = new MemberDTO();

				dto.setNo(no);
				dto.setWriter(writer);
				dto.setTitle(title);
				dto.setContents(contents);
				dto.setDate(date);
				dto.setView(view);
				dto.setListNum(listNum);
				dto.setDepth(depth);

				list.add(dto);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public MemberDTO getMember(int no) {
		conn = null;
		pstmt = null;
		rs = null;

		String query = "select * from user where no = ?";

		try {
			conn = Database.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			rs.next();

			MemberDTO member = new MemberDTO();
			member.setWriter(rs.getString("writer"));
			member.setTitle(rs.getString("title"));
			member.setContents(rs.getString("contents"));
			member.setDate(rs.getString("date"));
			member.setFile(rs.getString("file"));
			member.setView(rs.getInt("view"));

			return member;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public boolean setInsert(String writer, String title, String contents, int view, String inputDate) {
		conn = null;
		pstmt = null;
		rs = null;
		boolean result = false;

		String query = "insert into user values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			conn = Database.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, null);
			pstmt.setString(2, writer);
			pstmt.setString(3, title);
			pstmt.setString(4, contents);
			pstmt.setString(5, inputDate);
			pstmt.setInt(6, view);
			pstmt.setString(7, null);
			pstmt.setInt(8, 0);     /*해당 글의 no를 listNum으로 넣기*/
			pstmt.setInt(9, 0);
			pstmt.executeUpdate();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public boolean setDelete(int no) {
		conn = null;
		pstmt = null;
		rs = null;
		boolean result = false;

		String query = "delete from user where no = ?";

		try {
			conn = Database.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			pstmt.executeUpdate();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public boolean setUpdate(int no, String writer, String title, String contents) {
		conn = null;
		pstmt = null;
		rs = null;
		boolean result = false;

		String query = "update user set title=?, writer=?, contents=? where no=?";

		try {
			conn = Database.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, writer);
			pstmt.setString(2, title);
			pstmt.setString(3, contents);
			pstmt.setInt(4, no);
			pstmt.executeUpdate();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public boolean setCountUpdate(int no, int view) {
		conn = null;
		pstmt = null;
		rs = null;
		boolean result = false;

		String query = "update user set view=? where no=?";

		try {
			conn = Database.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, view);
			pstmt.setInt(2, no);
			pstmt.executeUpdate();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public int getCount() {
		conn = null;
		pstmt = null;
		rs = null;

		int count = 0;
		String sql = "select count(*) from user";
		try {
			conn = Database.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return count;
	}

	public int getCountFor(String option, String searchText) {
		conn = null;
		pstmt = null;
		rs = null;

		String query = "";
		int data = 0;
		if (searchText == null) {
			query = "select count(*) from user";
			data = 0;
		} else if (option.equals("title")) {
			query = "select count(*) from user where title like ?";
			data = 1;
		} else if (option.equals("writer")) {
			query = "select count(*) from user where writer like ?";
			data = 2;
		}

		int count = 0;
		try {
			conn = Database.getConnection();
			if (data == 1) {
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, "%" + searchText + "%");
			} else if (data == 2) {
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, "%" + searchText + "%");
			} else {
				pstmt = conn.prepareStatement(query);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return count;
	}

	public boolean setReplyInsert(String repleyWriter, String repleyContent, int no, int password) {
		conn = null;
		pstmt = null;
		rs = null;
		boolean result = false;

		String query = "insert into reply values(?, ?, ?, ?, ?)";

		try {
			conn = Database.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, null);
			pstmt.setInt(2, no);
			pstmt.setString(3, repleyWriter);
			pstmt.setString(4, repleyContent);
			pstmt.setInt(5, password);
			pstmt.executeUpdate();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public List<ReplyDTO> ReplyList(int no) {
		List<ReplyDTO> list2 = new ArrayList<ReplyDTO>();

		conn = null;
		pstmt = null;
		rs = null;

		String query = "select * from reply where col_num=?";

		try {
			conn = Database.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int no1 = rs.getInt("num");
				int colNo = rs.getInt("col_num");
				String replyWriter = rs.getString("reply_writer");
				String replyContent = rs.getString("reply_content");

				ReplyDTO dto = new ReplyDTO();

				dto.setNo(no1);
				dto.setColNo(colNo);
				dto.setReplyWriter(replyWriter);
				dto.setReplyContent(replyContent);

				list2.add(dto);
			}

			return list2;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public boolean setUpdateReply(int no, String changedRepltWriter, String changedReplyContent) {
		conn = null;
		pstmt = null;
		rs = null;
		boolean result = false;

		String query = "update reply set reply_writer=?, reply_content=? where num=?";

		try {
			conn = Database.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, changedRepltWriter);
			pstmt.setString(2, changedReplyContent);
			pstmt.setInt(3, no);
			pstmt.executeUpdate();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public boolean setReplyDelete(int no) {
		conn = null;
		pstmt = null;
		rs = null;
		boolean result = false;

		String query = "delete from reply where num = ?";

		try {
			conn = Database.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			pstmt.executeUpdate();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public int getPassword(int no) {
		conn = null;
		pstmt = null;
		rs = null;
		int password = 0;

		String query = "select pw from reply where num = ?";
		try {
			conn = Database.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				password = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return password;
	}

	public boolean setAnswerInsert(String answerWriter, String answerTitle, String answerContent, int motherNo, String inputDate) {
		conn = null;
		pstmt = null;
		rs = null;
		rs2 = null;
		boolean result = false;

		String query = "insert into user values(?, ?, ?, ?, ?, ?, ?, ?, ?)";   //insert
		String query2 = "select depth from user where no = ?";                 //depth check
		String query3 = "update user set listNum=? where no=? and depth=?";    //root content listNum update
		String query4 = "select listNum from user where no = ?";
		String query5 = "select listNum from user where depth=? and listNum=?";
		try {
			int motherDepth = 0;
			int motherListNum = 0;
			int motherRootListNum = 0;
			conn = Database.getConnection();
			
			pstmt4 = conn.prepareStatement(query4);
			pstmt4.setInt(1, motherNo);
			rs4 = pstmt4.executeQuery();
			if(rs4.next()) {
				motherListNum = rs4.getInt(1);
			}
			
/*******************************************************************************************/
			pstmt5 = conn.prepareStatement(query5);
			pstmt5.setInt(1, 0);
			pstmt5.setInt(2, motherListNum);
			rs5 = pstmt5.executeQuery();
			if(rs5.next()) {
				motherRootListNum = rs5.getInt(1);
			}

/*******************************************************************************************/
			pstmt3 = conn.prepareStatement(query3);
			pstmt3.setInt(1, motherNo);
			pstmt3.setInt(2, motherNo);
			pstmt3.setInt(3, 0);
			pstmt3.executeUpdate();
/*******************************************************************************************/
			pstmt2 = conn.prepareStatement(query2);
			pstmt2.setInt(1, motherNo);
			rs2 = pstmt2.executeQuery();
			if (rs2.next()) {
				motherDepth = rs2.getInt(1);
			}
/*************************************************************************************************/
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, null);
			pstmt.setString(2, answerWriter);
			pstmt.setString(3, answerTitle);
			pstmt.setString(4, answerContent);
			pstmt.setString(5, inputDate);
			pstmt.setInt(6, 1);     //view
			pstmt.setString(7, null);     //file
			
			if(motherDepth == 0) {    /*첫 답글일때*/
				pstmt.setInt(8, motherNo);
			}else {
				pstmt.setInt(8, motherRootListNum);   /*이중 답글일때*/				
			}
			pstmt.setInt(9, motherDepth + 1); /* 부모글 depth의 +1 */
			pstmt.executeUpdate();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}
