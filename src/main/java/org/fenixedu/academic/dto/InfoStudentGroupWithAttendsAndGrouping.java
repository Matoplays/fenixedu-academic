/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto;

import org.fenixedu.academic.domain.StudentGroup;

public class InfoStudentGroupWithAttendsAndGrouping extends InfoStudentGroupWithAttends {

    @Override
    public void copyFromDomain(final StudentGroup studentGroup) {
        super.copyFromDomain(studentGroup);

        if (studentGroup != null) {
            setInfoGrouping(InfoGrouping.newInfoFromDomain(studentGroup.getGrouping()));
        }
    }

    public static InfoStudentGroupWithAttendsAndGrouping newInfoFromDomain(final StudentGroup studentGroup) {
        final InfoStudentGroupWithAttendsAndGrouping infoStudentGroup;

        if (studentGroup != null) {
            infoStudentGroup = new InfoStudentGroupWithAttendsAndGrouping();
            infoStudentGroup.copyFromDomain(studentGroup);
        } else {
            infoStudentGroup = null;
        }

        return infoStudentGroup;
    }

}