package net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.phd.thesis.PhdJuryElementsRatificationEntity;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class PhdJuryElementsRatificationEntityProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		return Arrays.asList(PhdJuryElementsRatificationEntity.values());
	}

	@Override
	public Converter getConverter() {
		return new EnumConverter();
	}

}
