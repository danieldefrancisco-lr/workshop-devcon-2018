package commerce.workshop.initialization;

import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.expando.kernel.exception.DuplicateTableNameException;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.UnicodeProperties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = { "key=global.startup.events" }, service = LifecycleAction.class)
public class CPExpandoStartupAction extends SimpleAction {

	@Override
	public void run(String[] ids) throws ActionException {
		ExpandoTable table = null;
		long companyId = Long.parseLong(ids[0]);
		try {
			try {
				table = expandoTableLocalService.addDefaultTable(companyId, CPDefinition.class.getName());
			} catch (DuplicateTableNameException dtne) {
				table = expandoTableLocalService.getDefaultTable(companyId, CPDefinition.class.getName());
			}
			// Add the expando column
			ExpandoColumn columnTemporaryOut = expandoColumnLocalService.addColumn(table.getTableId(), "temporary-out",
					ExpandoColumnConstants.BOOLEAN, false);

			// Set the indexable property on the column
			UnicodeProperties properties = new UnicodeProperties();
			properties.setProperty(ExpandoColumnConstants.INDEX_TYPE, Boolean.TRUE.toString());
			columnTemporaryOut.setTypeSettingsProperties(properties);
			expandoColumnLocalService.updateExpandoColumn(columnTemporaryOut);
			
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Reference
	ExpandoColumnLocalService expandoColumnLocalService;
	@Reference
	ExpandoTableLocalService expandoTableLocalService;
	@Reference
	ExpandoValueLocalService expandoValueLocalService;

}
