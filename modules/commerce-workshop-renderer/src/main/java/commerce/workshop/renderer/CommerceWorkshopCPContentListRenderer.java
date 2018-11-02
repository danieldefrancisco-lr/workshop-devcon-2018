package commerce.workshop.renderer;

import com.liferay.commerce.product.constants.CPPortletKeys;
import com.liferay.commerce.product.content.constants.CPContentWebKeys;
import com.liferay.commerce.product.content.render.list.CPContentListRenderer;
import com.liferay.commerce.product.content.util.CPContentHelper;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author dfrancisco
 */
@Component(
		immediate = true,
		property = {
			"commerce.product.content.list.renderer.key=" + CommerceWorkshopCPContentListRenderer.KEY,
			"commerce.product.content.list.renderer.order=10",
			"commerce.product.content.list.renderer.portlet.name=" + CPPortletKeys.CP_PUBLISHER_WEB
		},	
		service = CPContentListRenderer.class
	)
public class CommerceWorkshopCPContentListRenderer implements CPContentListRenderer {

	public static final String KEY = "workshop-carousel-full";

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return LanguageUtil.get(resourceBundle, KEY);
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		httpServletRequest.setAttribute(
				CPContentWebKeys.CP_CONTENT_HELPER, _cpContentHelper);

		_jspRenderer.renderJSP(_servletContext,  httpServletRequest, httpServletResponse,
			"/render/view.jsp");
	}

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference(
			target = "(osgi.web.symbolicname=commerce.workshop.renderer)"
		)
	private ServletContext _servletContext;
	
	@Reference
	private CPContentHelper _cpContentHelper;




}