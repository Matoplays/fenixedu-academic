package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

public class ProcessNewAcademicServiceRequests extends Service {

    public void run(final Employee employee, final List<Integer> academicServiceRequestToProcessIds)
            throws FenixServiceException {
        for (Integer academicServiceRequestToProcessId : academicServiceRequestToProcessIds) {
            rootDomainObject.readAcademicServiceRequestByOID(academicServiceRequestToProcessId).edit(
                    AcademicServiceRequestSituationType.PROCESSING, employee, null);
        }
    }

}
