package controller;

import java.io.IOException;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import model.Modelo;





 
@WebServlet("/SimplexController")
public class SimplexController extends HttpServlet {
    private static final long serialVersionUID = 1L;
        
    public SimplexController() {
        super();
    }
     
    private void processarRequisicao(HttpServletRequest request, HttpServletResponse response) throws ServletException {
         
        String action = request.getParameter("action");
       
        
        //checa o parametro recebido no POST e toma a acao adequada
        
        if (action == null) {
        
        		throw new ServletException("No action specified.");
        
        } else if (action.equals("insere_var")) {
       
        	pegaParamModelo(request, response);
        
        } else if (action.equals("monta_matriz")) {
        	
        	montaMatriz(request, response);
        	
        } else if (action.equals("matrizJSON")){
        	
        	pegaMatrizJSON(request,response);
        	
        }
        
                
        
        	
     }
 
    private void pegaParamModelo(HttpServletRequest request, HttpServletResponse response){
         
        RequestDispatcher rd = null;
        rd = request.getRequestDispatcher("public/param_modelo.jsp");
         
        try {
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void pegaMatrizJSON(HttpServletRequest request, HttpServletResponse response){
        
    	
      		String matrizJSON = request.getParameter("matriz");
    		int qtdLinha = Integer.parseInt(request.getParameter("qtdLinha"));
    		int qtdColuna = Integer.parseInt(request.getParameter("qtdColuna"));
    		
    		Double[][] tabelaTemp = new Double[qtdLinha+1][qtdColuna+1];
    		
  		
    		try {
    		
    		JSONObject jObj;
			jObj = new JSONObject(matrizJSON); //Pega JSON completo
			
			Iterator itLinha = jObj.keys();//Extrai linhas para um Iterator
			
			
			while(itLinha.hasNext())
    		{
    		    String linha = itLinha.next().toString(); //Extrai cada linha como JSON
    		    JSONObject linhaJSON = new JSONObject(jObj.get(linha).toString()); // Pega
    		    
    		    int teste = linhaJSON.length();
    		    
    		    for(int coluna=0;coluna<teste;coluna++){
    		    	
    		    	
    		    	System.out.println(linha + " : " +  linhaJSON);
    		    	System.out.println(coluna + " : " +  linhaJSON.get(String.valueOf(coluna)).toString());
    		    	
    		    	tabelaTemp[Integer.parseInt(linha)][coluna] =  Double.valueOf(linhaJSON.get(String.valueOf(coluna)).toString());
    		    	
    		    	
    		    }
    	   		    
    		    	
    		    
    		}
    		    
    		    
    		
    		} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
    		
    		
    		
    		Modelo modelo = new Modelo(tabelaTemp, qtdLinha, qtdColuna);
    		
    		
    		//Etapa 1
    		while(modelo.checaVarBasicaNegativa()){
    			
    			modelo.efetuaTroca();
    			
    		}
    		
    		//Etapa 2
    		
    		while(modelo.checaVarNaoBasicaPositiva()){
    			
    			modelo.efetuaTroca();
    			
    		}
    		
    		//imprime no console (Debug)
    		modelo.imprimeResultado();
    		
    		
    		
    		String resultado = retornaResultado(modelo);
    		
    	
    		//Retorna o resultado no formato JSON para o front-end
    		
    		try {
    		    response.setContentType("application/json");  
    		    response.setCharacterEncoding("UTF-8");
    		    response.getWriter().write("{\"resultado\":\"" + resultado + "\"}");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		
    		
    		
    }
    
    
    		
    private String retornaResultado(Modelo m){
		
		
    	//Pega o valor da tabela (ML) e do cabecalho e preenche a variavel de resultado 
    	
    	String resultado = null;
		
		//Ajusta o valor de Z * -1
		if(m.getTabela(0,0)<0){
			m.setTabela(0, 0, m.getTabela(0,0)*-1);
		}
		
		for(int i=0;i<=m.getQtdRestricoes();i++)
			
			
			resultado = ((resultado==null)? "" : resultado) +  m.getColDesc(i) + "=" + Math.round(m.getTabela(i,0) * 100)/100.0d + " " ;
			
		return resultado;
		
	}
 
    private void montaMatriz(HttpServletRequest request, HttpServletResponse response){
        
        
    	try{  
            
         
            String varDecisao = request.getParameter("decisao");  
            String varRestricao = request.getParameter("restricao");  
            
            request.setAttribute("varDecisao", varDecisao);
            request.setAttribute("varRestricao", varRestricao);
             
              
         }catch (Exception e) {  
            request.setAttribute("msg", "Erro: " + e.getMessage());  
            
         }  

    	
    	
    	RequestDispatcher rd = null;
        
        rd = request.getRequestDispatcher("public/monta_matriz.jsp");
        
    	
        try {
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processarRequisicao(request, response);
    }
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processarRequisicao(request, response);
    }
}