package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;
import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class ConclusionProcessVersion extends ConclusionProcessVersion_Base {

    static final private Comparator<ConclusionProcessVersion> COMPARATOR_BY_CREATION_DATE_TIME = new Comparator<ConclusionProcessVersion>() {
	public int compare(ConclusionProcessVersion o1, ConclusionProcessVersion o2) {
	    return o1.getCreationDateTime().compareTo(o2.getCreationDateTime());
	}
    };

    static final public Comparator<ConclusionProcessVersion> COMPARATOR_BY_CREATION_DATE_TIME_AND_ID = new Comparator<ConclusionProcessVersion>() {
	final public int compare(ConclusionProcessVersion o1, ConclusionProcessVersion o2) {
	    final ComparatorChain chain = new ComparatorChain();
	    chain.addComparator(ConclusionProcessVersion.COMPARATOR_BY_CREATION_DATE_TIME);
	    chain.addComparator(ConclusionProcessVersion.COMPARATOR_BY_ID);
	    return chain.compare(o1, o2);
	}
    };

    protected ConclusionProcessVersion(final RegistrationConclusionBean bean) {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setCreationDateTime(new DateTime());
	super.setResponsible(AccessControl.getPerson());

	final YearMonthDay conclusion = bean.calculateConclusionDate();
	final Integer finalAverage = bean.calculateFinalAverage();
	final BigDecimal average = bean.calculateAverage();
	final Double ectsCredits = bean.calculateCredits();
	final ExecutionYear ingressionYear = bean.calculateIngressionYear();
	final ExecutionYear conclusionYear = bean.calculateConclusionYear();

	check(finalAverage, "error.ConclusionProcessVersion.argument.must.not.be.null");
	check(average, "error.ConclusionProcessVersion.argument.must.not.be.null");
	check(ectsCredits, "error.ConclusionProcessVersion.argument.must.not.be.null");
	check(ingressionYear, "error.ConclusionProcessVersion.argument.must.not.be.null");
	check(conclusionYear, "error.ConclusionProcessVersion.argument.must.not.be.null");

	super.setConclusionDate(conclusion.toLocalDate());
	super.setFinalAverage(finalAverage);
	super.setAverage(average);
	super.setCredits(BigDecimal.valueOf(ectsCredits));
	super.setCurriculum(bean.getCurriculumForConclusion().toString());
	super.setIngressionYear(ingressionYear);
	super.setConclusionYear(conclusionYear);
    }

    protected void update(final Person responsible, final Integer finalAverage, final LocalDate conclusionDate, final String notes) {
	check(finalAverage, "error.ConclusionProcessVersion.argument.must.not.be.null");
	check(conclusionDate, "error.ConclusionProcessVersion.argument.must.not.be.null");

	super.setResponsible(responsible);
	super.setFinalAverage(finalAverage);
	super.setConclusionDate(conclusionDate);
	super.setNotes(StringUtils.isEmpty(notes) ? null : notes);
    }

    protected void update(final Person responsible, final Integer finalAverage, BigDecimal average,
	    final LocalDate conclusionDate, final String notes) {
	update(responsible, finalAverage, conclusionDate, notes);
	super.setAverage(average);
    }

    @Override
    public void setDissertationEnrolment(final Enrolment dissertationEnrolment) {
	if (getConclusionProcess().isCycleConclusionProcess()) {
	    super.setDissertationEnrolment(dissertationEnrolment);
	} else {
	    throw new DomainException("error.ConclusionProcessVersion.wrong.method.usage");
	}
    }

    @Override
    public void setMasterDegreeThesis(MasterDegreeThesis masterDegreeThesis) {
	if (getConclusionProcess().isRegistrationConclusionProcess()) {
	    super.setMasterDegreeThesis(masterDegreeThesis);
	} else {
	    throw new DomainException("error.ConclusionProcessVersion.wrong.method.usage");
	}
    }

    @Override
    public void setConclusionProcess(final ConclusionProcess conclusionProcess) {
	throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setLastVersionConclusionProcess(ConclusionProcess lastVersionConclusionProcess) {
	throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void removeConclusionProcess() {
	throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setRootDomainObject(RootDomainObject rootDomainObject) {
	throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setCreationDateTime(DateTime creationDateTime) {
	throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setResponsible(Person responsible) {
	throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setConclusionDate(LocalDate conclusionDate) {
	throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setFinalAverage(Integer finalAverage) {
	throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setAverage(BigDecimal average) {
	throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setCredits(BigDecimal credits) {
	throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setCurriculum(String curriculum) {
	throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setIngressionYear(ExecutionYear ingressionYear) {
	throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setConclusionYear(ExecutionYear conclusionYear) {
	throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

    @Override
    public void setNotes(String notes) {
	throw new DomainException("error.ConclusionProcessVersion.method.not.allowed");
    }

}
