/*
 * Created on 1/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.Iterator;
import java.util.List;

import DataBeans.gesdis.InfoSection;
import DataBeans.util.Cloner;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac2
 */
public class EditSection implements IServico {

	private static EditSection service = new EditSection();

	/**
	 * The singleton access method of this class.
	 */
	public static EditSection getService() {
		return service;
	}

    
	public String getNome() {
		return "EditSection";
	}

	/**
	 * 
	 * @param newOrder
	 * @param oldOrder
	 * @param site
	 * @throws FenixServiceException
	 */
	
	private void organizeSections(int newOrder, int oldOrder, ISection newSuperiorSection, ISection oldSuperiorSection, ISite site)throws FenixServiceException
	{
		IPersistentSection persistentSection = null;

		List whereWasSectionList = null;
		List whereGoesSectionList = null;
		
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			persistentSection = persistentSuport.getIPersistentSection();

			whereWasSectionList = persistentSection.readBySiteAndSection(site,oldSuperiorSection);
			whereGoesSectionList = persistentSection.readBySiteAndSection(site,newSuperiorSection);
		
			Iterator iterWhereWasSection = whereWasSectionList.iterator();
			
			ISection oldSection = null;
			int iterOldSectionOrder;
			while (iterWhereWasSection.hasNext()) {
				
				oldSection = (ISection) iterWhereWasSection.next();
				iterOldSectionOrder = oldSection.getSectionOrder().intValue();
			
				if (iterOldSectionOrder > oldOrder) {
			
					oldSection.setSectionOrder(new Integer(iterOldSectionOrder-1));		
					persistentSection.lockWrite(oldSection);
				}
			}
			
			Iterator iterWhereGoesSection = whereGoesSectionList.iterator();
			
			ISection newSection = null;
			int iterNewSectionOrder;
			while (iterWhereGoesSection.hasNext()) {
				
				newSection = (ISection) iterWhereGoesSection.next();
				iterNewSectionOrder = newSection.getSectionOrder().intValue();
			
				if (iterNewSectionOrder >= newOrder) {
			
					newSection.setSectionOrder(new Integer(iterNewSectionOrder+1));
					persistentSection.lockWrite(newSection);
					}
				}
			
			} catch (ExcepcaoPersistencia excepcaoPersistencia) {
						throw new FenixServiceException(excepcaoPersistencia);
					}
			
	}
	
	
//	this method reorders some sections but not the section that we are editing
	private void organizeSectionsOrder(int newOrder, int oldOrder, ISection superiorSection, ISite site) throws FenixServiceException {

			IPersistentSection persistentSection = null;
			try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				persistentSection = persistentSuport.getIPersistentSection();

				List sectionsList = null;
				sectionsList = persistentSection.readBySiteAndSection(site,superiorSection);

				Iterator iterSections = sectionsList.iterator();

				if (newOrder - oldOrder > 0)
					while (iterSections.hasNext()) {

						ISection iterSection = (ISection) iterSections.next();
						int iterSectionOrder = iterSection.getSectionOrder().intValue();

						if (iterSectionOrder > oldOrder && iterSectionOrder <= newOrder) {
							iterSection.setSectionOrder(new Integer(iterSectionOrder - 1));
							persistentSection.lockWrite(iterSection);
						}
					} else
						while (iterSections.hasNext()) {
							ISection iterSection = (ISection) iterSections.next();

							int iterSectionOrder = iterSection.getSectionOrder().intValue();

							if (iterSectionOrder >= newOrder && iterSectionOrder < oldOrder) {

								iterSection.setSectionOrder(new Integer(iterSectionOrder + 1));
								persistentSection.lockWrite(iterSection);
							}
						} 
			} catch (ExcepcaoPersistencia excepcaoPersistencia) {
				throw new FenixServiceException(excepcaoPersistencia);
			}
		}

	/**
	 * Executes the service.
	 */
	public Boolean run (InfoSection oldInfoSection, InfoSection newInfoSection) throws FenixServiceException{
		
		ISection superiorSection = null; 
		IItem item=null;
		ISection newSuperiorSection = null;
		try {       		
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentSection persistentSection = sp.getIPersistentSection();
			
			ISite site = Cloner.copyInfoSite2ISite(oldInfoSection.getInfoSite());
			
			InfoSection oldSuperiorInfoSection = oldInfoSection.getSuperiorInfoSection();

			if(oldSuperiorInfoSection != null) 
				superiorSection = Cloner.copyInfoSection2ISection(oldSuperiorInfoSection);
			
			ISection section = persistentSection.readBySiteAndSectionAndName(site, superiorSection, oldInfoSection.getName());
			
			section.setLastModifiedDate(newInfoSection.getLastModifiedDate());
			section.setName(newInfoSection.getName());
			
			int newOrder = newInfoSection.getSectionOrder().intValue();
			int oldOrder = oldInfoSection.getSectionOrder().intValue();
			InfoSection newSuperiorInfoSection= newInfoSection.getSuperiorInfoSection();
		
			if(newSuperiorInfoSection!= oldSuperiorInfoSection)
			{
				if(newSuperiorInfoSection != null) 
					newSuperiorSection = Cloner.copyInfoSection2ISection(newSuperiorInfoSection);
				organizeSections(newOrder,oldOrder,newSuperiorSection,superiorSection,site);
				section.setSectionOrder(new Integer(newOrder));						
				section.setSuperiorSection(newSuperiorSection);

			}
			else
				if(newOrder!=oldOrder)
				{
					organizeSectionsOrder(newOrder,oldOrder,superiorSection,site);
					section.setSectionOrder(new Integer(newOrder));						

				}
		
		
			persistentSection.lockWrite(section);					
			}
			catch (ExcepcaoPersistencia e) {
				throw new FenixServiceException(e);
			}
				
		return new Boolean(true);
	}	
}