package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionDegree;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionSemester;
import net.sourceforge.fenixedu.domain.messaging.Announcement;

public class ExecutionCourseBean implements Serializable, HasExecutionSemester, HasExecutionDegree {

	private ExecutionDegree executionDegree;
	private CurricularYear curricularYear;
	private ExecutionCourse sourceExecutionCourse;
	private ExecutionCourse destinationExecutionCourse;
	private List<Announcement> announcements;
	private ExecutionSemester executionSemester;
	private Boolean chooseNotLinked;

	@Override
	public ExecutionSemester getExecutionPeriod() {
		return getExecutionSemester();
	}

	@Override
	public ExecutionDegree getExecutionDegree() {
		return executionDegree;
	}

	public void setExecutionDegree(ExecutionDegree executionDegree) {
		this.executionDegree = executionDegree;
	}

	public CurricularYear getCurricularYear() {
		return curricularYear;
	}

	public void setCurricularYear(CurricularYear curricularYear) {
		this.curricularYear = curricularYear;
	}

	public ExecutionSemester getExecutionSemester() {
		return executionSemester;
	}

	public void setExecutionSemester(ExecutionSemester executionSemester) {
		this.executionSemester = executionSemester;
	}

	public ExecutionCourse getSourceExecutionCourse() {
		return sourceExecutionCourse;
	}

	public void setSourceExecutionCourse(ExecutionCourse sourceExecutionCourse) {
		this.sourceExecutionCourse = sourceExecutionCourse;
	}

	public ExecutionCourse getDestinationExecutionCourse() {
		return destinationExecutionCourse;
	}

	public void setDestinationExecutionCourse(ExecutionCourse destinationExecutionCourse) {
		this.destinationExecutionCourse = destinationExecutionCourse;
	}

	public List<Announcement> getAnnouncements() {
		return announcements;
	}

	public void setAnnouncements(List<Announcement> announcements) {
		this.announcements = announcements;
	}

	public Boolean getChooseNotLinked() {
		return this.chooseNotLinked;
	}

	public void setChooseNotLinked(Boolean chooseNotLinked) {
		this.chooseNotLinked = chooseNotLinked;
	}

	public ExecutionCourseBean(ExecutionCourse executionCourse) {
		setSourceExecutionCourse(executionCourse);
	}

	public List<ExecutionCourse> getExecutionCourses() {
		List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
		if (this.chooseNotLinked) {
			result = this.getExecutionSemester().getExecutionCoursesWithNoCurricularCourses();
			Collections.sort(result, ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
		} else {
			for (final CurricularCourse curricularCourse : getDegreeCurricularPlan().getCurricularCourses(getExecutionSemester())) {
				if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(getCurricularYear(),
						getDegreeCurricularPlan(), getExecutionSemester())) {
					result.addAll(curricularCourse.getExecutionCoursesByExecutionPeriod(getExecutionSemester()));
				}
			}
		}
		return result;
	}

	private DegreeCurricularPlan getDegreeCurricularPlan() {
		return getExecutionDegree().getDegreeCurricularPlan();
	}

	public ExecutionCourseBean() {
	}

}
