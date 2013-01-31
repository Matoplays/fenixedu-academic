package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.enrolment.EnroledCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class ImprovementStudentCurriculumGroupBean extends StudentCurriculumGroupBean {

	static private final long serialVersionUID = 1L;

	public ImprovementStudentCurriculumGroupBean(final CurriculumGroup curriculumGroup, final ExecutionSemester executionSemester) {
		super(curriculumGroup, executionSemester, null);
	}

	@Override
	protected List<IDegreeModuleToEvaluate> buildCourseGroupsToEnrol(CurriculumGroup group, ExecutionSemester executionSemester) {
		return Collections.emptyList();
	}

	@Override
	protected List<StudentCurriculumEnrolmentBean> buildCurricularCoursesEnroled(CurriculumGroup group,
			ExecutionSemester executionSemester) {
		List<StudentCurriculumEnrolmentBean> result = new ArrayList<StudentCurriculumEnrolmentBean>();
		for (CurriculumModule curriculumModule : group.getCurriculumModules()) {
			if (curriculumModule.isEnrolment()) {
				Enrolment enrolment = (Enrolment) curriculumModule;
				if (enrolment.isImprovementEnroled() && enrolment.getExecutionPeriod().isBefore(executionSemester)) {
					result.add(new StudentCurriculumEnrolmentBean(enrolment));
				}
			}
		}

		return result;
	}

	@Override
	protected List<IDegreeModuleToEvaluate> buildCurricularCoursesToEnrol(CurriculumGroup group,
			ExecutionSemester executionSemester) {
		List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
		for (CurriculumModule curriculumModule : group.getCurriculumModules()) {
			if (curriculumModule.isEnrolment()) {
				Enrolment enrolment = (Enrolment) curriculumModule;
				if (enrolment.canBeImproved() && enrolment.getExecutionPeriod().isBefore(executionSemester)) {
					result.add(new EnroledCurriculumModuleWrapper(enrolment, enrolment.getExecutionPeriod()));
				}
			}
		}

		return result;
	}

	@Override
	protected List<StudentCurriculumGroupBean> buildCurriculumGroupsEnroled(CurriculumGroup parentGroup,
			ExecutionSemester executionSemester, int[] curricularYears) {
		final List<StudentCurriculumGroupBean> result = new ArrayList<StudentCurriculumGroupBean>();
		for (final CurriculumGroup curriculumGroup : parentGroup.getCurriculumGroupsToEnrolmentProcess()) {
			result.add(new ImprovementStudentCurriculumGroupBean(curriculumGroup, executionSemester));
		}

		return result;
	}

	@Override
	public List<IDegreeModuleToEvaluate> getSortedDegreeModulesToEvaluate() {
		final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>(getCurricularCoursesToEnrol());
		Collections.sort(result, IDegreeModuleToEvaluate.COMPARATOR_BY_EXECUTION_PERIOD);
		return result;
	}

	@Override
	public boolean isToBeDisabled() {
		return true;
	}

}
