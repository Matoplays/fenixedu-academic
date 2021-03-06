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
/*
 * Created on 6/Jul/2004
 *
 */
package org.fenixedu.academic.dto;

import org.fenixedu.academic.domain.Curriculum;

/**
 * @author Tânia Pousão
 * 
 */
public class InfoCurriculumWithInfoCurricularCourse extends InfoCurriculum {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.fenixedu.academic.dto.InfoCurriculum#copyFromDomain
     * (Dominio.Curriculum)
     */
    @Override
    public void copyFromDomain(Curriculum curriculum) {
        super.copyFromDomain(curriculum);
        if (curriculum != null) {
            setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curriculum.getCurricularCourse()));
        }
    }

    public static InfoCurriculum newInfoFromDomain(Curriculum curriculum) {
        InfoCurriculumWithInfoCurricularCourse infoCurriculum = null;
        if (curriculum != null) {
            infoCurriculum = new InfoCurriculumWithInfoCurricularCourse();
            infoCurriculum.copyFromDomain(curriculum);
        }
        return infoCurriculum;
    }
}