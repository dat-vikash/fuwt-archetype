<html>
<head></head>
<body>
    <#list list.listitem as x >

        Processed sample ${x.@id}
        <br/>
        <strong>
        Name: ${x.name}
        <br/>
        Synchronized: ${x.syncd}
        </strong>

    </#list>
</body>
</html>