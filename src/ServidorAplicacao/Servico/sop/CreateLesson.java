/*
 * CreateLesson.java
 *
 * Created on 2003/08/12
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o CreateLesson.
 *
 * @author Luis Cruz & Sara Ribeiro
 **/

import java.util.List;

import DataBeans.InfoLesson;
import DataBeans.InfoLessonServiceResult;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.Aula;
import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InterceptingServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidTimeIntervalServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class CreateLesson implements IServico {

	private static CreateLesson _servico = new CreateLesson();
	/**
	 * The singleton access method of this class.
	 **/
	public static CreateLesson getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CreateLesson() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "CreateLesson";
	}

	public InfoLessonServiceResult run(
		InfoLesson infoLesson,
		InfoShift infoShift)
		throws FenixServiceException {

		InfoLessonServiceResult result = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ISala sala =
				sp.getISalaPersistente().readByName(
					infoLesson.getInfoSala().getNome());

			IDisciplinaExecucaoPersistente executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoLesson
						.getInfoDisciplinaExecucao()
						.getInfoExecutionPeriod());

			IDisciplinaExecucao executionCourse =
				executionCourseDAO
					.readByExecutionCourseInitialsAndExecutionPeriod(
					infoLesson.getInfoDisciplinaExecucao().getSigla(),
					executionPeriod);

			IAula aula =
				new Aula(
					infoLesson.getDiaSemana(),
					infoLesson.getInicio(),
					infoLesson.getFim(),
					infoLesson.getTipo(),
					sala,
					executionCourse);

			result = validTimeInterval(aula);
			if (result.getMessageType() == 1) {
				throw new InvalidTimeIntervalServiceException();
			}

			boolean resultB = validNoInterceptingLesson(aula, executionPeriod);

			if (result.isSUCESS() && resultB) {
				try {
					sp.getIAulaPersistente().lockWrite(aula);

					ITurno shift =
						(ITurno) sp.getITurnoPersistente().readByOID(
							Turno.class,
							infoShift.getIdInternal());
					sp.getITurnoPersistente().lockWrite(shift);
					shift.getAssociatedLessons().add(aula);

				} catch (ExistingPersistentException ex) {
					throw new ExistingServiceException(ex);
				}
			} else {
				result.setMessageType(2);
			}

		} catch (ExcepcaoPersistencia ex) {

			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}

	/**
	 * @param aula
	 * @return InfoLessonServiceResult
	 */
	private boolean validNoInterceptingLesson(
		IAula lesson,
		IExecutionPeriod executionPeriod)
		throws ExistingServiceException, InterceptingServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IAulaPersistente persistentLesson = sp.getIAulaPersistente();

			List lessonMatchList =
				persistentLesson.readLessonsInBroadPeriod(
					lesson,
					null,
					executionPeriod);

			if (lessonMatchList.size() > 0) {
				if (lessonMatchList.contains(lesson)) {
					throw new ExistingServiceException();
				} else {
					throw new InterceptingServiceException();
				}
			} else {
				return true;
			}
		} catch (ExcepcaoPersistencia e) {
			return false;
		}
	}

	private InfoLessonServiceResult validTimeInterval(IAula lesson) {
		InfoLessonServiceResult result = new InfoLessonServiceResult();

		if (lesson.getInicio().getTime().getTime()
			>= lesson.getFim().getTime().getTime()) {
			result.setMessageType(
				InfoLessonServiceResult.INVALID_TIME_INTERVAL);
		}

		return result;
	}

}