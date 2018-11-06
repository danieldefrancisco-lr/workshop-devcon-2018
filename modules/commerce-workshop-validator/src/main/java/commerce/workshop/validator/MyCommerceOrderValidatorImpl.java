package commerce.workshop.validator;

import com.liferay.commerce.inventory.CPDefinitionInventoryEngineRegistry;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.order.CommerceOrderValidator;
import com.liferay.commerce.order.CommerceOrderValidatorResult;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.service.CPDefinitionInventoryLocalService;
import com.liferay.commerce.service.CommerceOrderItemLocalService;
import com.liferay.expando.kernel.exception.DuplicateTableNameException;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.ExpandoValue;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author dfrancisco
 */
@Component(immediate = true, property = { "commerce.order.validator.key=" + MyCommerceOrderValidatorImpl.KEY,
		"commerce.order.validator.priority:Integer=25" }, service = CommerceOrderValidator.class)
public class MyCommerceOrderValidatorImpl implements CommerceOrderValidator {

	public static final String KEY = "myvalidator";
	private static final Log _log = LogFactoryUtil.getLog(MyCommerceOrderValidatorImpl.class);

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return KEY;
	}

	@Activate
	public void activate() {
		ExpandoTable table = null;
		long[] companyIds = _portal.getCompanyIds();
		int companies = companyIds.length;
		for (int i = 0; i < companies; i++) {
			long companyId = companyIds[i];
			try {
				try {
					table = expandoTableLocalService.addDefaultTable(companyId, CPDefinition.class.getName());
				} catch (DuplicateTableNameException dtne) {
					table = expandoTableLocalService.getDefaultTable(companyId, CPDefinition.class.getName());
				}
				ExpandoColumn columnTemporaryOut = expandoColumnLocalService.getColumn(table.getTableId(),
						"temporary-out");
				if (columnTemporaryOut == null) {
					// Add the expando column
					columnTemporaryOut = expandoColumnLocalService.addColumn(table.getTableId(), "temporary-out",
							ExpandoColumnConstants.BOOLEAN, false);

					// Set the indexable property on the column
					UnicodeProperties properties = new UnicodeProperties();
					properties.setProperty(ExpandoColumnConstants.INDEX_TYPE, Boolean.TRUE.toString());
					columnTemporaryOut.setTypeSettingsProperties(properties);
					expandoColumnLocalService.updateExpandoColumn(columnTemporaryOut);
				}
			} catch (PortalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public CommerceOrderValidatorResult validate(CommerceOrderItem commerceOrderItem) throws PortalException {
		CPInstance cpInstance = commerceOrderItem.getCPInstance();

		CPDefinition cpDefinition = cpInstance.getCPDefinition();
		// get expando attribute from the product and check if the value is xxxxxx
		boolean isOut = getTemporaryOutValue(cpDefinition);

		_log.info("Checking if product " + cpDefinition.getName() + "with SKU: " + cpInstance.getSku()
				+ " has been marked as temporarily out");
		if (isOut) {
			return new CommerceOrderValidatorResult(commerceOrderItem.getCommerceOrderItemId(), false,
					"product-is-no-temporarily-available");
		} else {
			return new CommerceOrderValidatorResult(true);
		}
	}

	@Override
	public CommerceOrderValidatorResult validate(CPInstance cpInstance, int quantity) throws PortalException {
		if (cpInstance == null) {
			return new CommerceOrderValidatorResult(false, "please-select-a-valid-product");
		}

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		// get expando attribute from the product and check if the value is xxxxxx
		boolean isOut = getTemporaryOutValue(cpDefinition);

		_log.info("Checking if product " + cpDefinition.getName() + "with SKU: " + cpInstance.getSku()
				+ " has been marked as temporarily out");

		if (isOut) {
			return new CommerceOrderValidatorResult(false, "product-is-no-temporarily-available");
		} else {
			return new CommerceOrderValidatorResult(true);
		}
	}

	private boolean getTemporaryOutValue(CPDefinition cpDefinition) {
		ExpandoTable table = null;
		long companyId = cpDefinition.getCompanyId();
		try {
			table = expandoTableLocalService.getDefaultTable(companyId, CPDefinition.class.getName());

			ExpandoColumn columnTemporaryOut = expandoColumnLocalService.getColumn(table.getTableId(), "temporary-out");
			ExpandoValue expandoValue = expandoValueLocalService.getValue(table.getTableId(),
					columnTemporaryOut.getColumnId(), cpDefinition.getCPDefinitionId());
			if (expandoValue == null) {
				return false;
			}
			return expandoValue.getBoolean();
		} catch (PortalException e) {
			return false;
		}
	}

	@Reference
	private CommerceOrderItemLocalService _commerceOrderItemLocalService;

	@Reference
	private CPDefinitionInventoryEngineRegistry _cpDefinitionInventoryEngineRegistry;

	@Reference
	private CPDefinitionInventoryLocalService _cpDefinitionInventoryLocalService;

	@Reference
	ExpandoColumnLocalService expandoColumnLocalService;
	@Reference
	ExpandoTableLocalService expandoTableLocalService;
	@Reference
	ExpandoValueLocalService expandoValueLocalService;

	@Reference
	Portal _portal;

}