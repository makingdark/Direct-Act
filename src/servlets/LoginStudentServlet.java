package servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.VraagController;
import controller.StudentController;
import controller.TijdController;
import domein.Student;
import domein.Vraag;

public class LoginStudentServlet extends HttpServlet {

	private static final long serialVersionUID = 194503685680L;
	private StudentController controller = new StudentController();
	private VraagController vc = new VraagController();
	private TijdController tijd = new TijdController();

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Student s = new Student();
		Vraag v = vc.eersteVraag();
		if (v.getAfbeelding() == null) {
			String blob = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAYdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAuNWWFMmUAAAANSURBVBhXY/j//z8DAAj8Av6IXwbgAAAAAElFTkSuQmCC";
			v.setAfbeelding(blob);
		}
		RequestDispatcher rd = null;
		int code = Integer.parseInt(req.getParameter("code"));
		boolean done = true;
		if (req.getParameter("button").equals("login")) {
			if (controller.checkCode(code, done)) {
				s = controller.setStudent(s, code, done);
				req.getSession().setAttribute("student", s);
				req.getSession().setAttribute("vraag", v.getVraagstelling());
				req.getSession().setAttribute("vraagnummer", v.getNummer());
				req.getSession().setAttribute("context", v.getContext());
				req.getSession().setAttribute("type", v.getType());
				req.getSession().setAttribute("plaatje", v.getAfbeelding());
				req.getSession().setAttribute("rekenmachine",v.isRekenmachine());
				req.getSession().setAttribute("uren",tijd.getUur(System.currentTimeMillis()));
				req.getSession().setAttribute("minuten",tijd.getMinuut(System.currentTimeMillis()));
				req.getSession().setAttribute("seconden",tijd.getSeconde(System.currentTimeMillis()));
				req.getSession().setAttribute("toetsnummer",vc.getVolgendToetsNummer(true, s.getCode()));
				if (s.isFirstTime()) {
					rd = req.getRequestDispatcher("/enquete.jsp");
				} else {
					rd = req.getRequestDispatcher("/toets-vraag.jsp");
				}
			} else {
				req.setAttribute("msgs", "code bestaat niet");
				rd = req.getRequestDispatcher("/login-student.jsp");
			}
		}
		rd.forward(req, resp);

	}
}
