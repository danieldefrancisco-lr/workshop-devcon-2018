<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>


<%@ include file="./init.jsp"%>

<%
	CPDataSourceResult cpDataSourceResult = (CPDataSourceResult) request
			.getAttribute(CPWebKeys.CP_DATA_SOURCE_RESULT);
	int i=0;
%>

<div id="carouselExampleControls" class="carousel slide"
	data-ride="carousel">
	<div class="carousel-inner">
		<%
			for (CPCatalogEntry cpCatalogEntry : cpDataSourceResult.getCPCatalogEntries()) {
			if (i==0) {
		%>
		<div class="carousel-item active">
			<liferay-commerce-product:product-list-entry-renderer
				CPCatalogEntry="<%=cpCatalogEntry%>" />
		</div>


		<%
			i++;
			}
			else {
		%>
		<div class="carousel-item">
			<liferay-commerce-product:product-list-entry-renderer
				CPCatalogEntry="<%=cpCatalogEntry%>" />
		</div>
		<%
			}
			}
		%>
	</div>
		<a class="carousel-control-prev text-secondary" href="#carouselExampleControls" role="button" data-slide="prev">
        <aui:icon image="angle-left" markupView="lexicon" />
	</a>
	<a class="carousel-control-next text-secondary" href="#carouselExampleControls" role="button" data-slide="next">
		<aui:icon image="angle-right" markupView="lexicon" />
	</a>
</div>