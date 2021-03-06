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
package org.fenixedu.academic.ui.struts.action.operator;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PhotoType;
import org.fenixedu.academic.domain.Photograph;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.person.PhotographUploadBean;
import org.fenixedu.academic.dto.person.PhotographUploadBean.UnableToProcessTheImage;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.ContentType;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;

import com.google.common.io.ByteStreams;

@StrutsFunctionality(app = OperatorApplication.class, path = "submit-photo", titleKey = "link.operator.submitPhoto")
@Mapping(module = "operator", path = "/submitPhoto")
@Forwards(value = { @Forward(name = "chooseFile", path = "/operator/photo/submitPhoto_bd.jsp") })
public class SubmitPhotoAction extends FenixDispatchAction {

    private static final int MAX_RAW_SIZE = 1000000; // 2M

    @EntryPoint
    public ActionForward preparePhotoUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("photo", new PhotographUploadBean());
        request.setAttribute("phroperCaption", BundleUtil.getString(Bundle.APPLICATION, "phroper.caption"));
        request.setAttribute("phroperSubCaption", BundleUtil.getString(Bundle.APPLICATION, "phroper.subCaption"));
        request.setAttribute("phroperButtonCaption", BundleUtil.getString(Bundle.APPLICATION, "phroper.buttonCaption"));
        request.setAttribute("phroperLoadingCaption", BundleUtil.getString(Bundle.APPLICATION, "phroper.loadingCaption"));
        request.setAttribute("buttonClean", BundleUtil.getString(Bundle.APPLICATION, "button.clean"));
        request.setAttribute("buttonRevert", BundleUtil.getString(Bundle.APPLICATION, "button.phroper.revert"));

        return mapping.findForward("chooseFile");
    }

    public ActionForward photoUpload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        PhotographUploadBean photo = getRenderedObject();
        RenderUtils.invalidateViewState();
        String base64Thumbnail = request.getParameter("encodedThumbnail");
        String base64Image = request.getParameter("encodedPicture");
        if (base64Image != null && base64Thumbnail != null) {
            DateTime now = new DateTime();
            photo.setFilename("mylovelypic_" + now.getYear() + now.getMonthOfYear() + now.getDayOfMonth() + now.getHourOfDay()
                    + now.getMinuteOfDay() + now.getSecondOfMinute() + ".png");
            photo.setBase64RawContent(base64Image.split(",")[1]);
            photo.setBase64RawThumbnail(base64Thumbnail.split(",")[1]);
            photo.setContentType(base64Image.split(",")[0].split(":")[1].split(";")[0]);
        }

        ActionMessages actionMessages = new ActionMessages();
        if (photo.getFileInputStream() == null) {
            actionMessages.add("error", new ActionMessage("errors.fileRequired"));
            saveMessages(request, actionMessages);
            return preparePhotoUpload(mapping, actionForm, request, response);
        }

        if (ContentType.getContentType(photo.getContentType()) == null) {
            actionMessages.add("error", new ActionMessage("errors.unsupportedFile"));
            saveMessages(request, actionMessages);
            return preparePhotoUpload(mapping, actionForm, request, response);
        }

        if (photo.getRawSize() > MAX_RAW_SIZE) {
            actionMessages.add("error", new ActionMessage("errors.fileTooLarge"));
            saveMessages(request, actionMessages);
            photo.deleteTemporaryFiles();
            return preparePhotoUpload(mapping, actionForm, request, response);
        }

        try {
            photo.processImage();
        } catch (UnableToProcessTheImage e) {
            actionMessages.add("error", new ActionMessage("errors.unableToProcessImage"));
            saveMessages(request, actionMessages);
            photo.deleteTemporaryFiles();
            return preparePhotoUpload(mapping, actionForm, request, response);
        }

        try {
            updatePersonPhoto(photo);
        } catch (Exception e) {
            actionMessages.add("error", new ActionMessage("errors.unableToSaveImage"));
            saveMessages(request, actionMessages);
            photo.deleteTemporaryFiles();
            return preparePhotoUpload(mapping, actionForm, request, response);
        }

        actionMessages.add("success", new ActionMessage("label.operator.submit.ok", ""));
        saveMessages(request, actionMessages);
        return preparePhotoUpload(mapping, actionForm, request, response);
    }

    @Atomic
    private void updatePersonPhoto(final PhotographUploadBean photo) throws FileNotFoundException, IOException {
        if (photo.getUsername() == null) {
            throw new DomainException("error.operatorPhotoUpload.null.username");
        }
        final Person person = Person.readPersonByUsername(photo.getUsername());
        if (person == null) {
            throw new DomainException("error.operatorPhotoUpload.invalid.username");
        }
        person.setPersonalPhoto(new Photograph(PhotoType.INSTITUTIONAL, ContentType.getContentType(photo.getContentType()),
                ByteStreams.toByteArray(photo.getFileInputStream())));
    }
}