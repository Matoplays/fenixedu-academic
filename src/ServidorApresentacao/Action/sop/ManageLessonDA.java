package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import DataBeans.comparators.RoomAlphabeticComparator;
import ServidorApresentacao.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.DiaSemana;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ManageLessonDA
	extends FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

	public static String INVALID_TIME_INTERVAL =
		"errors.lesson.invalid.time.interval";
	public static String UNKNOWN_ERROR = "errors.unknown";

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		return mapping.findForward("ShowLessonForm");
	}

	public ActionForward chooseRoom(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaActionForm manageLessonForm = (DynaActionForm) form;
		request.setAttribute("manageLessonForm", manageLessonForm);

		DiaSemana weekDay =
			new DiaSemana(
				new Integer(
					formDay2EnumerateDay(
						(String) manageLessonForm.get("diaSemana"))));

		Calendar inicio = Calendar.getInstance();
		inicio.set(
			Calendar.HOUR_OF_DAY,
			Integer.parseInt((String) manageLessonForm.get("horaInicio")));
		inicio.set(
			Calendar.MINUTE,
			Integer.parseInt((String) manageLessonForm.get("minutosInicio")));
		inicio.set(Calendar.SECOND, 0);
		Calendar fim = Calendar.getInstance();
		fim.set(
			Calendar.HOUR_OF_DAY,
			Integer.parseInt((String) manageLessonForm.get("horaFim")));
		fim.set(
			Calendar.MINUTE,
			Integer.parseInt((String) manageLessonForm.get("minutosFim")));
		fim.set(Calendar.SECOND, 0);

		InfoRoom infoSala = new InfoRoom();
		infoSala.setNome((String) manageLessonForm.get("nomeSala"));

		ActionErrors actionErrors = checkTimeInterval(inicio, fim);

		if (actionErrors.isEmpty()) {
			InfoRoom infoRoom = new InfoRoom();
			infoRoom.setCapacidadeNormal(new Integer(0));
			infoRoom.setCapacidadeExame(new Integer(0));

			InfoLesson infoLesson = new InfoLesson();
			infoLesson.setDiaSemana(weekDay);
			infoLesson.setInicio(inicio);
			infoLesson.setFim(fim);

			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) (request
					.getAttribute(SessionConstants.EXECUTION_PERIOD));

			Object args[] = { infoRoom, infoLesson, infoExecutionPeriod };

			List emptyRoomsList =
				(List) ServiceUtils.executeService(
					SessionUtils.getUserView(request),
					"ReadEmptyRoomsService",
					args);

			if (emptyRoomsList == null || emptyRoomsList.isEmpty()) {
				actionErrors.add(
					"search.empty.rooms.no.rooms",
					new ActionError("search.empty.rooms.no.rooms"));
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}
			Collections.sort(emptyRoomsList, new RoomAlphabeticComparator());
			List listaSalas = new ArrayList();
			listaSalas.add(
				new LabelValueBean(infoSala.getNome(), infoSala.getNome()));
			for (int i = 0; i < emptyRoomsList.size(); i++) {
				InfoRoom elem = (InfoRoom) emptyRoomsList.get(i);
				listaSalas.add(
					new LabelValueBean(elem.getNome(), elem.getNome()));
			}
			//emptyRoomsList.add(0, infoSala);

			//sessao.removeAttribute("listaSalas");
			//sessao.removeAttribute("listaInfoSalas");
			request.setAttribute("listaSalas", listaSalas);
			//request.setAttribute("listaInfoSalas", emptyRoomsList);
			request.setAttribute(SessionConstants.LESSON, infoLesson);

			return mapping.findForward("ShowChooseRoomForm");
		} else {
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		}

	}

	public ActionForward createLesson(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaActionForm manageLessonForm = (DynaActionForm) form;
		request.setAttribute("manageLessonForm", manageLessonForm);

		DiaSemana weekDay =
			new DiaSemana(
				new Integer(
					formDay2EnumerateDay(
						(String) manageLessonForm.get("diaSemana"))));

		Calendar inicio = Calendar.getInstance();
		inicio.set(
			Calendar.HOUR_OF_DAY,
			Integer.parseInt((String) manageLessonForm.get("horaInicio")));
		inicio.set(
			Calendar.MINUTE,
			Integer.parseInt((String) manageLessonForm.get("minutosInicio")));
		inicio.set(Calendar.SECOND, 0);
		Calendar fim = Calendar.getInstance();
		fim.set(
			Calendar.HOUR_OF_DAY,
			Integer.parseInt((String) manageLessonForm.get("horaFim")));
		fim.set(
			Calendar.MINUTE,
			Integer.parseInt((String) manageLessonForm.get("minutosFim")));
		fim.set(Calendar.SECOND, 0);

		InfoRoom infoSala = new InfoRoom();
		infoSala.setNome((String) manageLessonForm.get("nomeSala"));

		ActionErrors actionErrors = checkTimeInterval(inicio, fim);

		if (actionErrors.isEmpty()) {
			InfoRoom infoRoom = new InfoRoom();
			infoRoom.setCapacidadeNormal(new Integer(0));
			infoRoom.setCapacidadeExame(new Integer(0));

			InfoLesson infoLesson = new InfoLesson();
			infoLesson.setDiaSemana(weekDay);
			infoLesson.setInicio(inicio);
			infoLesson.setFim(fim);

			InfoShift infoShift =
				(InfoShift) (request
					.getAttribute(SessionConstants.SHIFT));

			infoLesson.setInfoDisciplinaExecucao(infoShift.getInfoDisciplinaExecucao());
			infoLesson.setTipo(infoShift.getTipo());
			infoLesson.setInfoSala(infoSala);

			Object args[] = { infoLesson, infoShift };

			ServiceUtils.executeService(
					SessionUtils.getUserView(request),
					"CreateLesson",
					args);

			return mapping.findForward("EditShift");
		} else {
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		}
	}

	private String formDay2EnumerateDay(String string) {
		String result = string;
		if (string.equalsIgnoreCase("2")) {
			result = "2";
		}
		if (string.equalsIgnoreCase("3")) {
			result = "3";
		}
		if (string.equalsIgnoreCase("4")) {
			result = "4";
		}
		if (string.equalsIgnoreCase("5")) {
			result = "5";
		}
		if (string.equalsIgnoreCase("6")) {
			result = "6";
		}
		if (string.equalsIgnoreCase("S")) {
			result = "7";
		}
		if (string.equalsIgnoreCase("D")) {
			result = "1";
		}
		return result;
	}

	private ActionErrors checkTimeInterval(Calendar begining, Calendar end) {
		ActionErrors actionErrors = new ActionErrors();
		String beginMinAppend = "";
		String endMinAppend = "";

		if (begining.get(Calendar.MINUTE) == 0)
			beginMinAppend = "0";
		if (end.get(Calendar.MINUTE) == 0)
			endMinAppend = "0";

		if (begining.getTime().getTime() >= end.getTime().getTime()) {
			actionErrors.add(
				INVALID_TIME_INTERVAL,
				new ActionError(
					INVALID_TIME_INTERVAL,
					""
						+ begining.get(Calendar.HOUR_OF_DAY)
						+ ":"
						+ begining.get(Calendar.MINUTE)
						+ beginMinAppend
						+ " - "
						+ end.get(Calendar.HOUR_OF_DAY)
						+ ":"
						+ end.get(Calendar.MINUTE)
						+ endMinAppend));
		}
		return actionErrors;
	}

}