<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Simplex Venttsel</title>


<link rel="stylesheet" type="text/css" href="./resources/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="./resources/bootstrap/css/bootstrap-theme.min.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="./resources/bootstrap/js/bootstrap.min.js"></script>

<script type="text/javascript">

/*
(function(){
    // Convert array to object
    var convArrToObj = function(array){
        var thisEleObj = new Object();
        if(typeof array == "object"){
            for(var i in array){
                var thisEle = convArrToObj(array[i]);
                thisEleObj[i] = thisEle;
            }
        }else {
            thisEleObj = array;
        }
        return thisEleObj;
    };
    var oldJSONStringify = JSON.stringify;
    JSON.stringify = function(input){
        if(oldJSONStringify(input) == '[]')
            return oldJSONStringify(convArrToObj(input));
        else
            return oldJSONStringify(input);
    };
})();
*/

function chamaSimplex(){
	
	//limpa resultado
	$("#result").html("");
	
	var linha= ${varRestricao};
	var coluna= ${varDecisao};
	var f = {};
	var tipo = document.getElementById('maxmin').value;
	
	//Inverte total para o inicio
	for (i=0;i<=linha;i++) {
	 //f[i]=new Array();
	 f[i]= {};
	 
	 val = document.getElementById('linha[' + i + '][' + coluna + ']').value;
	 
	 if(i==0)
		 
		 f[i][0]=val;
		 
	 else{
			 
		 sinal = document.getElementById('sel[' + i + ']').value;
		 
		 if(sinal==">=")				 
			 val=val*-1; //inverte o sinal quando a restricao for de excesso
			
		 f[i][0]=val;
				 
		}
			 
		 
	 } 
	
	//preenche o resto da matriz
	for (i=0;i<=linha;i++) {
		 for (j=0;j<coluna;j++) {
		
		 //pega o valor do form
		 val = document.getElementById('linha[' + i + '][' + j + ']').value;
		   
		  if(i==0){
			  
	
			  
			  if(tipo=="MIN"){
				  val=val*-1;
			  }
			  
			  
			  f[i][j+1]=val;//coluna 0 já preenchida
		  
		  }else{
			  
		  //checa o sinal para inversão da formula		  
		  sinal = document.getElementById('sel[' + i + ']').value;
		  
		  if(sinal==">="){
				
			  val=val*-1;
			  
		  }
		
		  f[i][j+1]=val;
	
		  }
		 }
	}
	
		  
		//converte a matriz já pronta em JSON
		var param =  JSON.stringify(f);
		
		  
		var matrizJSON = $.ajax({
		      type: "POST",
		      dataType: "JSON",
		      url: "SimplexController?action=matrizJSON",
		      data: {matriz : JSON.stringify(f),
		    	  	 qtdLinha : linha,
		    	  	 qtdColuna: coluna},
		      success: function(resultData){
		    	  
		    	  var r = resultData.resultado;
		    	  $("#result").append("<h4>Resultado:<br><br>" + r.replace(/ /g, "<br>") + "</h4>");
		      },
		      error: function(xhr, textStatus, error){
		          console.log(xhr.statusText);
		          console.log(textStatus);
		          console.log(error);
		      }
		});
		
		tipo = null; 

		 
	

	
} 
 
 
 
 </script>

</head>
<body>

 <jsp:include page="barra_superior_param_modelo.jspf" />

 
    <div class="container" style="padding-top: 80px;">
        <div class="row">
            <div class="container">
                <div class="col-md-12">
                
                    <p>Entre valores positivos ou negativos para as variáveis. Utilize ponto como símbolo decimal.</p>
                    <br />
                    <form class="form" method="post" action="" role="form">
                    	 
                                                	        
                               	<%
	                               	int varDecisao = Integer.parseInt((String)request.getAttribute("varDecisao"));
	                           		int varRestricao = Integer.parseInt((String)request.getAttribute("varRestricao"));
										
                               	
                               	
                               		out.print("<table class=\"table table-striped\">");
                               		out.print("<thead>");
                               		out.print("<tr>");
                               		out.print("<th></th>");
                               		
                               		for(int j=0;j<varDecisao;j++){
                               			
                                   		
                           				out.print("<th class=\"text-center\">x" + (j+1) + "</th>");
                           			
                           			}
                               		
                               		
                               		out.print("<th>Operação</th>");
                               		out.print("<th></th>");
                               		
                               		out.print("</tr>");
                               		
                               		out.print("</tr>");
                               		out.print("</thead>");
                               		out.print("</tbody>");
                               		
                               		out.print("<tr>");
                               		out.print("<th>Função Objetivo:</th>");
                               		out.print("</tr>");
									
                               		out.print("<tr>");
                               		//out.print("<th class=\"text-center\">Z=</th>");
                               		out.print("<th>");
									out.print("<select class=\"form-control\" name=\"maxmin\" id=\"maxmin\">");
                       				out.print("<option value='MAX' selected>MAX</option>");
                       				out.print("<option value='MIN'>MIN</option>");
                       				out.print("</select>");
                       				out.print("</th>");
                               		
                               		
                               		
                               		for(int j=0;j<=varDecisao;j++){
                               		
                               			
                           				//out.print("<label for=\"linha[" + i + "][" + j + "]\"> x" + j + "</label>");
  										
                           				if(j<varDecisao){
                           					
                           					out.print("<th>");
                           					out.print("<input class=\"form-control\"  type=\"text\" name=\"linha[0][" + j + "]\" id=\"linha[0][" + j + "]\">");
											out.print("</th>");
										
                           					
                           				}else{
                           					
                           					out.print("<th></th>");
                           					out.print("<th>");
                           					out.print("<input class=\"form-control\" value=0 disabled type=\"text\" name=\"linha[0][" + j + "]\" id=\"linha[0][" + j + "]\">");
    	  			                   		out.print("</th>");
                           						
                           				} 
  												
	                           						
                           			
                           			
                               		}
                               		
                               		
                               		

                               		
                               		out.print("</tr>");
                               		
                               		out.print("<tr>");
                               		out.print("<th>Restrições:</th>");
                               		out.print("</tr>");
                               		                               		
                               		
                               		for(int i=1;i<=varRestricao;i++){
	                               		
                               			//out.print("<div class=\"form-group\">");
                               			//out.print("<div class= row>");
                               			out.print("<tr>");
                               			out.print("<th class=\"text-center\">" + i + "</th>");
                               			
                               			                               			
                               			for(int j=0;j<=varDecisao;j++){
	  										
                               				if(j<varDecisao){
                               					
                               				out.print("<th>");
                               				//out.print("<label for=\"linha[" + i + "][" + j + "]\"> x" + j + "</label>");
	  										out.print("<input class=\"form-control\" required type=\"text\" name=\"linha[" + i + "][" + j + "]\" id=\"linha[" + i + "][" + j + "]\">");
	  										out.print("</th>");
                               				}
	  										else{
	  											
	  											out.print("<th>");
	  											out.print("<select class=\"form-control\" style=\"width 40px\" name=\"sel[" + i + "]\" id=\"sel[" + i + "]\">");
	                               				out.print("<option><=</option>");
	                               				out.print("<option>>=</option>");
	                               				//out.print("<option>=</option>");
	                               				out.print("</select>");
	                               				out.print("</th>");
	                               				
	                               				out.print("<th>");
	                               				//out.print("<label for=\"linha[" + i + "][" + j + "]\"> x" + j + "</label>");
		  										out.print("<input class=\"form-control\" required type=\"text\" name=\"linha[" + i + "][" + j + "]\" id=\"linha[" + i + "][" + j + "]\">");
		  										out.print("</th>");
	                               				
	                               				
	  										}
	  										
	  										
	  									}
	                               		
                               			out.print("</tr>");
	                               		
	                               		
                               		}
                               		
                               		out.print("</table>");
								%>
                               
                            <input type="button" class="btn btn-info" value="Calcular" onclick="chamaSimplex()">   
                               
                               
                               
                             
                      
                    </form>
                </div>
            </div>
        </div>
    </div>
	<div class="container" id="result">
				
	</div>


<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="./resources/bootstrap/js/bootstrap.min.js"></script>
 -->
</body>
</html>