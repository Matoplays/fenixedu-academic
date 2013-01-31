package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.services.Service;

public class ReadCurrentExecutionYear extends FenixService {

	@Service
	public static InfoExecutionYear run() {
		return InfoExecutionYear.newInfoFromDomain(ExecutionYear.readCurrentExecutionYear());
	}

}