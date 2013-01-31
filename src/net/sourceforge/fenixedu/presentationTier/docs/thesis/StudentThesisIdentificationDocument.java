package net.sourceforge.fenixedu.presentationTier.docs.thesis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import net.sourceforge.fenixedu.util.JasperPrintProcessor;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class StudentThesisIdentificationDocument extends ThesisDocument {

	private static final long serialVersionUID = 1L;

	public StudentThesisIdentificationDocument(Thesis thesis) {
		super(thesis);
	}

	@Override
	protected void fillThesisInfo() {
		Thesis thesis = getThesis();

		ThesisFile file = thesis.getDissertation();
		if (file != null) {
			addParameter("thesisTitle", file.getTitle());
			addParameter("thesisSubtitle", neverNull(file.getSubTitle()));
			addParameter("thesisLanguage", getLanguage(file));
		} else {
			addParameter("thesisTitle", EMPTY_STR);
			addParameter("thesisSubtitle", EMPTY_STR);
			addParameter("thesisLanguage", EMPTY_STR);
		}

		String date = null;

		DateTime discussion = thesis.getDiscussed();
		if (discussion != null) {
			date = String.format(new Locale("pt"), "%1$td/%1$tm/%1$tY", discussion.toDate());
		}

		addParameter("discussion", neverNull(date));

		int index = 0;
		for (String keyword : splitKeywords(thesis.getKeywordsPt())) {
			addParameter("keywordPt" + index++, keyword);
		}

		while (index < 6) {
			addParameter("keywordPt" + index++, EMPTY_STR);
		}

		index = 0;
		for (String keyword : splitKeywords(thesis.getKeywordsEn())) {
			addParameter("keywordEn" + index++, keyword);
		}

		while (index < 6) {
			addParameter("keywordEn" + index++, EMPTY_STR);
		}

		addParameter("keywordsPt", thesis.getKeywordsPt());
		addParameter("keywordsEn", thesis.getKeywordsEn());

		addParameter("abstractPt", neverNull(thesis.getThesisAbstractPt()));
		addParameter("abstractEn", neverNull(thesis.getThesisAbstractEn()));
	}

	private String getLanguage(ThesisFile file) {
		Language language = file.getLanguage();

		if (language == null) {
			return EMPTY_STR;
		}

		return getEnumerationBundle().getString(language.name());
	}

	private List<String> splitKeywords(String keywords) {
		List<String> result = new ArrayList<String>();

		if (keywords == null) {
			return result;
		}

		for (String part : keywords.split(",")) {
			String trimmed = part.trim();

			if (trimmed.length() > 0) {
				result.add(trimmed);
			}
		}

		return result;
	}

	@Override
	public String getReportFileName() {
		Thesis thesis = getThesis();
		return "identificacao-aluno-" + thesis.getStudent().getNumber();
	}

	@Override
	public JasperPrintProcessor getPreProcessor() {
		return LineProcessor.instance;
	}

	private static class LineProcessor implements JasperPrintProcessor {

		private static LineProcessor instance = new LineProcessor();

		private static String[][] FIELD_LINE_MAP = { { "textField-title", "13", "line-title-2" },
				{ "textField-subtitle", "13", "line-subtitle-2" } };

		private static float LINE_DISTANCE = 11.5f;

		@Override
		public JasperPrint process(JasperPrint jasperPrint) {
			Map<String, JRPrintElement> map = getElementsMap(jasperPrint);

			for (String[] element2 : FIELD_LINE_MAP) {
				String elementKey = element2[0];
				Integer height = new Integer(element2[1]);

				JRPrintElement element = map.get(elementKey);

				if (element == null) {
					continue;
				}

				if (element.getHeight() > height.intValue()) {
					// height increased
					for (int j = 2; j < element2.length; j++) {
						JRPrintElement line = map.get(element2[j]);

						if (line != null) {
							line.setY(element.getY() + ((int) (j * LINE_DISTANCE)));
						}
					}
				} else {
					// height is the same
					for (int j = 2; j < element2.length; j++) {
						JRPrintElement line = map.get(element2[j]);

						if (line != null) {
							removeElement(jasperPrint, line);
						}
					}
				}
			}

			return jasperPrint;
		}

		private Map<String, JRPrintElement> getElementsMap(JasperPrint jasperPrint) {
			Map<String, JRPrintElement> map = new HashMap<String, JRPrintElement>();

			Iterator pages = jasperPrint.getPages().iterator();
			while (pages.hasNext()) {
				JRPrintPage page = (JRPrintPage) pages.next();

				Iterator elements = page.getElements().iterator();
				while (elements.hasNext()) {
					JRPrintElement element = (JRPrintElement) elements.next();

					map.put(element.getKey(), element);
				}
			}

			return map;
		}

		private void removeElement(JasperPrint jasperPrint, JRPrintElement target) {
			Iterator pages = jasperPrint.getPages().iterator();
			while (pages.hasNext()) {
				JRPrintPage page = (JRPrintPage) pages.next();

				Iterator elements = page.getElements().iterator();
				while (elements.hasNext()) {
					JRPrintElement element = (JRPrintElement) elements.next();

					if (element == target) {
						elements.remove();
					}
				}
			}
		}

	}
}
