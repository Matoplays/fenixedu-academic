<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="label.schedule" /></h2>


<bean:define id="month" name="yearMonth" property="month" />
<bean:define id="year" name="yearMonth" property="year" />
<bean:define id="employeeNumber" name="employeeScheduleBean" property="employee.employeeNumber" />
<bean:define id="employeeID" name="employeeScheduleBean" property="employee.idInternal" />

<p><bean:message key="label.show" />: <html:link
	page="<%="/viewEmployeeAssiduousness.do?method=showWorkSheet&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
	<bean:message key="link.workSheet" />
</html:link>, <html:link
	page="<%="/viewEmployeeAssiduousness.do?method=showSchedule&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
	<bean:message key="label.schedule" />
</html:link>, <html:link
	page="<%="/viewEmployeeAssiduousness.do?method=showClockings&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
	<bean:message key="link.clockings" />
</html:link>, <html:link
	page="<%="/viewEmployeeAssiduousness.do?method=showJustifications&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
	<bean:message key="link.justifications" />
</html:link></p>



	<span class="toprint"><br />
	</span>
	<fr:view name="employeeScheduleBean" property="employee" schema="show.employeeInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="showinfo1 thbold" />
		</fr:layout>
	</fr:view>

		<%net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session
                    .getAttribute(net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants.U_VIEW);
		if (net.sourceforge.fenixedu.domain.assiduousness.StaffManagementSection.isMember(user.getPerson())) {%>
			<logic:notEmpty name="employeeScheduleBean" property="employee.assiduousness">
				<ul>
					<li>
					<html:link page="<%="/employeeAssiduousness.do?method=prepareAssociateEmployeeWorkSchedule&amp;employeeID="+employeeID.toString()%>">
						<bean:message key="link.associateWorkSchedule" />
					</html:link>
					</li>
				</ul>	
			</logic:notEmpty>
		<% } %>

<logic:notEmpty name="employeeScheduleBean" property="employeeWorkWeekScheduleList">
	<bean:size id="listSize" name="employeeScheduleBean" property="employeeWorkWeekScheduleList"/>
	<logic:iterate id="employeeWorkScheduleBean" name="employeeScheduleBean" property="employeeWorkWeekScheduleList">
		<table class="tstyle1 thtop thlight printborder">
			<logic:notEqual name="listSize" value="1">
				<tr>
					<th class="cornerleft"></th>
					<th colspan="5">
						<bean:message key="label.week"/> <bean:write name="employeeWorkScheduleBean" property="workWeekNumber"/>
					</th>
				</tr>
			</logic:notEqual>
			<tr>
				<th class="cornerleft"></th>
				<th><b><bean:message key="MONDAY_ACRONYM" /></b></th>
				<th><b><bean:message key="TUESDAY_ACRONYM" /></b></th>
				<th><b><bean:message key="WEDNESDAY_ACRONYM" /></b></th>
				<th><b><bean:message key="THURSDAY_ACRONYM" /></b></th>
				<th><b><bean:message key="FRIDAY_ACRONYM" /></b></th>
			</tr>			
			<tr>
				<th style="text-align: right;"><bean:message key="label.acronym" />:</th>			
				<td class="acenter">
					<logic:notEmpty name="employeeWorkScheduleBean" property="mondaySchedule">
						<bean:write name="employeeWorkScheduleBean" property="mondaySchedule.acronym" />
					</logic:notEmpty>
				</td>
				<td class="acenter">
					<logic:notEmpty name="employeeWorkScheduleBean" property="tuesdaySchedule">
						<bean:write name="employeeWorkScheduleBean" property="tuesdaySchedule.acronym" />
					</logic:notEmpty>
				</td>
				<td class="acenter">
					<logic:notEmpty name="employeeWorkScheduleBean" property="wednesdaySchedule">
						<bean:write name="employeeWorkScheduleBean" property="wednesdaySchedule.acronym" />
					</logic:notEmpty>
				</td>
				<td class="acenter">
					<logic:notEmpty name="employeeWorkScheduleBean" property="thursdaySchedule">
						<bean:write name="employeeWorkScheduleBean" property="thursdaySchedule.acronym" />
					</logic:notEmpty>
				</td>
				<td class="acenter">
					<logic:notEmpty name="employeeWorkScheduleBean" property="fridaySchedule">
						<bean:write name="employeeWorkScheduleBean" property="fridaySchedule.acronym" />
					</logic:notEmpty>
				</td>
			</tr>
			<tr>
				<th style="text-align: right;"><bean:message key="label.normalWorkPeriod" />:</th>
				<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="mondayNormalWorkPeriod" filter="false" /></td>
				<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="tuesdayNormalWorkPeriod" filter="false" /></td>
				<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="wednesdayNormalWorkPeriod" filter="false" /></td>
				<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="thursdayNormalWorkPeriod" filter="false" /></td>
				<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="fridayNormalWorkPeriod" filter="false" /></td>												
			</tr>
			<logic:equal name="employeeWorkScheduleBean" property="hasFixedPeriod" value="true">
				<tr>
					<th style="text-align: right;"><bean:message key="label.fixedWorkPeriod" />:</th>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="mondayFixedWorkPeriod" filter="false" /></td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="tuesdayFixedWorkPeriod" filter="false" /></td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="wednesdayFixedWorkPeriod" filter="false" /></td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="thursdayFixedWorkPeriod" filter="false" /></td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="fridayFixedWorkPeriod" filter="false" /></td>												
				</tr>
			</logic:equal>
			<logic:equal name="employeeWorkScheduleBean" property="hasMealPeriod" value="true">
				<tr>
					<th style="text-align: right;"><bean:message key="label.mealPeriod" />:</th>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean"	property="mondayMealPeriod" filter="false" /><br />
						<p class="mvert05" style="color: #888;"><bean:write	name="employeeWorkScheduleBean" property="mondayMandatoryMealPeriods" filter="false" /></p>
					</td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean"	property="tuesdayMealPeriod" filter="false" /><br />
						<p class="mvert05" style="color: #888;"><bean:write	name="employeeWorkScheduleBean" property="tuesdayMandatoryMealPeriods" filter="false" /></p>
					</td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean"	property="wednesdayMealPeriod" filter="false" /><br />
						<p class="mvert05" style="color: #888;"><bean:write	name="employeeWorkScheduleBean" property="wednesdayMandatoryMealPeriods" filter="false" /></p>
					</td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean"	property="thursdayMealPeriod" filter="false" /><br />
						<p class="mvert05" style="color: #888;"><bean:write	name="employeeWorkScheduleBean" property="thursdayMandatoryMealPeriods" filter="false" /></p>
					</td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean"	property="fridayMealPeriod" filter="false" /><br />
						<p class="mvert05" style="color: #888;"><bean:write	name="employeeWorkScheduleBean" property="fridayMandatoryMealPeriods" filter="false" /></p>
					</td>												
				</tr>
			</logic:equal>
		</table>
	</logic:iterate>
</logic:notEmpty>

<logic:empty name="employeeScheduleBean" property="employeeWorkWeekScheduleList">
	<bean:message key="message.employee.noInfo" />
</logic:empty>