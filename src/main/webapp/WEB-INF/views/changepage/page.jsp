<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
.link {
	margin: 5px;
}
</style>
<div style="float: right; margin: 5px 50px;">
	<ul class="pagination pagination-sm" style="margin: 0;">
		<c:if test="${requestScope.page.nowPage>3}">
			<li><a href="${ctxPath}${requestScope.page.action}page=1">&laquo;</a>
			</li>
			<li><a
				href="${ctxPath}${requestScope.page.action}page=${requestScope.page.nowPage-1}">&lsaquo;</a>
			</li>
		</c:if>
		<c:if test="${requestScope.page.pagesCount>1 }">
			<c:forEach var="index"
				begin="${(requestScope.page.nowPage>3)? (requestScope.page.nowPage-2):1 }"
				end="${(requestScope.page.nowPage<requestScope.page.pagesCount-3)? (requestScope.page.nowPage+2):requestScope.page.pagesCount }">
				<c:choose>
					<c:when test="${requestScope.page.nowPage==index}">
						<li class='disabled'><a href="#">-${index }-</a></li>
					</c:when>
					<c:otherwise>
						<li><a
							href="${ctxPath}${requestScope.page.action}page=${index }">${index }</a>
						</li>
					</c:otherwise>
				</c:choose>

			</c:forEach>
		</c:if>
		<c:if
			test="${requestScope.page.nowPage<requestScope.page.pagesCount-2}">
			<li><a
				href="${ctxPath}${requestScope.page.action}page=${requestScope.page.nowPage +1}">&rsaquo;</a>
			</li>
			<li><a
				href="${ctxPath}${requestScope.page.action}page=${requestScope.page.pagesCount}">&raquo;</a>
			</li>
		</c:if>

	</ul>
</div>
<c:if test="${requestScope.page.rowsCount>0 }">
	<div style="float: left;">[ 第${1+(requestScope.page.nowPage-1)*(requestScope.page.pageSize)} ~ ${(requestScope.page.nowPage-1)*(requestScope.page.pageSize)+requestScope.page.pageRowsCount}筆-共${requestScope.page.rowsCount}筆 ]</div>
</c:if>
 
<div sytle="clear: both;"></div>





