package config;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter({ "/*" })
public class EncodingFiter implements Filter {
	private final String charset = "utf-8";

	public EncodingFiter() {
		System.out.println("Fiter Created!");
	}

	public void destroy() {
		System.out.println("System Down!");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("Servlet Auto Start Successfully");

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("utf-8");

		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("system Start");
		Connection conn = Database.getConnection();
		if (conn != null) {
			System.out.println(conn + " : DB Connention Success!");
		} else {
			System.out.println("DB Connention Fail!!");
		}
	}
}
