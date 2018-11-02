package commerce.workshop.datasource;

import com.liferay.commerce.product.catalog.CPQuery;
import com.liferay.commerce.product.data.source.CPDataSource;
import com.liferay.commerce.product.data.source.CPDataSourceResult;
import com.liferay.commerce.product.util.CPDefinitionHelper;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author dfrancisco
 */
@Component(
	immediate = true,
	property = {
			"commerce.product.data.source.name=workshopDataSource"
	},
	service = CPDataSource.class
)
public class CommerceWorkshopDatasource implements CPDataSource {

	@Override
	public String getLabel(Locale locale) {
		return
				LanguageUtil.get(locale, "workshop-datasource");
	}

	@Override
	public String getName() {
		return "Workshop Datasource";
	}

	@Override
	public CPDataSourceResult getResult(HttpServletRequest httpServletRequest, int start, int end) throws Exception {		
		//New arrivals
		//search query elasticsearch
		long groupId = _portal.getScopeGroupId(httpServletRequest);

		SearchContext searchContext = new SearchContext();

		LinkedHashMap<String, Object> params = new LinkedHashMap<>();

		params.put("keywords", StringPool.STAR);

		Map<String, Serializable> attributes = new HashMap<>();

		attributes.put(Field.STATUS, WorkflowConstants.STATUS_APPROVED);
		
		attributes.put("params", params);

		searchContext.setAttributes(attributes);

		searchContext.setCompanyId(_portal.getCompanyId(httpServletRequest));
		searchContext.setGroupIds(new long[] {groupId});

		searchContext.setKeywords(StringPool.STAR);
		
		searchContext.setSorts(new Sort("modifiedDate",true));

		return _cpDefinitionHelper.search(
			groupId, searchContext, new CPQuery(), start, end);
		
	}
	
	@Reference
	private Portal _portal;
	
	@Reference
	private CPDefinitionHelper _cpDefinitionHelper;


}