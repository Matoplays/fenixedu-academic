package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.enrolment;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteExternalEnrolments {

	@Service
	public static void run(final Registration registration, String[] externalEnrolmentIDs) throws FenixServiceException {
		for (final String externalEnrolmentID : externalEnrolmentIDs) {
			final ExternalEnrolment externalEnrolment =
					getExternalEnrolmentByID(registration, Integer.valueOf(externalEnrolmentID));
			if (externalEnrolment == null) {
				throw new FenixServiceException("error.DeleteExternalEnrolments.externalEnrolmentID.doesnot.belong.to.student");
			}
			externalEnrolment.delete();
		}
	}

	private static ExternalEnrolment getExternalEnrolmentByID(final Registration registration, final Integer externalEnrolmentID) {
		for (final ExternalEnrolment externalEnrolment : registration.getExternalEnrolmentsSet()) {
			if (externalEnrolment.getIdInternal().equals(externalEnrolmentID)) {
				return externalEnrolment;
			}
		}
		return null;
	}
}