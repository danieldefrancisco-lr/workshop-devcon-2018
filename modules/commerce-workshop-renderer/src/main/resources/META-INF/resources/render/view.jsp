
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

<style>
	.carousel-item > .col-md-4 {
		margin: auto;
	}
</style>