package ServidorPersistente;

import java.util.Date;
import java.util.List;

import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;

/**
 * @author �ngela
 */

public interface IPersistentEnrolmentEvaluation extends IPersistentObject {
	public void deleteAll() throws ExcepcaoPersistencia;
	public void lockWrite(IEnrolmentEvaluation enrolmentEvaluationToWrite) throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(IEnrolmentEvaluation enrolmentEvaluation) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;	
	public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(IEnrolment enrolment,EnrolmentEvaluationType evaluationType,String grade) throws ExcepcaoPersistencia;
	public List readEnrolmentEvaluationByEnrolmentEvaluationState(IEnrolment enrolment,EnrolmentEvaluationState evaluationState) throws ExcepcaoPersistencia; 
	public List readEnrolmentEvaluationByEnrolment(IEnrolment enrolment) throws ExcepcaoPersistencia; 

	public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(IEnrolment enrolment,EnrolmentEvaluationType evaluationType,String grade,Date whenAltered) throws ExcepcaoPersistencia;
}