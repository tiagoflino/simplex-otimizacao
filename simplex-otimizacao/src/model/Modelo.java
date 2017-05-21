package model;

public class Modelo {
	
	private int qtdVarDecisao;
	private int qtdRestricoes;
	private int LP;
	private int CP;
	
	private enum tipoSolucao {
		Otima, Inexistente, Ilimitada;

	}
	
	tipoSolucao status;
	
	private Double[][] tabela;
	private String[] linhaDesc;
	private String[] colDesc;
	
	public void setTabela(int i, int j, double val) {
		this.tabela[i][j] = val;
	}
	
	public Double getTabela(int i, int j) {
		return tabela[i][j];
	}
	
	public String getLinhaDesc(int i) {
		return linhaDesc[i];
	}


	public void setLinhaDesc(String[] linhaDesc) {
		this.linhaDesc = linhaDesc;
	}


	public String getColDesc(int i) {
		return colDesc[i];
	}


	public void setColDesc(String[] colDesc) {
		this.colDesc = colDesc;
	}


	public Modelo( Double[][] tabEntrada, int qtdRestricoes, int qtdVarDecisao){
		
		
		setQtdVarDecisao(qtdVarDecisao);
		setQtdRestricoes(qtdRestricoes);
		int marcaVar = 1;
		
		//Define a tabela com cabecalho e coluna p/ variaveis decisao
		tabela = new Double[qtdRestricoes + 1][qtdVarDecisao +1]; 
		linhaDesc = new String[qtdVarDecisao +1];
		colDesc = new String[qtdRestricoes + 1];
		
		
		//grava cabecalho linha
		linhaDesc[0] = "ML";
		for(int x = 1; x <= qtdVarDecisao; x++){
			
			linhaDesc[x] = "x" + marcaVar;
			marcaVar++;
					
		}
		
		//grava coluna descricao
		colDesc[0] = "Z";
		for(int x = 1; x <= qtdRestricoes; x++){
			
			colDesc[x] = "x" + marcaVar;
			marcaVar++;
					
		}
		
		//copia tabela
		tabela = tabEntrada.clone();
		
	}
	
	
	public void imprimeResultado(){
		
		System.out.println("Resultado:\n");
		
		for(int i=0;i<=qtdRestricoes;i++)
			
			System.out.println("  " + colDesc[i] + ": " + tabela[i][0]);
			
		
		
	}
	
	
	
	
	public void efetuaTroca(){
		
		double inversoEP = (1.0 / tabela[getLP()][getCP()]);
		
		//Cria tabela auxiliar para troca
		Double[][] tabAux = new Double[qtdRestricoes + 1][qtdVarDecisao +1];
		
		//Inverte EP
		tabAux[getLP()][getCP()] = inversoEP;
		
		//Multiplica LP pelo inverso EP
		for(int j=0;j<=qtdVarDecisao;j++){
			
			if(j!=getCP())
				tabAux[getLP()][j] = (tabela[getLP()][j] * inversoEP);
			
		}
		
		//Multiplica CP pelo inverso EP negativo
		for(int i=0;i<=qtdRestricoes;i++){
			
			if(i!=getLP())
				tabAux[i][getCP()] = (tabela[i][getCP()] * inversoEP * -1);
			
		}
		
		//Preenche as celulas faltantes com SCS(tabela) da LP x SCI(tabAux) da CP
		for(int i=0;i<=qtdRestricoes;i++)
			for(int j=0;j<=qtdVarDecisao;j++)
				
				if(tabAux[i][j] == null)
					
					tabAux[i][j] = tabela[getLP()][j] * tabAux[i][getCP()];
				
			
			
		
		//Troca VB com VNB no cabecalho
		String trocaCab;
		
		trocaCab = linhaDesc[getCP()];
		linhaDesc[getCP()] = colDesc[getLP()];
		colDesc[getLP()] = trocaCab;
		
		
		//Soma SCS(tabela) + SCI(tabAux) das celulas não permissivas  
		for(int i=0;i<=qtdRestricoes;i++)
			for(int j=0;j<=qtdVarDecisao;j++)
				
				if((i != getLP()) && (j != getCP()))
					
					tabAux[i][j] = tabela[i][j] + tabAux[i][j];
					
				
		//Clona tabela auxiliar para principal do modelo
		tabela = tabAux.clone();
			
		
	}
	
	
	public boolean checaVarNaoBasicaPositiva(){
		
		int linha = 0;
		int coluna = 1;
		
		while (coluna <= qtdVarDecisao){
			
			if(tabela[0][coluna] <= 0)
				
				coluna++;
			
			else{
			
				while (linha <= qtdRestricoes){
					
					if(tabela[linha][coluna] > 0){
						
						marcaLinColPermissiva(coluna);
						
						return true;
						
						
					} else{
						
						if(linha == qtdRestricoes){
							
							//retorna solucao inexistente se VB negativa sem elementos negativos
							status = tipoSolucao.Ilimitada;
							
							return false;
							
						}
						
						linha++;
						
					}
					
					
					
				}
				
				
				
				
			}
			
			
		}
		return false;
		
		
		
	}
	
	
	
	public boolean checaVarBasicaNegativa(){
		
		int linha = 1;
		int coluna = 1;
						
		
		while (linha <= qtdRestricoes){
			
			if(tabela[linha][0] >= 0)
				
				linha++;
			
			else{
								
				while(coluna <= qtdVarDecisao ){
					
					if(tabela[linha][coluna] < 0){
						
						marcaLinColPermissiva(coluna);
						
							
								
						return true;
																	
					}else{
						if(coluna == qtdVarDecisao){
							
							//retorna solucao inexistente se VB negativa sem elementos negativos
							status = tipoSolucao.Inexistente;
							
							return false;
							
						}
						
						coluna++;
						
						
					}
					
				}
			}
			
				
				
			}
		return false;
			
			
			
			
		}
		
	public void marcaLinColPermissiva(int coluna){
		
		//Marca coluna permissiva
		setCP(coluna);
		
		//acha linha permissiva
		double min = Double.MAX_VALUE;
		
		for(int i=1; i<=qtdRestricoes;i++)
			
			if((tabela[i][0] / tabela[i][getCP()] >=0) && (tabela[i][0] / tabela[i][getCP()] < min)) {
				
				min = (tabela[i][0] / tabela[i][getCP()]);
				setLP(i);
			}
		
	}	
	
	
	
	
	/**
	 * @return the qtdVarDecisao
	 */
	public int getQtdVarDecisao() {
		return qtdVarDecisao;
	}


	/**
	 * @param qtdVarDecisao the qtdVarDecisao to set
	 */
	public void setQtdVarDecisao(int qtdVarDecisao) {
		this.qtdVarDecisao = qtdVarDecisao;
	}

	/**
	 * @return the qtdRestricoes
	 */
	public int getQtdRestricoes() {
		return qtdRestricoes;
	}

	/**
	 * @param qtdRestricoes the qtdRestricoes to set
	 */
	public void setQtdRestricoes(int qtdRestricoes) {
		this.qtdRestricoes = qtdRestricoes;
	}


	/**
	 * @return the lP
	 */
	public int getLP() {
		return LP;
	}


	/**
	 * @param lP the lP to set
	 */
	public void setLP(int lP) {
		LP = lP;
	}


	/**
	 * @return the cP
	 */
	public int getCP() {
		return CP;
	}


	/**
	 * @param cP the cP to set
	 */
	public void setCP(int cP) {
		CP = cP;
	}


	
	

}
