<html>
<head></head>
<body>
    <#list samples.sample as x >

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