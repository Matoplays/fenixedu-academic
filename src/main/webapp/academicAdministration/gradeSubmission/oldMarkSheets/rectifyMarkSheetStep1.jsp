<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectifyOldMarkSheet"/></h2>

<p class="breadcumbs"><span class="actual"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectifyOldMarkSheet.step.one"/></span> &gt; <span><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectifyOldMarkSheet.step.two"/></span> &gt; <span><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectifyOldMarkSheet.step.three"/></span></p>

<logic:messagesPresent message="true">
	<ul>
		<html:messages bundle="ACADEMIC_OFFICE_RESOURCES" id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<bean:define id="urlPath" name="edit" property="url" />

<fr:edit id="edit"
		 name="edit"
		 type="org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementCreateBean"
		 schema="oldMarkSheet.rectify.step.one"
		 action='<%= "/rectifyOldMarkSheet.do?method=rectifyMarkSheetStepOne" + urlPath %>'>
	<fr:destination name="postBack" path="/rectifyOldMarkSheet.do?method=prepareSearchMarkSheetPostBack"/>
	<fr:destination name="invalid" path="/rectifyOldMarkSheet.do?method=prepareSearchMarkSheetInvalid"/>
	<fr:destination name="cancel" path='<%= "/rectifyOldMarkSheet.do?method=backSearchMarkSheet" + urlPath %>'/>
	<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle5 thright thlight ulnomargin"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>


