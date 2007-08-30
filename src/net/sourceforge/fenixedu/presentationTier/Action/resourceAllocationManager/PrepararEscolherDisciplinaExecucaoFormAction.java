package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.CurricularYearAndSemesterAndInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 * @author tfc130
 */
public class PrepararEscolherDisciplinaExecucaoFormAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

            IUserView userView = SessionUtils.getUserView(request);

            // Ler Disciplinas em Execucao
            InfoExecutionDegree iLE = (InfoExecutionDegree) request
                    .getAttribute("infoLicenciaturaExecucao");
            Integer semestre = (Integer) request.getAttribute("semestre");
            Integer anoCurricular = (Integer) request.getAttribute("anoCurricular");

            Object argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular[] = { new CurricularYearAndSemesterAndInfoExecutionDegree(
                    anoCurricular, semestre, iLE) };
            List infoDisciplinasExecucao = (ArrayList) ServiceManagerServiceFactory.executeService(
                    userView, "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
                    argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular);

            //Collections.sort(infoDisciplinasExecucao);

            List disciplinasExecucao = new ArrayList();
            disciplinasExecucao.add(new LabelValueBean("escolher", ""));
            for (int i = 0; i < infoDisciplinasExecucao.size(); i++) {
                InfoExecutionCourse elem = (InfoExecutionCourse) infoDisciplinasExecucao.get(i);
                disciplinasExecucao.add(new LabelValueBean(elem.getNome(), (new Integer(
                        infoDisciplinasExecucao.indexOf(elem) + 1)).toString()));
            }

            request.setAttribute("disciplinasExecucao", disciplinasExecucao);
            request.setAttribute("infoDisciplinasExecucao", infoDisciplinasExecucao);

            return mapping.findForward("Sucesso");
    }
}