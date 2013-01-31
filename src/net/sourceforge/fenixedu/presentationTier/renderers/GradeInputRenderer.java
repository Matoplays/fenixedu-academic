package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlMenu;
import pt.ist.fenixWebFramework.renderers.components.HtmlSimpleValueComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextInput;
import pt.ist.fenixWebFramework.renderers.components.controllers.HtmlController;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;
import pt.ist.fenixWebFramework.renderers.validators.RequiredValidator;

public class GradeInputRenderer extends InputRenderer {

	private boolean required = false;

	private Integer maxLength;

	private String size;

	@Override
	protected Layout getLayout(Object object, Class type) {
		return new Layout() {

			@Override
			public HtmlComponent createComponent(Object object, Class type) {
				Grade grade = (Grade) object;
				HtmlInlineContainer container = new HtmlInlineContainer();

				MetaSlot slot = (MetaSlot) getInputContext().getMetaObject();

				final HtmlGradeTextInput value = new HtmlGradeTextInput(isRequired());
				value.bind(slot);

				value.setMaxLength(getMaxLength());
				value.setSize(getSize());

				HtmlMenu menu = new HtmlMenu();
				menu.setName(slot.getKey().toString() + "_scale");

				menu.createDefaultOption(RenderUtils.getResourceString("renderers.menu.default.title"));
				for (GradeScale scale : GradeScale.values()) {
					menu.createOption(RenderUtils.getEnumString(scale)).setValue(scale.getName());
				}

				if (grade != null && !grade.isEmpty()) {
					value.setValue(grade.getValue());
					menu.setValue(grade.getGradeScale().getName());
				}

				menu.setController(new HtmlController() {

					@Override
					public void execute(IViewState viewState) {
						HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getControlledComponent();
						value.setConverter(new GradeConverter(component.getValue()));
					}

				});

				container.addChild(value);
				container.addChild(menu);

				return container;
			}

		};
	}

	private static class GradeConverter extends Converter {

		private GradeScale gradeScale;

		public GradeConverter(String gradeScaleName) {
			if (gradeScaleName != null && gradeScaleName.length() > 0) {
				gradeScale = GradeScale.valueOf(gradeScaleName);
			}
		}

		@Override
		public Object convert(Class type, Object value) {
			String gradeValue = (String) value;
			if (gradeValue == null || gradeValue.length() == 0) {
				return Grade.createEmptyGrade();
			} else {
				return Grade.createGrade(gradeValue, getGradeScale());
			}
		}

		public GradeScale getGradeScale() {
			return gradeScale;
		}

		public void setGradeScale(GradeScale gradeScale) {
			this.gradeScale = gradeScale;
		}

	}

	private static class HtmlGradeTextInput extends HtmlTextInput {

		public HtmlGradeTextInput(final boolean required) {
			HtmlChainValidator htmlChainValidator = new HtmlChainValidator(this);
			super.setChainValidator(htmlChainValidator);
			new HtmlGradeTextInputValidator(htmlChainValidator);
			if (required) {
				htmlChainValidator.addValidator(new RequiredValidator(htmlChainValidator));
			}
		}

		@Override
		public void setChainValidator(HtmlChainValidator chainValidator) {
		}

		@Override
		public void addValidator(HtmlValidator htmlValidator) {
		}

		private static class HtmlGradeTextInputValidator extends HtmlValidator {

			private Object[] arguments;

			public HtmlGradeTextInputValidator(HtmlChainValidator htmlChainValidator) {
				super(htmlChainValidator);
			}

			public Object[] getArguments() {
				return arguments;
			}

			public void setArguments(Object... arguments) {
				this.arguments = arguments;
			}

			@Override
			protected String getResourceMessage(String message) {
				return RenderUtils.getFormatedResourceString(message, getArguments());
			}

			@Override
			public void performValidation() {

				HtmlGradeTextInput htmlGradeTextInput = (HtmlGradeTextInput) getComponent();
				GradeConverter gradeConverter = (GradeConverter) htmlGradeTextInput.getConverter();
				GradeScale gradeScale = gradeConverter.getGradeScale();

				String value = getComponent().getValue().trim();
				if (value != null && value.length() > 0) {
					if (gradeScale == null) {
						setValid(false);
						setMessage("renderers.validator.grade.no.grade.scale");
					} else {
						if (!gradeScale.belongsTo(value)) {
							setValid(false);
							setMessage("renderers.validator.grade.invalid.grade.value");
							setArguments(value, RenderUtils.getEnumString(gradeScale));
						}
					}
				}
			}
		}
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}
