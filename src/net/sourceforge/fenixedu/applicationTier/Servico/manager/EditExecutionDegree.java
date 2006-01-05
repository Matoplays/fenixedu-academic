package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ICampus;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditExecutionDegree implements IService {

    public void run(InfoExecutionDegree infoExecutionDegree) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final IExecutionDegree oldExecutionDegree = (IExecutionDegree) persistentSuport
                .getIPersistentObject().readByOID(ExecutionDegree.class,
                        infoExecutionDegree.getIdInternal());
        if (oldExecutionDegree == null) {
            throw new NonExistingServiceException("message.nonExistingExecutionDegree", null);
        }

        final ICampus campus = (ICampus) persistentSuport.getIPersistentObject().readByOID(Campus.class,
                infoExecutionDegree.getInfoCampus().getIdInternal());
        if (campus == null) {
            throw new NonExistingServiceException("message.nonExistingCampus", null);
        }

        final IExecutionYear executionYear = (IExecutionYear) persistentSuport.getIPersistentObject()
                .readByOID(ExecutionYear.class,
                        infoExecutionDegree.getInfoExecutionYear().getIdInternal());
        if (executionYear == null) {
            throw new NonExistingServiceException("message.non.existing.execution.year", null);
        }

        oldExecutionDegree.setCampus(campus);
        oldExecutionDegree.setExecutionYear(executionYear);
        oldExecutionDegree.setTemporaryExamMap(infoExecutionDegree.getTemporaryExamMap());
    }

}
