<h1>${title!}</h1>
${(aaa)???string}

<!--判断集合是否实例化-->
${(list)??}

<!--判断集合是否实例化-->
<#if (list)??>
	不为空--
<#else>
	为空
</#if>

<#list list as l>
	${l.name}
</#list>


${list?size}
${.now?string("[yyyy/MM/dd]")}
<br>
${abcStr}
${(list)??}
