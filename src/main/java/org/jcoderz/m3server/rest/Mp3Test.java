package org.jcoderz.m3server.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jcoderz.mp3.intern.util.Environment;

/**
 * Servlet implementation class Mp3Test
 */
public class Mp3Test extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public Mp3Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String path = (String) request.getParameter("path");

		File audio = Environment.getAudioFolder();
		File file = new File(audio, path);
		System.err.println("SERVLET: audio=" + audio + ", path=" + path
				+ ", file=" + file);
		if (file.exists()) {
			response.setContentType("audio/mpeg3");
			System.err.println("SERVLET: file=" + file);
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ file.getName() + "\"");
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				OutputStream out = response.getOutputStream();

				byte[] loader = new byte[8192];
				while ((fis.read(loader)) > 0) {
					out.write(loader);
				}
			} finally {
				if (fis != null) {
					fis.close();
				}
			}
			response.flushBuffer();
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setContentType("text/html");
			System.err.println("SERVLET: HTML response");
			PrintWriter pw = response.getWriter();
			pw.println("<html><body>File not found: " + file + "</body></html>");
			pw.flush();
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
