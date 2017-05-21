<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
 
<link rel="stylesheet" type="text/css"
    href="./resources/bootstrap/css/bootstrap.min.css" />
 
<link rel="stylesheet" type="text/css"
    href="./resources/bootstrap/css/bootstrap-theme.min.css" />
 
<script src="./resources/bootstrap/js/bootstrap.min.js"></script>
 
</head>
<body>
    <jsp:include page="barra_superior_param_modelo.jspf" />
 
    <div class="container" style="padding-top: 80px;">
        <div class="row">
            <div class="container">
                <div class="col-md-8">
                    <h1>Indique a quantidade de variáveis de decisão e restrições</h1>
                    <br />
                    <form class="form-horizontal" method="post" action="SimplexController?action=monta_matriz" role="form">
                        <div class="form-group">
                            <label for="dec" class="col-sm-5 control-label">Quantidade variáveis decisão:</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="dec" name="decisao"/><br>
                                
                            </div>
                        </div>
	                    <div class="form-group">
                            <label for="rest" class="col-sm-5 control-label">Quantidade restrições:</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="rest" name="restricao"/><br>
                                
                            </div>
                            <button type="submit" value="Confirma" class="btn btn-success" id="submit">Confirma</button>
                            </div>
                       
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>